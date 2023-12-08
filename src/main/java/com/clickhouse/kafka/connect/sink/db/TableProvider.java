package com.clickhouse.kafka.connect.sink.db;

import com.clickhouse.kafka.connect.sink.db.mapping.Table;

public interface TableProvider {
    Table getTable(String topic);
}
