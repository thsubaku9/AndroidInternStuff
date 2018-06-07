package com.example.mahe.finalp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class UserCreate extends AppCompatActivity {

    EditText email,phoneno,password,uname;
    Button crate;
    Spinner gen;
    public static String temail,tphone,tpass,tuname,tgen;
    private FirebaseAuth mAuth;
    private String verifyCode;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private static final String TAG = "PhoneAuthentication: ";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);
        email=(EditText) findViewById(R.id.editText3);
        phoneno=(EditText) findViewById(R.id.editText4);
        password=(EditText) findViewById(R.id.editText6);
        uname=(EditText) findViewById(R.id.editText5);
        crate=(Button) findViewById(R.id.button2);
        gen=(Spinner) findViewById(R.id.gender);

        mAuth=FirebaseAuth.getInstance();
        String[] gn={"Male","Female"};
        ArrayAdapter aa= new ArrayAdapter(this,android.R.layout.simple_spinner_item,gn);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gen.setAdapter(aa);

        crate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                temail=email.getText().toString();
                tpass=password.getText().toString();
                tphone=phoneno.getText().toString();
                tuname=uname.getText().toString();
                tgen=gen.toString();
                if(TextUtils.isEmpty(temail)|TextUtils.isEmpty(tpass)|TextUtils.isEmpty(tphone)|TextUtils.isEmpty(tuname))
                {
                    Snackbar.make(v,"One of the fields is empty",Snackbar.LENGTH_SHORT).setAction("Action",null).show();}
                else if((!temail.contains("@")) | !(temail.contains(".")) | (tphone.contains("~")))
                {Snackbar.make(v,"Illegal string",Snackbar.LENGTH_SHORT).setAction("Action",null).show();}
                else
                {
                    //First authenticate otp
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(tphone,45,TimeUnit.SECONDS,UserCreate.this,mCallbacks);
                    //Add user to database (add this fn in onComplete method of OTPhandler

                }
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG,"OTP verfied");
            signInWithPhoneAuthCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "Verification failed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException|| e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show(); }
            }

            @Override
            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token)
            {super.onCodeSent(verificationId,token);
             Log.d(TAG, "onCodeSent:" + verificationId);
             verifyCode = verificationId;mResendToken = token;
            }

            private void signInWithPhoneAuthCredentials(PhoneAuthCredential credential)
            {
                mAuth.signInWithCredential(credential).addOnCompleteListener(UserCreate.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //mAuth.createUserWithEmailAndPassword("humblebumble@yo.u","honeycombs");

                           mAuth.createUserWithEmailAndPassword(temail,tpass).addOnCompleteListener(UserCreate.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {

                                        mAuth.signInWithEmailAndPassword(temail,tpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                //Adding the necessary details
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.sendEmailVerification();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(tuname+"~"+tphone).build();
                                                //display name will need to be parsed before updating
                                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){Toast.makeText(getApplicationContext(),"User with name: "+ tuname+" has been created",Toast.LENGTH_SHORT).show();}
                                                    }
                                                });

                                                //
                                            }
                                        });

                                    }
                                }
                            });

                        }
                        else
                        {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
            }


        };

    }
}
