package com.example.sogong_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapterInsertGroup extends RecyclerView.Adapter<CustomAdapterInsertGroup.CustomInsertGroupViewHolder> {

    private ArrayList<GroupInfo> arrayList;
    private Context context;


    public CustomAdapterInsertGroup(ArrayList<GroupInfo>arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CustomInsertGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mygroup, parent, false);
        CustomInsertGroupViewHolder holder = new CustomInsertGroupViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomInsertGroupViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getGroup())
                .into(holder.iv_profile);
        holder.tv_name.setText(arrayList.get(position).getGroup());
        holder.tv_purpose.setText(arrayList.get(position).getPurpose());
        holder.tv_king.setText(arrayList.get(position).getUser());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomInsertGroupViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile;
        TextView tv_name;
        TextView tv_purpose;
        TextView tv_king;

        public CustomInsertGroupViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_purpose = itemView.findViewById(R.id.tv_purpose);
            this.tv_king = itemView.findViewById(R.id.tv_king);

            itemView.setClickable(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){

                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        AlertDialog ad=builder.create();
                        builder.setTitle("????????????");
                        builder.setMessage("?????? ???????????????????");
                        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                String groupName= (String) tv_name.getText();
                                DatabaseReference mDatabaseRef;
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = firebaseUser.getUid();
                                String path="UserGroup/" +uid;
                                mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);
                                mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                        // ?????????????????? ????????????????????? ???????????? ???????????? ???
                                        boolean ifMemberOfGroup = false;
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            GroupInfo myGroups = snapshot.getValue(GroupInfo.class); //??????????????? User ????????? ???????????? ?????????.

                                            if(groupName.equals(myGroups.getGroup())){
                                                Toast.makeText(context,"?????? ???????????? ????????????.",Toast.LENGTH_SHORT).show();
                                                ifMemberOfGroup=true;
                                                break;
                                            }
                                        }
                                        if(!ifMemberOfGroup){
                                            mDatabaseRef.child(groupName).setValue(arrayList.get(pos));
                                            Toast.makeText(context,"????????? ??????????????????",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(context,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                                        //????????? ??????????????? ?????? ?????? ???
                                        Log.e("InsertGroupActivity", String.valueOf(databaseError.toException())); // ????????? ??????
                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ad.dismiss();
                                    }
                                }
                        );
                        builder.create().show();

                    }
                }
            });

        }


    }
}
