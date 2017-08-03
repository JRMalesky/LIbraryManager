package com.LibraryManager.andres.librarymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CatalogActivity extends AppCompatActivity {
    private static final String TAG = "CatalogActivity";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private ArrayList<String> arrayfromdata = new ArrayList<>();
    private EditText Search;
    private String Test;
    public static String mbookname = "Lolita";
    private Button Sort;
    private int Sorted = 0;
    private ArrayAdapter adapter;
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
        Sort = (Button) findViewById(R.id.buttonSort);


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
        Sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Sorted == 0) {
                    Collections.sort(arrayfromdata, Collections.reverseOrder());
                    SearchableAdapter mSearchAdapter = new SearchableAdapter(CatalogActivity.this, arrayfromdata);
                    adapter = new ArrayAdapter(CatalogActivity.this, android.R.layout.simple_list_item_1, arrayfromdata);
                    mList.setAdapter(mSearchAdapter);
                    Sorted = 1;
                } else {
                    Collections.sort(arrayfromdata, null);
                    SearchableAdapter mSearchAdapter = new SearchableAdapter(CatalogActivity.this, arrayfromdata);
                    adapter = new ArrayAdapter(CatalogActivity.this, android.R.layout.simple_list_item_1, arrayfromdata);
                    mList.setAdapter(mSearchAdapter);
                    Sorted = 0;
                }
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

                SearchableAdapter mSearchAdapter = new SearchableAdapter(CatalogActivity.this, arrayfromdata);
                mSearchAdapter.getFilter().filter(Test);

                mList.setAdapter(mSearchAdapter);
            }
        });
    }


    private void showdata(DataSnapshot dataSnapshot, ArrayAdapter adaptor) {
        arrayfromdata = new ArrayList<>();
        for(DataSnapshot ds: dataSnapshot.child("Books").getChildren())
        {
            String Bookname;
            Bookname = ds.getKey().toString();

            Log.d(TAG, "showData: BookName " + Bookname);
            arrayfromdata.add(Bookname);
        }
            adaptor = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayfromdata);
            SearchableAdapter mSearchAdapter = new SearchableAdapter(CatalogActivity.this, arrayfromdata);

            mList.setAdapter(mSearchAdapter);

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
