package com.afrione.africoinservice.infrastructure.web.controllers;



import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.data.request.NotificationRequest;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.NotificationConstant;
import com.afrione.africoinservice.usecases.data.response.otc.NotificationResponse;
import com.afrione.africoinservice.usecases.exceptions.APIRequestException;
import com.afrione.africoinservice.usecases.feature.customer.notification.NotificationUseCases;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Transactional
@RestController
@RequestMapping(value = "/api/v1/admin/notification", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Manages user notification", description = "Admin manage user notification")
public class NotificationController {


    private final NotificationUseCases notificationUseCase;

    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponseJSON<NotificationResponse>> createNotification(@RequestBody @Valid  CreateNotificationJSON request , @AuthenticationPrincipal AuthenticatedUser authenticatedUser){



        NotificationResponse response = notificationUseCase.createNotification(request.toRequest(), authenticatedUser);

           return ResponseEntity.ok(new ApiResponseJSON<>("successful",response ));
    }


    @PostMapping(value = "/edit/{id}")
    public ResponseEntity<ApiResponseJSON<NotificationResponse>> editNotification(@PathVariable("id") Long id, @RequestBody @Valid  CreateNotificationJSON request , @AuthenticationPrincipal AuthenticatedUser authenticatedUser){


        NotificationResponse response = notificationUseCase.editNotification(request.toRequest(), id ,authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("successful",response ));
    }

     @GetMapping("/audience-count")
    public ResponseEntity<ApiResponseJSON<Long>> getUserCount(@Schema(description = "(ALL|ACTIVE|PROSPECTIVE|NG|GH)")
                                                          @Pattern(regexp = "(ALL|ACTIVE|PROSPECTIVE|NG|GH)") @RequestParam  String audience,
                                                          @AuthenticationPrincipal AuthenticatedUser authenticatedUser){


       Long res = notificationUseCase.getUserCount(audience, authenticatedUser);

       return ResponseEntity.ok(new ApiResponseJSON<>("successful",res ));

    }


//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteNotificationRecord(@PathVariable("id") Long id ){
//
//        notificationUseCase.deleteNotificationRecord(id);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponseJSON<PagedResponse<NotificationResponse>>>  fetchNotificationResponse(@RequestParam(required = false) String keyword,
                                                                                          @RequestParam(required = false) @Schema(description = "(ALL|ACTIVE|PROSPECTIVE|NG|GH)")
                                                                                          @Pattern(regexp = "(ALL|ACTIVE|PROSPECTIVE|NG|GH)") String audience, @RequestParam(required = false) @Schema(description = "(ALL|ACTIVE|PROSPECTIVE|NG|GH)" ) @Pattern(regexp = "(ALL|ACTIVE|PROSPECTIVE|NG|GH)") String notificationType,
                                                                                          @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                                          @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
                                                                                          @AuthenticationPrincipal AuthenticatedUser authenticatedUser){

      PagedResponse<NotificationResponse> response = notificationUseCase.fetchNotification(startDate, endDate, keyword, audience, notificationType,  pageNo, pageSize, authenticatedUser);

      return ResponseEntity.ok(new ApiResponseJSON<>("successful",response ));

    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ApiResponseJSON<NotificationResponse>> fetchNotificationDetails(@PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser ){


        NotificationResponse response = notificationUseCase.notificationDetail(id, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("successful", response));
    }

    @GetMapping("/constant")
    public ResponseEntity<ApiResponseJSON<NotificationConstant>> fetchConstant(@AuthenticationPrincipal AuthenticatedUser authenticatedUser ){

     NotificationConstant notificationConstant = notificationUseCase.getConstant(authenticatedUser);

       return ResponseEntity.ok(new ApiResponseJSON<>("successful",  notificationConstant));


    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseJSON<Void>> deleteNotificationRecord(@PathVariable("id") Long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser  ){

        notificationUseCase.deleteNotification(id, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Deleted successfully"));
    }



    @Data
    public static class CreateNotificationJSON{

        @NotBlank(message = "message must not be blank")
        private String message;

        @NotBlank(message = "Title must not be blank")
        @Length(max = 250)
        private String title;

        @Pattern(regexp = "(ALL|ACTIVE|PROSPECTIVE|NG|GH)")
        private String audience;

        @Pattern(regexp = "PUSH_NOTIFICATION|LOGIN_ALERT")
        @NotBlank(message = "The notification type cannot be null/blank")
        private String notificationType;

        private Long recipientCount;

        @Schema(description = "This is required for push notification type if you want request to be sent instantly")
        private Boolean now;

        @Schema(description = "This is required for push notification type. You sent UTC datetime")
        private Instant time;


        public NotificationRequest toRequest(){

            if(notificationType.equalsIgnoreCase("PUSH_NOTIFICATION")){

                if(audience == null){

                    throw new APIRequestException( HttpStatus.BAD_REQUEST, "Audience is required for push notification type");
                }


                if(now == null && time == null){

                    throw new APIRequestException(HttpStatus.BAD_REQUEST, "now & time cannot be both null for push notification type");
                }

            }


            return NotificationRequest.builder()
                    .title(title)
                    .message(message)
                    .recipientCount(recipientCount)
                    .audience(audience)
                    .notificationType(notificationType)
                    .time(time)
                    .now(now != null && now)
                    .build();
        }

    }

    public record UserCountResponse(long userCount){};



}
