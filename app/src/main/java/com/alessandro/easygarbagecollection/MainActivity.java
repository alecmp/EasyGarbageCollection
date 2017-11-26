package com.alessandro.easygarbagecollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alessandro.easygarbagecollection.Auth.Login;
import com.alessandro.easygarbagecollection.Auth.Signup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Toolbar toolbar;
    DrawerLayout drawer;
    FirebaseUser mUser;
    private String userId;
    private Uri mPhotoUrl;
    ActionBarDrawerToggle toggle;
    private SharedPreferences pref;
    private static final String SHARED_PREFERENCES_TYPE = "Account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(bundle.getString("fullname"))
                    .build();
            user.updateProfile(profileUpdates);


        }

        String fullName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (fullName == null) fullName = Signup.getFullname(); //caso di registrazione
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        userId = mUser.getUid();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        TextView nav_fullname = (TextView) header.findViewById(R.id.nav_fullname);
        nav_fullname.setText(fullName);
        TextView nav_email = (TextView) header.findViewById(R.id.nav_email);
        nav_email.setText(userEmail);
        final ImageView profile = (ImageView) header.findViewById(R.id.user_profile_photo);


        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {


                mPhotoUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/easy-garbage-collection-c4f8b.appspot.com/o/avatardefault.png?alt=media&token=8c68725a-a21d-4c38-9c99-88b5b825a8b3");
                if (mUser != null && mUser.getPhotoUrl() != null) {
                    mPhotoUrl = mUser.getPhotoUrl();
                }

               pref = getSharedPreferences(SHARED_PREFERENCES_TYPE, MODE_PRIVATE);
                String localStr = pref.getString(userId, null);
                if (localStr != null) {
                    Uri localUrl = Uri.parse(localStr);
                    if (localUrl != mPhotoUrl) {
                        mPhotoUrl = localUrl;


                    }

                }


                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(getApplicationContext())
                        .load(mPhotoUrl)
                        .apply(requestOptions)
                        //.skipMemoryCache(true)
                        .into(profile);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        ViewPager vp_pages = (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);

        TabLayout tbl_pages = (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
