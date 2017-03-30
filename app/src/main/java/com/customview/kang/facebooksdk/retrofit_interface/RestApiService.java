package com.customview.kang.facebooksdk.retrofit_interface;

import com.customview.kang.facebooksdk.User;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by kang on 2017-03-30.
 */

public interface RestApiService {
    @POST("")
    Call<User> getUserPassword(@Query("email") String email);
    @POST("")
    Call<User> setUserData(@Query("email") String email,
                           @Query("password") String password,
                           @Query("name") String name,
                           @Query("gender") String gender,
                           @Query("age") Integer age
    );
}
