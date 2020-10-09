package com.example.solvers.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.solvers.R;
import com.example.solvers.utils.MarkwonBuilder;
import com.example.solvers.views.dialogs.SubjectDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import io.noties.markwon.ext.latex.JLatexMathPlugin;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;

public class CreatePostActivity extends AppCompatActivity {

    private FirebaseUser user;

    Toolbar mActionBar;
    EditText txtDescription;
    FloatingActionButton btSubmit;

    ProgressBar loading;

    private FirebaseFirestore db;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        //Init Firebase instances
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Get logged user
        user = mFirebaseAuth.getCurrentUser();

        //Text area initialization
        txtDescription = findViewById(R.id.txtDescription);
        txtDescription.requestFocus();
        if(txtDescription.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        //Set toolbar return button
        mActionBar = findViewById(R.id.create_post_toolbar);
        mActionBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mActionBar.setNavigationOnClickListener(view -> CreatePostActivity.this.finish());

        //Init Markdown interpreter and editor
        final Markwon markwon = MarkwonBuilder.build(this,txtDescription.getTextSize());
        final MarkwonEditor editor = MarkwonEditor.create(markwon);
        txtDescription.addTextChangedListener(MarkwonEditorTextWatcher.withPreRender(
                editor,
                Executors.newCachedThreadPool(),
                txtDescription));

        btSubmit = findViewById(R.id.btSubmit);
        loading = findViewById(R.id.loading_post_submit);

        getActions();
    }

    public void getActions(){
        btSubmit.setOnClickListener(view -> {
            if(txtDescription.getText().toString() != null && !txtDescription.getText().toString().equals("")){
                SubjectDialog subjectDialog = new SubjectDialog(CreatePostActivity.this);
                subjectDialog.setOnSendListener(subject -> {
                    dialogSend(subject);
                });
                subjectDialog.show();
            }
        });
    }

    public void dialogSend(String subject){
        loading.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        HashMap<String, Object> post = new HashMap<>();
        post.put("subject", subject);
        post.put("description", txtDescription.getText().toString());
        post.put("isAnswered", false);
        post.put("author", user.getUid());
        post.put("createdAt", new Date());

        db.collection("posts").add(post)
                .addOnSuccessListener(documentReference -> CreatePostActivity.this.finish())
                .addOnFailureListener(e -> Log.e("error", "Error adding document", e));
    }
}