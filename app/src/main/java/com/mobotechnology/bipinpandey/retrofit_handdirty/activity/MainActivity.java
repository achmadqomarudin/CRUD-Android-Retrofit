package com.mobotechnology.bipinpandey.retrofit_handdirty.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobotechnology.bipinpandey.retrofit_handdirty.R;
import com.mobotechnology.bipinpandey.retrofit_handdirty.adapter.NoticeAdapter;
import com.mobotechnology.bipinpandey.retrofit_handdirty.model.Model;
import com.mobotechnology.bipinpandey.retrofit_handdirty.model.Notice;
import com.mobotechnology.bipinpandey.retrofit_handdirty.model.NoticeList;
import com.mobotechnology.bipinpandey.retrofit_handdirty.my_interface.GetNoticeDataService;
import com.mobotechnology.bipinpandey.retrofit_handdirty.network.RetrofitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private NoticeAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    Retrofit getApiService;
    private Dialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        fetchData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 7000);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postData();
            }
        });

        fetchData();
    }

    private void setView() {
        swipeRefreshLayout = findViewById(R.id.refresh);
        fabAdd = findViewById(R.id.fab_add);
    }

    private void postData() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.tambah_data);
        dialog.setTitle("Ubah Profile");
        getApiService = RetrofitInstance.getRetrofitInstance();

        final EditText etJudul = dialog.findViewById(R.id.et_judul_dialog);
        final EditText etIsi = dialog.findViewById(R.id.et_isi_dilog);

        final Button dialogButton = dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String judul = etJudul.getText().toString();
                final String isi = etIsi.getText().toString();
                final int id = 1;

                if (!TextUtils.isEmpty(judul) || !TextUtils.isEmpty(isi)) {

                    Model model = new Model(judul, isi, id);

                    GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
                    Call<Model> results = service.simpan(model);
                    results.enqueue(new Callback<Model>() {
                        @Override
                        public void onResponse(Call<Model> call, Response<Model> response) {
                            dialog.dismiss();

                            if (response.isSuccessful()) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response.body().toString());

                                    jsonObject.put("name", judul);
                                    jsonObject.put("description", isi);
                                    jsonObject.put("privacy", id);

                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(getApplicationContext(), "message : " + jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Model> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void fetchData() {

        /** Bikin handle untuk RetrofitInstance interface*/
        GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);

        /** Menggil method dg parameter di interface untuk mendapatkan data notice*/
        Call<NoticeList> call = service.getNoticeData();

        /**Log URL yg dipanggil*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<NoticeList>() {
            @Override
            public void onResponse(Call<NoticeList> call, final Response<NoticeList> response) {

                try {
                    if (response != null) {
                        generateNoticeList(response.body().getNoticeArrayList());
                    } else {
                        Toast.makeText(MainActivity.this, "Data tidak ada, coba cek internet anda", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Maaf, sistem kami sedang mengalami gangguan teknis.\n" +
                            "Silahkan coba beberapa saat lagi.", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<NoticeList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ada kesalahan dalam aplikasi : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method untuk generate List di notice menggunakan RecyclerView dg custom adapter
     */
    private void generateNoticeList(ArrayList<Notice> noticeArrayList) {
        recyclerView = findViewById(R.id.recycler_view_notice_list);
        adapter = new NoticeAdapter(noticeArrayList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
