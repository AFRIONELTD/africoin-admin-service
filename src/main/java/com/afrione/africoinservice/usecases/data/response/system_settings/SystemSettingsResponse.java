package com.afrione.africoinservice.usecases.data.response.system_settings;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SystemSettingsResponse {
	private Long id;
	private String dateCreated;
	private String value;
	private boolean updatable;
	private String description;
	private String valueType;
}
