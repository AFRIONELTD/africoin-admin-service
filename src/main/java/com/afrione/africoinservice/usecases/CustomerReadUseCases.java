package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.customer.AppUserModel;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
public interface CustomerReadUseCases extends ExternalRequestUseCases {
    PagedResponse<AppUserModel> listUsers(int pageNo, int pageSize, Long accountId);
    AppUserModel getAppUser(String userId, Long accountId);
}
