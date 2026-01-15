package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.response.otc.UserDocReviewRequest;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
public interface CustomerWriteUseCases extends ExternalRequestUseCases {
    void reviewUserDocument(UserDocReviewRequest request, Long accountId);
}
