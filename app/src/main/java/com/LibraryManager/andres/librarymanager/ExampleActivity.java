package com.LibraryManager.andres.librarymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExampleActivity extends AppCompatActivity {


    private Button test1, test2, test3;
    public static String mbookname2 = "Lolita";
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mNameRef = mRootRef.child("Books").child("Lolita").child("BookName");
    DatabaseReference mNameRef1 = mRootRef.child("Books").child("Dune").child("BookName");
    DatabaseReference mNameRef2 = mRootRef.child("Books").child("Brave New World").child("BookName");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        test1 = (Button) findViewById(R.id. buttonTESTBOOK);
        test2 = (Button) findViewById(R.id. buttonBooktest2);
        test3 = (Button) findViewById(R.id. buttonBookTest3);


        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbookname2 = test1.getText().toString();
                startActivity(new Intent(ExampleActivity.this, MainActivity.class));
            }
        });
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbookname2 = test2.getText().toString();
                startActivity(new Intent(ExampleActivity.this, MainActivity.class));
            }
        });
        test3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbookname2 = test3.getText().toString();
                startActivity(new Intent(ExampleActivity.this, MainActivity.class));
            }
        });

    }
    @Override
    protected void  onStart() {
        super.onStart();
        mNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bookname = dataSnapshot.getValue(String.class);

                test1.setText(bookname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mNameRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bookname = dataSnapshot.getValue(String.class);

                test2.setText(bookname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mNameRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bookname = dataSnapshot.getValue(String.class);

                test3.setText(bookname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
