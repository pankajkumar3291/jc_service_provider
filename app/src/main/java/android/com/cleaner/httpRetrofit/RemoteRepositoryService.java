package android.com.cleaner.httpRetrofit;
import android.com.cleaner.apiResponses.allPreviousJobs.AllPreviousJobs;
import android.com.cleaner.apiResponses.bookACleaner.BookCleaner;
import android.com.cleaner.apiResponses.cancelled_appintments.GetCancelledAppointments;
import android.com.cleaner.apiResponses.changePassword.ChangePassword;
import android.com.cleaner.apiResponses.completedJobs.CustomerCompletedJobs;
import android.com.cleaner.apiResponses.customerCurrentJobs.CustomerCurrentJobs;
import android.com.cleaner.apiResponses.customerFullDetailsApis.CustomerFullDetails;
import android.com.cleaner.apiResponses.dailyJobScheduled.DailyJobScheduled;
import android.com.cleaner.apiResponses.filteredCleaner.FilteredCleaner;
import android.com.cleaner.apiResponses.forgetPasswordResponse.ForgetPassword;
import android.com.cleaner.apiResponses.getAllPriceList.GetAllPriceList;
import android.com.cleaner.apiResponses.getAllZipcode.GetAllZipcodeMain;
import android.com.cleaner.apiResponses.getCityList.CityList;
import android.com.cleaner.apiResponses.getFAQ.FAQ;
import android.com.cleaner.apiResponses.getStateList.GetStateList;
import android.com.cleaner.apiResponses.getZipcodeList.ZipCodeList;
import android.com.cleaner.apiResponses.logout.LogoutApi;
import android.com.cleaner.apiResponses.matchOtp.MatchOTP;
import android.com.cleaner.apiResponses.monthlyJobScheduled.MonthlyJobScheduled;
import android.com.cleaner.apiResponses.offerredServices.OfferredServices;
import android.com.cleaner.apiResponses.previousJobsDetails.PreviousJobDetails;
import android.com.cleaner.apiResponses.refferalCode.RefferalCode;
import android.com.cleaner.apiResponses.requestModelsForMonthlyJob.MonthlyJobRequestModel;
import android.com.cleaner.apiResponses.reviewsAddedByProvider.ReviewsAdded;
import android.com.cleaner.apiResponses.signInResponse.SignIn;
import android.com.cleaner.apiResponses.signUpResponse.SignUp;
import android.com.cleaner.apiResponses.specialRequestToCleaner.SpecialRequestToCleaner;
import android.com.cleaner.apiResponses.typesOfServices.TypesOfSerives;
import android.com.cleaner.apiResponses.updateCustomerProfile.UpdateCustomerProfile;
import android.com.cleaner.apiResponses.updateProfile.GetProfile;
import android.com.cleaner.apiResponses.weeklyJobScheduled.AllWorkingDays;
import android.com.cleaner.apiResponses.weeklyJobScheduled.weeklyJobScheduledResponse.WeeklyJobScheduled;
import android.com.cleaner.models.CancelAppointmentByCustomerModel;
import android.com.cleaner.requestModelsForWeeklyJob.WeeklyJobRequestModel;
import io.reactivex.Observable;
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
import retrofit2.http.Path;
public interface RemoteRepositoryService {
    //  1- MARK:SIGNIN
    @FormUrlEncoded
    @POST("customer/userLogin")
    Call<SignIn> signInAPI(@Field("email") String email,
                           @Field("password") String password,
                           @Field("device_id") String device_id);
    // 2- MARK: REGISTER SIGNUP
    @FormUrlEncoded
    @POST("customer/userRegister")
    Call<SignUp> signUpAPI(@Field("first_name") String first_name,
                           @Field("last_name") String last_name,
                           @Field("email") String email,
                           @Field("password") String password,
                           @Field("mobile") String mobile,
                           @Field("address") String address,
                           @Field("device_type") String device_type,
                           @Field("role") String role,
                           @Field("state") Integer state,
                           @Field("city") String city,
                           @Field("zipCode") Integer zipCode,
                           @Field("device_id") String device_id); // device id = Firebase Generated toekn
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
    @GET("customer/getAllServiceTypeList/{language}")
    Call<TypesOfSerives> makingTypesOfServices(@Path("language")String language);
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
    @GET("customer/getAllServiceTypeList/{language}")
    Observable<OfferredServices> getOffrredServicesAPI(@Path("language")String language);
    // MARK: FAQ API - 14
    @GET("customer/getcustomerFaQ")
    Observable<FAQ> getFAQAPI();
    //MARK: BOOK A CLEANER - 15
    @FormUrlEncoded
    @Headers("Cache-Control: max-age=640000")
    @POST("customer/InstantBooking")
    Observable<BookCleaner> bookCleanerAPI(@Field("id") String id,
                                           @Field("zipcode") String zipcode,
                                           @Field("date") String date,
                                           @Field("time") String time,
                                           @Field("servicetypes") String servicetypes,
                                           @Field("address") String address);
    // BOOK A CLEANER TEST
    //LogoutApi
    @FormUrlEncoded
    @Headers("Cache-Control: max-age=640000")
    @POST("customer/InstantBooking")
    Call<BookCleaner> bookACleanerTest(@Field("id") String id,
                                       @Field("zipcode") String zipcode,
                                       @Field("date") String date,
                                       @Field("time") String time,
                                       @Field("servicetypes") String servicetypes,
                                       @Field("address") String address);



