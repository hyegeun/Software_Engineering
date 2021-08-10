package com.example.sogong_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostContentActivity extends AppCompatActivity {

    TextView Groupnm;
    TextView Boardnm;
    TextView GrpBod;
    TextView postTitle;
    TextView postContent;
    TextView postWriter;
    Intent groupIntent;
    String st_groupName;
    String st_boardName;
    String st_postTitle;

    String path;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

        groupIntent = getIntent();   //이전 화면으로부터 인자 전달받음


        //board_screen_layout 에서 컴포넌트들 받아옴
        postTitle = (TextView) findViewById(R.id.postTitle);
        postContent = (TextView) findViewById(R.id.postContent);
        postWriter = (TextView) findViewById(R.id.postWriter);
        Boardnm = (TextView) findViewById(R.id.bdnamepost);
        Groupnm = (TextView) findViewById(R.id.grpnamepost);
        GrpBod = (TextView) findViewById(R.id.grpbdpost);
        findViewById(R.id.gotoboard).setOnClickListener(onClickListener);


        //이전 화면에서 클릭한 게시판의 이름을 제목으로 사용
        st_groupName=groupIntent.getStringExtra("GROUPNAME");
        st_boardName=groupIntent.getStringExtra("BOARDNAME");
        st_postTitle=groupIntent.getStringExtra("POSTNAME");
        Groupnm.setText(st_groupName);
        Boardnm.setText(st_boardName);
        postTitle.setText(st_postTitle);

        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        path = "GroupInfo/" + st_groupName + "/BoardInfo/" + st_boardName + "/PostInfo";
        databaseReference = database.getReference(path); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){


                    PostInfo newPost = snapshot.getValue(PostInfo.class); //만들어뒀던 User 객체에 데이터를 담는다.
                    if(newPost.getTitle().equals(postTitle.getText())){
                        postContent.setText(newPost.getContent());
                        postWriter.setText(newPost.getWriter());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                //디비를 가져오던중 에러 발생 시
                Log.e("PostContentActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.gotoboard:
                    startgoboard();
                    break;
            }
        }
    };

    private void startgoboard() {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("GROUPNAME", st_groupName);
        intent.putExtra("BOARDNAME", st_boardName);

        startActivity(intent);

    }


}