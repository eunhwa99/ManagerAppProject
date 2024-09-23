package com.mobileteam.A_manager.sign;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileteam.A_manager.calendar_page.calendar;
import com.mobileteam.A_manager.database.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mobileteam.A_manager.R;

public class login extends AppCompatActivity {
    EditText id, password;
    Button login;
    TextView find_id, find_password, join;
    private FirebaseAuth firebaseAuth;
    String Id, Pass;
    CheckBox autoLogin;

    public static Context login_context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_context=getApplicationContext();

        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        find_id = findViewById(R.id.find_id);
        join = findViewById(R.id.join);
        firebaseAuth = FirebaseAuth.getInstance();
        autoLogin = findViewById(R.id.auto_login);

        //자동로그인
        boolean check_auto = PreferenceManager.getBoolean(this, "auto_login");
        if(check_auto){
            String saved_Id = PreferenceManager.getString(this, "user_id");
            String saved_Password = PreferenceManager.getString(this, "user_pw");
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("로그인 중...");
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
            dialog.show();

            firebaseAuth.signInWithEmailAndPassword(saved_Id, saved_Password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(login.this, "자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login.this, calendar.class);
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(login.this,"자동로그인 실패. 수동으로 로그인 해주세요.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        //로그인 버튼 리스너
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(login.this);
                dialog.setMessage("로그인 중...");
                dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
                dialog.show();
                if(check()==true){
                firebaseAuth.signInWithEmailAndPassword(Id, Pass).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(autoLogin.isChecked()){
                                PreferenceManager.setString(login.this, "user_id", Id);
                                PreferenceManager.setString(login.this, "user_pw", Pass);
                                PreferenceManager.setBoolean(login.this, "auto_login", true);
                            }
                            dialog.dismiss();
                            Intent intent = new Intent(login.this, calendar.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(login.this,"아이디/비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
            }

        });

        //아이디 찾기 눌렀을때
        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //아이디 찾기 화면으로 넘어감
                Intent intent = new Intent(login.this, com.mobileteam.A_manager.sign.find_id.class); //this 대신 getActivity()
                startActivity(intent);
            }
        });
        //회원가입 눌렀을때 넘어가게
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 화면으로 넘어감
                Intent intent = new Intent(login.this, com.mobileteam.A_manager.sign.Register.class); //this 대신 getActivity()
                startActivity(intent);
            }
        });


    }

    private boolean check(){
        Id = id.getText().toString().trim();
        Pass = password.getText().toString().trim();
        if(Id.equals("")){
            Toast.makeText(login.this,"아이디를 바르게 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Pass.equals("")){
            Toast.makeText(login.this,"비밀번호를 바르게 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public static Context getContext(){
        return login_context;
    }
}