    @FormUrlEncoded
    @POST("customer/logout")
    Call<LogoutApi> logOut(@Field("id") String id);



    // MARK: GET ALL ZIPCODE -16
    @GET("customer/getAllZipcodes")
    Observable<GetAllZipcodeMain> getAllZipcodeAPI();
    // MARK: FILTERED CLEANER API - 17
    @FormUrlEncoded
    @POST("customer/showserviceproviderprofile")
    Observable<FilteredCleaner> filteredCleanerApi(@Field("provider_id") String provider_id);
    // MARK: CUSTOMER FULL DETAILS API------18
    @FormUrlEncoded
    @POST("customer/customerfullDetails")
    Observable<CustomerFullDetails> customerFullDetailsApi(@Field("id") String id);
    // MARK: UPDATE CUSTOMER PROFILE API ------19
    @FormUrlEncoded
    @POST("customer/updateCustomerProfile")
    Observable<UpdateCustomerProfile> updateCustomerProfileApi(@Field("id") String id,
                                                               @Field("first_name") String first_name,
                                                               @Field("last_name") String last_name,
                                                               @Field("mobile") String mobile,
                                                               @Field("address") String address,
                                                               @Field("state") String state,
                                                               @Field("city") String city,
                                                               @Field("zipCode") String zipCode);
    // MARK: COMPLETED JOBS API ------20
    @FormUrlEncoded
    @POST("customer/ListOfCompletedJobsForCustomer/{language}")
    Observable<CustomerCompletedJobs> customerCompletedJobsApi(@Path("language")String language,@Field("Customer_id") String Customer_id);
    // MARK: CURRENT JOBS API   21
    @FormUrlEncoded
    @POST("customer/ListOfCurrentJobsForCustomer/{language}")
    Observable<CustomerCurrentJobs> customerCurrentJobsApi(@Path("language")String language,@Field("Customer_id") String Customer_id);
    // MARK: DAILY JOB SCHEDULED  22
    @FormUrlEncoded
    @POST("customer/DailyScheduleingJob")
    Observable<DailyJobScheduled> dailyJobScheduled(@Field("customer_id") String customer_id,
                                                    @Field("time") String time,
                                                    @Field("zipcode") String zipcode,
                                                    @Field("address") String address,
                                                    @Field("service_ids") String service_ids);
    // MARK: ALL WORKING DAYS 23
    @GET("customer/allworkingdays")
    Observable<AllWorkingDays> allWorkingDays();
    // WEEKLY JOB SCHEDULED  24
    @POST("customer/WeeklyScheduleingJob")
    Observable<WeeklyJobScheduled> weeklyJobScheduled(@Body WeeklyJobRequestModel weeklyJobRequestModel);
    // MONTHLY JOB SCHEDULED  25
    @POST("customer/MonthlyScheduleingJob")
    Observable<MonthlyJobScheduled> monthlyJobScheduled(@Body MonthlyJobRequestModel monthlyJobRequestModel);
    // SPECIAL REQUEST TO THE CLEANER  26
    @FormUrlEncoded
    @POST("customer/SpecialRequestToCleaner")
    Observable<SpecialRequestToCleaner> specialRequestToCleaner(@Field("Job_id") String Job_id,
                                                                @Field("customer_id") String customer_id,
                                                                @Field("Provider_id") String Provider_id,
                                                                @Field("packing_instrustion") String packing_instrustion,
                                                                @Field("special_Request") String special_Request);
    // REVIEWS ADDED 27
    @FormUrlEncoded
    @POST("customer/providerReviews")
    Observable<ReviewsAdded> reviewsAdded(@Field("customer_id") String customer_id,
                                          @Field("job_id") String job_id,
                                          @Field("provider_id") String provider_id,
                                          @Field("review") String review,
                                          @Field("comment") String comment);



    @FormUrlEncoded
    @POST("customer/CanceljobByCustomer")
    Call<CancelAppointmentByCustomerModel> cancelAppointmentByCustomer(@Field("job_id") String language,
                                                                           @Field("cutomer_id") String Customer_id);


    // REFERRAL CODE API 28
    @FormUrlEncoded
    @POST("customer/showReferralcode")
    Observable<RefferalCode> refferalCode(@Field("Customer_id") String Customer_id);
    // All Previous Jobs  29
    @FormUrlEncoded
    @POST("customer/AllPreviousJobs/{language}")
    Observable<AllPreviousJobs> allPreviousJobs(@Path("language") String language,
                                                @Field("Customer_id") String Customer_id);
    // PREVIOUS JOB DETAILS 30
    @FormUrlEncoded
    @POST("customer/FullPreviousJobDetails/{language}")
    Observable<PreviousJobDetails> previousJobsDetails(@Path("language")String language,
                                                       @Field("provider_id") String provider_id,
                                                       @Field("job_id") String job_id,
                                                       @Field("customer_id") String customerId);
    @FormUrlEncoded
    @POST("customer/ReebookPreviousCleaner")
    Observable<PreviousJobDetails> rebookCleaner(@Field("job_id") String job_id,
                                                 @Field("customer_id") String customer_id,
                                                 @Field("date") String date,
                                                 @Field("time") String time);
    @FormUrlEncoded
    @POST("customer/ListOfCanceledJobsForCustomer/{language}")
    Call<GetCancelledAppointments> getCanelledAppointments(@Path("language")String language,
                                                           @Field("Customer_id") String customer_id);




}
