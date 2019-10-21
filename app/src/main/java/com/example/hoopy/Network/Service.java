package com.example.hoopy.Network;


import com.example.hoopy.NetworkResponse.FileUploadResponse;
import com.example.hoopy.NetworkResponse.UserAddResponse;
import com.example.hoopy.NetworkResponse.UserGetResponse;
import com.example.hoopy.models.User;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service {
    @Multipart
    @POST("upload_test")
    Single<Response<FileUploadResponse>> addPhoto(@Part MultipartBody.Part file);

    @POST("insert_test")
    Single<Response<UserAddResponse>> addUser(@Body User body);


    @FormUrlEncoded
    @POST("fetch_data_test")
    Single<Response<UserGetResponse>> getUser( @Field("username") String username);

    @FormUrlEncoded
    @POST("update_data_test")
    Single<Response<UserAddResponse>> updateUser(@Field("user_id") Long user_id,@Field("name") String name, @Field("username") String username,
                                                 @Field("contact") Long contact,@Field("email") String email);
}
