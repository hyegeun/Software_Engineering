package com.example.sogong_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapterPost extends RecyclerView.Adapter<CustomAdapterPost.PostViewHolder> {

    private ArrayList<PostInfo> arrayList;
    private Context context;
    private String groupName;
    private String boardName;

    public CustomAdapterPost(ArrayList<PostInfo>arrayList, Context context, String groupName, String boardName) {
        this.arrayList = arrayList;
        this.context = context;
        this.groupName = groupName;
        this.boardName = boardName;

    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, parent, false);
        PostViewHolder holder = new PostViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_writer.setText(arrayList.get(position).getWriter());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {


        TextView tv_title;
        TextView tv_writer;


        public PostViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.tv_postTitle);
            this.tv_writer = itemView.findViewById(R.id.tv_postWriter);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Intent intent=new Intent(context,PostContentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("GROUPNAME", groupName);
                        intent.putExtra("BOARDNAME", boardName);
                        intent.putExtra("POSTNAME", String.valueOf(arrayList.get(pos).getTitle()));
                        context.startActivity(intent);
                    }
                }
            });

        }


    }
}
