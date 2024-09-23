package com.mobileteam.A_manager.sign;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mobileteam.A_manager.R;
import com.mobileteam.A_manager.database.PreferenceManager;
import com.mobileteam.A_manager.explain.explain_main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SetUp extends AppCompatActivity {


    private Button logout_btn;
    private Button withdrawal_btn;
    private Button appinfo_btn;
    private Button auto_login_setup_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);


        // 여기부터 id 등록해주세요
        logout_btn=findViewById(R.id.logout);
        withdrawal_btn= findViewById(R.id.withdrawal);
        appinfo_btn=findViewById(R.id.app_info);
        auto_login_setup_btn=findViewById(R.id.auto_login_setup);

        btn_clicked(); //버튼 눌렀을 때 리스너 등록하는 메소드, (다른 버튼들도 여기에다 넣어주세요)

        appinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), explain_main.class);
                startActivity(intent);
            }
        });
    }


    private void btn_clicked(){
        logout_btn.setOnClickListener(new View.OnClickListener() {
            private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            @Override
            public void onClick(View v) {
                //유저가 로그인 하지 않는 상태 --> 이 액티비티를 종료하고 로그인 액티비티를 연다.
                if(firebaseAuth.getCurrentUser()==null){
                    finish();
                    startActivity(new Intent(getApplicationContext(), login.class));
                }
                else{
                  //  FirebaseUser user=firebaseAuth.getCurrentUser();
                    showDialog(firebaseAuth);
                }
            }
        });
        withdrawal_btn.setOnClickListener(new View.OnClickListener() {
            private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SetUp.this);
                alert_confirm.setTitle("알림")
                        .setMessage("계정을 삭제 하시겠습니까?").setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SetUp.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(), login.class));
                                            }
                                        });
                            }
                        }
                );
                alert_confirm.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert_confirm.show();

            }
        });
        auto_login_setup_btn.setOnClickListener(new View.OnClickListener() {
            private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            Context login_context=login.getContext();
            boolean check_auto = PreferenceManager.getBoolean(login_context, "auto_login");
            String auto_login_now = "";
            @Override
            public void onClick(View v) {
                final Switch auto_login_sw = new Switch(getApplicationContext());
                AlertDialog.Builder alert_login=new AlertDialog.Builder(SetUp.this);
                //View auto_login_view = getLayoutInflater().inflate(R.layout.popup_auto_login,null);
                if(check_auto){
                    auto_login_now = "자동 로그인 설정";
                }
                else {
                    auto_login_now = "자동 로그인 미설정";
                }
                auto_login_sw.setChecked(PreferenceManager.getBoolean(SetUp.this, "auto_login"));
                auto_login_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!isChecked){
                            Toast.makeText(getApplicationContext(),"자동로그인 미설정",Toast.LENGTH_LONG).show();
                            PreferenceManager.setBoolean(login_context, "auto_login", false);
                        }
                        else if(isChecked){
                            Toast.makeText(getApplicationContext(),"자동로그인 설정",Toast.LENGTH_LONG).show();
                            PreferenceManager.setBoolean(login_context, "auto_login", true);
                        }
                    }
                });


                alert_login.setTitle("자동로그인 설정")
                        //.setView(auto_login_view)
                        .setMessage("현재 "+auto_login_now)
                        .setView(auto_login_sw)
                        .setPositiveButton("저장",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

            }
        });
    }
    private void showDialog(FirebaseAuth firebaseAuth){
        AlertDialog.Builder builder=new AlertDialog.Builder(SetUp.this);
        AlertDialog alertDialog;

        builder.setTitle("알림")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Context login_context=login.getContext();
                                // 자동로그인 방지/헤제
                                boolean check_auto = PreferenceManager.getBoolean(login_context, "auto_login");
                                if(check_auto) {
                                    PreferenceManager.setBoolean(login_context, "auto_login", false);
                                }
                               firebaseAuth.signOut();
                                finish();

                                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),login.class));
                            }
                        });

        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }

    class visibilitySwitchListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){

            }
        }
    }

}
