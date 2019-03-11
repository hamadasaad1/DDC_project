package com.ibnsaad.ddc.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitApi {

    @FormUrlEncoded
    @POST("getdata.php?type=signup")
    Call<ResponseBody> createUser(
            @Field("first_name")String name,
            @Field("last_name") String lname,
            @Field("address") String adress,
            @Field("tel") String mobile ,
            @Field("email_address") String email ,
            @Field("password") String password,
            @Field("disability") String disability,
            @Field("sex") String sex,
            @Field("date_of_birth") String date,
            @Field("documentation") String documentation

    );
}
