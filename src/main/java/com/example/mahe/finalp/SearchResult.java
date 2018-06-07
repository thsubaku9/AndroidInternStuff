package com.example.mahe.finalp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchResult extends AppCompatActivity {

    Intent k2;
    Button p1,p2;
    TextView a1,a2,a3,a4;
    EditText e1;
    String email,from,unq;
    DatabaseReference myRef;
    vcreatepool attempt;
    Integer sec,m;
    String l1,l2,l3,l4,l5,l6,l7,l8,l9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        k2=getIntent();
        myRef= FirebaseDatabase.getInstance().getReference();
        p1=(Button) findViewById(R.id.cancel);
        p2=(Button) findViewById(R.id.accept);
        a1=(TextView) findViewById(R.id.Starttassign);
        a2=(TextView) findViewById(R.id.Startdassign);
        a3=(TextView) findViewById(R.id.seatnoassign);
        a4=(TextView) findViewById(R.id.priceassign);
        e1=(EditText) findViewById(R.id.seatcnt);
        String[] v=new String[4];
        v=k2.getStringArrayExtra("gettim");
        email=k2.getStringExtra("email");
        from=k2.getStringExtra("from");
        unq=k2.getStringExtra("unq");
        a1.setText(v[0]);
        a2.setText(v[1]);
        a3.setText(v[2]);
        a4.setText(v[3]);

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.getText().toString().equals(""))
                {
                    Snackbar.make(v,"Empty seat count",Snackbar.LENGTH_SHORT).setAction("notty",null).show();}
                else
                {
                m=Integer.valueOf(a3.getText().toString());
                sec=Integer.valueOf(e1.getText().toString());
                if(m.intValue()<sec.intValue()){
                    Toast.makeText(getApplicationContext(),"Select a lower seat count",Toast.LENGTH_SHORT).show();}
                else if(sec.intValue()<0)
                {Toast.makeText(getApplicationContext(),"Select a non negative value",Toast.LENGTH_SHORT).show();}

                    else {
                    try{
                    if (from.equals("uid")) {
                        Query idq = myRef.child("uid").orderByChild("uid").equalTo(unq);
                        idq.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot meg : dataSnapshot.getChildren()) {
                                        unq = meg.getValue(vcreatepool.class).stend;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    Query idq = myRef.child("stend").orderByChild("stend").equalTo(unq);
                    idq.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot di : dataSnapshot.getChildren()) {
                                    l1 = di.getValue(vcreatepool.class).startPoint;
                                    l2 = di.getValue(vcreatepool.class).endPoint;
                                    l3 = di.getValue(vcreatepool.class).startTime;      //3
                                    l4 = di.getValue(vcreatepool.class).startDate;
                                    l5 = di.getValue(vcreatepool.class).seats;
                                    l6 = di.getValue(vcreatepool.class).price;      //6
                                    l7 = di.getValue(vcreatepool.class).email;
                                    l8 = di.getValue(vcreatepool.class).uid;
                                    l9 = di.getValue(vcreatepool.class).stend;      //9

                                        l7 = l7 + " ~" + email;

                                    Integer megh=Integer.valueOf(l5)-sec;
                                    l5=megh.toString();
                                    attempt = new vcreatepool(l1, l2, l3, l4, l5, l6, l7, l8);
                                    myRef.child("stend").child(attempt.stend).setValue(attempt);
                                    //vcreatepool(String startp, String endp, String startT, String startD, String seats, String price,String email,String uid)
                                    Toast.makeText(getApplicationContext(), "You have been booked", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                }

                //
            }
        }
        });
    }
}
