package com.LibraryManager.andres.librarymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";

    private EditText Email;
    private Button BackToLogin, Send;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Email = (EditText) findViewById(R.id.editTextEmailToResetPass);
        BackToLogin = (Button) findViewById(R.id.buttonResetToLogIn);
        Send = (Button) findViewById(R.id.buttonSendResetEmail);
        mAuth = FirebaseAuth.getInstance();

        BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( ResetPasswordActivity.this, LogInActivity.class));
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = Email.getText().toString().trim();
                if(!input.equals(""))
                {
                    mAuth.sendPasswordResetEmail(input).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Reset Email sent", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPasswordActivity.this, LogInActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(ResetPasswordActivity.this, "Please enter an Email", Toast.LENGTH_LONG).show();
            }
        });

    }
}
