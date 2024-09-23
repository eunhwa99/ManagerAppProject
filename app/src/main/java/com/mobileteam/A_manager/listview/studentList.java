package com.mobileteam.A_manager.listview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mobileteam.A_manager.R;
import com.mobileteam.A_manager.calendar_page.popup_activity;
import com.mobileteam.A_manager.database.Students;
import com.mobileteam.A_manager.info.add_std;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class studentList extends AppCompatActivity{
    private ListView listview;
    private MyAdapter adapter;
    private EditText editTextFilter;
    private  Realm realm;

    private ArrayList<Students> studentlist;
    private Students modified_student;
    private int modified_position;

    public studentList() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        realm=Realm.getDefaultInstance();
        studentlist=new ArrayList<>();

        readData();
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

        //listview 클릭시 --> 팝업
        listview.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Students students=(Students)parent.getItemAtPosition(position);
                TextView txtResult=(TextView)findViewById(R.id.txtText);
                Intent intent=new Intent(getApplicationContext(), popup_activity.class);
                String str=students.getInfo();

                intent.putExtra("Info", str);
                startActivity(intent);
            }
        }));

        // listview에 꾹 눌렀을 때 이벤트 정의 ==> 삭제 기능
        listview.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                modified_student=(Students)parent.getItemAtPosition(position);
                AlertDialog.Builder builder=new AlertDialog.Builder(studentList.this);
                AlertDialog alertDialog;

                builder.setTitle("알림")
                        .setMessage("삭제 혹은 수정 하시겠습니까?")
                        .setPositiveButton("수정",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent(getApplicationContext(), add_std.class);
                                        intent.putExtra("Student", modified_student);
                                        startActivityForResult(intent,1003);
                                      //  modified_position=position;
                                    }
                                });
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteData(modified_student);
                        studentlist.remove(position);
                        Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1003: case 1004:
                if (resultCode == RESULT_OK) {
                    readData();
                    adapter.notifyDataSetChanged();
                } else {

                }
                break;
        }
    }



        private void CreateView(){
        //Adapter 생성
        adapter=new MyAdapter(this, studentlist,0);

        // 리스트뷰 참조 및 Apater 달기
        listview=(ListView)findViewById(R.id.listview);
        listview.setAdapter(adapter);
    }


    //학생들 데이터 읽기
    private void readData(){
        studentlist.clear();
        RealmResults<Students>results=realm.where(Students.class).findAll();
        studentlist.addAll(realm.copyFromRealm(results));
    }

    private void modify(Students student){

        String name=student.getName();
        int age=student.getAge();
        String phone=student.getPhone();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // 쿼리를 해서 하나를 가져온다.
                Students std=realm.where(Students.class).equalTo("name",name).and().equalTo("age",age)
                        .and().equalTo("phone",phone).findFirst();


            }
        });

    }

    private void deleteData(Students student){

        String name=student.getName();
        int age=student.getAge();
        String phone=student.getPhone();

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                Students student=realm.where(Students.class).equalTo("name",name).and().equalTo("age",age)
                        .and().equalTo("phone",phone).findFirst();
                student.deleteFromRealm(); //삭제
            }
        });
    }

    public void add_page(View v){
        Intent intent = new Intent(getApplicationContext(), add_std.class);
        startActivityForResult(intent,1004);

    }
}
