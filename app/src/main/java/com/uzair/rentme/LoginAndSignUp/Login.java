package com.uzair.rentme.LoginAndSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uzair.rentme.Customer.CustomerHome;
import com.uzair.rentme.Owner.OwnerHome;
import com.uzair.rentme.R;

public class Login extends AppCompatActivity {

    private String roleOfUser;
    private ConstraintLayout layout;
    private Toolbar mToolbar;
    private TextInputLayout tvUserEmail, tvUserPassword;
    private FirebaseAuth mUserAuth;
    private DatabaseReference mDatabaseRef;
    private ProgressDialog mProgressDialog;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        initViews();


        //sign up click
        findViewById(R.id.signUpText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        // login button
        findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userLoginWithEmail();
            }
        });

    }


    private void initViews() {
        mToolbar = findViewById(R.id.customerToolBar);
        setSupportActionBar(mToolbar);

        mProgressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        layout = findViewById(R.id.loginLayout);

        // Edit text
        tvUserEmail = findViewById(R.id.emailTextInputLayoutLogin);
        tvUserPassword = findViewById(R.id.passwordTextInputLayoutLogin);

        // firebase

        mUserAuth = FirebaseAuth.getInstance();
       // currentUser = mUserAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Roles");

    }


    private void userLoginWithEmail() {
        String user_email = tvUserEmail.getEditText().getText().toString();
        String user_passw = tvUserPassword.getEditText().getText().toString();


        try {

            if (!user_email.isEmpty() && !user_passw.isEmpty()) {

                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                getRoleOfUser(user_email , user_passw);


            } else {
                tvUserEmail.setError("Required");
                tvUserPassword.setError("Required");
            }


        } catch (Exception e) {
            snackBar("Something went wrong");
            Log.d("catch", "" + e.getMessage());
            mProgressDialog.dismiss();
            clearFields();
        }


    }


    private void getRoleOfUser(final String user_email , final String user_passw) {
// get the role of user in start
        mUserAuth.signInWithEmailAndPassword(user_email, user_passw).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            FirebaseUser user = mUserAuth.getCurrentUser();

                            mDatabaseRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    roleOfUser = dataSnapshot.child("role").getValue().toString();
                                  //  Toast.makeText(Login.this, "Roles is " + roleOfUser, Toast.LENGTH_SHORT).show();


                                    switch (roleOfUser) {
                                        case "Customer": {
                                            snackBar("Login SuccessFully");
                                            mProgressDialog.dismiss();
                                            startActivity(new Intent(Login.this, CustomerHome.class));
                                              Login.this.finish();
                                            clearFields();
                                            break;

                                        }
                                        case "Owner": {
                                            snackBar("Login SuccessFully");
                                            mProgressDialog.dismiss();
                                            startActivity(new Intent(Login.this, OwnerHome.class));
                                             Login.this.finish();
                                            clearFields();
                                            break;
                                        }
                                        default: {
                                            snackBar("Something Wrong");
                                            clearFields();
                                        }
                                    }





                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });





                        } else {
                            snackBar("Something went wrong");
                            mProgressDialog.dismiss();
                            clearFields();
                        }


                    }
                });





    }


    private void snackBar(String message) {
        Snackbar snackbar = Snackbar.make(layout,
                message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }


    private void clearFields() {
        tvUserEmail.getEditText().setText("");
        tvUserPassword.getEditText().setText("");
    }


    @Override
    protected void onStart() {
        super.onStart();
     //   FirebaseUser currentUser = mUserAuth.getCurrentUser();
    }
}

