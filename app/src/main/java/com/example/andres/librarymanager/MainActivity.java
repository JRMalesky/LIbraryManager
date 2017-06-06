package com.example.andres.librarymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    TextView mConditionTextView;
    TextView mNameTextView;
    TextView mAuthorTextView;
    Button mButtonCheckIn;
    Button mButtonCheckOut;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("Books").child("Lolita").child("Availability");
    DatabaseReference mNameRef = mRootRef.child("Books").child("Lolita").child("BookName");
    DatabaseReference mAuthorRef = mRootRef.child("Books").child("Lolita").child("Author");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI Elements.
        mConditionTextView = (TextView) findViewById(R.id.texviewCondition);
        mNameTextView = (TextView) findViewById(R.id.textViewName);
        mAuthorTextView = (TextView) findViewById(R.id.textViewAuthor);
        mButtonCheckIn = (Button) findViewById(R.id.buttonTest1);
        mButtonCheckOut = (Button) findViewById(R.id.buttonTest2);

    }
    @Override
    protected void  onStart(){
        super.onStart();
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
                mConditionTextView.setText("Available");
            else
                mConditionTextView.setText("Unavailable");


        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
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
    }
}
