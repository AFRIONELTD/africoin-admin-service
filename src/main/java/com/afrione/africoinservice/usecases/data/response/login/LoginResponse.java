package com.afrione.africoinservice.usecases.data.response.login;

import com.afrione.africoinservice.usecases.data.value_objects.CustomerToken;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import lombok.Data;


@Data

public class LoginResponse {
    private CustomerToken userToken;
    private PortalUserModel user;
}
