package com.LibraryManager.andres.librarymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class CatalogActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "CatalogActivity";
    private FirebaseDatabase mFirebaseDatabase;
    private int Sorting = 0, Sorting2 = 0, Sorting4= 0, Sorting5 = 0, Sorting3 = 0, Sorting6 = 0;
    private DataSnapshot mData;
    private FirebaseAuth mAuth;
    String[] Stuff = {"Book Name A-Z", "Book Name Z-A", "Author A-Z", "Author Z-A", "Genre A-Z", "Genre Z-A"};
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private ArrayList<String> arrayfromdata = new ArrayList<>();
    private EditText Search;
    private String Test;
    public static String mbookname = "Lolita";

    private StorageReference mStorageRef;
    public static int activity = 0;
    private ArrayAdapter adapter;
    private ListView mList;
    private ListViewAdapter mListViewAdapter;
    private ArrayList<BookList> arraylist = new ArrayList<>();
    private DrawerLayout mDraw;
    private Spinner spinner;
    private ActionBarDrawerToggle mToggle;
    private TextView mEmail, mDisplayName;

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
                    mEmail.setText(user.getEmail());
                    mDisplayName.setText(user.getDisplayName());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(CatalogActivity.this, LogInActivity.class));
                }
                // ...
            }
        };
        mStorageRef = FirebaseStorage.getInstance().getReference("TownLikeAlice.jpg");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mList = (ListView) findViewById(R.id.ListView1);
        Search = (EditText) findViewById(R.id.editTextSearch);

        mDraw = (DrawerLayout) findViewById(R.id.activity_catalog);
        mToggle = new ActionBarDrawerToggle(CatalogActivity.this, mDraw, R.string.open, R.string.close);
        mDraw.addDrawerListener(mToggle);
        spinner = (Spinner) findViewById(R.id.spinner);
        mToggle.syncState();
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Stuff);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    if(Sorting == 0) {
                        showdata(mData, adapter);
                        Sorting++;
                        Sorting2 = 0;
                        Sorting3 = 0;
                        Sorting4 = 0;
                        Sorting5 = 0;
                        Sorting6 = 0;
                    }

                }
                if(position == 1)
                {
                    if(Sorting4 == 0) {
                        showdata(mData, adapter);
                        Sorting4++;
                        mListViewAdapter.Inverse();
                        mList.setAdapter(mListViewAdapter);
                        Sorting2 = 0;
                        Sorting3 = 0;
                        Sorting = 0;
                        Sorting5 = 0;
                        Sorting6 = 0;
                    }

                }

                if(position == 3)
                {
                    showdata(mData,adapter);
                    if(Sorting5 == 0) {
                        mListViewAdapter.SortAuthor();
                        mList.setAdapter(mListViewAdapter);
                        Sorting2 = 0;
                        Sorting3 = 0;
                        Sorting4 = 0;
                        Sorting = 0;
                        Sorting6 = 0;
                        Sorting5++;
                        mListViewAdapter.Inverse();
                        mList.setAdapter(mListViewAdapter);
                    }
                }
                if(position == 2)
                {
                    if(Sorting2 == 0) {
                        showdata(mData,adapter);
                        mListViewAdapter.SortAuthor();
                        mList.setAdapter(mListViewAdapter);
                        Sorting = 0;
                        Sorting3 = 0;
                        Sorting4 = 0;
                        Sorting5 = 0;
                        Sorting6 = 0;
                        Sorting2++;
                    }
                }
                if(position == 4)
                {
                    if(Sorting3 == 0) {
                        showdata(mData,adapter);
                        mListViewAdapter.SortGenre();
                        mList.setAdapter(mListViewAdapter);
                        Sorting2 = 0;
                        Sorting = 0;
                        Sorting4 = 0;
                        Sorting5 = 0;
                        Sorting6 = 0;
                        Sorting3++;
                    }
                }
                if(position == 5)
                {
                    if(Sorting6 == 0) {
                        showdata(mData,adapter);
                        mListViewAdapter.SortGenre();
                        mList.setAdapter(mListViewAdapter);
                        Sorting2 = 0;
                        Sorting3 = 0;
                        Sorting4 = 0;
                        Sorting5 = 0;
                        Sorting = 0;
                        Sorting6++;
                        mListViewAdapter.Inverse();
                        mList.setAdapter(mListViewAdapter);
                    }
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigation = (NavigationView) findViewById(R.id.mNav);
        navigation.setNavigationItemSelectedListener(this);
        View header = navigation.getHeaderView(0);
        mEmail = (TextView) header.findViewById(R.id.email);
        mDisplayName = (TextView) header.findViewById(R.id.username);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mData = dataSnapshot;
                showdata(dataSnapshot, adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mbookname = arraylist.get(position).getBookName();
                activity = 0;
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
                String text = Search.getText().toString().toLowerCase(Locale.getDefault());

                SearchableAdapter mSearchAdapter = new SearchableAdapter(CatalogActivity.this, arrayfromdata);
                mSearchAdapter.getFilter().filter(Test);

                mListViewAdapter.filter(text);
                //mList.setAdapter(mListViewAdapter);
                if(TextUtils.isEmpty(Search.getText()))
                {
                    spinner.setEnabled(true);
                }
                else
                    spinner.setEnabled(false);

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Catalog:
                return true;
            case R.id.MyBooks:
                return true;
            case R.id.FeaturedBooks:
                return true;
            case R.id.ResetPass:
                return true;
            case R.id.SignOut:
                return true;
        }
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.activity_catalog);
        if(drawerLayout.isDrawerOpen((GravityCompat.START)))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void showdata(DataSnapshot dataSnapshot, ArrayAdapter adaptor) {
        arrayfromdata = new ArrayList<>();
        arraylist = new ArrayList<>();
        boolean mavailability = true;
        for(DataSnapshot ds: dataSnapshot.child("Books").getChildren())
        {
            String Author = "";

            String Bookname;
            String Flag = "";
            Bookname = ds.getKey().toString();
            if(!ds.child("Author").getValue().equals(null)) {
                Author = ds.child("Author").getValue().toString();
            }
            String Genre = ds.child("Genre").getValue().toString();
            if(!ds.child("DownloadUrl").getValue().equals(null)) {
                Flag = ds.child("DownloadUrl").getValue().toString();
            }
            else
                Flag = "Hola";
            if((int) ds.child("Availability").getValue(int.class) == 0)
            {
                mavailability = false;
            }
            else
                mavailability = true;
            BookList bookList = new BookList(Bookname, Author, Genre, Flag, mavailability);
            Log.d(TAG, "showData: BookName " + Bookname);

            arrayfromdata.add(Bookname);
            arraylist.add(bookList);
        }

            SearchableAdapter mSearchAdapter = new SearchableAdapter(CatalogActivity.this, arrayfromdata);
            mListViewAdapter = new ListViewAdapter(CatalogActivity.this, arraylist);
            mList.setAdapter(mListViewAdapter);


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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Catalog) {
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_catalog);
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (id == R.id.MyBooks) {
            startActivity(new Intent(CatalogActivity.this, MyBooksActivity.class));
        } else if (id == R.id.FeaturedBooks) {
            startActivity(new Intent(CatalogActivity.this, FeaturesActivity.class));
        } else if (id == R.id.ResetPass) {
           mAuth.sendPasswordResetEmail(FeaturesActivity.userEmail);
            Toast.makeText(CatalogActivity.this, "Reset Email sent", Toast.LENGTH_LONG).show();
        } else if (id == R.id.SignOut) {
            mAuth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_catalog);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
