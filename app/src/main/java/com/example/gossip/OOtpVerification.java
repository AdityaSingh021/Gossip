package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OOtpVerification extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String AuthenticationId;
    private ImageView take_image;
    private boolean resend=false;
    private EditText edtOTP;
    private TextView generateOTPBtn;
    private EditText Name;
    private TextView textView1;
    private ImageView editPhoneNumber;
    private TextView timerTextView;
    private String edtPhone;
    private String s;
    private String n;
    Uri filepath;
    Bitmap bitmap;
    ImageView Mydp;
    private String profilePicLink;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");

//    private String edtPhone="";
    private TextView verifyOTPBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ootp_verification);
        verifyOTPBtn=findViewById(R.id.verifyOTPBtn);
        mAuth=FirebaseAuth.getInstance();
        PinView pinView=findViewById(R.id.pinview);
        edtPhone=String.valueOf(getIntent().getStringExtra("Mobile"));
        Toast.makeText(getApplicationContext(), "Sending OTP", Toast.LENGTH_SHORT).show();
        timerTextView=findViewById(R.id.textView);
        editPhoneNumber=findViewById(R.id.editPhone);
        textView1=findViewById(R.id.textView1);
        textView1.setText("sent on +91"+edtPhone);
//        startTimer();
        timerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resend) sendVerificationCode("+91"+edtPhone);
                Toast.makeText(getApplicationContext(),"Resending OTP",Toast.LENGTH_SHORT).show();
            }
        });
        sendVerificationCode("+91"+edtPhone);
        editPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadtofirebase();
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(pinView.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                } else {
//                    Log.i("justCheck1","yess");
//                    Toast.makeText(getApplicationContext(),"1111111",Toast.LENGTH_SHORT).show();
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(pinView.getText().toString());
                }
            }
        });
    }



    // End of On Create Method ...............................................................................................................................................>

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        startTimer();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
//        verifyOTPBtn.setVisibility(View.VISIBLE);
//        edtOTP.setVisibility(View.VISIBLE);
//        generateOTPBtn.setVisibility(View.INVISIBLE);

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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public void verifyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(AuthenticationId,code);
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        Toast.makeText(getApplicationContext(),"Signing In please wait",Toast.LENGTH_SHORT).show();
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
                            int len=AuthenticationId.length();
                            Log.i("MyAuthId",AuthenticationId);
                            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child(edtPhone).child("Name").exists()){
                                        String myName=snapshot.child(edtPhone).child("Name").getValue(String.class);
                                        Container.setMyName(myName);
                                        myEdit.putString("name",myName);



                                        try{
                                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                            StorageReference storageReference = firebaseStorage.getReference();
                                            StorageReference imgRef = storageReference.child("ProfilePictures").child(edtPhone);

                                            imgRef.getBytes(5024 * 5024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                                temp = bitmap;
                                                    if (bitmap != null)
                                                        MemoryData.saveProfilePicture(bitmap, edtPhone, getApplicationContext());


                                                }
                                            }).addOnFailureListener(exception -> {
                                            });
                                        }catch (Exception e){}

                                        myEdit.apply();
                                    }else{
                                        String UserId="User@"+AuthenticationId.substring(len-5,len);
                                        Container.setMyName(UserId);
                                        databaseReference.child("Users").child(edtPhone).child("Name").setValue(UserId);
                                        databaseReference.child("Users").child(edtPhone).child("State").setValue(0);
                                        myEdit.putString("name",UserId);
                                        myEdit.apply();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            myEdit.apply();


                            //save mobile to memory
//                            Toast.makeText(getApplicationContext(),edtPhone,Toast.LENGTH_SHORT).show();
//                            Log.i()
                            MemoryData.saveData(String.valueOf(edtPhone),getApplicationContext());


//                            saving name to Memory
//                            Toast.makeText(getApplicationContext(),Name.getText().toString(),Toast.LENGTH_SHORT).show();
//                            MemoryData.saveName(Name.getText().toString(),getApplicationContext());
//                            databaseReference.child("Users").child(edtPhone).child()
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("mobile",edtPhone.toString());
                            i.putExtra("FirstTime","Yes");
//                            i.putExtra("name","Aditya");
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
//    public void uploadtofirebase(){
////        ProgressDialog dialog=new ProgressDialog(this);
////        dialog.setTitle("File Uploader");
////        dialog.show();
//        FirebaseStorage storage=FirebaseStorage.getInstance();
//        StorageReference uploader=storage.getReference().child(String.valueOf(edtPhone));
//        if(filepath!=null){
//            uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                if (!Login.this.isFinishing() && dialog != null) {
////                dialog.dismiss();
////                }
//                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            s=edtPhone;
////                            n=Name.getText().toString();
//                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                            if(!snapshot.child("Users").hasChild(s)){
////                                Log.i("justCheck2","yess");
//                                    databaseReference.child("Users").child(s).child("Name").setValue(n);
//                                    databaseReference.child(s).child("Status").setValue(0);
////                                if(profilePicLink==null) {
////                                    databaseReference.child(s).child("profilePic").setValue("");
////                                    Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
////                                }
//                                    databaseReference.child(s).child("profilePic").setValue(uri.toString());
////                                    Toast.makeText(getApplicationContext(),"Registering...",Toast.LENGTH_SHORT).show();
////                            }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
//                                }
//                            });
////                        profilePicLink=uri.toString();
////                        Toast.makeText(getApplicationContext(),profilePicLink,Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
////                float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
////                dialog.setMessage("Uploaded :"+(int)percent+" %");
//                }
//            });
//        }
//    }

    //------------- Resend Otp Timer ------------------------------->

    private void startTimer() {
        timerTextView.setTextColor(Color.WHITE);
        // Initialize the countdown timer
        CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer TextView with the remaining time
                long secondsRemaining = millisUntilFinished / 1000;
                timerTextView.setText("Resend OTP in " + secondsRemaining + " seconds");
            }

            @Override
            public void onFinish() {
                // Timer finished, enable the "Resend OTP" button
                timerTextView.setText("Resend OTP"); // Clear the timer text
                timerTextView.setTextColor(Color.parseColor("#B2F3E9"));
                resend=true;
            }
        };

        // Start the timer
        countDownTimer.start();
    }
}