package com.hdlight.wallpaperapps.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivitySignUpBinding;
import com.hdlight.wallpaperapps.utils.Utils;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends BaseActivity {

    ActivitySignUpBinding binding;
    boolean passwordVisible;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.FullScreenUIMode(SignUpActivity.this);

        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        binding.etUserName.setCursorVisible(true);
        binding.imgHideShow.setImageResource(R.drawable.hide);
        binding.imgHideShow.setOnClickListener(this);
        binding.txtCreateButton.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);

//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("users/9316256275");
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                SignUp signUp = snapshot.getValue(SignUp.class);
//
////                Toast.makeText(SignUpActivity.this, signUp.getMobile(), Toast.LENGTH_SHORT  ).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("SignUp", "onCancelled:" + error );
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgHideShow:
                showHidePass(binding.imgHideShow);
                break;
            case R.id.txtLogin:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;
            case R.id.txtCreateButton:
//                startActivity(new Intent(SignUpActivity.this, OtpActivity.class));
//                if (binding.etMobileNumber.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(SignUpActivity.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
//                } else if (binding.etMobileNumber.getText().toString().trim().length() < 10) {
//                    Toast.makeText(SignUpActivity.this, "Please enter correct Number", Toast.LENGTH_SHORT).show();
//                } else {
//                    sendOtp();
//                }
                if (isValid()) {
                    sendOtp();
                }
                break;
        }
    }

    public void showHidePass(View view) {

        if (view.getId() == R.id.imgHideShow) {

            if (binding.etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.hide);

                //Show Password
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.show);

                //Hide Password
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    private void sendOtp() {

        String userName = binding.etUserName.getText().toString();
        String mobileNumber = binding.etMobileNumber.getText().toString().trim();
        String password = binding.etPassword.getText().toString();

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.txtCreateButton.setVisibility(View.INVISIBLE);

        reference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(mobileNumber)){
                    Toast.makeText(SignUpActivity.this, "User already exits", Toast.LENGTH_SHORT).show();

                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtCreateButton.setVisibility(View.VISIBLE);

                }else {

//                    Toast.makeText(SignUpActivity.this, "onCodeSent", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtCreateButton.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(SignUpActivity.this.getApplicationContext(), OtpActivity.class);
                    intent.putExtra("username", userName);
                    intent.putExtra("mobile", mobileNumber);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    boolean isValid() {
        String message = "";

        if (binding.etUserName.getText().toString().trim().isEmpty()) {
            message = getString(R.string.please_enter_username);
            binding.etUserName.requestFocus();
        } else if (binding.etMobileNumber.getText().toString().trim().isEmpty()) {
            message = getString(R.string.please_enter_mobile_number);
            binding.etMobileNumber.requestFocus();
        } else if (binding.etMobileNumber.getText().toString().trim().length() < 10) {
            message = getString(R.string.mobile_no_length);
            binding.etMobileNumber.requestFocus();
        } else if (binding.etPassword.getText().toString().trim().isEmpty()) {
            message = getString(R.string.please_enter_password);
            binding.etPassword.requestFocus();
        } else if (binding.etPassword.getText().toString().trim().length() < 8) {
            message = getString(R.string.password_length);
            binding.etPassword.requestFocus();
        }

        if (!message.trim().isEmpty()) {
            Utils.hideKeyBoardFromView(this);
//            Utils.showSnackBar(rootView, activity, message, Const.alert, Const.successDuration);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        }
        return message.trim().isEmpty();
    }


}