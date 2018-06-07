package com.example.mahe.finalp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText e2,e3;
    Button login;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e2=(EditText) findViewById(R.id.editText);
        e3=(EditText) findViewById(R.id.editText2);
        login=(Button) findViewById(R.id.button);
        mAuth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=e2.getText().toString();
                String pass=e3.getText().toString();
                if(TextUtils.isEmpty(email)|TextUtils.isEmpty(pass))
                {
                    Snackbar.make(v,"Empty fields detected",Snackbar.LENGTH_SHORT).setAction("notty",null).show();
                }
                else if((!email.contains("@")) | !(email.contains(".")))
                {
                    Snackbar.make(v,"Invalid Email type",Snackbar.LENGTH_SHORT).setAction("notty",null).show();
                }

                else
                {
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                Intent megs=new Intent(Login.this,MainView.class);
                                megs.putExtra("name",name);
                                megs.putExtra("email",email);
                                megs.putExtra("uid",uid);
                                megs.putExtra("password",e3.getText().toString());
                                startActivity(megs);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Sign in error",Toast.LENGTH_SHORT).show();}
                        }
                    });
                }
            }
        });
    }

    public void switchtocreate( android.view.View a)
    {Intent ik=new Intent(getApplicationContext(),UserCreate.class);
    startActivity(ik);
    }

    public void switchtoforgot(android.view.View a)
    {Intent ij=new Intent(getApplicationContext(),ForgotP.class);
        startActivity(ij);}

}
