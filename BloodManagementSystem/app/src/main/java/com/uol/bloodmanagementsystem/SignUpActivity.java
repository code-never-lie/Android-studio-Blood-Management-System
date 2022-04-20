package com.uol.bloodmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private Button signup, cancel;
    private EditText txtUsername, txtEmail, txtPassword, txtPhone;
    private FirebaseAuth firebaseAuth;
    private String usernameInput, useremailInput, userpassInput, userphoneInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup=(Button)findViewById(R.id.btnSignUp);
        cancel=(Button)findViewById(R.id.btnCancel);

        txtUsername = (EditText)findViewById(R.id.username);
        txtEmail = (EditText)findViewById(R.id.email);
        txtPassword = (EditText)findViewById(R.id.password);
        txtPhone = (EditText)findViewById(R.id.phone);


        firebaseAuth = FirebaseAuth.getInstance();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = txtUsername.getText().toString().trim();
                useremailInput = txtEmail.getText().toString().trim();
                userpassInput = txtPassword.getText().toString().trim();
                userphoneInput = txtPhone.getText().toString().trim();

               // final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, "Please wait ...", "Processing ...", true);

               // firebaseAuth.fetchSignInMethodsForEmail(txtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                 //   @Override
                   // public void onComplete(@NonNull Task<SignInMethodQueryResult> task)
                   // {
                   //     boolean check = !task.getResult().getSignInMethods().isEmpty();
                   //     if (!check) {

                if(usernameInput.isEmpty())
                {
                    txtUsername.setError("User name Required");
                    txtUsername.requestFocus();
                    return;
                }
                if(useremailInput.isEmpty())
                {
                    txtEmail.setError("Email Required");
                    txtEmail.requestFocus();
                    return;
                }
                if(userpassInput.isEmpty())
                {
                    txtPassword.setError("Password Required");
                    txtPassword.requestFocus();
                    return;
                }
                if(userpassInput.length()<6)
                {
                    txtPassword.setError("Password should be atleast 6 characters long");
                    txtPassword.requestFocus();
                    return;
                }
                if(userphoneInput.isEmpty())
                {
                    txtPhone.setError("Phone number Required");
                    txtPhone.requestFocus();
                    return;

                }
                if(userphoneInput.length() != 11)
                {
                    txtPhone.setError("Enter valid phone number");
                    txtPhone.requestFocus();
                    return;
                }


                    final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, "Please wait ...", "Processing ...", true);

                    (firebaseAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // progressDialog.dismiss();

                            if (task.isSuccessful()) {

                                SignUp_UserInfo user = new SignUp_UserInfo(usernameInput, useremailInput, userpassInput, userphoneInput);
                                FirebaseDatabase.getInstance().getReference("Users_Info")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();

                                        if (task.isSuccessful()) {
                                            //    progressDialog.dismiss();

                                            Toast.makeText(SignUpActivity.this, "Account Registered Successfully.", Toast.LENGTH_LONG).show();
                                            //Intent go = new Intent(SignUpActivity.this, LoginActivity.class);
                                            //startActivity(go);
                                            finish();

                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Account data not saved.", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }
                                    /*
                                    Toast.makeText(SignUpActivity.this,"Registered Successfully.", Toast.LENGTH_LONG).show();
                                    Intent go = new Intent(SignUpActivity.this,LoginActivity.class);
                                    startActivity(go); */

                            else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                            //Intent go = new Intent(SignUpActivity.this,HomeActivity.class);
                            //startActivity(go);
                   //     }
                     //   else
                      //  {
                        //    Toast.makeText(SignUpActivity.this, "Email Already Exist.", Toast.LENGTH_LONG).show();
                       // }
                 //   }
               // });
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //System.exit(0);
            }
        });

    }

    private void registerUserInfo()
    {

        if(usernameInput.isEmpty())
        {
            txtUsername.setError("User name Required");
            txtUsername.requestFocus();
            return;
        }
        if(useremailInput.isEmpty())
        {
            txtEmail.setError("Email Required");
            txtEmail.requestFocus();
            return;
        }
        if(userpassInput.isEmpty())
        {
            txtPassword.setError("Password Required");
            txtPassword.requestFocus();
            return;
        }
        if(userpassInput.length()<6)
        {
            txtPassword.setError("Password should be atleast 6 characters long");
            txtPassword.requestFocus();
            return;
        }
        if(userphoneInput.isEmpty())
        {
            txtPhone.setError("Phone number Required");
            txtPhone.requestFocus();
            return;

        }
        if(userphoneInput.length() != 10)
        {
            txtPhone.setError("Enter valid phone number");
            txtPhone.requestFocus();
            return;
        }

    }
}
