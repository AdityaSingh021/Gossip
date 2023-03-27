package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String AuthenticationId;
    private EditText edtOTP;
    private TextView verifyOTPBtn;
    private TextView generateOTPBtn;
    private EditText Name;
    private EditText edtPhone;
    private String s;
    private String n;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        Name=findViewById(R.id.name);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
        verifyOTPBtn.setVisibility(View.INVISIBLE);
        edtOTP.setVisibility(View.INVISIBLE);
        mAuth=FirebaseAuth.getInstance();


        if(!MemoryData.getData(this).isEmpty()){
            Intent i = new Intent(Login.this, BottomNavigationPage.class);
            Bundle bundle =new Bundle();
            bundle.putString("mobile",MemoryData.getData(this));
            bundle.putString("name",MemoryData.getData(this));
//            i.putExtra("mobile",MemoryData.getData(this));
//            i.putExtra("name",MemoryData.getName(this));
            BlankFragment frag=new BlankFragment();
            frag.setArguments(bundle);
//            Fragmentclass frag=new Fragmentclass();
            startActivity(i);
            finish();
        }

//        SharedPreferences sh = getSharedPreferences("MyAuthenticationId", MODE_PRIVATE);
//        String s1 = sh.getString("AuthId", "");
//        if(!s1.isEmpty()){
//            Intent i=new Intent(getApplicationContext(),HomeActivity.class);
//            startActivity(i);
//            finish();
//        }
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
                    } else {
                        String phone = "+91" + edtPhone.getText().toString();
                        Toast.makeText(getApplicationContext(), "Sending Request", Toast.LENGTH_SHORT).show();
                        sendVerificationCode(phone);
                    }




            }
        });
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s=edtPhone.getText().toString();
                n=Name.getText().toString();
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(Login.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.child("Users").hasChild(s)){
                                databaseReference.child("Users").child(s).child("Name").setValue(n);
                                Toast.makeText(getApplicationContext(),"Registering...",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(edtOTP.getText().toString());
                }
            }
        });
    }



    // End of On Create Method ...............................................................................................................................................>




    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        verifyOTPBtn.setVisibility(View.VISIBLE);
        edtOTP.setVisibility(View.VISIBLE);
        generateOTPBtn.setVisibility(View.INVISIBLE);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            AuthenticationId=s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                edtOTP.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    public void verifyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(AuthenticationId,code);
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.



                            //shared preference to store authentication id;

                            SharedPreferences sharedPreferences = getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("AuthId", AuthenticationId);
                            myEdit.apply();


                            //save mobile to memory

                            MemoryData.saveData(edtPhone.getText().toString(),Login.this);


                            //saving name to Memory

                            MemoryData.saveName(n,Login.this);
                            Intent i = new Intent(Login.this, HomeActivity.class);
                            i.putExtra("mobile",s);
                            i.putExtra("name",n);
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}