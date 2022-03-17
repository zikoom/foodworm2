package com.example.foodworm2;

import android.util.Log;

public class PurchaseHistory {
    //구매자 아이디
    public String id;
    //구매한 날짜
    public String date_year;
    public String date_month;
    public String date_day;
    public String date_time;
    //각각의 식권별 구매한 개수
    public int kor;
    public int jpa;
    public int usa;
    //거래내역 Key값
    public String purchaseKey;

    public PurchaseHistory(String id, String date, String purchaseKey, int kor, int jpa, int usa){
        this.id = id;
        this.purchaseKey = purchaseKey;
        this.kor = kor;
        this.jpa = jpa;
        this.usa = usa;

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
    public PurchaseHistory(){}

}
