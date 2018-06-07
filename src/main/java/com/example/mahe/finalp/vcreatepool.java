package com.example.mahe.finalp;

public class vcreatepool {
    public String startPoint;
    public String endPoint;
    public String startTime;
    public String startDate;
    public String seats;
    public String price;
    public String stend;
    public String email;
    public String uid;
    vcreatepool()
    {}
    vcreatepool(String startp, String endp, String startT, String startD, String seats, String price,String email,String uid)
    {   this.uid=uid;
        this.startPoint=startp;
        this.endPoint=endp;
        this.startTime=startT;
        this.startDate=startD;
        this.seats=seats;
        this.price=price;
        this.stend=startp+" + "+endp+"~"+startT+"<"+startD;
        this.stend=this.stend.replace(".","*");
        this.stend=this.stend.replace(" & ","&");
        this.email=email;
    }

}
