package com.example.foodworm2;

//전체 유저들의 사용내역을 기록하는 클래스
public class UsingHistroy {
    //사용자 아이디
    public String id;
    //사용한 날짜
    public String date_year;
    public String date_month;
    public String date_day;
    //거래내역 Key값
    public String usingkey;
    //사용한 식권의 종류
    public String kindsofticket;  //1. 한식, 2. 양식 3. 일식

    public UsingHistroy(String id, String date, String usingkey, String kindsofticket){
        this.id = id;
        this.usingkey = usingkey;
        this.kindsofticket = kindsofticket;

        int idx = date.indexOf("년");
        this.date_year = date.substring(0,idx);
        //year를 끄집어내어 저장
        //date에서 year를 날린다
        date = date.substring(idx+1);

        //월idx찾아 저장
        idx = date.indexOf("월");
        this.date_month = date.substring(0,idx);
        //date에서 month를 끄집어내어 저장
        date = date.substring(idx+1);
        //date에서 month를 날린다

        //일 idx를 찾아 저장
        idx = date.indexOf("일");
        this.date_day = date.substring(0,idx);
    }

    public UsingHistroy(){}
}
