package com.ivmarchenko.useraggregator.config;

import lombok.Data;

import java.util.Map;

@Data
public class DatabaseConfig {
    private String name;
    private String strategy;
    private String url;
    private String user;
    private String table;
    private String password;
    private Map<String, String> mapping;
}
