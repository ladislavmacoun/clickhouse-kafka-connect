package com.clickhouse.kafka.connect.sink.data.convert;

import com.clickhouse.kafka.connect.sink.data.Record;
import com.clickhouse.kafka.connect.sink.data.SchemaType;
import com.clickhouse.kafka.connect.sink.kafka.OffsetContainer;
import org.apache.kafka.connect.sink.SinkRecord;

public class ByteRecordConvertor implements RecordConvertor {
    @Override
    public Record convert(SinkRecord sinkRecord) {
        String topic = sinkRecord.topic();
        int partition = sinkRecord.kafkaPartition().intValue();
        byte[] payload  = (byte[]) sinkRecord.value();
        long offset = sinkRecord.kafkaOffset();
        return new Record(SchemaType.SCHEMA_LESS, new OffsetContainer(topic, partition, offset), sinkRecord, payload);
    }
}
