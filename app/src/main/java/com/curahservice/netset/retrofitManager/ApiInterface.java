package com.curahservice.netset.retrofitManager;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Neeraj Narwal on 5/5/17.
 */
public interface ApiInterface {


    @FormUrlEncoded
    @POST("createAccount")
    Call<JsonObject> createAccount(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("confirm_password") String confirmPassword,
                                   @Field("register_type") String registerType,
                                   @Field("device_id") String deviceId,
                                   @Field("device_type") String deviceType,
                                   @Field("user_type") String userType,
                                   @Field("social_id") String socialId,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String longitude


    );


    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Field("email") String email,
                           @Field("password") String password,
                           @Field("device_id") String deviceId,
                           @Field("device_type") String deviceType,
                           @Field("user_type") String userType,
                           @Field("latitude") String latitude,
                           @Field("longitude") String longitude

    );
    @FormUrlEncoded
    @POST("changePassword")
    Call<JsonObject> changePasswordApi(@Field("user_id") String userid,
                                       @Field("old_password") String oldPass,
                                       @Field("new_password") String new_password

    );

    @FormUrlEncoded
    @POST("bankAccount")
    Call<JsonObject> bankAccount(@Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<JsonObject> forgotPass(@Field("email") String email


    );

    @FormUrlEncoded
    @POST("addCard")
    Call<JsonObject> addCard(@Field("user_id") String userid,
                             @Field("cardNumber") String cardNumber,
                             @Field("name") String name,
                             @Field("expirationDate") String expirationDate,
                             @Field("cvv") String cvv

    );

    @FormUrlEncoded
    @POST("getNotification")
    Call<JsonObject> getNotification
            (@Field("user_id") String userid

            );

    @Multipart
    @POST("createProfile")
    Call<JsonObject> createProfile(
            @Part("user_id") RequestBody userId,
            @Part("firstname") RequestBody firstName,
            @Part("lastname") RequestBody lastName,
            @Part("address") RequestBody address,
            @Part("phone") RequestBody phone,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("fb_link") RequestBody fb_link,
            @Part("ins_link") RequestBody ins_link,
            @Part("yelp_link") RequestBody yelp_link,
            @Part("appointment_from") RequestBody appointment_from,
            @Part("appointment_to") RequestBody appointment_to,
            @Part("breaktime_from") RequestBody breaktime_from,
            @Part("breaktime_to") RequestBody breaktime_to,
            @Part("experience") RequestBody experience,
            @Part("license_number") RequestBody license_number,
            @Part MultipartBody.Part body,
            @Part MultipartBody.Part driving,
            @Part MultipartBody.Part identityCard
    );


    @Multipart
    @POST("createProfile")
    Call<JsonObject> createProfile(
            @Part("user_id") RequestBody userId,
            @Part("firstname") RequestBody firstName,
            @Part("lastname") RequestBody lastName,
            @Part("address") RequestBody address,
            @Part("phone") RequestBody phone,
            @Part("city") RequestBody city,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("fb_link") RequestBody fb_link,
            @Part("ins_link") RequestBody ins_link,
            @Part("yelp_link") RequestBody yelp_link,
            @Part("appointment_from") RequestBody appointment_from,
            @Part("appointment_to") RequestBody appointment_to,
            @Part("experience") RequestBody experience);


    @Multipart
    @POST("createProfile")
    Call<JsonObject> createProfile(
            @Part("user_id") RequestBody userId,
            @Part("firstname") RequestBody firstName,
            @Part("lastname") RequestBody lastName,
            @Part("address") RequestBody address,
            @Part("phone") RequestBody phone,
            @Part("city") RequestBody city,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("fb_link") RequestBody fb_link,
            @Part("ins_link") RequestBody ins_link,
            @Part("yelp_link") RequestBody yelp_link,
            @Part("appointment_from") RequestBody appointment_from,
            @Part("appointment_to") RequestBody appointment_to,
            @Part("experience") RequestBody experience,
            @Part MultipartBody.Part body);


    @Multipart
    @POST("editMyDocument")
    Call<JsonObject> editMyDocument(
            @Part("user_id") RequestBody userId,
            @Part("license_number") RequestBody license_number,
            @Part MultipartBody.Part driving,
            @Part MultipartBody.Part identity);

    @Multipart
    @POST("editMyDocument")
    Call<JsonObject> editMyDocument(
            @Part("user_id") RequestBody userId,
            @Part("license_number") RequestBody license_number,
            @Part MultipartBody.Part driving);


    @FormUrlEncoded
    @POST("newAppointments")
    Call<JsonObject> newAppointmentsApi
            (@Field("provider_id") String userid

            );

    @FormUrlEncoded
    @POST("logout")
    Call<JsonObject> logoutApi
            (@Field("user_id") String userid

            );

    @FormUrlEncoded
    @POST("history")
    Call<JsonObject> history
            (@Field("user_id") String userid,
             @Field("user_type") String userType
            );

    @FormUrlEncoded
    @POST("changeNotification")
    Call<JsonObject> changeNotification(@Field("user_id") String userid,
                                        @Field("type") String type
    );

    @FormUrlEncoded
    @POST("suggestedServices")
    Call<JsonObject> suggestedServices
            (@Field("user_id") String userid

            );


    @FormUrlEncoded
    @POST("contactUs")
    Call<JsonObject> contactUs
            (@Field("user_id") String userid,
             @Field("name") String name,
             @Field("subject") String subject,
             @Field("email") String email,
             @Field("message") String massage
            );


    @FormUrlEncoded
    @POST("addBank")
    Call<JsonObject> addAccountNumber
            (@Field("user_id") String userid,
             @Field("bankName") String bankName,
             @Field("name") String name,
             @Field("accountNumber") String accountNumber,
             @Field("routingNumber") String routingNumber,
             @Field("postalcode") String postalcode,
             @Field("region") String region,
             @Field("locality") String locality,
             @Field("phone") String phone

            );


    @FormUrlEncoded
    @POST("addBank")
    Call<JsonObject> addBankApi
            (@Field("user_id") String userid
            );


    @FormUrlEncoded
    @POST("acceptRejectAppointments")
    Call<JsonObject> acceptRejectAppointments
            (@Field("user_id") String userid,
             @Field("booking_id") String appointmentId,
             @Field("status") String status


            );

    @FormUrlEncoded
    @POST("appointmentDetail")
    Call<JsonObject> appointmentDetail
            (@Field("user_id") String userid,
             @Field("user_type") String user_type,
             @Field("booking_id") String booking_id,
             @Field("latitude") String latitude,
             @Field("longitude") String longitude
            );

    @FormUrlEncoded
    @POST("startService")
    Call<JsonObject> startService
            (@Field("provider_id") String userid,
             @Field("booking_id") String appointmentId
            );

    @FormUrlEncoded
    @POST("viewPortfolio")
    Call<JsonObject> viewPortfolio
            (@Field("user_id") String userid
            );


    @FormUrlEncoded
    @POST("myReviews")
    Call<JsonObject> reviewList(@Field("user_id") String userid,
                                @Field("profile_id") String profileId);


    @FormUrlEncoded
    @POST("showServices")
    Call<JsonObject> getServices(@Field("user_id") String userid);


    @FormUrlEncoded
    @POST("deletePortfolio")
    Call<JsonObject> deletePortfolio(@Field("user_id") String userid,
                                     @Field("portfolio_id") String portfolio_id);


    @Multipart
    @POST("addPortfolio")
    Call<JsonObject> addPortfolio
            (@Part("user_id") RequestBody userid,
             @Part("title") RequestBody title,
             @Part("description") RequestBody description,
             @Part("type") RequestBody type,
             @Part MultipartBody.Part body,
             @Part MultipartBody.Part thumb
            );


    @Multipart
    @POST("addPortfolio")
    Call<JsonObject> addPortfolio
            (@Part("user_id") RequestBody userid,
             @Part("title") RequestBody title,
             @Part("description") RequestBody description,
             @Part("type") RequestBody type,
             @Part MultipartBody.Part body
            );


    @Multipart
    @POST("editPortfolio")
    Call<JsonObject> editPortfolio
            (@Part("portfolio_id") RequestBody portfolio_id,
             @Part("user_id") RequestBody userid,
             @Part("title") RequestBody title,
             @Part("description") RequestBody description,
             @Part("type") RequestBody type,
             @Part MultipartBody.Part body,
             @Part MultipartBody.Part thumb
            );

    @Multipart
    @POST("editPortfolio")
    Call<JsonObject> editPortfolio
            (@Part("portfolio_id") RequestBody portfolio_id,
             @Part("user_id") RequestBody userid,
             @Part("title") RequestBody title,
             @Part("description") RequestBody description,
             @Part("type") RequestBody type,
             @Part MultipartBody.Part body
            );

    @Multipart
    @POST("editPortfolio")
    Call<JsonObject> editPortfolio
            (@Part("portfolio_id") RequestBody portfolio_id,
             @Part("user_id") RequestBody userid,
             @Part("title") RequestBody title,
             @Part("description") RequestBody description,
             @Part("type") RequestBody type
            );


    @FormUrlEncoded
    @POST("rating")
    Call<JsonObject> rating
            (@Field("rater_id") String userId,
             @Field("ratable_id") String otherId,
             @Field("booking_id") String booking_id,
             @Field("rating") String rating,
             @Field("description") String description
            );

    @Multipart
    @POST("sendMessage")
    Call<JsonObject> sendMessage
            (@Part("sender_id") RequestBody userId,
             @Part("receiver_id") RequestBody otherId,
             @Part("message") RequestBody message,
             @Part MultipartBody.Part body
            );

    @FormUrlEncoded
    @POST("getConversations")
    Call<JsonObject> getConversation(@Field("sender_id") String userId,
                                     @Field("receiver_id") String otherId,
                                     @Field("connection_id") String connection_id

    );


    @FormUrlEncoded
    @POST("messages")
    Call<JsonObject> getMessage(@Field("user_id") String userid
    );


    @FormUrlEncoded
    @POST("deleteConnections")
    Call<JsonObject> deleteConnections(@Field("user_id") String userid,
                                       @Field("connection_id") String connection_id
    );

    @Multipart
    @POST("sendMessage")
    Call<JsonObject> sendMessage
            (@Part("sender_id") RequestBody userId,
             @Part("receiver_id") RequestBody otherId,
             @Part("message") RequestBody message
            );


    @FormUrlEncoded
    @POST("endService")
    Call<JsonObject> endService
            (@Field("provider_id") String userid,
             @Field("booking_id") String appointmentId
            );

    @FormUrlEncoded
    @POST("cancelAppointment")
    Call<JsonObject> cancelAppointment
            (@Field("user_id") String userid,
             @Field("booking_id") String appointmentId,
             @Field("user_type") String user_type,
             @Field("cancel_description") String cancelMessage
            );


    @POST("addService")
    Call<JsonObject> addService
            (@Body RequestBody service

            );


    @POST("addTimeForAppointment")
    Call<JsonObject> addTimeForAppointment
            (@Body RequestBody service

            );




    @FormUrlEncoded
    @POST("deleteAppointment")
    Call<JsonObject> deleteAppoitnment
            (@Field("user_id") String userid,
             @Field("appointmentTime_id") String appointmentTime_id


            );


    @FormUrlEncoded
    @POST("seeAppointment")
    Call<JsonObject> seeAppointment(@Field("user_id") String userid,
                                    @Field("provider_id") String provider_id,
                                    @Field("date") String date
    );

    @FormUrlEncoded
    @POST("updateUserServices")
    Call<JsonObject> updateUserServices
            (@Field("user_id") String userid,
             @Field("service_id") String service_id,
             @Field("price") String price

            );

    @FormUrlEncoded
    @POST("deleteUserServices")
    Call<JsonObject> deleteService
            (@Field("user_id") String userid,
             @Field("service_id") String service_id

            );

    @FormUrlEncoded
    @POST("providerAppointments")
    Call<JsonObject> seeProviderAppointment(@Field("provider_id") String provider_id,
                                            @Field("date") String date
    );


    @FormUrlEncoded
    @POST("logout")
    Call<JsonObject> logout(@Field("user_id") String remember_token,
                            @Field("remember_token") String userid

    );



    /*@POST("verify_email")
    Call<Object> verify_email(@Body RequestBody requestBody);


    @POST("get_profile")
    Call<JsonObject> get_profileApi(@Body RequestBody requestBody);



    @POST("schools")
    Call<JsonObject> schoolsApi(@Body RequestBody requestBody);

    @POST("settings")
    Call<JsonObject> settinApi(@Body RequestBody requestBody);

    @POST("set_settings")
    Call<JsonObject> set_settinApi(@Body RequestBody requestBody);*/

}
