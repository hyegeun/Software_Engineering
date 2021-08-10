package com.example.sogong_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapterBoard extends RecyclerView.Adapter<CustomAdapterBoard.CustomViewHolder> {

    private ArrayList<BoardInfo> arrayList;
    private Context context;
    private String groupName;


    public CustomAdapterBoard(ArrayList<BoardInfo> arrayList, Context context,String groupName) {
        this.arrayList = arrayList;
        this.context = context;
        this.groupName = groupName;

    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_myboard, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getBoardName())
                .into(holder.img_board);
        holder.tv_boardName.setText(arrayList.get(position).getBoardName());
        holder.tv_boardPurpose.setText(arrayList.get(position).getBoardPurpose());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView img_board;
        TextView tv_boardName;
        TextView tv_boardPurpose;

        public CustomViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            this.img_board = itemView.findViewById(R.id.img_board);
            this.tv_boardName = itemView.findViewById(R.id.tv_boardName);
            this.tv_boardPurpose = itemView.findViewById(R.id.tv_boardPurpose);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Intent intent=new Intent(context,BoardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("GROUPNAME", groupName);
                        intent.putExtra("BOARDNAME", arrayList.get(pos).getBoardName());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
