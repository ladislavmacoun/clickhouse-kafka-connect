package com.clickhouse.kafka.connect.sink.data.convert;


import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.storage.HeaderConverter;

import java.util.Map;

public class IgnoreHeaderConverter implements HeaderConverter {
    private static final SchemaAndValue NULL_SCHEMA_AND_VALUE = new SchemaAndValue(null, null);
    private static final ConfigDef CONFIG_DEF = new ConfigDef();

    @Override
    public SchemaAndValue toConnectHeader(String topic, String headerKey, byte[] value) {
        return NULL_SCHEMA_AND_VALUE;
    }

    @Override
    public byte[] fromConnectHeader(String topic, String headerKey, Schema schema, Object value) {
        return null;
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // do nothing
    }
}