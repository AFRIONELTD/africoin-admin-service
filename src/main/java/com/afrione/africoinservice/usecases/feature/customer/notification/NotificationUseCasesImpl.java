package com.afrione.africoinservice.usecases.feature.customer.notification;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.data.request.NotificationRequest;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.AmountSummaryResponse;
import com.afrione.africoinservice.usecases.data.response.otc.NotificationConstant;
import com.afrione.africoinservice.usecases.data.response.otc.NotificationResponse;
import com.afrione.africoinservice.usecases.exceptions.APIRequestException;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationUseCasesImpl implements  NotificationUseCases{

    private final ApplicationProperty applicationProperty;

    private final RestClientService restClientService;

    private final ObjectMapper objectMapper;

    private final AppUserEntityDao appUserEntityDao;


    public NotificationConstant getConstant(AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/notification/constant", applicationProperty.customerServiceUrl()));


            RestClientResponse response = restClientService.getRequest(url.toString(),generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    NotificationConstant.class
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for funding transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for funding transactions " + e.getMessage());
        }
    }





    public NotificationResponse notificationDetail( Long id ,AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/notification/details/%s", applicationProperty.customerServiceUrl(), id));


            RestClientResponse response = restClientService.getRequest(url.toString(),generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    NotificationResponse.class
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for funding transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for funding transactions " + e.getMessage());
        }
    }




    public PagedResponse<NotificationResponse> fetchNotification(LocalDate startDate, LocalDate endDate, String keyword, String audience, String notificationTYpe , int pageNo, int pageSie,AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/notification/fetch", applicationProperty.customerServiceUrl() ));
            url.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSie);

            if(startDate != null) {
                url.append("&startDate=").append(startDate.toString());
            }

            if(endDate != null) {
                url.append("&endDate=").append(endDate.toString());
            }

            if(keyword != null && !keyword.isBlank()  ){

                url.append("&keyword=").append(keyword);
            }

            if(keyword != null && !keyword.isBlank()  ){

                url.append("&audience=").append(audience);
            }

            if(keyword != null && !keyword.isBlank()  ){

                url.append("&notificationType=").append(notificationTYpe);
            }




            RestClientResponse response = restClientService.getRequest(url.toString(),generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    new TypeReference<PagedResponse<NotificationResponse>>() {
                    }
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for funding transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for funding transactions " + e.getMessage());
        }
    }



    public Long getUserCount(String audience ,AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/notification/audience-count?audience=%s", applicationProperty.customerServiceUrl(), audience ));


            RestClientResponse response = restClientService.getRequest(url.toString(),generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    UserCountResponse.class
            ).userCount;



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for funding transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for funding transactions " + e.getMessage());
        }
    }



    public NotificationResponse editNotification(NotificationRequest request, Long id ,AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/notification/edit/%s", applicationProperty.customerServiceUrl(), id));


            RestClientResponse response = restClientService.postRequest(url.toString(), objectMapper.writeValueAsString(request) ,generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    NotificationResponse.class
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for funding transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for funding transactions " + e.getMessage());
        }
    }





    public NotificationResponse createNotification(NotificationRequest request, AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/notification/create", applicationProperty.customerServiceUrl()));


            RestClientResponse response = restClientService.postRequest(url.toString(), objectMapper.writeValueAsString(request) ,generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    NotificationResponse.class
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for funding transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for funding transactions " + e.getMessage());
        }
    }







    private String getAdminUsername(Long accountId) {
        AppUserEntity user = appUserEntityDao.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Admin user not found for account: " + accountId));
        //user.getFirstName() + " " + user.getLastName();
        return  user.getEmail();
    }


    @Override
    public ApplicationProperty getApplicationProperty() {
        return applicationProperty;
    }


    public record UserCountResponse(long userCount){};

}
