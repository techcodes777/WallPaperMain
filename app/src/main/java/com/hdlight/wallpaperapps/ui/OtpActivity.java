package com.hdlight.wallpaperapps.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityOtpBinding;
import com.hdlight.wallpaperapps.utils.Constant;
import com.hdlight.wallpaperapps.utils.SharedPref;
import com.hdlight.wallpaperapps.utils.Utils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends BaseActivity {

    ActivityOtpBinding binding;
    String getbackendotp;
    String userName;
    String mobileNumber;
    String password;
    String data;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);

        Utils.FullScreenUIMode(OtpActivity.this);
        userName = getIntent().getStringExtra("username");
        mobileNumber = getIntent().getStringExtra("mobile");
        password = getIntent().getStringExtra("password");
        data = getIntent().getStringExtra("data");

        Constant.phNo = mobileNumber;
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            }

            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OtpActivity.this, e.getMessage(), 0).show();
            }

            public void onCodeSent(String backendotp, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
               getbackendotp = backendotp;
               mResendToken = forceResendingToken;
            }
        };
        sendOtp();
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wallpaper-19f8f-default-rtdb.firebaseio.com/");

        binding.txtVerifyButton.setOnClickListener(this);
        binding.txtResendOtp.setOnClickListener(this);

        EditText[] edit = {binding.inputotp1, binding.inputotp2, binding.inputotp3, binding.inputotp4, binding.inputotp5, binding.inputotp6};

        binding.inputotp1.addTextChangedListener(new GenericTextWatcher(binding.inputotp1, edit));
        binding.inputotp2.addTextChangedListener(new GenericTextWatcher(binding.inputotp2, edit));
        binding.inputotp3.addTextChangedListener(new GenericTextWatcher(binding.inputotp3, edit));
        binding.inputotp4.addTextChangedListener(new GenericTextWatcher(binding.inputotp4, edit));
        binding.inputotp5.addTextChangedListener(new GenericTextWatcher(binding.inputotp5, edit));
        binding.inputotp6.addTextChangedListener(new GenericTextWatcher(binding.inputotp6, edit));



    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txtVerifyButton:
                submitOtp();
                break;
            case R.id.txtResendOtp:
                binding.txtResendOtp.setVisibility(4);
                binding.progressBarResend.setVisibility(0);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public final void run() {
                        resendOtp();
                    }
                }, 2000);
                break;
        }
    }

    private void sendOtp() {
        PhoneAuthProvider instance = PhoneAuthProvider.getInstance();
        instance.verifyPhoneNumber("+91" + this.mobileNumber, 60, TimeUnit.SECONDS, OtpActivity.this, mCallbacks);
    }

    public void resendOtp() {
        PhoneAuthProvider instance = PhoneAuthProvider.getInstance();
        instance.verifyPhoneNumber("+91" + Constant.phNo, 60, TimeUnit.SECONDS, this, this.mCallbacks, this.mResendToken);
        binding.txtResendOtp.setVisibility(View.VISIBLE);
        binding.progressBarResend.setVisibility(View.GONE);
    }

    private void submitOtp() {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("username", userName);
//        map.put("mobile", mobileNumber);
//        map.put("password", password);
//        reference.push().se
        if (!binding.inputotp1.getText().toString().trim().isEmpty() && !binding.inputotp2.getText().toString().trim().isEmpty()
                && !binding.inputotp3.getText().toString().trim().isEmpty()
                && !binding.inputotp4.getText().toString().trim().isEmpty()
                && !binding.inputotp5.getText().toString().trim().isEmpty()
                && !binding.inputotp6.getText().toString().trim().isEmpty()) {

            String getuserotp = binding.inputotp1.getText().toString() +
                    binding.inputotp2.getText().toString() +
                    binding.inputotp3.getText().toString() +
                    binding.inputotp4.getText().toString() +
                    binding.inputotp5.getText().toString() +
                    binding.inputotp6.getText().toString();

            if (getbackendotp != null) {

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.txtVerifyButton.setVisibility(View.INVISIBLE);

                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getbackendotp, getuserotp);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(OtpActivity.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                binding.progressBar.setVisibility(View.GONE);
                                binding.txtVerifyButton.setVisibility(View.VISIBLE);

                                if (task.isSuccessful()) {
                                    //do create new user
                                    if (data != null){
                                        if (data.equals("update")){
//                                            SharedPref.passSharePref(OtpActivity.this,"1");
                                            startActivity(new Intent(OtpActivity.this,NewPasswordActivity.class).putExtra("mobile",mobileNumber));
                                            finish();
                                            Log.e("Update Otp", "onComplete: " );

//                                            Toast.makeText(OtpActivity.this, "Update Otp", Toast.LENGTH_SHORT).show();
                                        }else {
                                        }
                                    }else {
                                        reference.child("users").child(mobileNumber).child("username").setValue(userName);
                                        reference.child("users").child(mobileNumber).child("mobile").setValue(mobileNumber);
                                        reference.child("users").child(mobileNumber).child("password").setValue(password);
                                        SharedPref.passSharePref(OtpActivity.this,"0");

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                }
                                else {
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        // The verification code entered was invalid
                                        Toast.makeText(OtpActivity.this, "Enter correct OTP", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });


            } else {
                Toast.makeText(OtpActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(OtpActivity.this, "Please fill all number", Toast.LENGTH_SHORT).show();
        }


    }

    public class GenericTextWatcher implements TextWatcher {
        private final EditText[] editText;
        private View view;

        public GenericTextWatcher(View view, EditText editText[]) {
            this.editText = editText;
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.inputotp1:
                    if (text.length() == 1)
                        editText[1].requestFocus();
                    break;
                case R.id.inputotp2:

                    if (text.length() == 1)
                        editText[2].requestFocus();
                    else if (text.length() == 0)
                        editText[0].requestFocus();
                    break;
                case R.id.inputotp3:
                    if (text.length() == 1)
                        editText[3].requestFocus();
                    else if (text.length() == 0)
                        editText[1].requestFocus();
                    break;
                case R.id.inputotp4:
                    if (text.length() == 1)
                        editText[4].requestFocus();
                    else if (text.length() == 0) {
                        editText[2].requestFocus();
                    }
                    break;
                case R.id.inputotp5:
                    if (text.length() == 1)
                        editText[5].requestFocus();
                    else if (text.length() == 0) {
                        editText[3].requestFocus();
                    }
                    break;
                case R.id.inputotp6:
                    if (text.length() == 0)
                        editText[4].requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }


    public class PhoneNumberFormattingTextWatcher implements TextWatcher {

        private boolean mFormatting;
        private boolean mDeletingHyphen;
        private int mHyphenStart;
        private boolean mDeletingBackward;

        public PhoneNumberFormattingTextWatcher() {
        }

        public synchronized void afterTextChanged(Editable text) {
            // Make sure to ignore calls to afterTextChanged caused by the work done below
            if (!mFormatting) {
                mFormatting = true;

                // If deleting the hyphen, also delete the char before or after that
                if (mDeletingHyphen && mHyphenStart > 0) {
                    if (mDeletingBackward) {
                        if (mHyphenStart - 1 < text.length()) {
                            text.delete(mHyphenStart - 1, mHyphenStart);
                        }
                    } else if (mHyphenStart < text.length()) {
                        text.delete(mHyphenStart, mHyphenStart + 1);
                    }
                }

                PhoneNumberUtils.formatNumber(text, PhoneNumberUtils.FORMAT_NANP);

                mFormatting = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Check if the user is deleting a hyphen
            if (!mFormatting) {
                // Make sure user is deleting one char, without a selection
                final int selStart = Selection.getSelectionStart(s);
                final int selEnd = Selection.getSelectionEnd(s);
                if (s.length() > 1 // Can delete another character
                        && count == 1 // Deleting only one character
                        && after == 0 // Deleting
                        && s.charAt(start) == '-' // a hyphen
                        && selStart == selEnd) { // no selection
                    mDeletingHyphen = true;
                    mHyphenStart = start;
                    // Check if the user is deleting forward or backward
                    if (selStart == start + 1) {
                        mDeletingBackward = true;
                    } else {
                        mDeletingBackward = false;
                    }
                } else {
                    mDeletingHyphen = false;
                }
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Does nothing
        }
    }

}