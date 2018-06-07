package com.example.mahe.finalp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Search extends AppCompatActivity {

    ImageButton gmap;
    Button fpool,fidpool;
    static Intent i,carry;
    public String uid,email,stend;
    EditText st,en,sttime,stdate,fuid;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        st=(EditText) findViewById(R.id.editText8);
        en=(EditText) findViewById(R.id.editText9);
        sttime=(EditText) findViewById(R.id.editText10);
        stdate=(EditText) findViewById(R.id.editText11);
        fuid=(EditText) findViewById(R.id.editText12);
        gmap = (ImageButton) findViewById(R.id.imageButton);
        fpool=(Button) findViewById(R.id.button7);
        fidpool=(Button) findViewById(R.id.button8);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Intent doda=getIntent();
        uid=doda.getStringExtra("uid");
        email=doda.getStringExtra("email");

        double[] locations=doda.getDoubleArrayExtra("coord");
        try
        {

            st.setText(String.format("%f & %f",locations[0],locations[1]));
            en.setText(String.format("%f & %f",locations[2],locations[3]));
        }
        catch(Exception e)
        {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        //doda wont be null so make sure to handle exception
        gmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(),Map.class);
                i.putExtra("fromActivity","search");
                i.putExtra("uid",uid);
                i.putExtra("email",email);
                startActivity(i);
                finish();
                //startActivityForResult(i, 1);
            }
        });

        fpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(st.getText().toString())|TextUtils.isEmpty(en.getText().toString())|TextUtils.isEmpty(sttime.getText().toString())|TextUtils.isEmpty(stdate.getText().toString()))
                {
                    Snackbar.make(v,"Empty fields detected",Snackbar.LENGTH_SHORT).setAction("notty",null).show();
                }

                else
                {
                    String startp=st.getText().toString();
                    String endp=en.getText().toString();
                    stend=startp+" + "+endp+"~"+sttime.getText().toString()+"<"+stdate.getText().toString();
                    stend=stend.replace(".","*");
                    stend=stend.replace(" & ","&");
                    Query idgaf=myRef.child("stend").orderByChild("stend").equalTo(stend);

                    idgaf.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {   for(DataSnapshot d12 : dataSnapshot.getChildren())
                            {   String[] v=new String[4];
                                v[0]=d12.getValue(vcreatepool.class).startTime;
                                v[1]=d12.getValue(vcreatepool.class).startDate;
                                v[2]=d12.getValue(vcreatepool.class).seats;
                                v[3]=d12.getValue(vcreatepool.class).price;
                                Intent keen=new Intent(Search.this,SearchResult.class);
                                keen.putExtra("gettim",v);
                                keen.putExtra("from","query");
                                keen.putExtra("unq",stend);
                                keen.putExtra("email",email);
                                startActivity(keen);
                                finish();

                            }

                            }
                            else
                            {Toast.makeText(getApplicationContext(),"No match for query",Toast.LENGTH_SHORT).show();}

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //query to search
                }
            }
        });

        fidpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(fuid.getText().toString()))
                {
                    Snackbar.make(v,"UID is empty",Snackbar.LENGTH_SHORT).setAction("notty",null).show();
                }
                else
                {
                    final String srcuid=fuid.getText().toString();
                    Query idq=myRef.child("uid").orderByChild("uid").equalTo(srcuid);
                    idq.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {   for(DataSnapshot d12 : dataSnapshot.getChildren())
                            {   String[] v=new String[4];
                                v[0]=d12.getValue(vcreatepool.class).startTime;
                                v[1]=d12.getValue(vcreatepool.class).startDate;
                                v[2]=d12.getValue(vcreatepool.class).seats;
                                v[3]=d12.getValue(vcreatepool.class).price;
                                Intent keen=new Intent(Search.this,SearchResult.class);
                                keen.putExtra("gettim",v);
                                keen.putExtra("from","uid");
                                keen.putExtra("unq",srcuid);
                                keen.putExtra("email",email);
                                startActivity(keen);
                                finish();

                            }

                            }
                            else
                            {Toast.makeText(getApplicationContext(),"No match for UID detected",Toast.LENGTH_SHORT).show();}

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }
}

/*
idq.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                //Toast.makeText(getApplicationContext(),"It exists",Toast.LENGTH_SHORT).show();
                            for(DataSnapshot d1: dataSnapshot.getChildren())
                            {   String name= d1.getValue(userd.class).name;
                                int age= d1.getValue(userd.class).age;
                                Toast.makeText(getApplicationContext(),String.format("Username: %s \nAge: %d",name,age), Toast.LENGTH_SHORT).show();
                            }
                        }}

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
 */