package com.uol.bloodmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText txtuserEmail, txtuserPassword;
    private TextView signUp;
    private Button login, exitt;
    private FirebaseAuth firebaseAuth;

    private FirebaseUser CurrentUser;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtuserEmail = (EditText)findViewById(R.id.usernameLogin);
        txtuserPassword = (EditText)findViewById(R.id.passwordLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        CurrentUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        signUp=(TextView)findViewById(R.id.signUp);
        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        login=(Button)findViewById(R.id.btnLogin);
        exitt=(Button)findViewById(R.id.btnExit);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtuserEmail.getText().toString().isEmpty() || txtuserPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email and Password is Required.", Toast.LENGTH_LONG).show();
                    txtuserEmail.requestFocus();
                    return;
                } else {
                    final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait ...", "Logging in ...", true);

                    (firebaseAuth.signInWithEmailAndPassword(txtuserEmail.getText().toString(), txtuserPassword.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Successfully.", Toast.LENGTH_LONG).show();
                                Intent go = new Intent(LoginActivity.this, HomeActivity.class);
                                go.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                startActivity(go);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    //Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    //startActivity(intent);
                }
            }
        });

        exitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(CurrentUser != null)
        {
            //Toast.makeText(LoginActivity.this,"Welcome back",Toast.LENGTH_LONG).show();

            VerifyUserExistance();
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Current User is empty",Toast.LENGTH_SHORT).show();

        }
    }

    String currentUserID;
    private void VerifyUserExistance()
    {
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        rootRef.child("Users_Info").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("user_email").exists())) {
                    Toast.makeText(LoginActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();

                    Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(in);
                    finish();
                    }
                    else {
                    Toast.makeText(LoginActivity.this, " Not Welcome", Toast.LENGTH_SHORT).show();

                    //Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                    //startActivity(in);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
