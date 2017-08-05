package com.example.rohan.miniproject.Profile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.rohan.miniproject.FragmentContainers.Tab1ContainerFragment;
import com.example.rohan.miniproject.FragmentContainers.Tab2ContainerFragment;
import com.example.rohan.miniproject.FragmentContainers.Tab3ContainerFragment;
import com.example.rohan.miniproject.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Rohan on 30/03/17.
 */
public class UserProfile extends Fragment {



    ImageView imgdp;
    TextView txtname;
    String facebookUserId = "";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Uri uri;
    ImageView imgsettings;
    ImageView imgsignout;

    ViewPager viewPager;
    TabLayout tabLayout;

    public String name;

    FrameLayout frameLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("test", "tab 2 oncreateview");
        View view = inflater.inflate(R.layout.profile_mainpage, null);


        frameLayout = (FrameLayout)view.findViewById(R.id.flcontainer);
        imgsettings = (ImageView)view.findViewById(R.id.imgsettings);
        imgsignout = (ImageView)view.findViewById(R.id.imgsignout);
        imgdp = (ImageView)view.findViewById(R.id.imgdp);
        txtname = (TextView)view.findViewById(R.id.txtname);
        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        txtname.setText(name);
        imgsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_edit_name");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }
                SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();
                settingsDialogFragment.show(manager,"fragment_edit_name");
            }
        });
        imgsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("disconnect");
                databaseReference.setValue("Disconnected");
                AuthUI.getInstance().signOut(getActivity());

            }
        });

        /*tabLayout = (TabLayout)view.findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager)view.findViewById(R.id.vpPager);
        */

        RealtimeUpdates realtimeUpdates = new RealtimeUpdates();
        Bundle bundle = new Bundle();
        bundle.putString("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        realtimeUpdates.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);

        transaction.replace(R.id.flcontainer,realtimeUpdates);

        transaction.commit();
        getChildFragmentManager().executePendingTransactions();

       /* viewPager.setAdapter(new MyPagerAdapter(getActivity(),getFragmentManager(),realtimeUpdates,new Tab2ContainerFragment()));
        tabLayout.setupWithViewPager(viewPager);
        */

        // find the Facebook profile and get the user's id
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(profile.getProviderId().equals(("facebook.com"))) {
                facebookUserId = profile.getUid();
                // construct the URL to the profile picture, with a custom height
// alternatively, use '?type=small|medium|large' instead of ?height=
                String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

                Glide.with(getActivity())
                        .load(photoUrl)
                        .asBitmap()
                        .into(new BitmapImageViewTarget(imgdp) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imgdp.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }
            else
            {
                Glide.with(getActivity())
                        .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(imgdp) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imgdp.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }
        }
        uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PhotoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    DatabaseReference databaseReference = dataSnapshot.getRef();
                    databaseReference.setValue(uri.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    DatabaseReference databaseReference = dataSnapshot.getRef();
                    databaseReference.setValue(user.getDisplayName().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;

    }


    public static class MyPagerAdapter extends FragmentStatePagerAdapter {
        private static int NUM_ITEMS = 2;

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


        RealtimeUpdates realtimeUpdates;
        Tab2ContainerFragment tab2ContainerFragment;
        Tab3ContainerFragment tab3;

        /*Tab5ContainerFragment tab5;*/
        //CameraFragment cameraFragment;

        public MyPagerAdapter(Context context, FragmentManager fragmentManager, RealtimeUpdates realtimeUpdates, Tab2ContainerFragment tab2ContainerFragment) {
            super(fragmentManager);


            this.realtimeUpdates = realtimeUpdates;
            this.tab2ContainerFragment = tab2ContainerFragment;


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
                    return realtimeUpdates;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    //return new Tab2ContainerFragment();
                    return tab2ContainerFragment;


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
                    return "Live";
                case 1:
                    return "History";

                default:
                    return null;
            }


        }



    }
}
