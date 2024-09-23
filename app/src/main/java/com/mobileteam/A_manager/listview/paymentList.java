package com.mobileteam.A_manager.listview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileteam.A_manager.R;
import com.mobileteam.A_manager.calendar_page.calendar;
import com.mobileteam.A_manager.database.Students;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class paymentList extends AppCompatActivity {
    public static int showM;
    private ListView listview;
    private MyAdapter adapter;
    private EditText editTextFilter;
    private Realm realm;
    private  int month;
    private RealmResults<Students> stu;
    private ArrayList<Students> studentlist;
    private TextView tv_payment_month;
    private Button prev, next, save;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentlistview);
        Intent payIntent = getIntent();
        month = payIntent.getIntExtra("month",0);
        showM=month;
        checkBox = (CheckBox)findViewById(R.id.payment_checkBox);
        tv_payment_month = (TextView)findViewById(R.id.payment_month);
        tv_payment_month.setText(month+"월");
        studentlist=new ArrayList<>();


        prev = (Button)findViewById(R.id.prev_month);
        next = (Button)findViewById(R.id.next_month);
        save = (Button)findViewById(R.id.save);

        realm=Realm.getDefaultInstance();

        /*stu = realm.where(Students.class).equalTo("paymentchk",true).findAll();
        for(Students s:stu){
            s.setPaymentchk(false);
        }
        stu = realm.where(Students.class).equalTo("paid",true).findAll();
        for(Students s:stu){
            s.setPaid(false);
        }
        realm.commitTransaction();*/
        stu = realm.where(Students.class).findAll();
        getStudent();
        //날짜 순서대로 정렬하기
        Collections.sort(studentlist, sortBydate);
        CreateView();


        // filtering
        editTextFilter=(EditText)findViewById(R.id.edittxt);
        editTextFilter.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text=editTextFilter.getText().toString().toLowerCase(Locale.getDefault());
                Log.i("Text",text);
                adapter.filter(text);
            }
        });




    }


    public void save(View view){
        //저장 버튼 누를때
        Intent intent=new Intent(getApplicationContext(), calendar.class);
        startActivity(intent);

    }

    public void reset(View view){
        //리셋버튼 누를 때
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("이달의 결제정보를 리셋 하시겠습니까?");
        alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //현재 보고있는 달 리셋 시키기
                        realm.beginTransaction();
                        if(showM==1){
                            for(Students s : stu){
                                s.setjan(-1);
                            }
                        }
                        if(showM==2){
                            for(Students s : stu){
                                s.setfab(-1);
                            }
                        }
                        if(showM==3){
                            for(Students s : stu){
                                s.setmar(-1);
                            }
                        }
                        if(showM==4){
                            for(Students s : stu){
                                s.setapr(-1);
                            }
                        }
                        if(showM==5){
                            for(Students s : stu){
                                s.setmay(-1);
                            }
                        }
                        if(showM==6){
                            for(Students s : stu){
                                s.setjun(-1);
                            }
                        }
                        if(showM==7){
                            for(Students s : stu){
                                s.setjul(-1);
                            }
                        }
                        if(showM==8){
                            for(Students s : stu){
                                s.setaug(-1);
                            }
                        }
                        if(showM==9){
                            for(Students s : stu){
                                s.setsep(-1);
                            }
                        }
                        if(showM==10){
                            for(Students s : stu){
                                s.setoct(-1);
                            }
                        }
                        if(showM==11){
                            for(Students s : stu){
                                s.setnov(-1);
                            }
                        }
                        if(showM==12){
                            for(Students s : stu){
                                s.setdec(-1);
                            }
                        }
                        realm.commitTransaction();

                        Intent intent=new Intent(getApplicationContext(), calendar.class);
                        startActivity(intent);
                    }
                });
        alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();






    }


    private void CreateView(){
        //Adapter 생성
        adapter=new MyAdapter(this, studentlist,2);

        // 리스트뷰 참조 및 Adapter 달기
        listview=(ListView)findViewById(R.id.payment_listview);
        listview.setAdapter(adapter);
    }


    private void getStudent(){
        if(stu.size()>0)
            studentlist.addAll(realm.copyFromRealm(stu));
    }

    //다음달 버튼 눌렀을 때
    public void nextmon(View view) {
        showM++;
        if(showM>12){
            showM-=12;
        }
        tv_payment_month.setText(showM+"월");
        Log.i("gooddes", String.valueOf(showM));

        CreateView();
    }


    //이전 달 버튼 눌렀을 때
    public void premon(View view) {
        showM--;
        if(showM<1){
            showM+=12;
        }
        tv_payment_month.setText(showM+"월");
        Log.i("gooddes", String.valueOf(month));

        CreateView();
    }

    private final static Comparator<Students> sortBydate = new Comparator<Students>() {
        @Override
        public int compare(Students o1, Students o2) {
            return Integer.compare(o1.getDate(),o2.getDate());
        }
    };

}
