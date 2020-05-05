package com.example.solvers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.solvers.models.Post;

import java.util.List;

public class CreatePost extends AppCompatActivity {

    private EditText etTitle,etDescription;
    private Button btPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        etTitle = findViewById(R.id.postTitle);
        etDescription = findViewById(R.id.postDesc);
        btPost = findViewById(R.id.btPost);

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String desc = etDescription.getText().toString();

                Post post = new Post(title, desc);

                Home.posts.add(post);
                launchActivity();
            }
        });
    }

    public void launchActivity(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
