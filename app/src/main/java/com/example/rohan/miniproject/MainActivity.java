package com.example.rohan.miniproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rohan.miniproject.FragmentContainers.BaseContainerFragment;
import com.example.rohan.miniproject.FragmentContainers.Tab1ContainerFragment;
import com.example.rohan.miniproject.FragmentContainers.Tab2ContainerFragment;
import com.example.rohan.miniproject.FragmentContainers.Tab3ContainerFragment;
import com.example.rohan.miniproject.NoSwipeViewPager.NonSwipeableViewPager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;


    Tab1ContainerFragment tab1ContainerFragment;
    Tab2ContainerFragment tab2ContainerFragment;
    Tab3ContainerFragment tab3ContainerFragment;



    private NonSwipeableViewPager vpPager;
    private MyPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in

                        setContentView(R.layout.activity_main);
                       /* Button button = (Button) findViewById(R.id.btnsignout);
                        button.setVisibility(View.VISIBLE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AuthUI.getInstance()
                                        .signOut(MainActivity.this);
                            }
                        });
                        */

                        initlayout();
                       // FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("PhotoURL").setValue(user.getPhotoUrl());
                        Toast.makeText(MainActivity.this,"Signed in",Toast.LENGTH_LONG).show();
                        Log.d("Oauth", "onAuthStateChanged:signed_in:" + user.getUid());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("disconnect");
                        databaseReference.onDisconnect().setValue("Disconnected");

                        //getToken(user);


                    } else {
                        // User is signed out
                        Log.d("Oauth", "onAuthStateChanged:signed_out");
                        Toast.makeText(MainActivity.this,"Signed Out",Toast.LENGTH_LONG).show();

                        startActivityForResult(
                                // Get an instance of AuthUI based on the default app
                                AuthUI.getInstance().createSignInIntentBuilder()
                                        .setLogo(R.drawable.heartbeat)
                                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())).build(),
                                RC_SIGN_IN);
                    }
                    // [START_EXCLUDE]

                    // [END_EXCLUDE]
                }
            };



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                setContentView(R.layout.activity_main);
                Button button = (Button)findViewById(R.id.btnsignout);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AuthUI.getInstance()
                                .signOut(MainActivity.this);
                    }
                });
            }
            }
*/
        }


    @Override
    public void onBackPressed()
    {


        boolean isPopFragment = false;
        //String currentTabTag = mTabHost.getCurrentTabTag();
        int position = vpPager.getCurrentItem();
        //if(position != 2) {
        isPopFragment = ((BaseContainerFragment) adapterViewPager.getItem(position)).popFragment();

        if (!isPopFragment) {
            finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void initlayout()
    {

       FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("disconnect").setValue("Connected");
        tab1ContainerFragment = new Tab1ContainerFragment();
        tab2ContainerFragment = new Tab2ContainerFragment();
        tab3ContainerFragment = new Tab3ContainerFragment();

        vpPager = (NonSwipeableViewPager) findViewById(R.id.vpPager);





		/*mTabHost = (FragmentTabHost)findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.vpPager);

        mTabHost.addTab(mTabHost.newTabSpec(TAB_1_TAG).setIndicator("tab1"), Tab1ContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_2_TAG).setIndicator("tab2"), Tab2ContainerFragment.class, null);
*/

        TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);



        adapterViewPager = new MyPagerAdapter(this,getFragmentManager(),tab1ContainerFragment,tab2ContainerFragment,tab3ContainerFragment);
        vpPager.setAdapter(adapterViewPager);
        //vpPager.setCurrentItem(3);
        vpPager.setOffscreenPageLimit(3);
        vpPager.setScrollDurationFactor(2);
        tabLayout.setupWithViewPager(vpPager);
    }


    public static class MyPagerAdapter extends FragmentStatePagerAdapter {
        private static int NUM_ITEMS = 3;

      /*  protected static final int[] ICONS = new int[] {

                R.drawable.photo_camera

        };*/

        Context context;

        //TODO private int[] customtabviews = {R.layout.imagecalendar,R.layout.imagegroup,R.layout.imagehome,R.layout.imagemenu,R.layout.imageuser};

      /*TODO  public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            return LayoutInflater.from(context).inflate(customtabviews[position], null);
        }
        */


        Tab1ContainerFragment tab1ContainerFragment;
        Tab2ContainerFragment tab2ContainerFragment;
        Tab3ContainerFragment tab3;

        /*Tab5ContainerFragment tab5;*/
        //CameraFragment cameraFragment;

        public MyPagerAdapter(Context context, FragmentManager fragmentManager, Tab1ContainerFragment tab1ContainerFragment, Tab2ContainerFragment tab2ContainerFragment, Tab3ContainerFragment tab3ContainerFragment) {
            super(fragmentManager);


            this.tab1ContainerFragment = tab1ContainerFragment;
            this.tab2ContainerFragment = tab2ContainerFragment;
            tab3 = tab3ContainerFragment;

            this.context = context;
            /*
            this.tab3 = tab3;
            this.tab4 = tab4;
            this.tab5 = tab5;*/
            //this.cameraFragment = cameraFragment;


        }

        /* @Override
         public int getIconResId(int index) {
             return ICONS[index % ICONS.length];
         }
 */
        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    //return new Tab1ContainerFragment();
                    return tab1ContainerFragment;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    //return new Tab2ContainerFragment();
                    return tab2ContainerFragment;

                case 2:

                    return tab3;

               /* case 4:
                    return cameraFragment;*/
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position)
            {
                case 0:
                    return "PROFILE";
                case 1:
                    return "Feed";
                case 2:
                    return "Chats";

                default:
                    return null;
            }


        }



    }

}
