package com.example.solvers.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.solvers.R;
import com.example.solvers.adapters.PostRecyclerViewAdapter;
import com.example.solvers.models.Post;
import com.example.solvers.utils.StringSimilarity;
import com.example.solvers.views.CreatePostActivity;
import com.example.solvers.views.HomeActivity;
import com.example.solvers.views.PostInfoActivity;
import com.example.solvers.views.SolversHowTo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class HomeFragment extends Fragment {
    public static String TAG = "com.example.solvers.views.fragments.HomeFragment.post_index";

    View view;

    RecyclerView recyclerView;
    PostRecyclerViewAdapter postAdapter;

    FloatingActionButton btCreatPost;
    FloatingActionButton btInfo;

    FirebaseAuth mFirebaseAuth;

    AvatarView profileImg;
    Toolbar toolbar;
    android.widget.SearchView searchView;
    String filter = "";

    private FirebaseFirestore db;
    private FirebaseUser user;

    private LinkedList<Post> postList = new LinkedList<>();
    private LinkedList<Post> oldPostList = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //Create toolbar in fragment
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //Init Firebase instances
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Get logged user
        user = mFirebaseAuth.getCurrentUser();

        //Real time Firestore sync
        db.collection("posts")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.e("error", "listen:error", e);
                        return;
                    }

                    //Check type of change in posts
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

                    //To not update the list with filter if a post change
                    if(filter.isEmpty()){
                        buildRecyclerView();
                    }
                });


        profileImg = view.findViewById(R.id.toolbar_avatar);
        buildProfileImg();

        btCreatPost = view.findViewById(R.id.floatingActionButton);
        btCreatPost.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CreatePostActivity.class);
            startActivity(intent);
        });

        btInfo = view.findViewById(R.id.floatingActionButtonINFO);
        btInfo.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SolversHowTo.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_toolbar, menu);

        //Search view methods
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (android.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPosts(filter);
                searchItem.collapseActionView();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                filter = s;
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            if(!filter.isEmpty() && postList!=oldPostList){
                filter = "";
                postList = oldPostList;
                buildRecyclerView();
            }
            return true;
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                searchView.setQuery(filter,false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if (searchView.getQuery().equals("")) filter = "";
                if (filter.isEmpty()) {
                    if (postList!=oldPostList && !oldPostList.isEmpty()) postList = oldPostList;
                    buildRecyclerView();
                    return true;
                }
                else return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public void buildProfileImg(){
        if(user.getPhotoUrl()!=null){
            String url = user.getPhotoUrl().toString();

            IImageLoader imgLoader = new PicassoLoader();
            imgLoader.loadImage(profileImg, url,user.getDisplayName());
        }
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
            int removeIndex = -1;
            Iterator<Post> iterator = postList.iterator();
            while (iterator.hasNext()){
                if(iterator.next().getId().equals(post.getId())) removeIndex = index;
                index++;
            }
            if (removeIndex > -1) postList.remove(removeIndex);
        }
    }

    private void filterPosts(String filter){
        LinkedList<Post> aux = new LinkedList<>();

        if (postList!=oldPostList && !oldPostList.isEmpty()) postList = oldPostList;

        for (Post post: postList) {
            if(StringSimilarity.similarity(post.getSubject().toLowerCase(),filter.toLowerCase())>0.5){
                aux.add(post);
            }
        }
        if(!aux.isEmpty()) {
            oldPostList = postList;
            postList = aux;
        }
        buildRecyclerView();
    }
}