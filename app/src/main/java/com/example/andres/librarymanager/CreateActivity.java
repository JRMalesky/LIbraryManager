package com.example.andres.librarymanager;

import android.content.Intent;
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

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mNewEmail;
    private EditText mNewPass;
    private Button mCreateNew, mBackToLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mNewEmail = (EditText) findViewById(R.id.editTextCreateEmail);
        mNewPass = (EditText) findViewById(R.id.editTextCreatePass);
        mCreateNew = (Button) findViewById(R.id.buttonCreateNew);
        mBackToLogIn = (Button) findViewById(R.id.buttonBackToLog);

        mCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = mNewEmail.getText().toString();
                String Pass = mNewPass.getText().toString();
                mAuth.createUserWithEmailAndPassword(Email, Pass)
                        .addOnCompleteListener(CreateActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(CreateActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(CreateActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(CreateActivity.this, LogInActivity.class));
                                }

                                // ...
                            }
                        });
            }
        });

        mBackToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateActivity.this, LogInActivity.class));
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
