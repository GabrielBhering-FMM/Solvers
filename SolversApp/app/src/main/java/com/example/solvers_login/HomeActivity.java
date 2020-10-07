package com.example.solvers_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.solvers_login.models.Post;
import com.example.solvers_login.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    public static String TAG = "com.example.solvers_login.HomeActivity.user";

    private User user;
    private List<Post> postList = new ArrayList<>();

    Button logout;
    FloatingActionButton btCreatPost;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getUser();

        db.collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("error", "listen:error", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value ){
                            if(doc.get("description") != null){

                                HashMap<String, Object> postHash = (HashMap<String, Object>) doc.getData();
                                postHash.put("id",doc.getId());

                                Log.d("post", postHash.toString());

                                Post post = new Post(postHash);

                                postList.add(post);

                                Log.d("post", postList.toString());
                            }
                        }
                    }
                });

        btCreatPost = findViewById(R.id.floatingActionButton);

        btCreatPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                intent.putExtra(TAG, user);
                startActivity(intent);
            }
        });

//        logout = findViewById(R.id.btn_logout);
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            FirebaseAuth.getInstance().signOut();
//
//            Intent HomeToMain = new Intent(getApplicationContext(), MainActivity.class);
//            HomeToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(HomeToMain);
//            finish();
//            }
//        });
    }

    private void getUser(){
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        db.collection("users")
                .whereEqualTo("uid",firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                HashMap<String,Object> userHash = (HashMap<String, Object>) document.getData();
                                user = new User(userHash);
                            }
                        } else {
                            Log.d("mama", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
