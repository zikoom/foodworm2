package com.example.foodworm2;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String id;
    public String pw;
    public String pushkey;

    //식권 보유량
    public int kor;
    public int jpa;
    public int usa;

    //식권구매횟수
    public int numofbuying_kor;
    public int numofbuying_jpa;
    public int numofbuying_usa;

    //식권사용횟수
    public int numofusing_kor;
    public int numofusing_jpa;
    public int numofusing_usa;

    public User(String id, String pw, String key){
        this.id = id;
        this.pw = pw;
        pushkey = key;

        kor=0;
        jpa=0;
        usa=0;

        //누적 식권구매량
        numofbuying_kor = 0;
        numofbuying_jpa = 0;
        numofbuying_usa = 0;

        //누적 식권 사용량
        numofusing_kor = 0;
        numofusing_jpa = 0;
        numofusing_usa = 0;


    }

    public User(){}

}
