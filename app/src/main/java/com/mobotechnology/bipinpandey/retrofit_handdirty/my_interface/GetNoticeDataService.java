package com.mobotechnology.bipinpandey.retrofit_handdirty.my_interface;

import com.mobotechnology.bipinpandey.retrofit_handdirty.model.Model;
import com.mobotechnology.bipinpandey.retrofit_handdirty.model.NoticeList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetNoticeDataService {

    @GET("folder")
    Call<NoticeList> getNoticeData();

    @FormUrlEncoded
    @POST("folder")
    Call<ResponseBody> savePost(
            @Field("name") String name,
            @Field("description") String description,
            @Field("privacy") int privacy
    );

    @POST("folder")
    Call<Model> simpan(
            @Body Model model
    );

    @PUT("folder/{id}")
    Call<Model> update(
            @Path("id") String id,
            @Body Model model
    );

    @DELETE("folder/{id}")
    Call<Void> deletePost(@Path("id") String id);
}