package com.afrione.africoinservice.usecases.data.response.otc;

import java.util.List;

public  record NotificationConstant(List<NotificationAudienceModel> audience, List<NotificationTypeModel> type){};