package com.clickhouse.kafka.connect.sink.db;

import com.clickhouse.kafka.connect.sink.db.mapping.Table;

public class SimpleTableProvider implements TableProvider {
    private final Table table;
    SimpleTableProvider(String tableName) {
        this.table = new Table(tableName);
    }

    @Override
    public Table getTable(String topic) {
        return table;
    }
}
