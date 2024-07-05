package com.hdlight.wallpaperapps.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityLoginBinding;
import com.hdlight.wallpaperapps.utils.SharedPref;
import com.hdlight.wallpaperapps.utils.Utils;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.FullScreenUIMode(LoginActivity.this);

        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wallpaper-19f8f-default-rtdb.firebaseio.com/");
        binding.imgHideShow.setOnClickListener(this);
        binding.txtCreateNow.setOnClickListener(this);
        binding.txtLoginButton.setOnClickListener(this);
        binding.txtForgotPassword.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgHideShow:
                showHidePass(binding.imgHideShow);
                break;
            case R.id.txtCreateNow:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.txtLoginButton:
                if (isValid()){
                    binding.txtLoginButton.setVisibility(View.INVISIBLE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    signingUser();
                }
                break;
            case R.id.txtForgotPassword:
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                break;

        }
    }

    private void signingUser() {

       final String mobileNumber = binding.etMobileNumber.getText().toString().trim();
       final String password = binding.etPassword.getText().toString();

       firebaseAuth.signInWithCustomToken(password);

               reference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                       if (snapshot.hasChild(mobileNumber)){

                           final String getPassword = snapshot.child(mobileNumber).child("password").getValue(String.class);
                           if (getPassword.equals(password)){
                               SharedPref.loginSharePref(LoginActivity.this,mobileNumber,password,true);

                               new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                   public final void run() {
                                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                       finish();
                                       binding.txtLoginButton.setVisibility(View.VISIBLE);
                                       binding.progressBar.setVisibility(View.GONE);
                                   }
                               }, 1000);
                           }else {
                               Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                               binding.txtLoginButton.setVisibility(View.VISIBLE);
                               binding.progressBar.setVisibility(View.GONE);
                           }
                       }else {
                           Toast.makeText(LoginActivity.this, "Wrong mobile number", Toast.LENGTH_SHORT).show();
                           binding.txtLoginButton.setVisibility(View.VISIBLE);
                           binding.progressBar.setVisibility(View.GONE);
                       }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
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

    boolean isValid() {
        String message = "";

        if (binding.etMobileNumber.getText().toString().trim().isEmpty()) {
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