package com.example.andres.librarymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class CatalogActivity extends AppCompatActivity {
    private static final String TAG = "CatalogActivity";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private ArrayList<String> array = new ArrayList<>();
    private EditText Search;
    private String Test;
    public static String mbookname = "Lolita";

    ArrayAdapter adapter;
    private ListView mList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_catalog);
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
                    startActivity(new Intent(CatalogActivity.this, LogInActivity.class));
                }
                // ...
            }
        };

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mList = (ListView) findViewById(R.id.ListView1);
        Search = (EditText) findViewById(R.id.editTextSearch);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showdata(dataSnapshot, adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mbookname = mList.getItemAtPosition(position).toString();
                startActivity(new Intent(CatalogActivity.this, MainActivity.class));
            }
        });

        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                Test = Search.getText().toString();
                adapter = new ArrayAdapter(CatalogActivity.this, android.R.layout.simple_list_item_1, array);
                adapter.getFilter().filter(Test);
                mList.setAdapter(adapter);
            }
        });
    }


    private void showdata(DataSnapshot dataSnapshot, ArrayAdapter adaptor) {
        array = new ArrayList<>();
        for(DataSnapshot ds: dataSnapshot.child("Books").getChildren())
        {
            String Bookname;
            Bookname = ds.getKey().toString();

            Log.d(TAG, "showData: BookName " + Bookname);
            array.add(Bookname);
        }
            adaptor = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);


            mList.setAdapter(adaptor);

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
