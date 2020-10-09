package com.example.solvers.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.solvers.R;
import com.example.solvers.views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class AccountFragment extends Fragment {

    View view;

    AvatarView profileImg;
    TextView profileName;
    Button logout;

    FirebaseAuth mFirebaseAuth;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        //Get Firebase instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Get widgets
        profileImg = view.findViewById(R.id.account_photo);
        profileName = view.findViewById(R.id.account_name);
        logout = view.findViewById(R.id.logout);

        setNameAndPhoto();

        setActions();

        return view;
    }

    public void setActions(){
        logout.setOnClickListener(v -> {
            mFirebaseAuth.signOut();

            Intent HomeToMain = new Intent(view.getContext(), MainActivity.class);
            HomeToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(HomeToMain);
        });
    }

    public void setNameAndPhoto(){
        IImageLoader imgLoader = new PicassoLoader();

        if(mFirebaseAuth.getCurrentUser().getDisplayName()!=null){
            profileName.setText(mFirebaseAuth.getCurrentUser().getDisplayName().isEmpty()?"User":mFirebaseAuth.getCurrentUser().getDisplayName());
            imgLoader.loadImage(profileImg, String.valueOf(mFirebaseAuth.getCurrentUser().getPhotoUrl()),mFirebaseAuth.getCurrentUser().getDisplayName().isEmpty()?"User":mFirebaseAuth.getCurrentUser().getDisplayName());
        }else{
            profileName.setText("User");
            imgLoader.loadImage(profileImg, String.valueOf(mFirebaseAuth.getCurrentUser().getPhotoUrl()),"User");
        }
    }
}