package com.mobotechnology.bipinpandey.retrofit_handdirty.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobotechnology.bipinpandey.retrofit_handdirty.R;
import com.mobotechnology.bipinpandey.retrofit_handdirty.activity.DetailNoteActivity;
import com.mobotechnology.bipinpandey.retrofit_handdirty.model.Notice;
import com.mobotechnology.bipinpandey.retrofit_handdirty.my_interface.GetNoticeDataService;
import com.mobotechnology.bipinpandey.retrofit_handdirty.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    Context context;
    private ArrayList<Notice> dataList;

    public NoticeAdapter(ArrayList<Notice> dataList, Context context) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_view_row, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoticeViewHolder holder, final int position) {
        holder.txtNoticeTitle.setText(dataList.get(position).getTitle());
        holder.txtNoticeBrief.setText(dataList.get(position).getBrief());
        holder.txtNoticeFilePath.setText(dataList.get(position).getFileSource());
        holder.Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailNoteActivity.class);
                i.putExtra("judul", dataList.get(position).getTitle());
                i.putExtra("isi", dataList.get(position).getBrief());
                i.putExtra("id", dataList.get(position).getId());
                context.startActivity(i);
            }
        });
        holder.Container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(view.getRootView().getContext());
                dialog.setMessage("Apakah anda yakin ingin menghapusnya?");
                dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        GetNoticeDataService getNoticeDataService = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
                        Call<Void> result = getNoticeDataService.deletePost(dataList.get(position).getId());

                        result.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
                dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder {

        TextView txtNoticeTitle, txtNoticeBrief, txtNoticeFilePath;
        CardView Container;

        NoticeViewHolder(View itemView) {
            super(itemView);
            txtNoticeTitle = itemView.findViewById(R.id.txt_notice_title);
            txtNoticeBrief = itemView.findViewById(R.id.txt_notice_brief);
            txtNoticeFilePath = itemView.findViewById(R.id.txt_notice_file_path);
            Container = itemView.findViewById(R.id.container);
        }
    }
}