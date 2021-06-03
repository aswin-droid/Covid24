package com.example.covid24;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etMob, etPass, etConPass;
    Button btReg;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmailId);
        etMob = findViewById(R.id.etMobile);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etCpass);
        btReg = findViewById(R.id.btRegister);

        firestore = FirebaseFirestore.getInstance();

        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().isEmpty()) {
                    if (!etEmail.getText().toString().isEmpty() && isValidEmail(etEmail.getText().toString())) {
                        if (etMob.getText().toString().length() == 10) {
                            if (etPass.getText().toString().length() >= 6) {
                                if (etPass.getText().toString().equals(etConPass.getText().toString())) {

                                    registerUser();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Password Confirmation Failed...", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter password with minimum 6 characters", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter correct Mobile No", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Check your Email Address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter UserName", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void registerUser() {

        Map<Object, String> userData = new HashMap<>();
        userData.put("username", etName.getText().toString());
        userData.put("email", etEmail.getText().toString());
        userData.put("mobile", etMob.getText().toString());
        userData.put("password", etPass.getText().toString());

        firestore.collection("Users").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}