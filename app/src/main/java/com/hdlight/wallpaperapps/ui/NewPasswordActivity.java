package com.hdlight.wallpaperapps.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityNewPasswordBinding;
import com.hdlight.wallpaperapps.utils.Constant;
import com.hdlight.wallpaperapps.utils.SharedPref;
import com.hdlight.wallpaperapps.utils.Utils;

public class NewPasswordActivity extends BaseActivity {

    ActivityNewPasswordBinding binding;
    String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.FullScreenUIMode(NewPasswordActivity.this);
        mobileNo = getIntent().getStringExtra("mobile");
        Constant.moNo = mobileNo;

        binding.imgHideShow.setOnClickListener(this);
        binding.imgConfirmHideShow.setOnClickListener(this);
        binding.txtUpdateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgHideShow:
                showHidePass(binding.imgHideShow);
                break;
            case R.id.imgConfirmHideShow:
                showHideConfPass(binding.imgConfirmHideShow);
                break;
            case R.id.txtUpdateButton:
                if (isValid()) {
                    binding.txtUpdateButton.setVisibility(View.INVISIBLE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    setNewPassword();
                }
                break;
        }
    }

    private void setNewPassword() {

        String phoneNo = binding.etConfirmPassword.getText().toString().trim();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(Constant.moNo).child("password").setValue(phoneNo);

//        SharedPreferences sp = getSharedPreferences("pass", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        editor.apply();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public final void run() {
                startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
                finish();
                binding.txtUpdateButton.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        }, 1000);

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

    private void showHideConfPass(View view) {

//        Toast.makeText(this, "Confirm", Toast.LENGTH_SHORT).show();

        if (binding.etConfirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.hide);

            //Show Password
            binding.etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.show);

            //Hide Password
            binding.etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }


    boolean isValid() {
        String message = "";

        String etPass = binding.etPassword.getText().toString().trim();
        String etCoPass = binding.etConfirmPassword.getText().toString().trim();

        if (etPass.isEmpty()) {
            message = getString(R.string.please_enter_pass);
            binding.etPassword.requestFocus();
        } else if (binding.etPassword.getText().toString().trim().length() < 8) {
            message = getString(R.string.password_length);
            binding.etPassword.requestFocus();
        } else if (etCoPass.isEmpty()) {
            message = getString(R.string.please_enter_co_pass);
            binding.etConfirmPassword.requestFocus();
        } else if (binding.etConfirmPassword.getText().toString().trim().length() < 8) {
            message = getString(R.string.password_length);
            binding.etConfirmPassword.requestFocus();
        } else if (!etPass.equals(etCoPass)) {
            message = getString(R.string.pass_not_match);
        }

        if (!message.trim().isEmpty()) {
            Utils.hideKeyBoardFromView(this);
//            Utils.showSnackBar(rootView, activity, message, Const.alert, Const.successDuration);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        }
        return message.trim().isEmpty();
    }


}