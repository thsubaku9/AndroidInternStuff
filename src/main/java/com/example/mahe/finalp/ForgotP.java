package com.example.mahe.finalp;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotP extends AppCompatActivity {

    EditText fpass;
    Button fps;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_p);
        fpass=(EditText) findViewById(R.id.editText7);
        fps=(Button) findViewById(R.id.button3);
        mAuth=FirebaseAuth.getInstance();

        fps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fpass.getText().toString().equals(""))
                {
                    Snackbar.make(v,"Email field is empty",Snackbar.LENGTH_SHORT).setAction("notty",null).show();}
                else if(!fpass.getText().toString().contains("@")|| !fpass.getText().toString().contains("."))
                {
                    Snackbar.make(v,"Invalid Email",Snackbar.LENGTH_SHORT).setAction("notty",null).show();
                }

                else
                {
                    mAuth.sendPasswordResetEmail(fpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Email has been sent",Toast.LENGTH_LONG).show();}
                        }
                    });
                }
            }
        });
    }
}
