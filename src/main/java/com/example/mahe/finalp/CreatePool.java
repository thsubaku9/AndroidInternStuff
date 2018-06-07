package com.example.mahe.finalp;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePool extends AppCompatActivity {

    ImageButton mp;
    Button cre;
    EditText a1,a2,a3,a4,a5,a6;
    String st,stp,enp,cst,seats,sdate;
    static Intent j;
    public String uid;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);
        a1=(EditText) findViewById(R.id.editText13);
        a2=(EditText) findViewById(R.id.editText14);
        a3=(EditText) findViewById(R.id.editText15);
        a4=(EditText) findViewById(R.id.editText16);
        a5=(EditText) findViewById(R.id.editText17);
        a6=(EditText) findViewById(R.id.editText18);

        mp=(ImageButton) findViewById(R.id.imageButton2);
        cre=(Button) findViewById(R.id.buttonCreatePool);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Intent doda=getIntent();
        uid=doda.getStringExtra("uid");
        double[] locations=doda.getDoubleArrayExtra("coord");

        try
        {
            a1.setText(String.format("%f & %f",locations[0],locations[1]));
            a2.setText(String.format("%f & %f",locations[2],locations[3]));
        }
        catch(Exception e)
        {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j = new Intent(getApplicationContext(),Map.class);
                j.putExtra("fromActivity","create");    //decides how the next activity should open
                j.putExtra("uid",uid);                        //necessary value
                startActivity(j);
                finish();
            }
        });

        cre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(a1.getText().toString())|TextUtils.isEmpty(a2.getText().toString())|TextUtils.isEmpty(a3.getText().toString())|TextUtils.isEmpty(a4.getText().toString())|TextUtils.isEmpty(a5.getText().toString())|TextUtils.isEmpty(a6.getText().toString()))
                {
                    Snackbar.make(v,"Empty fields Present",Snackbar.LENGTH_SHORT).setAction("notty",null).show();

                }

                else
                {
                    vcreatepool nupool=new vcreatepool(a1.getText().toString(),a2.getText().toString(),a3.getText().toString(),a4.getText().toString(),a5.getText().toString(),a6.getText().toString(),"",uid);
                    try
                    {
                        myRef.child("uid").child(uid).setValue(nupool);
                        myRef.child("stend").child(nupool.stend).setValue(nupool);
                        Toast.makeText(getApplicationContext(),"Pool has been created",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch (Exception e)
                    {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();}
                }
            }
        });
    }
}
