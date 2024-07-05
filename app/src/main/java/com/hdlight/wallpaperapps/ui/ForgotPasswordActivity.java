package com.hdlight.wallpaperapps.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityForgotPasswordBinding;
import com.hdlight.wallpaperapps.utils.SharedPref;
import com.hdlight.wallpaperapps.utils.Utils;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends BaseActivity {


    ActivityForgotPasswordBinding binding;
    String countryCode = "+91";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.FullScreenUIMode(ForgotPasswordActivity.this);

        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);
        binding.txtOtpButton.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.txtOtpButton.setVisibility(View.GONE);

            final String phNo = binding.etMobileNumber.getText().toString().trim();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            Query checkUser = FirebaseDatabase.getInstance().getReference("users").child(phNo);
//            Toast.makeText(this, checkUser.toString(), Toast.LENGTH_SHORT).show();
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e("Info", "onDataChange: " + String.valueOf(snapshot.getValue()));

                    if (snapshot.exists()) {
//                        Toast.makeText(ForgotPasswordActivity.this, "Otp", Toast.LENGTH_SHORT).show();
//                        sendOtp(phNo);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            public final void run() {
                                Intent intent = new Intent(ForgotPasswordActivity.this.getApplicationContext(), OtpActivity.class);
                                intent.putExtra("mobile", phNo);
                                intent.putExtra("data", "update");
                                startActivity(intent);
                                finish();
                                binding.progressBar.setVisibility(View.GONE);
                                binding.txtOtpButton.setVisibility(View.VISIBLE);
                            }
                        }, 1000);

                    }else {
                        Toast.makeText(ForgotPasswordActivity.this, "User not register", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    public void sendOtp(String mobileNumber){

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.txtOtpButton.setVisibility(View.INVISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber,
                60,
                TimeUnit.SECONDS,
                ForgotPasswordActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        binding.progressBar.setVisibility(View.GONE);
                        binding.txtOtpButton.setVisibility(View.VISIBLE);

                        Toast.makeText(ForgotPasswordActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        binding.progressBar.setVisibility(View.GONE);
                        binding.txtOtpButton.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        binding.progressBar.setVisibility(View.GONE);
                        binding.txtOtpButton.setVisibility(View.VISIBLE);

                        Toast.makeText(ForgotPasswordActivity.this, "onCodeSent", Toast.LENGTH_SHORT).show();

//                        SharedPref.passSharePref(ForgotPasswordActivity.this,"1");

                        Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                        intent.putExtra("mobile", mobileNumber);
                        intent.putExtra("backendotp", backendotp);
                        intent.putExtra("data", "update");
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

}