package com.mobileteam.A_manager.database;

import java.io.Serializable;

import io.realm.RealmObject;

public class Students extends RealmObject implements Serializable {
    private int std_id;
    private String name, phone;
    private int date, money;
    private int mon, tue, wed, thu, fri, sat, sun;
    private int age;
    private String par_name, par_phone;
    private String memo;
    private boolean attendchk, attended, addattend;
    private int jan, fab, mar, apr, may, jun, jul, aug, sep, oct, nov, dec;

    public Students(String name, int age, String phone, int date){
        this.name=name;
        this.age=age;
        this.phone=phone;
        this.date=date;
        mon = tue = wed = thu = fri = sat = sun = -1;
        attendchk = attended = addattend = false;
        jan = fab = mar = apr = may = jun = jul = aug = sep = oct = nov = dec = -1;
    }

    public Students(){
        mon = tue = wed = thu = fri = sat = sun = -1;
        attendchk = attended = addattend = false;
        jan = fab = mar = apr = may = jun = jul = aug = sep = oct = nov = dec = -1;
    }

    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public int getAge(){
        return age;
    }
    public int getDate(){
        return date;
    }
    public int getMon() { return mon; }
    public int getTue() { return tue; }
    public int getWed() { return wed; }
    public int getThu() { return thu; }
    public int getFri() { return fri; }
    public int getSat() { return sat; }
    public int getSun() { return sun; }
    public int getjan(){ return jan;}
    public int getfab(){ return fab; }
    public int getmar(){ return mar;}
    public int getapr(){return apr;}
    public int getmay(){return may;}
    public int getjun(){return jun;}
    public int getjul(){return jul;}
    public int getaug(){return aug;}
    public int getsep(){return sep;}
    public int getoct(){return oct;}
    public int getnov(){return nov;}
    public int getdec(){return dec;}
    public String getPar_name() { return par_name; }
    public String getPar_phone() { return par_phone; }
    public String getMemo() {return memo;}
    public int getStd_id(){return std_id;}
    public boolean getAttendchk() {return attendchk;}
    public boolean getAttended() {return attended;}
    public boolean getAddattended(){return addattend;}
    public int getMoney(){return money;}

    public String getInfo(){

        String day="";
        if(mon!=-1) day=day+" 월요일 "+mon/100+"시 "+mon%100+"분 ";
        if(tue!=-1) day=day+", 화요일 "+tue/100+"시 "+tue%100+"분 ";
        if(wed!=-1) day=day+", 수요일 "+wed/100+"시 "+wed%100+"분 ";
        if(thu!=-1) day=day+", 목요일 "+thu/100+"시 "+thu%100+"분 ";
        if(fri!=-1) day=day+", 금요일 "+fri/100+"시 "+fri%100+"분 ";
        if(sat!=-1) day=day+", 토요일 "+sat/100+"사 "+sat%100+"분 ";
        if(sun!=-1) day=day+", 일요일 "+sun/100+"시 "+sat%100+"분 ";

        String stu="";
        stu=stu+"이름: "+name+'\n'+"나이: "+age+'\n'+"전화: "+phone+'\n'+"등원 시간: "+day+'\n'+"결제 날짜/금액: 매달 "+date+"일/"+money+"원"+'\n'+'\n'
                +"학부모 이름: "+par_name+'\n'+"학부모 전화: "+par_phone+'\n'+"메모: "+memo+'\n';

        return stu;
    }

    public void setStudent(String name, String phone, int age){
        this.name = name;
        this.phone = phone;
        this.age = age;
    }
    public void setParent(String name, String phone){
        this.par_name = name;
        this.par_phone = phone;
    }
    public void setDate(int date){
        this.date = date;
    }
    public void setMemo(String memo){
        this.memo = memo;
    }
    public void setTime(int mon, int tue, int wed, int thu, int fri, int sat, int sun){
        this.mon = mon; this.tue = tue; this.wed = wed; this.thu = thu; this.fri = fri; this.sat = sat; this.sun = sun;
    }
    public void setStd_id(int std_id){
        this.std_id = std_id;
    }

    public void setAttendchk(boolean attendchk){
        this.attendchk = attendchk;
    }
    public void setAttended(boolean attended){
        this.attended = attended;
    }
    public void setjan(int n){this.jan = n; }
    public void setfab(int n){this.fab = n;}
    public void setmar(int n){this.mar = n;}
    public void setapr(int n){this.apr = n;}
    public void setmay(int n){this.may = n;}
    public void setjun(int n){this.jun = n;}
    public void setjul(int n){this.jul = n;}
    public void setaug(int n){this.aug = n;}
    public void setsep(int n){this.sep = n;}
    public void setoct(int n){this.oct = n;}
    public void setnov(int n){this.nov = n;}
    public void setdec(int n){this.dec = n;}
    public void setMoney(int n){this.money = n;}
    }


