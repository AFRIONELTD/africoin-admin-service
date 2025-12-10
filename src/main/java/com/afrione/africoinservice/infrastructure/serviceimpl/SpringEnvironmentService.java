package com.afrione.africoinservice.infrastructure.serviceimpl;


import com.afrione.africoinservice.domain.services.EnvironmentService;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Service;


@Service
public class SpringEnvironmentService implements EnvironmentService {

    private final StandardEnvironment environment;
    public SpringEnvironmentService(StandardEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public String getVariable(String variableName) {
        return environment.getProperty(variableName);
    }

    @Override
    public String getVariable(String variableName, String defaultValue) {
        return environment.getProperty(variableName, defaultValue);
    }

    @Override
    public String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }
}
