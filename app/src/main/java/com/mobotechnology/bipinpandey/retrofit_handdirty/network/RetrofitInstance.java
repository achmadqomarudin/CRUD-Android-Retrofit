package com.mobotechnology.bipinpandey.retrofit_handdirty.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://54d025cb.ngrok.io/api/";

    public static Retrofit getRetrofitInstance() {

        OkHttpClient Client = new OkHttpClient.Builder()
                .addInterceptor(

                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requesBuilder = original.newBuilder()
                                        .addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1NTUyOTI0NzksIm5iZiI6MTU1NTI5MjQ3OSwianRpIjoiNDJjOTVkNmItZjFjNS00NGFjLTllODEtY2UwYmMwOGQ2NzIyIiwiaWRlbnRpdHkiOiJBZ3VuZyIsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyJ9.3QntxBLWHh69WGy1Qbp8F_0ttg6T9Piba2FUNLZ3QyA")
                                        .method(original.method(), original.body());

                                Request request = requesBuilder.build();
                                return chain.proceed(request);
                            }
                        }

                ).build();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(Client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}