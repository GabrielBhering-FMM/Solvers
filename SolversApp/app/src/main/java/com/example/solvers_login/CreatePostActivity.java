package com.example.solvers_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.solvers_login.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class CreatePostActivity extends AppCompatActivity {

    private User user;

    Toolbar mActionBar;
    EditText txtDescription;
    EditText txtSubject;
    Button btSubmit;

    ProgressBar loading;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(HomeActivity.TAG);

        loading = findViewById(R.id.loading_post_submit);

        txtDescription = findViewById(R.id.txtDescription);
        txtDescription.requestFocus();
        if(txtDescription.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        txtSubject = findViewById(R.id.txtSubject);

        mActionBar = findViewById(R.id.create_post_toolbar);
        mActionBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePostActivity.this.finish();
            }
        });

        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                DocumentReference userRef = db.collection("users").document(user.getUid());

                HashMap<String, Object> post = new HashMap<>();
                post.put("subject", txtSubject.getText().toString());
                post.put("description", txtDescription.getText().toString());
                post.put("isAnswered", false);
                post.put("author", userRef);
                post.put("createdAt", new Date());

                db.collection("posts").add(post)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            CreatePostActivity.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error", "Error adding document", e);
                        }
                    });
            }
        });
    }
}