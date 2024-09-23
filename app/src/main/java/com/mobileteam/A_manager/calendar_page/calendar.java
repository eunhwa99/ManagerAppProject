package com.mobileteam.A_manager.calendar_page;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileteam.A_manager.R;
import com.mobileteam.A_manager.listview.attenList;
import com.mobileteam.A_manager.listview.paymentList;
import com.mobileteam.A_manager.listview.studentList;
import com.mobileteam.A_manager.sign.SetUp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class calendar extends AppCompatActivity {
    public static String DATE="";
    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     */
    private GridAdapter gridAdapter;

    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 캘린더 변수
     */
    private Calendar mCal;

    //추가
    Date date;
    SimpleDateFormat curYearFormat;
    SimpleDateFormat curMonthFormat;
    int showM,showY;
    int test=0;
    int dayNum;
    int day;
    Integer today;
    Integer nowMonth;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Button att_btn;
    private Button list_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        tvDate = (TextView)findViewById(R.id.tv_date);
        gridView = (GridView)findViewById(R.id.gridview);

        att_btn=(Button)findViewById(R.id.attend_btn);

        ImageView iv_last =  (ImageView)findViewById(R.id.iv_lastmonth);
        ImageView iv_next =  (ImageView)findViewById(R.id.iv_nextmonth);




        // 오늘 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();

        date = new Date(now);
        /**
         * 현재 년도, 월, 일 가져오기
         */
        SimpleDateFormat simpleDate=new SimpleDateFormat("yyyyMMdd");
        DATE=simpleDate.format(date);

        //연,월,일을 따로 저장
        curYearFormat = new SimpleDateFormat("yyyy");
        curMonthFormat = new SimpleDateFormat("MM");
        //final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
            test++;
        }
        //Toast.makeText(getApplicationContext(), "dayNum = " + dayNum , Toast.LENGTH_SHORT).show();
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);



        showM = Integer.parseInt(curMonthFormat.format(date));
        showY = Integer.parseInt(curYearFormat.format(date));

        today = mCal.get(Calendar.DAY_OF_MONTH);

        PaintDrawable pd = new PaintDrawable(R.color.purple_200);
        pd.setAlpha(70);

        gridView.setSelector(pd);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                day = position - 5 - dayNum;

                Intent intent = new Intent(getApplicationContext(), popup_activity.class);
                /**
                 * 선택한 날짜 넣어주기
                 */
                String[] strArr=tvDate.getText().toString().split("/");



                int calc;
                String YEAR=strArr[0];
                String MONTH=strArr[1];
                String Day=String.valueOf(day);


                if(!Day_in(YEAR, MONTH, day)) return;

                if(day/10==0){
                    Day="0"+day;
                }
                String final_date =YEAR+'/'+MONTH+'/'+Day;

                calc = (dayNum + day - 1) % 7;

                intent.putExtra("spec", day);

                intent.putExtra("data",final_date);
                intent.putExtra("new_date?",2);


                intent.putExtra("day_of_week", calc);

                startActivity(intent);
                overridePendingTransition(0,0);


                //Toast.makeText(getApplicationContext(), "test = " + day, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "position: "  + position, Toast.LENGTH_SHORT).show();


            }

        });


        list_btn = findViewById(R.id.list_btn);
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calendar.this, studentList.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }

    }

    /**
     * 그리드뷰 어댑터
     *
     */
    private class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            today = mCal.get(Calendar.DAY_OF_MONTH);
            nowMonth = mCal.get(Calendar.MONTH);

            String sToday = String.valueOf(today);
            if (position%7==0){
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.sunday));
            }
            if (position%7==6){
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.saturday));
            }
            if (sToday.equals(getItem(position)) && (showM == (nowMonth+1))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.day));
            }
            return convertView;
        }



    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

    int clickLeft,clickRight;
    int lastM,lastY,nextM,nextY;

    public void lastMonth(View v){
        clickLeft++;
        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");


        showM--;

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(showY, showM-1, 1);
        dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //Toast.makeText(getApplicationContext(), "dayNum = " + dayNum , Toast.LENGTH_SHORT).show();
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);
        lastM = Integer.parseInt(curMonthFormat.format(date))  - clickLeft;
        lastY = Integer.parseInt(curYearFormat.format(date));


        if(showM<1){
            showY--;
            showM += 12;
            //Toast.makeText(this, showM+"  "+showY, Toast.LENGTH_LONG).show();
        }
        if(showM<10) {
            tvDate.setText(showY + "/" + "0" + showM);
        }
        else if(showM<=12){
            tvDate.setText(showY + "/"  + showM);
        }


    }

    public void nextMonth(View v){
        clickRight++;
        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");


        showM++;
        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(showY, showM-1, 1);
        dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //Toast.makeText(getApplicationContext(), "dayNum = " + dayNum , Toast.LENGTH_SHORT).show();
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);
        nextM = Integer.parseInt(curMonthFormat.format(date))  + clickRight;
        nextY = Integer.parseInt(curYearFormat.format(date));

        if(showM>12){
            showY++;
            showM-=12;

        }
        if(showM<10) {
            tvDate.setText(showY + "/" + "0" + showM);
        }
        else if(nextM<=12){
            tvDate.setText(showY + "/"  + showM);
        }

    }

    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    //해당 날짜가 안에 있는지 검사하는 메소드
    private boolean Day_in(String year, String month, int day){
        if(day < 1) return false;
        int m = Integer.parseInt(month);
        int y = Integer.parseInt(year);

        switch(m){
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                if(day > 31) return false;
                break;
            case 4: case 6: case 9: case 11:
                if(day > 30) return false;
                break;
            case 2:
                if(y%4 == 0 && day > 29) return false;
                if(y%4 > 0 && day > 28) return false;
        }

        return true;
    }

    //출석 체크 버튼
    public void attend(View v){
        // 오늘 요일

        Intent intent=new Intent(getApplicationContext(), attenList.class);
        startActivity(intent);
    }
    //결제 체크 버튼
    public void payment(View v){
        // 오늘 요일

        Intent intent=new Intent(getApplicationContext(), paymentList.class);
        intent.putExtra("month",nowMonth+1);
        startActivity(intent);

        //Toast.makeText(getApplicationContext(),nowMonth,Toast.LENGTH_LONG);
    }
    //설정 버튼
    public void goSetUp(View v){
        Intent intent=new Intent(getApplicationContext(), SetUp.class);
        startActivity(intent);
    }

}
