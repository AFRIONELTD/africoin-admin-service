package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.response.customer.UserDocReviewRequest;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
public interface CustomerWriteUseCases {
    void reviewUserDocument(UserDocReviewRequest request, Long accountId);
}
