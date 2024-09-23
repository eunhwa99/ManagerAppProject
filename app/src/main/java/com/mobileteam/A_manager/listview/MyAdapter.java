package com.mobileteam.A_manager.listview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileteam.A_manager.R;
import com.mobileteam.A_manager.database.Students;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Students> studentlist, filteredItemList;
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    int what;
    Calendar calendar;
    private int day_of_week;
    private Realm realm;
    private Students stu;
    int month, showmonth;
    RealmResults<Students> stud;
    Context context;

    public MyAdapter(Context context, ArrayList<Students> data, int what) {
        mContext = context;
        filteredItemList=data;
        studentlist=new ArrayList<>();
        studentlist.addAll(filteredItemList);
        mLayoutInflater = LayoutInflater.from(mContext);
        this.what=what;
        calendar = Calendar.getInstance();
        day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        realm = Realm.getDefaultInstance();
        month = calendar.get(Calendar.MONTH)+3;
        showmonth = paymentList.showM;
        this.context = context;
    }

    //Adapter에 사용되는 데이터의 개수
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    // 지정한 position에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    // 지정한 position에 있는 데이터와 관계된 아이템의 ID 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      //  final int pos=position;
       // final Context context=parent.getContext();

        if(convertView==null){
           // LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(what==0) {
                convertView = mLayoutInflater.inflate(R.layout.showlist, parent, false);
            }
            else if(what==1){
                convertView=mLayoutInflater.inflate(R.layout.attendlist,parent,false);
            }
            else if(what==2){
                convertView=mLayoutInflater.inflate(R.layout.paymentlist,parent,false);
            }
            else if(what==4){
                convertView=mLayoutInflater.inflate(R.layout.attendnottoday,parent,false);

            }
        }

        TextView nameText;
        TextView ageText;
        TextView phoneText;
        TextView dateText;
        TextView timeText;
        TextView idText;
        TextView moneyText;
        ImageButton sendBtn;

        // position에 위치한 데이터 참조 획득
        Students students=filteredItemList.get(position);

        if(what==0) {
            // 화면에 표시될 View(Layout이 inflate 된)으로부터 위젯에 대한 참조 획득
            nameText = (TextView) convertView.findViewById(R.id.Name);
            ageText = (TextView) convertView.findViewById(R.id.Age);
            phoneText = (TextView) convertView.findViewById(R.id.Phone);
            nameText.setText(students.getName());
            ageText.setText(students.getAge() + "");
            phoneText.setText(students.getPhone() + "(" + students.getPar_phone() + ")");
        }
        else if(what==4){
            nameText=(TextView)convertView.findViewById(R.id.Name);
            nameText.setText(students.getName());
            timeText = convertView.findViewById(R.id.Time);
            //등원 시각 표시
            int dow;
            if(day_of_week==1){//일
                dow = students.getSun();
            }
            else if(day_of_week==2){//월
                dow = students.getMon();
            }
            else if(day_of_week==3){//화
                dow = students.getTue();
            }
            else if(day_of_week==4){//수
                dow = students.getWed();
            }
            else if(day_of_week==5){//목
                dow = students.getThu();
            }
            else if(day_of_week==6){//금
                dow = students.getFri();
            }
            else {//토
                dow = students.getSat();
            }

            timeText.setText(dow/100 + " : " + dow % 100);


        }
        else if(what==1){
            nameText=(TextView)convertView.findViewById(R.id.Name);
            nameText.setText(students.getName());
            timeText = convertView.findViewById(R.id.Time);
            idText = convertView.findViewById(R.id.attend_std_id);
            sendBtn = convertView.findViewById(R.id.attend_send_message);

            //등원 시각 표시
            idText.setText(students.getStd_id() + "");
            int dow;
            if(day_of_week==1){//일
                dow = students.getSun();
            }
            else if(day_of_week==2){//월
                dow = students.getMon();
            }
            else if(day_of_week==3){//화
                dow = students.getTue();
            }
            else if(day_of_week==4){//수
                dow = students.getWed();
            }
            else if(day_of_week==5){//목
                dow = students.getThu();
            }
            else if(day_of_week==6){//금
                dow = students.getFri();
            }
            else {//토
                dow = students.getSat();
            }

            timeText.setText(dow/100 + " : " + dow % 100);

            //출석 체크 관리
            CheckBox chk1 = convertView.findViewById(R.id.checkBox1);
            TextView attended = convertView.findViewById(R.id.attended);

            if(students.getAttended()){//출석이 체크 되었다면
                chk1.setVisibility(View.GONE);
                attended.setVisibility(View.VISIBLE);
                sendBtn.setImageResource(R.drawable.send_off);
            }
            else if(students.getAttendchk()){//출석 체크 박스에 체크가 되어 있었다면
                chk1.setChecked(true);
            }
            else{//아직 등원하지 않았다면
                chk1.setChecked(false);
            }

            chk1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realm.beginTransaction();
                    stu = realm.where(Students.class).equalTo("std_id", Integer.parseInt(idText.getText().toString())).findFirst();
                    if(chk1.isChecked()){
                        stu.setAttendchk(true);
                    }
                    else{
                        stu.setAttendchk(false);
                    }
                    realm.commitTransaction();
                }
            });

            //메시지 전송 버튼을 눌렀을 때
            sendBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(students.getAttended() == true) return;
                    if(students.getPhone().length() == 0 && students.getPar_phone().length() == 0){
                        Toast.makeText((attenList) context, "전화번호 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String PhoneNo = "sms:";
                    String who = null;
                    if(students.getPar_phone().length() > 0){
                        PhoneNo += students.getPar_phone().replace("-", "") + ";";
                        who = "학부모님";
                    }
                    else if(students.getPhone().length() > 0){
                        PhoneNo += students.getPhone().replace("-", "") + ";";
                        who = "학생";
                    }

                    Toast.makeText((attenList) context, who + " 전화번호로 문자를 전송합니다.", Toast.LENGTH_SHORT).show();

                    Uri smsUri = Uri.parse(PhoneNo);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                    intent.putExtra("sms_body", students.getName() + " 학생이 등원하지 않았습니다. 확인 부탁 드립니다.");
                    context.startActivity(intent);
                }
            });
        }
        else if(what==2){
            nameText=(TextView)convertView.findViewById(R.id.payment_name);
            nameText.setText(students.getName());
            CheckBox paycheck = convertView.findViewById(R.id.payment_checkBox);
            TextView pay_date = convertView.findViewById(R.id.pay_day);
            moneyText = convertView.findViewById(R.id.pay_money);
            idText = convertView.findViewById(R.id.attend_std_id);
            sendBtn = convertView.findViewById(R.id.payment_send_message);
            moneyText.setText(students.getMoney()+" 원");
            idText.setText(students.getStd_id() + "");

            if(!paycheck.isChecked()){
                //메시지 전송 버튼을 눌렀을 때
                sendBtn.setImageResource(R.drawable.send);
                sendBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //if(students.getPaymentchk() == true) return;
                        /*
                        if(students.getPhone().length() == 0 && students.getPar_phone().length() == 0){
                            Toast.makeText((paymentList) context, "전화번호 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AlertDialog.Builder clsBuilder = new AlertDialog.Builder((paymentList) context );
                        clsBuilder.setTitle( "메시지를 전송합니다." );
                        EditText message = new EditText((paymentList)context);
                        message.setFilters(new InputFilter[]{new InputFilter.LengthFilter((70))});
                        Boolean who = false;
                        if(students.getPar_phone().length()>0){
                            clsBuilder.setMessage("학부모 P. " + students.getPar_phone());
                            who = false;
                        }
                        else{
                            clsBuilder.setMessage("학생 P. " + students.getPhone());
                            who = true;
                        }
                        message.setText(students.getName() + " 학생 "+showmonth+"월 결제되지 않았습니다."+" 확인 부탁 드립니다.");
                        clsBuilder.setView( message);
                        Boolean finalWho = who;
                        clsBuilder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Toast.makeText((paymentList) context, message.getText().toString(), Toast.LENGTH_SHORT).show();
                                send_message(students, message.getText().toString(), finalWho);Toast.makeText((paymentList) context, "메시지를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        clsBuilder.setNegativeButton("취소", null);
                        clsBuilder.show();
                        */
                        if(students.getAttended() == true) return;
                        if(students.getPhone().length() == 0 && students.getPar_phone().length() == 0){
                            Toast.makeText((attenList) context, "전화번호 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String PhoneNo = "sms:";
                        String who = null;
                        if(students.getPar_phone().length() > 0){
                            PhoneNo += students.getPar_phone().replace("-", "") + ";";
                            who = "학부모님";
                        }
                        else if(students.getPhone().length() > 0){
                            PhoneNo += students.getPhone().replace("-", "") + ";";
                            who = "학생";
                        }

                        Toast.makeText(context, who + " 전화번호로 문자를 전송합니다.", Toast.LENGTH_SHORT).show();

                        Uri smsUri = Uri.parse(PhoneNo);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                        intent.putExtra("sms_body", students.getName() + " 학생 "+showmonth+"월 결제되지 않았습니다."+" 확인 부탁 드립니다.");
                        context.startActivity(intent);
                    }
                });
            }
            else if(paycheck.isChecked()){
                sendBtn.setImageResource(R.drawable.send_off);
            }



            int checking = 1;
            if(showmonth==1){
                checking=students.getjan();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==2){
                checking= students.getfab();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==3){
                checking= students.getmar();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==4){
                checking= students.getapr();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==5){
                checking= students.getmay();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==6){
                checking= students.getjan();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==7){
                checking= students.getjul();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==8){
                checking= students.getaug();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==9){
                checking= students.getsep();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==10){
                checking= students.getoct();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==11){
                checking= students.getnov();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);
                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            else if(showmonth==12){
                checking= students.getdec();
                if(checking==1){
                    paycheck.setChecked(true);
                    paycheck.setText("결제 완료");
                    sendBtn.setImageResource(R.drawable.send_off);
                    sendBtn.setClickable(false);

                }
                else{
                    paycheck.setChecked(false);
                    paycheck.setText("결제 필요");
                }}
            Log.i("checkshowing", String.valueOf(showmonth));
            pay_date.setText(students.getDate()+"일");
            Log.i("checkshow", String.valueOf(checking));
            paycheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realm.beginTransaction();
                    stu = realm.where(Students.class).equalTo("std_id", Integer.parseInt(idText.getText().toString())).findFirst();
                    if(paycheck.isChecked()){
                        sendBtn.setImageResource(R.drawable.send_off);
                        if(showmonth==1){ stu.setjan(1); }
                        if(showmonth==2){ stu.setfab(1); }
                        if(showmonth==3){ stu.setmar(1); }
                        if(showmonth==4){ stu.setapr(1); }
                        if(showmonth==5){ stu.setmay(1); }
                        if(showmonth==6){ stu.setjun(1); }
                        if(showmonth==7){ stu.setjul(1); }
                        if(showmonth==8){ stu.setaug(1); }
                        if(showmonth==9){ stu.setsep(1); }
                        if(showmonth==10){ stu.setoct(1); }
                        if(showmonth==11){ stu.setnov(1); }
                        if(showmonth==12){ stu.setdec(1); }
                        paycheck.setText("결제 완료");
                        sendBtn.setClickable(false);
                    }
                    else{
                        sendBtn.setImageResource(R.drawable.send);
                        paycheck.setText("결제 필요");
                        if(showmonth==1){ stu.setjan(-1); }
                        if(showmonth==2){ stu.setfab(-1); }
                        if(showmonth==3){ stu.setmar(-1); }
                        if(showmonth==4){ stu.setapr(-1); }
                        if(showmonth==5){ stu.setmay(-1); }
                        if(showmonth==6){ stu.setjun(-1); }
                        if(showmonth==7){ stu.setjul(-1); }
                        if(showmonth==8){ stu.setaug(-1); }
                        if(showmonth==9){ stu.setsep(-1); }
                        if(showmonth==10){ stu.setoct(-1); }
                        if(showmonth==11){ stu.setnov(-1); }
                        if(showmonth==12){ stu.setdec(-1); }
                    }
                    realm.commitTransaction();
                }
            });

        }
        return convertView;
    }

    public void filter(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        filteredItemList.clear();
        if(charText.length()==0){
            filteredItemList.addAll(studentlist);
        }
        else{
            for(Students student:studentlist){
                String name= student.getName();
                Log.i("Name",name);
                Log.i("Text",charText);
                if(name.toLowerCase().contains(charText)){
                    filteredItemList.add(student);
                }
            }
        }
    }

    void send_message(Students students, String text, Boolean who){
        String phoneNo;
        if(who == false){//학부모
            phoneNo = students.getPar_phone();
        }
        else{//학생
            phoneNo = students.getPhone();
        }
        phoneNo = phoneNo.replaceAll("-", "");
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, text, null, null);
            Toast.makeText(context, "메시지를 전송하였습니다.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "메시지 전송이 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
