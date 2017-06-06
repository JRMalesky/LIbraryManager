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
    Button mButtonTest1;
    Button mButtonTest2;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("Dune");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI Elements.
        mConditionTextView = (TextView) findViewById(R.id.texviewCondition);

        mButtonTest1 = (Button) findViewById(R.id.buttonTest1);
        mButtonTest2 = (Button) findViewById(R.id.buttonTest2);
    }
    @Override
    protected void  onStart(){
        super.onStart();

    mConditionRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String text = dataSnapshot.getValue(String.class);
            mConditionTextView.setText(text);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
        mButtonTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue("Unavailable");
            }
        });
        mButtonTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue("Available");
            }
        });
    }
}
