package com.mobotechnology.bipinpandey.retrofit_handdirty.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobotechnology.bipinpandey.retrofit_handdirty.R;
import com.mobotechnology.bipinpandey.retrofit_handdirty.model.Model;
import com.mobotechnology.bipinpandey.retrofit_handdirty.my_interface.GetNoticeDataService;
import com.mobotechnology.bipinpandey.retrofit_handdirty.network.RetrofitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etJudul, etIsi;
    private FloatingActionButton fbAdd;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        setView();
        setOnClick();
        setText();
    }

    private void setView() {
        etJudul = findViewById(R.id.et_judul);
        etIsi = findViewById(R.id.et_isi);
        fbAdd = findViewById(R.id.save_data);
    }

    private void setOnClick() {
        fbAdd.setOnClickListener(this);
    }

    private void setText() {
        etJudul.setText(getIntent().getStringExtra("judul"));
        etIsi.setText(getIntent().getStringExtra("isi"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_data:
                updateData();
                break;
        }
    }

    private void updateData() {
        final String judul = etJudul.getText().toString();
        final String isi = etIsi.getText().toString();
        final int id = 1;

        if (!TextUtils.isEmpty(judul) || !TextUtils.isEmpty(isi)) {

            Model model = new Model(judul, isi, id);

            String idn = getIntent().getStringExtra("id");

            GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
            Call<Model> results = service.update(idn, model);
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
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
