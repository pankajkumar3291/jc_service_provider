package android.com.cleaner.httpRetrofit;

import android.com.cleaner.apiResponses.bookACleaner.BookCleaner;
import android.com.cleaner.apiResponses.changePassword.ChangePassword;
import android.com.cleaner.apiResponses.forgetPasswordResponse.ForgetPassword;
import android.com.cleaner.apiResponses.getAllPriceList.GetAllPriceList;
import android.com.cleaner.apiResponses.getCityList.CityList;
import android.com.cleaner.apiResponses.getFAQ.FAQ;
import android.com.cleaner.apiResponses.getStateList.GetStateList;
import android.com.cleaner.apiResponses.getZipcodeList.ZipCodeList;
import android.com.cleaner.apiResponses.matchOtp.MatchOTP;
import android.com.cleaner.apiResponses.offerredServices.OfferredServices;
import android.com.cleaner.apiResponses.signInResponse.SignIn;
import android.com.cleaner.apiResponses.signUpResponse.SignUp;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.apiResponses.updateProfile.GetProfile;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RemoteRepositoryService {


    //  1- MARK:SIGNIN

    @FormUrlEncoded
    @POST("customer/userLogin")
    Call<SignIn> signInAPI(@Field("email") String email,
                           @Field("password") String password);


    // 2- MARK: REGISTER SIGNUP


    @FormUrlEncoded
    @POST("customer/userRegister")
    Call<SignUp> signUpAPI(@Field("first_name") String first_name,
                           @Field("last_name") String last_name,
                           @Field("email") String email,
                           @Field("password") String password,
                           @Field("mobile") String mobile,
                           @Field("address") String address,
                           @Field("device_id") String device_id,
                           @Field("device_type") String device_type,
                           @Field("role") String role,
                           @Field("state") String state,
                           @Field("city") String city,
                           @Field("zipCode") String zipCode);


// 3-MARK: FORGET PASSWORD

    @FormUrlEncoded
    @POST("customer/forgotPassword")
    Call<ForgetPassword> forgetPasswordAPI(@Field("email") String email);


    // 4- MARK: MATCH OTP

    @FormUrlEncoded
    @POST("customer/matchOTP")
    Call<MatchOTP> matchOtpAPI(@Field("userId") String userId,
                               @Field("otp") String otp);


    // MARK: RESET PASSWORD - 5

    @FormUrlEncoded
    @POST("customer/resetPassword")
    Call<ForgetPassword> resetPasswordAPI(@Field("email") String email,
                                          @Field("password") String password);


    // MARK: ALLSERVICETYPE- 6

    @GET("customer/getAllServiceTypeList")
    Call<TypesOfSerives> makingTypesOfServices();


    // MARK: STATE - 7

    @GET("customer/getStateList")
    Call<GetStateList> getStateListAPI();


    // MARK: PRICE -8

    @FormUrlEncoded
    @POST("customer/getAllPriceList")
    Call<GetAllPriceList> getAllPriceListAPI(@Field("service_id") String service_id);


    // MARK: CITY API - 9

    @FormUrlEncoded
    @POST("customer/getCityList")
    Observable<CityList> getCitiesAPI(@Field("state_id") String state_id);


    // MARK: ZIPCODE API - 10

    @FormUrlEncoded
    @POST("customer/getZipcodeList")
    Observable<ZipCodeList> getZipCodeListAPI(@Field("city_id") String city_id);


    // MARK: PROFILE API - 11

    @Multipart
    @POST("customer/userProfilePic")
    Call<GetProfile> postImage(@Part("id") RequestBody id, // String will also take
                               @Part MultipartBody.Part image);


    // MARK: CHANGE PASSWORD API -12

    @FormUrlEncoded
    @POST("customer/changePassword")
    Observable<ChangePassword> changePasswordAPI(@Field("id") String id,
                                                 @Field("old_password") String old_password,
                                                 @Field("new_password") String new_password);


    // MARK: ALL TYPE SERVICE - OFFRRED SERVICES - 13

    @GET("customer/getAllServiceTypeList")
    Observable<OfferredServices> getOffrredServicesAPI();


    // MARK: FAQ API - 14


    @GET("customer/getcustomerFaQ")
    Observable<FAQ> getFAQAPI();


    //MARK: BOOK A CLEANER - 15

    @FormUrlEncoded
    @POST("customer/bookingcleaner")
    Observable<BookCleaner> bookCleanerAPI(@Field("cutomer_id") String cutomer_id,
                                           @Field("lat") String lat,
                                           @Field("long") String lng,
                                           @Field("date") String date,
                                           @Field("time") String time,
                                           @Field("status") String status);


}
