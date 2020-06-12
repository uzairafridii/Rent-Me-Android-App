package com.uzair.rentme.LoginAndSignUp;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uzair.rentme.R;

import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private String[] roles = {"Owner", "Customer"};
    private String roleOfUser = null;
    private ArrayAdapter arrayAdapter;
    private Spinner spinner;
    private TextInputLayout tvName, tvEmail, tvPhone, tvPassword, tvConfirmPassword;
    private Toolbar mToolbar;
    private ConstraintLayout constraintLayout;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        initViews();

        findViewById(R.id.signInText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this , Login.class));
                SignUp.this.finish();
            }
        });

        // click on sign up button to registe user
        findViewById(R.id.signUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUserAccount();
            }
        });

    }

    // initViews
    private void initViews() {
        mToolbar = findViewById(R.id.signUpToolbar);
        setSupportActionBar(mToolbar);

        // spinner with adapter
        spinner = findViewById(R.id.roleSpinner);
        spinner.setOnItemSelectedListener(this);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, roles);
        spinner.setAdapter(arrayAdapter);

        mProgressDialog = new ProgressDialog(this , R.style.MyAlertDialogStyle);
        constraintLayout = findViewById(R.id.constraintLayout);
        // edit text
        tvName = findViewById(R.id.userNameTextInputLayout);
        tvEmail = findViewById(R.id.emailTextInputLayout);
        tvPassword = findViewById(R.id.passwordTextInputLayout);
        tvConfirmPassword = findViewById(R.id.confirmPassword);
        tvPhone = findViewById(R.id.phoneTextInputLayout);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }


    // create account
    private void createUserAccount() {
        final String name, email, phoneNumber, userPassword, userConfirmPass;
        name = tvName.getEditText().getText().toString();
        email = tvEmail.getEditText().getText().toString();
        phoneNumber = tvPhone.getEditText().getText().toString();
        userPassword = tvPassword.getEditText().getText().toString();
        userConfirmPass = tvConfirmPassword.getEditText().getText().toString();

        try {

            if (!name.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty()
                    && !userPassword.isEmpty() && !userConfirmPass.isEmpty()
                    && userPassword.equals(userConfirmPass) && !roleOfUser.isEmpty())
            {
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful())
                                {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("name", name);
                                    data.put("email", email);
                                    data.put("phone", phoneNumber);
                                    data.put("password", userPassword);
                                    data.put("role", roleOfUser);

                                    Map<String , String> roles = new HashMap<>();
                                    roles.put("role", roleOfUser);

                                    mDatabaseRef.child("Roles").child(mAuth.getCurrentUser().getUid()).setValue(roles);
                                    mDatabaseRef.child("Users")
                                            .child(mAuth.getCurrentUser().getUid())
                                            .setValue(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        mProgressDialog.dismiss();
                                                        Snackbar snackbar = Snackbar.make(constraintLayout ,
                                                                "SuccessFully Registered", Snackbar.LENGTH_LONG);
                                                        snackbar.setActionTextColor(Color.GREEN);
                                                        snackbar.show();

                                                    }
                                                    else
                                                    {
                                                        mProgressDialog.dismiss();
                                                        Snackbar snackbar = Snackbar.make(constraintLayout ,
                                                                "Something went wrong", Snackbar.LENGTH_LONG);
                                                        snackbar.setActionTextColor(Color.RED);
                                                        snackbar.show();
                                                    }

                                                }
                                            });
                                }
                                else
                                {
                                    mProgressDialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(constraintLayout ,
                                            "Something went wrong", Snackbar.LENGTH_LONG);
                                    snackbar.setActionTextColor(Color.RED);
                                    snackbar.show();
                                }

                            }
                        });

            }
            else
            {
                tvPhone.setError("Required");
                tvName.setError("Required");
                tvEmail.setError("Required");
                tvPassword.setError("Required");
                tvConfirmPassword.setError("Required");
            }


        } catch (Exception e) {

            Snackbar snackbar = Snackbar.make(constraintLayout ,
                    "Something went wrong", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            Log.d("catch", ""+e.getMessage());

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        roleOfUser = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        Toast.makeText(this, "Please Select Your Role", Toast.LENGTH_SHORT).show();

    }
}
