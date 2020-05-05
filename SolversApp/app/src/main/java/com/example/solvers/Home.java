package com.example.solvers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.solvers.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Home extends AppCompatActivity {
    private TextView txtUsername;
    private ListView postList;
    private FloatingActionButton fab;

    private String username;

    public static List<Post> posts = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtUsername = findViewById(R.id.username);
        postList = findViewById(R.id.postList);
        fab = findViewById(R.id.floatingActionButton2);

        username = RegisterActivity.username_geral;

        String string = "Posts criados por: "+username;
        txtUsername.setText(string);

        ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, posts);
        postList.setAdapter(adapter);

        actions();
    }

    public void actions(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity();
            }
        });
    }

    public void launchActivity(){
        Intent intent1 = new Intent(this, CreatePost.class);
        startActivity(intent1);
        finish();
    }
}
