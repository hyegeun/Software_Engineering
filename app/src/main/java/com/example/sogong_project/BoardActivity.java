package com.example.sogong_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    TextView grpbd2;
    TextView boardTitle;
    TextView groupName;
    Intent boardIntent;
    String st_groupName;
    String st_boardName;
    String path;


    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostInfo> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        boardIntent = getIntent();   //이전 화면으로부터 인자 전달받음


        //board_screen_layout 에서 컴포넌트들 받아옴
        groupName = (TextView) findViewById(R.id.groupName);
        boardTitle = (TextView) findViewById(R.id.boardName);
        grpbd2 = (TextView) findViewById(R.id.grpbd2);

        //이전 화면에서 클릭한 게시판의 이름을 제목으로 사용
        st_groupName=boardIntent.getStringExtra("GROUPNAME");
        st_boardName=boardIntent.getStringExtra("BOARDNAME");
        groupName.setText(st_groupName);
        boardTitle.setText(st_boardName);

        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);  //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        path = "GroupInfo/" + st_groupName + "/BoardInfo/" + st_boardName + "/PostInfo";
        databaseReference = database.getReference(path); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostInfo newPost = snapshot.getValue(PostInfo.class); //만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(newPost); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비

                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                //디비를 가져오던중 에러 발생 시
                Log.e("BoardActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new CustomAdapterPost(arrayList, this, st_groupName, st_boardName);
        recyclerview.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        findViewById(R.id.createPostButton).setOnClickListener(onClickListener);
        findViewById(R.id.deleteBoardButton).setOnClickListener(onClickListener);
        findViewById(R.id.grouphome).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.deleteBoardButton:
                    startDeleteBoardActivity(st_boardName);
                    break;
                case R.id.createPostButton:
                    startCreatePostActivity();
                    break;
                case R.id.grouphome:
                    startgogrouphome();
            }
        }
    };

    private void startDeleteBoardActivity(String a) {
        path = "GroupInfo/" + st_groupName + "/BoardInfo" ;
        databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.child(a).setValue(null);
        Toast.makeText(BoardActivity.this, "게시판이 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    private void startCreatePostActivity() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("GROUPNAME", st_groupName);
        intent.putExtra("BOARDNAME", st_boardName);

        startActivity(intent);

    }

    private void startgogrouphome() {
        Intent intent = new Intent(this, MyGroupActivity.class);
        intent.putExtra("TEXT", st_groupName);
        startActivity(intent);

    }
}