package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;
    private EditText mEmailET;
    private EditText mPasswordET;
    private EditText mRepeatPasswordET;
    private EditText mNameET;
    private TextView mToggleTV;
    private Button mLoginBtn;

    private boolean mLoginModeActive;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mDatabase.getReference().child("users");



        mEmailET = findViewById(R.id.emailET);
        mPasswordET = findViewById(R.id.passwordET);
        mRepeatPasswordET = findViewById(R.id.repeatPasswordET);
        mNameET = findViewById(R.id.nameET);
        mToggleTV = findViewById(R.id.toggleLoginSignUpTV);
        mLoginBtn = findViewById(R.id.loginSignUpBtn);

        mLoginBtn.setOnClickListener(v -> {
            loginSignUpUser(mEmailET.getText().toString().trim(), mPasswordET.getText().toString().trim());
        });

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, UserListActivity.class));
        }

    }

    private void loginSignUpUser(String email, String password) {

        if (mLoginModeActive){
            if (mPasswordET.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (mEmailET.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", mNameET.getText().toString().trim());
                                    startActivity(intent);
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }
                            }
                        });
            }

        } else {
            if (!mPasswordET.getText().toString().trim().equals(mRepeatPasswordET.getText().toString().trim())){
                Toast.makeText(this, "Password don't match", Toast.LENGTH_SHORT).show();
            } else if (mPasswordET.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (mEmailET.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            } else{
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    createUser(user);
                                    Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", mNameET.getText().toString().trim());
                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }


        }




    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(mNameET.getText().toString().trim());

        mUsersDatabaseReference.push().setValue(user);
    }

    public void toggleLoginMode(View view) {

        if (mLoginModeActive){
            mLoginModeActive =  false;
            mLoginBtn.setText("Sign Up");
            mToggleTV.setText("Or, log in");
            mRepeatPasswordET.setVisibility(View.VISIBLE);
        }else {
            mLoginModeActive =  true;
            mLoginBtn.setText("Log in");
            mToggleTV.setText("Or, Sign Up");
            mRepeatPasswordET.setVisibility(View.GONE);
        }
    }
}