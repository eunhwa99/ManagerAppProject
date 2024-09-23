package com.mobileteam.A_manager.sign;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mobileteam.A_manager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    // 순서대로: 아이디, 비밀번호, 비밀번호 확인, 본인이름, 학원이름
    EditText mIdText, mPasswordText, mPasswordcheckText, mName, mAcademyname;
    Button mjoin_button, mdelete_button, mcheck_button;
    private FirebaseAuth firebaseAuth;
    private boolean FLAG=false; //비밀번호 일치 여부

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //액션 바 등록하기
        // ActionBar actionBar = getSupportActionBar();
        // actionBar.setTitle("Create Account");

        //actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
        //actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘

        //파이어베이스 접근 설정
        firebaseAuth=FirebaseAuth.getInstance();
        mName=findViewById(R.id.nameEt);
        mAcademyname=findViewById(R.id.academyEt);
        mIdText=findViewById(R.id.idEt);
        mPasswordText=findViewById(R.id.passwordEdt);
        mPasswordcheckText=findViewById(R.id.passwordcheckEdt);
        mjoin_button=findViewById(R.id.join_button);
        mdelete_button=findViewById(R.id.delete);
        mcheck_button=findViewById(R.id.check_button);

        builder=new AlertDialog.Builder(Register.this);

        //취소 버튼 클릭 리스너
        mdelete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 비밀번호 확인 버튼 클릭 리스너
        mcheck_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd=mPasswordText.getText().toString().trim();
                String pwdcheck=mPasswordcheckText.getText().toString().trim();
                if(isValidPassword(pwd)) {
                    if (pwd.equals(pwdcheck)) {
                        // 비밀번호가 일치합니다.
                        FLAG = true;
                        builder.setTitle("알림").setMessage("비밀번호가 일치합니다.");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        // 비밀번호가 일치하지 않습니다.
                        FLAG = false;
                        builder.setTitle("알림").setMessage("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        });

        //가입 버튼 클릭 리스너 --> firebase에 데이터를 저장한다
        mjoin_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String id = mIdText.getText().toString().trim(); //trim(): 공백제거함수
                if (!isValidEmail(id)) {
                    builder.setTitle("알림").setMessage("유효하지 않은 이메일 형식입니다. 다시 입력해주세요.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    String pwd = mPasswordText.getText().toString().trim();
                    String pwdcheck = mPasswordcheckText.getText().toString().trim();

                    if (pwd.equals(pwdcheck) && FLAG == true) {
                        //ProgressDialog 클래스: 사용자에게 실시간 진행 상태 알릴 수 있다.
                        //진행이 끝나면 팝업창 사라짐
                        final ProgressDialog mDialog = new ProgressDialog(Register.this);
                        mDialog.setMessage("가입중입니다...");
                        mDialog.show();

                        //파이어베이스에 신규계정 등록하기(회원가입)
                        firebaseAuth.createUserWithEmailAndPassword(id, pwd).addOnCompleteListener(
                                com.mobileteam.A_manager.sign.Register.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //가입 성공시
                                        if (task.isSuccessful()) {
                                            mDialog.dismiss(); //위에서 생성한 팝업창 없애기

                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            String email = user.getEmail();
                                            String uid = user.getUid();
                                            String name = mName.getText().toString().trim();
                                            String academy=mAcademyname.getText().toString().trim();

                                            //Hash MAp 테이블을 파이어베이스 데이터베이스에 저장
                                            HashMap<Object, String> hashMap = new HashMap<>();

                                            hashMap.put("academy", academy);
                                            hashMap.put("email", email);
                                            hashMap.put("name", name);


                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference reference = database.getReference("Users");
                                            reference.push().setValue(hashMap);

                                            //가입이 이루어졌을 시 가입 화면 빠져나감.
                                            Intent intent = new Intent(Register.this, com.mobileteam.A_manager.calendar_page.calendar.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(Register.this, "회원이 되셨습니다.!!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            mDialog.dismiss();
                                            FLAG=false;
                                            Toast.makeText(Register.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                            return; // 해당 메소드 진행을 멈추고 빠져나간다.
                                        }
                                    }
                                }
                        );

                    } else {
                        Toast.makeText(Register.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });

    }



    // 이메일 유효성 검사
    private boolean isValidEmail(String email){
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidPassword(String password){

        if(password.length()<6){
            builder.setTitle("알림").setMessage("6자리 이상 입력해주세요.");
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog=builder.create();
            alertDialog.show();
            return false;
        }
        else return true;
    }

  /*  public boolean onSupportNavigateUp(){
        onBackPressed(); //뒤로가기 버튼이 눌렸을 때
        return super.onSupportNavigateUp(); //뒤로가기 버튼
    }*/

}

//https://call203.tistory.com/28
//https://bellog.tistory.com/3
