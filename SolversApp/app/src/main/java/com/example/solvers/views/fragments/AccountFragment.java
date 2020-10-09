package com.example.solvers.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.solvers.R;
import com.example.solvers.views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    View view;

    Button logout;

    FirebaseAuth mFirebaseAuth;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();


        //        TODO: Create logout button
        logout = view.findViewById(R.id.logout);

        logout.setOnClickListener(v -> {
            mFirebaseAuth.signOut();

            Intent HomeToMain = new Intent(view.getContext(), MainActivity.class);
            HomeToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(HomeToMain);
        });

        return view;
    }
}