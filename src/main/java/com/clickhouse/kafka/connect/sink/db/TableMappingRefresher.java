package com.clickhouse.kafka.connect.sink.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.TimerTask;

public class TableMappingRefresher extends TimerTask {
  private static final Logger LOGGER = LoggerFactory.getLogger(TableMappingRefresher.class);
  private MappingTableProvider provider;

  public TableMappingRefresher(final MappingTableProvider provider) {
    this.provider = provider;
  }

  @Override
  public void run() {
    try {
      provider.updateMapping();
    } catch (Exception e) {
      LOGGER.error("Update mapping Error: {}", e.getMessage());
    }
    
  }
}
