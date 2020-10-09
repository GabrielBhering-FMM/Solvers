package com.example.solvers.views.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.solvers.R;
import com.example.solvers.adapters.PostRecyclerViewAdapter;
import com.example.solvers.models.Post;
import com.example.solvers.utils.StringSimilarity;
import com.example.solvers.views.CreatePostActivity;
import com.example.solvers.views.PostInfoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.LinkedList;

public class HomeFragment extends Fragment {
    public static String TAG = "com.example.solvers.views.fragments.HomeFragment.post_index";

    View view;

    RecyclerView recyclerView;
    PostRecyclerViewAdapter postAdapter;

    FloatingActionButton btCreatPost;

    FirebaseAuth mFirebaseAuth;

    Toolbar toolbar;
    String filter;

    private FirebaseFirestore db;

    private FirebaseUser user;

    private LinkedList<Post> postList = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Init Firebase instances
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Get logged user
        user = mFirebaseAuth.getCurrentUser();
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //Real time Firestore sync
        db.collection("posts")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w("error", "listen:error", e);
                        return;
                    }

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Log.d("post", "New post: " + dc.getDocument().getData());
                                addPost(dc.getDocument());
                                break;
                            case MODIFIED:
                                Log.d("post", "Modified post: " + dc.getDocument().getData());
                                modifyPost(dc.getDocument());
                                break;
                            case REMOVED:
                                Log.d("post", "Removed post: " + dc.getDocument().getData());
                                deletePost(dc.getDocument());
                                break;
                        }
                    }
                    Log.d("post", "Post List: "+postList.toString());
                    buildRecyclerView();
                });

        //TODO: Create SearchView listener
        toolbar = view.findViewById(R.id.toolbar);

        btCreatPost = view.findViewById(R.id.floatingActionButton);
        btCreatPost.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CreatePostActivity.class);
            startActivity(intent);
        });

        return view;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private void buildRecyclerView(){
        recyclerView = view.findViewById(R.id.post_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        postAdapter = new PostRecyclerViewAdapter(postList,view.getContext());
        postAdapter.setOnItemClickListener(position -> {
                Intent intent = new Intent(view.getContext(), PostInfoActivity.class);
                intent.putExtra(TAG, postList.get(position).getId());
                startActivity(intent);
            }
        );

        recyclerView.setAdapter(postAdapter);
    }

    private void addPost(QueryDocumentSnapshot doc){
        if(doc.get("description") != null){
            HashMap<String, Object> postHash = (HashMap<String, Object>) doc.getData();
            postHash.put("id",doc.getId());

            Post post = new Post(postHash);

            postList.addFirst(post);
        }
    }

    private void modifyPost(QueryDocumentSnapshot doc){
        if(doc.get("description") != null){
            HashMap<String, Object> postHash = (HashMap<String, Object>) doc.getData();
            postHash.put("id",doc.getId());

            Post post = new Post(postHash);

            int index = 0;
            for(Post post_data : postList){
                if(post_data.getId().equals(post.getId())){
                    postList.set(index, post);
                }
                index++;
            }
        }
    }

    private void deletePost(QueryDocumentSnapshot doc){
        if(doc.get("description") != null){
            HashMap<String, Object> postHash = (HashMap<String, Object>) doc.getData();
            postHash.put("id",doc.getId());

            Post post = new Post(postHash);

            int index = 0;
            for(Post post_data : postList){
                if(post_data.getId().equals(post.getId())){
                    postList.remove(index);
                    Log.d("remove_post",postList.toString());
                }
                index++;
            }
        }
    }

    //TODO: Implement filter in toolbar
    private void filterPosts(String filter){
        LinkedList<Post> aux = new LinkedList<>();

        for (Post post: postList) {
            if(StringSimilarity.similarity(post.getSubject().toLowerCase(),filter.toLowerCase())>0.5){
                aux.add(post);
            }
        }
        if(!aux.isEmpty()) postList = aux;
        buildRecyclerView();
    }
}