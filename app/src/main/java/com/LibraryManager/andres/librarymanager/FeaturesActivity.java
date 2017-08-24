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
import android.widget.ImageView;
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

public class FeaturesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String TAG = "FeaturesActivity";
    private ListView Featured1, Featured2, Featured3;
    private TextView NewArr, Popular, Genre, mEmail, mDisplayName;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayList<BookList> ArrayFeatured1, ArrayFeatured2, ArrayFeatured3;
    private ListViewAdapter mNewArr, mPopular, mGenre;
    private DataSnapshot mData;
    private DrawerLayout mDraw;
    public static String userUid;
    private ActionBarDrawerToggle mToggle;
    public static String userDisplayname, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    userUid = user.getUid();
                    userDisplayname = user.getDisplayName();
                    userEmail = user.getEmail().toString();
                    mEmail.setText(userEmail);
                    mDisplayName.setText(userDisplayname);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(FeaturesActivity.this, LogInActivity.class));
                }
                // ...
            }
        };
        mAuth = FirebaseAuth.getInstance();
        mDraw = (DrawerLayout) findViewById(R.id.activity_featured);
        mToggle = new ActionBarDrawerToggle(FeaturesActivity.this, mDraw, R.string.open, R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigation = (NavigationView) findViewById(R.id.mNav);
        navigation.setNavigationItemSelectedListener(this);
        View header = navigation.getHeaderView(0);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mEmail = (TextView) header.findViewById(R.id.email);
        mDisplayName = (TextView) header.findViewById(R.id.username);

        Featured1 = (ListView) findViewById(R.id.listViewFeatured1);
        Featured2 = (ListView) findViewById(R.id.listViewFeatured2);
        Featured3 = (ListView) findViewById(R.id.listViewFeatured3);
        NewArr = (TextView) findViewById(R.id.textViewFeatured1);
        Popular = (TextView) findViewById(R.id.textViewFeatured2);
        Popular.setText("Featured Author");
        Genre = (TextView) findViewById(R.id.textViewFeatured3);

        Featured1.setScrollbarFadingEnabled(false);
        Featured2.setScrollbarFadingEnabled(false);
        Featured3.setScrollbarFadingEnabled(false);
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
        Featured1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CatalogActivity.mbookname = ArrayFeatured1.get(position).getBookName();
                CatalogActivity.activity = 1;
                startActivity(new Intent(FeaturesActivity.this, MainActivity.class));
            }
        });

        Featured2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CatalogActivity.mbookname = ArrayFeatured2.get(position).getBookName();
                CatalogActivity.activity = 1;
                startActivity(new Intent(FeaturesActivity.this, MainActivity.class));
            }
        });

        Featured3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CatalogActivity.mbookname = ArrayFeatured3.get(position).getBookName();
                CatalogActivity.activity = 1;
                startActivity(new Intent(FeaturesActivity.this, MainActivity.class));
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
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.activity_featured);
        if(drawerLayout.isDrawerOpen((GravityCompat.START)))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


    private void showdata(DataSnapshot dataSnapshot) {
        ArrayFeatured1 = new ArrayList<>();
        ArrayFeatured2 = new ArrayList<>();
        ArrayFeatured3 = new ArrayList<>();
        boolean mavailability = true;
        for(DataSnapshot ds: dataSnapshot.child("New Arrivals").getChildren())
        {
            String Author = "";

            String Bookname;
            String Flag = "";
            Bookname = ds.getKey().toString();
            Author = mData.child("Books").child(Bookname).child("Author").getValue().toString();
            Flag = mData.child("Books").child(Bookname).child("DownloadUrl").getValue().toString();
            String Genre = mData.child("Books").child(Bookname).child("Genre").getValue().toString();
            int mAvailability = mData.child("Books").child(Bookname).child("Availability").getValue(int.class);
            if(mAvailability == 0)
            {
                mavailability = false;
            }
            else
                mavailability = true;
            BookList bookList = new BookList(Bookname, Author, Genre, Flag, mavailability);
            if((boolean)ds.getValue())
            {
                ArrayFeatured1.add(bookList);
            }
            Log.d(TAG, "showData: BookName " + Bookname);


        }
        for(DataSnapshot ds: dataSnapshot.child("Books").getChildren())
        {
            String Author = "";

            String Bookname;
            String Flag = "";
            Bookname = ds.getKey().toString();
            Author = ds.child("Author").getValue().toString();
            Flag = ds.child("DownloadUrl").getValue().toString();
            String Genre = ds.child("Genre").getValue().toString();
            int mAvailability = (int) ds.child("Availability").getValue(int.class);
            if(mAvailability == 0)
            {
                mavailability = false;
            }
            else
                mavailability = true;
            BookList bookList = new BookList(Bookname, Author, Genre, Flag, mavailability);
            if(mData.child("Genre").getValue().equals(Genre))
            {
                ArrayFeatured3.add(bookList);
            }
            Log.d(TAG, "showData: BookName " + Bookname);


        }

        for(DataSnapshot ds: dataSnapshot.child("Books").getChildren())
        {
            String Author = "";

            String Bookname;
            String Flag = "";
            Bookname = ds.getKey().toString();
            Author = ds.child("Author").getValue().toString();
            Flag = ds.child("DownloadUrl").getValue().toString();
            String Genre = ds.child("Genre").getValue().toString();
            if( ds.child("Availability").getValue(int.class) == 0)
            {
                mavailability = false;
            }
            else
                mavailability = true;
            BookList bookList = new BookList(Bookname, Author, Genre, Flag, mavailability);
            if(mData.child("Author").getValue().equals(Author))
            {
                ArrayFeatured2.add(bookList);
            }
            Log.d(TAG, "showData: BookName " + Bookname);


        }
        mNewArr = new ListViewAdapter(FeaturesActivity.this, ArrayFeatured1);
        mGenre =  new ListViewAdapter(FeaturesActivity.this, ArrayFeatured3);
        mPopular = new ListViewAdapter(FeaturesActivity.this, ArrayFeatured2);
        Featured1.setAdapter(mNewArr);
        Featured2.setAdapter(mPopular);
        Featured3.setAdapter(mGenre);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Catalog) {

            startActivity(new Intent(FeaturesActivity.this, CatalogActivity.class));

        } else if (id == R.id.MyBooks) {
            startActivity(new Intent(FeaturesActivity.this, MyBooksActivity.class));
        } else if (id == R.id.FeaturedBooks) {
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_featured);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.ResetPass) {
            mAuth.sendPasswordResetEmail(FeaturesActivity.userEmail);
            Toast.makeText(FeaturesActivity.this, "Reset Email sent", Toast.LENGTH_LONG).show();
        } else if (id == R.id.SignOut) {
            mAuth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_featured);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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