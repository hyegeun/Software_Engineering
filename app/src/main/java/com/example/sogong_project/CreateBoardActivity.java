package com.example.sogong_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateBoardActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtBoard, mEtBoardPurpose;
    private Button mBtnBoardCreate;
    private String st_groupName;
    private String path;

    Intent groupIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        groupIntent = getIntent();   //이전 화면으로부터 인자 전달받음
        st_groupName=groupIntent.getStringExtra("GROUPNAME");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        path = "GroupInfo/" + st_groupName + "/BoardInfo" ;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);

        mEtBoard = findViewById(R.id.createBoardName);
        mEtBoardPurpose = findViewById(R.id.createBoardPurpose);
        mBtnBoardCreate = findViewById(R.id.Btn_createbd);

        mBtnBoardCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strBoard = mEtBoard.getText().toString();
                String strPurpose = mEtBoardPurpose.getText().toString();
                BoardInfo boardInfo = new BoardInfo(strBoard, strPurpose);

                mDatabaseRef.child(strBoard).setValue(boardInfo);
                Toast.makeText(CreateBoardActivity.this, "게시판이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateBoardActivity.this, MyGroupActivity.class);
                intent.putExtra("TEXT",st_groupName);
                startActivity(intent);
            }
        });

        Button btn_cancel = findViewById(R.id.Btn_cancelbd);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }

}
