package com.uol.bloodmanagementsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected FirebaseUser myFirebaseUser;
    protected FirebaseAuth myAuth;
    private DatabaseReference userRef;
    String uid;
    String profileName, profileEmail, profileImage;

    //private FirebaseUser CurrentUser;
    //private DatabaseReference rootRef;
    private TextView myEmail;
    private ImageView profilePic;
    private TextView loginUserName, loginUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myAuth = FirebaseAuth.getInstance();
        //CurrentUser = mAuth.getCurrentUser();
        //rootRef = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Snackbar.make(view, "Welcome to Chat Box", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent gone = new Intent(HomeActivity.this, ContactDonor_ChatActivity.class);
                startActivity(gone);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);
        profilePic = (ImageView)navHeaderView.findViewById(R.id.imageViewProfile);
        loginUserName = (TextView)navHeaderView.findViewById(R.id.LoginUserName);
        loginUserEmail = (TextView)navHeaderView.findViewById(R.id.LoginUserEmail);


        myFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uid = myFirebaseUser.getUid();

        userRef = FirebaseDatabase.getInstance().getReference();
       // Toast.makeText(this, "" + myFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileName = dataSnapshot.child("Users_Info").child(uid).child("user_name").getValue(String.class);
                profileEmail = dataSnapshot.child("Users_Info").child(uid).child("user_email").getValue(String.class);

//                profileImage = dataSnapshot.child("Users_Info").child(uid).child("image").getValue().toString();
//                Picasso.get().load(profileImage).into(profilePic);

                //Toast.makeText(HomeActivity.this, "Retrieving profile data" , Toast.LENGTH_SHORT).show();

                loginUserName.setText(profileName);
                loginUserEmail.setText(profileEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(HomeActivity.this, "Error retrieving profile data", Toast.LENGTH_SHORT).show();
            }
        });


        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.MyFrameLayout,new DonorListFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_donors);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            HomeActivity.super.onBackPressed();
                            Toast.makeText(HomeActivity.this, "Yes button pressed",
                                    Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton("No",  new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, "No button pressed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Fragment fragment = new ProfileFragment();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.MyFrameLayout,fragment);
            ft.commit();

            return true;
        }
        if (id == R.id.action_sign_out)
        {
            //final ProgressDialog progressDialog = ProgressDialog.show(HomeActivity.this, "Please wait ...", "Logging out ...", true);

            /*
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(HomeActivity.this, "You have been signed out.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
                */
            myAuth.signOut();
            //SendUserToLoginActivity();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            //progressDialog.dismiss();
            Toast.makeText(HomeActivity.this,"You have been signed out.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment=null;
        switch (id) {
            case R.id.nav_profile:
                fragment=new ProfileFragment();
                break;
            case R.id.nav_donors:
                fragment=new DonorListFragment();
                break;
            case  R.id.nav_add_donor:
                fragment=new Add_DonorFragment();
                break;
            case R.id.nav_contact:
                fragment=new ContactDonorFragment();
                break;
            case R.id.nav_video:
                fragment=new CameraFragment();
                break;
        }

        /*
        if (id == R.id.nav_add_donor){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MyFrameLayout, new Add_DonorFragment());
        ft.commit();}

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        if(fragment!=null)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.MyFrameLayout,fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
