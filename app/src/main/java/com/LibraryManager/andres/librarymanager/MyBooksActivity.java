package com.LibraryManager.andres.librarymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class MyBooksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MyBooksActivity";
    private ListView Owned, Before;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ListViewAdapter mListViewAdapter, mListViewAdapterBefore;
    private ArrayList<BookList> ArrayBefore = new ArrayList<>();
    private ArrayList<BookList> ArrayAfter = new ArrayList<>();
    private TextView text1, text2;
    private FirebaseAuth mAuth;
    private HorizontalScrollView Stuffy;
    private TextView mEmail, mDisplayName;
    Calendar calendar;
    private Button mHey;
    private DataSnapshot mData;
    private DrawerLayout mDraw;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth.AuthStateListener mAuthListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

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
                    startActivity(new Intent(MyBooksActivity.this, LogInActivity.class));
                }
                // ...
            }
        };
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mHey = (Button) findViewById(R.id.buttonheyDude);
        mAuth = FirebaseAuth.getInstance();
        text1 = (TextView) findViewById(R.id.textViewOwned);
        text2 = (TextView) findViewById(R.id.textViewBefore);
        text1.setText("Books you have Checked Out");
        text2.setText("Previous Books");
        Before = (ListView) findViewById(R.id.listViewBefore);
        Owned = (ListView) findViewById(R.id.listViewOwned);
        mDraw = (DrawerLayout) findViewById(R.id.activity_my_books);
        mToggle = new ActionBarDrawerToggle(MyBooksActivity.this, mDraw, R.string.open, R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
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
                showdata(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mHey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyBooksActivity.this, MenuActivity.class));
            }
        });
        Owned.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CatalogActivity.mbookname = ArrayBefore.get(position).getBookName();
                CatalogActivity.activity = 1;
                startActivity(new Intent(MyBooksActivity.this, MainActivity.class));
            }
        });

        Before.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CatalogActivity.mbookname = ArrayAfter.get(position).getBookName();
                CatalogActivity.activity = 1;
                startActivity(new Intent(MyBooksActivity.this, MainActivity.class));
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
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.activity_my_books);
        if(drawerLayout.isDrawerOpen((GravityCompat.START)))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void showdata(DataSnapshot dataSnapshot) {
        ArrayBefore = new ArrayList<>();
        ArrayAfter = new ArrayList<>();
        for(DataSnapshot ds: dataSnapshot.child("users").child(FeaturesActivity.userUid).child("BooksChecked").getChildren())
        {
            String Author = "";
            boolean mavailability = true;
            String Bookname;
            String Flag = "";
            Bookname = ds.getKey().toString();
            Author = mData.child("Books").child(Bookname).child("Author").getValue().toString();
            Flag = mData.child("Books").child(Bookname).child("DownloadUrl").getValue().toString();
            String Genre = mData.child("Books").child(Bookname).child("Genre").getValue().toString();

            BookList bookList = new BookList(Bookname, Author, Genre, Flag, mavailability);
            if((boolean)ds.getValue())
            {
                ArrayBefore.add(bookList);
            }
            else
                ArrayAfter.add(bookList);
            Log.d(TAG, "showData: BookName " + Bookname);


        }
        mListViewAdapterBefore = new ListViewAdapter(MyBooksActivity.this, ArrayAfter);
        mListViewAdapter = new ListViewAdapter(MyBooksActivity.this, ArrayBefore);
        Before.setAdapter(mListViewAdapterBefore);
        Owned.setAdapter(mListViewAdapter);

        Owned.setScrollbarFadingEnabled(false);
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
            startActivity(new Intent(MyBooksActivity.this, CatalogActivity.class));

        } else if (id == R.id.MyBooks) {
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_my_books);
            drawerLayout.closeDrawer(GravityCompat.START);;
        } else if (id == R.id.FeaturedBooks) {
            startActivity(new Intent(MyBooksActivity.this, FeaturesActivity.class));
        } else if (id == R.id.ResetPass) {
            mAuth.sendPasswordResetEmail(FeaturesActivity.userEmail);
            Toast.makeText(MyBooksActivity.this, "Reset Email sent", Toast.LENGTH_LONG).show();
        } else if (id == R.id.SignOut) {
            startActivity(new Intent(MyBooksActivity.this, MyBooksActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_my_books);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
