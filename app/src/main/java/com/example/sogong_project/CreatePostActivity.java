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

public class CreatePostActivity extends AppCompatActivity {

    private static final String TAG = "CreatePostActivity";
    private FirebaseAuth mAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtPost, mEtPostContent;
    private Button mBtnPostCreate;
    private String st_groupName;
    private String st_boardName;
    private String path;

    Intent boardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        boardIntent = getIntent();   //이전 화면으로부터 인자 전달받음
        st_groupName=boardIntent.getStringExtra("GROUPNAME");
        st_boardName=boardIntent.getStringExtra("BOARDNAME");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        path = "GroupInfo/" + st_groupName + "/BoardInfo/" + st_boardName + "/PostInfo" ;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);

        mEtPost = findViewById(R.id.createPostTitle);
        mEtPostContent = findViewById(R.id.createPostContent);
        mBtnPostCreate = findViewById(R.id.Btn_createpost);

        mBtnPostCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String writer=firebaseUser.getEmail();
                String strPost = mEtPost.getText().toString();
                String strContent = mEtPostContent.getText().toString();

                PostInfo postInfo = new PostInfo( strPost, strContent, writer);

                mDatabaseRef.child(strPost).setValue(postInfo);
                Toast.makeText(CreatePostActivity.this, "글이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreatePostActivity.this, BoardActivity.class);
                intent.putExtra("GROUPNAME",st_groupName);
                intent.putExtra("BOARDNAME",st_boardName);
                startActivity(intent);
            }
        });

        Button btn_cancel = findViewById(R.id.Btn_cancelpost);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }

}
