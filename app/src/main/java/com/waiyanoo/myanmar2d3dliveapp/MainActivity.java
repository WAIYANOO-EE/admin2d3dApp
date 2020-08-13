package com.waiyanoo.myanmar2d3dliveapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.waiyanoo.myanmar2d3dliveapp.ThreeDHistoryFragment;
import com.waiyanoo.myanmar2d3dliveapp.TwoDHistoryFragment;
import com.facebook.FacebookSdk;
import com.google.android.material.navigation.NavigationView;
import com.waiyanoo.myanmar2d3dliveapp.R;

import java.sql.Array;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity extends AppCompatActivity {
    public  String currentFrag="Home";
    public static String preFrag;
    public static final String TAG = "FacebookLogin";
    DrawerLayout drawerLayout;
    public static Toolbar toolbar;
    NavigationView navMenu;



    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth mAuth;
    GoogleSignInClient signInClient;
    CallbackManager callbackManager;
    TextView userName;
    LoginButton loginButton;
    Button btnLogOut;
    CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navMenu = findViewById(R.id.navMenu);


        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.WHITE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        toggle.syncState();

        FacebookSdk.sdkInitialize(getApplicationContext());
        navMenu.getMenu().getItem(0).setChecked(true);
        setTitle("Home");
        setFragment(new HomeFragment());
        currentFrag="Home";
        preFrag = "Home";
        View myView = navMenu.getHeaderView(0);
         profile = myView.findViewById(R.id.user_profile);
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        loginButton = myView.findViewById(R.id.facebook_Login);

            userName = myView.findViewById(R.id.user_name);

        btnLogOut = myView.findViewById(R.id.btn_logout);



        if (user != null)
        {
           updateUI(user);
        }else {
            updateUI(null);
        }



        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null)
                {
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    updateUI(null);

                }

            }
        });

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:"+loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError" + error);

            }

        });

        View pageView = getLayoutInflater().inflate(R.layout.facebook_page,null);
        navMenu.getMenu().getItem(3).setActionView(pageView);
        RelativeLayout fbgo = pageView.findViewById(R.id.fbgo);
        fbgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3fPDSmW"));
                startActivity(browserIntent);
            }
        });

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        setTitle("Home");
                        setFragment(new HomeFragment());
                        break;
                    case R.id.dd_history_menu:
                        setTitle("2D History");
                        setFragment(new TwoDHistoryFragment());
                        break;
                    case R.id.ddd_history_menu:
                        setTitle("3D History");
                        setFragment(new ThreeDHistoryFragment());
                        break;
                    case R.id.facebook_page:
                        setTitle("Facebook Page");
                }


                drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });


    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }


    private void updateUI(FirebaseUser user) {

        if (user != null)
        {
            btnLogOut.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);
            profile.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            userName.setText(user.getDisplayName());
            Glide.with(getApplicationContext())
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .into(profile);
        } else {

            loginButton.setVisibility(View.VISIBLE);
            userName.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.GONE);
        }

    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else {
            if ( preFrag.equals("Home") && currentFrag.equals("Home"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit !");
                builder.setMessage("Are you sure to exit ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
            }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {
            updateUI(user);
        }

    }

    public void setFragment(Fragment f)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_fragment,f);
        ft.commit();
    }

}
