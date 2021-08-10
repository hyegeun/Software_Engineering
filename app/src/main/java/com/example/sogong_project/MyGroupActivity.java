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

public class MyGroupActivity extends AppCompatActivity {

    TextView groupTitle;
    Intent groupIntent;
    String path;
    String groupName;

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<BoardInfo> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, userdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        groupIntent = getIntent();   //이전 화면으로부터 인자 전달받음


        //board_screen_layout 에서 컴포넌트들 받아옴

        groupTitle = (TextView) findViewById(R.id.grouptitle);

        //이전 화면에서 클릭한 게시판의 이름을 제목으로 사용
        groupName = groupIntent.getStringExtra("TEXT");
        groupTitle.setText(groupName);

        recyclerview = findViewById(R.id.rv);
        recyclerview.setHasFixedSize(true);  //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        path = "GroupInfo/" + groupName + "/BoardInfo";
        databaseReference = database.getReference(path); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    BoardInfo newBoard = snapshot.getValue(BoardInfo.class); //만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(newBoard); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                //디비를 가져오던중 에러 발생 시
                Log.e("MyGroupActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new CustomAdapterBoard(arrayList, this, (String) groupTitle.getText());
        recyclerview.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        findViewById(R.id.getOutButton).setOnClickListener(onClickListener);
        findViewById(R.id.boardCreateButton).setOnClickListener(onClickListener);
        findViewById(R.id.groupdeleteButton).setOnClickListener(onClickListener);
        findViewById(R.id.myhome).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.boardCreateButton:
                    startCreateBoardActivity();
                    break;
                case R.id.groupdeleteButton:
                    startDeleteGroupActivity(groupName);
                    break;
                case R.id.getOutButton:
                    startGetOutActivity(groupName);
                    break;
                case R.id.myhome:
                    startgotohome();

            }
        }
    };

    private void startDeleteGroupActivity(String a) {
        path = "GroupInfo/";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String path2 = "UserGroup/" + uid;
        String kingemail = groupIntent.getStringExtra("kingmail");
        String useremail = user.getEmail();

        if (kingemail == useremail) {
            databaseReference = FirebaseDatabase.getInstance().getReference(path);
            userdatabase = FirebaseDatabase.getInstance().getReference(path2);
            databaseReference.child(a).setValue(null);
            userdatabase.child(a).setValue(null);
            Toast.makeText(MyGroupActivity.this, "그룹이 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        }
        else {
            Toast.makeText(MyGroupActivity.this, "그룹은 그룹장만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private void startGetOutActivity(String a) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        path = "UserGroup/" + uid;

        databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.child(a).setValue(null);
        Toast.makeText(MyGroupActivity.this, "그룹을 탈퇴했습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    private void startCreateBoardActivity() {
        Intent intent = new Intent(this, CreateBoardActivity.class);
        intent.putExtra("GROUPNAME", groupName);
        startActivity(intent);
    }

    private void startgotohome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}