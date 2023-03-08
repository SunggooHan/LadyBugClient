package com.sweteam5.ladybugclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sweteam5.ladybugclient.R;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.noticeViewHolder> {
    Context context;
    ArrayList<NoticeInfo> noticeArrayList;
    FirebaseFirestore db;

    public NoticeAdapter(Context context, ArrayList<NoticeInfo> noticeArrayList){
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }


    @NonNull
    @Override
    public NoticeAdapter.noticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.notice_group_layout, parent, false);
        return new noticeViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.noticeViewHolder holder, int position) {
        NoticeInfo notice = noticeArrayList.get(position);

        holder.title.setText(notice.title);
        holder.date.setText(notice.date);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notice를 자세히 보는 창으로 이동
                Intent intent = new Intent(context, NoticeContentActivity.class);
                Bundle noticeBundle = new Bundle();
                noticeBundle.putString("title", notice.getTitle());
                noticeBundle.putString("content", notice.getContent());
                noticeBundle.putString("date", notice.getDate());
                intent.putExtra("notice", noticeBundle);
                context.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    public static class noticeViewHolder extends RecyclerView.ViewHolder{
        TextView title, date;
        public noticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.noticeTitleTextView);
            date = itemView.findViewById(R.id.noticeDateTimeTextView);
            //content = itemView.findViewById(R.id.noticeWriteContentEditText);
        }
    }
}
