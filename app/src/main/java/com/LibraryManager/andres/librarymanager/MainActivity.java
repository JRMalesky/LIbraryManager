package com.LibraryManager.andres.librarymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mConditionTextView;
    private TextView mNameTextView;
    private TextView mAuthorTextView;
    private Button mButtonCheckIn;
    private Button mButtonCheckOut;
    private Button mSynopsis;
    private ImageView image;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("Books").child(CatalogActivity.mbookname).child("Availability");
    DatabaseReference mNameRef = mRootRef.child("Books").child(CatalogActivity.mbookname).child("BookName");
    DatabaseReference mAuthorRef = mRootRef.child("Books").child(CatalogActivity.mbookname).child("Author");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                }
                // ...
            }
        };
        //UI Elements.
        mConditionTextView = (TextView) findViewById(R.id.texviewCondition);
        mNameTextView = (TextView) findViewById(R.id.textViewName);
        mAuthorTextView = (TextView) findViewById(R.id.textViewAuthor);
        mButtonCheckIn = (Button) findViewById(R.id.buttonTest1);
        mButtonCheckOut = (Button) findViewById(R.id.buttonTest2);
        mSynopsis = (Button) findViewById(R.id.buttonSynopsis);
        image = (ImageView) findViewById(R.id.imageViewAvailability);
        mButtonCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue(true);
            }
        });
        mButtonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue(false);
            }
        });
        mSynopsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Pop.class));
            }
        });
    }
    @Override
    protected void  onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);


                mNameTextView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAuthorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mAuthorTextView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    mConditionRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            boolean Availability = dataSnapshot.getValue(boolean.class);
            if(Availability == true)
            {
                mConditionTextView.setText("Available");
                image.setImageResource(R.drawable.checkinmarker);
            }

            else
            {
                mConditionTextView.setText("Unavailable");
                image.setImageResource(R.drawable.checkoutmarker);
            }


        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
