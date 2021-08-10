package com.example.sogong_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.security.acl.Group;


public class CreateGroupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private DatabaseReference insertgroup;
    private EditText mEtGroup, mEtPurpose;
    private Button mBtnCreate;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("GroupInfo");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        String insertpath = "UserGroup/" + uid;
        insertgroup = FirebaseDatabase.getInstance().getReference(insertpath);

        mEtGroup = findViewById(R.id.createGroupText);
        mEtPurpose = findViewById(R.id.createPurposeText);
        mBtnCreate = findViewById(R.id.createButton);

        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strGroup = mEtGroup.getText().toString();
                String strPurpose = mEtPurpose.getText().toString();
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                GroupInfo groupInfo = new GroupInfo(firebaseUser.getEmail(), strGroup, strPurpose);

                BoardInfo boardInfo1 = new BoardInfo("공지사항", "공지하는 곳입니다.");
                BoardInfo boardInfo2 = new BoardInfo("자유게시판", "자유롭게 활동하는 곳입니다.");

                mDatabaseRef.child(strGroup).setValue(groupInfo);
                insertgroup.child(strGroup).setValue(groupInfo);
                mDatabaseRef.child(strGroup).child("BoardInfo").child(boardInfo1.getBoardName()).setValue(boardInfo1);
                mDatabaseRef.child(strGroup).child("BoardInfo").child(boardInfo2.getBoardName()).setValue(boardInfo2);

                Toast.makeText(CreateGroupActivity.this, "그룹이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}