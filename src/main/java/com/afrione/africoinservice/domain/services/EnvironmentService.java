package com.afrione.africoinservice.domain.services;


public interface EnvironmentService {
    String getVariable(String variableName);
    String getVariable(String variableName, String defaultValue);
    String[] getActiveProfiles();
}
