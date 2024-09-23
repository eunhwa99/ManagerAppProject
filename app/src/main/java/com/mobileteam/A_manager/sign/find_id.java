package com.mobileteam.A_manager.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import com.mobileteam.A_manager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class find_id extends AppCompatActivity {
    //아이디찾기
    EditText academy_name, name;
    Button check;
    //비밀번호 찾기
    private EditText email;
    private Button pw_But;
    private FirebaseAuth firebaseAuth;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findid);

        academy_name = findViewById(R.id.academy_name);
        name = findViewById(R.id.name);
        check = findViewById(R.id.check);
        pw_But = findViewById(R.id.pw_check);
        firebaseAuth = FirebaseAuth.getInstance();

        builder=new AlertDialog.Builder(find_id.this);

        //아이디 찾기 눌렀을때
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String academ_name = academy_name.getText().toString().trim();
                String nam = name.getText().toString().trim();
                // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");

                final String[] target = new String[1];
                int[] flag = new int[1];

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            String name=childDataSnapshot.child("name").getValue().toString().trim();
                            String academy = childDataSnapshot.child("academy").getValue().toString().trim();

                            if (academy.equals(academ_name) && name.equals(nam)) {

                               target[0] = String.valueOf(childDataSnapshot.child("email").getValue());
                                builder.setTitle("알림").setMessage("귀하의 아이디는 " + target[0] + " 입니다.");
                                builder.setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                alertDialog = builder.create();
                                alertDialog.show();
                               flag[0]=1;
                               break;
                            }

                        }
                        if(flag[0]==0){
                            builder.setTitle("알림").setMessage("아이디가 존재하지 않습니다.");
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }});



        //비밀번호 찾기 눌렀을 때
        pw_But.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                email = findViewById(R.id.find_email);
                String Email = email.getText().toString().trim();

                if (!isValidEmail(Email)) {
                    Toast.makeText(find_id.this, "가입 시 입력한 이메일을 적어주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(Email);
                    Toast.makeText(find_id.this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    //이후 메인화면으로 넘어가기
                }
            }
        });

    }

    //이메일 유효 검사
    private boolean isValidEmail(String email){
        if (email.isEmpty()) {
            Log.i("유효", "비었음");
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Log.i("유효", "형식오류");
            return false;
        } else {
            return true;
        }
    }

}
