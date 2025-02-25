package com.ivmarchenko.useraggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;


@ConfigurationProperties(prefix = "db")
public record DatabaseListConfig(List<DatabaseConfig> dataSources) {
}