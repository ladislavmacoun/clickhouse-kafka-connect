package com.clickhouse.kafka.connect.sink.db;

import com.clickhouse.kafka.connect.sink.ClickHouseSinkConfig;
import com.clickhouse.kafka.connect.sink.db.helper.ClickHouseHelperClient;
import com.clickhouse.kafka.connect.sink.db.mapping.Table;
import com.clickhouse.kafka.connect.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MappingTableProvider implements TableProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MappingTableProvider.class);
    private Map<String, Table> mapping;
    private ClickHouseHelperClient chc;
    private ClickHouseSinkConfig csc;
    private AtomicBoolean isUpdateMappingRunning = new AtomicBoolean(false);

    MappingTableProvider(ClickHouseHelperClient client, ClickHouseSinkConfig config) {
        this.chc = client;
        this.csc = config;

        updateMapping();
        if (mapping.isEmpty()) {
            throw new RuntimeException("Did not find any tables in destination Please create before running.");
        }
    }

    @Override
    public Table getTable(String topic) {
        String tableName = Utils.getTableName(topic, csc.getTopicToTableMap());
        Table table = this.mapping.get(tableName);
        if (table == null) {
            if (csc.getSuppressTableExistenceException()) {
                LOGGER.warn("Table [{}] does not exist, but error was suppressed.", tableName);
            } else {
                //TODO to pick the correct exception here
                LOGGER.error("Table [{}] does not exist - see docs for more details about table names and topic names.", tableName);
                throw new RuntimeException(String.format("Table %s does not exist", tableName));
            }
        }

        return table;//It'll only be null if we suppressed the error
    }

    public void updateMapping() {
        // Do not start a new update cycle if one is already in progress
        if (this.isUpdateMappingRunning.get()) {
            return;
        }
        this.isUpdateMappingRunning.set(true);

        LOGGER.debug("Update table mapping.");

        try {
            // Getting tables from ClickHouse
            List<Table> tableList = this.chc.extractTablesMapping(this.mapping);
            if (tableList.isEmpty()) {
                return;
            }

            HashMap<String, Table> mapping = new HashMap<String, Table>();

            // Adding new tables to mapping
            // TODO: check Kafka Connect's topics name or topics regex config and
            // only add tables to in-memory mapping that matches the topics we consume.
            for (Table table : tableList) {
                mapping.put(table.getName(), table);
            }

            this.mapping = mapping;
        } finally {
            this.isUpdateMappingRunning.set(false);
        }
    }
}
