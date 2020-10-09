package com.example.solvers.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.solvers.R;
import com.example.solvers.models.Post;
import com.example.solvers.views.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.noties.markwon.Markwon;

public class PostInfoActivity extends AppCompatActivity {

    private Post post;

    Toolbar toolbar;
    TextView txtDescInfo;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);

        final Markwon markwon = Markwon.create(this);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String postId = intent.getStringExtra(HomeFragment.TAG);
        Log.d("post_info",postId);

        toolbar = findViewById(R.id.toolbar_info);
        toolbar.setNavigationOnClickListener(view -> PostInfoActivity.this.finish());

        txtDescInfo = findViewById(R.id.txt_desc_info);

        getPost(postId).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    HashMap<String, Object> postHash = (HashMap<String, Object>) doc.getData();
                    postHash.put("id",doc.getId());
                    post = new Post(postHash);

                    toolbar.setTitle(post.getSubject());
                    markwon.setMarkdown(txtDescInfo, post.getDescription());
                }
            }
        });

        //TODO: Create post info
    }

    public Task<DocumentSnapshot> getPost(String id){
        return db.collection("posts").document(id).get();
    }
}