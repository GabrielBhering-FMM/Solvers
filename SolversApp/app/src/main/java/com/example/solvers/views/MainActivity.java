package com.example.solvers.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solvers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextInputLayout emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    ProgressBar pd;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailId = findViewById(R.id.txt_email_cad);
        password = findViewById(R.id.txt_senha_cad);

        btnSignUp = findViewById(R.id.btn_cad);
        tvSignIn = findViewById(R.id.txt_ja_tem_cad);

        pd = findViewById(R.id.progressBar2);
        pd.setIndeterminate(true);

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if(mFirebaseUser != null){
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        };

        setEvents();
    }

    public void setEvents(){
        btnSignUp.setOnClickListener(v -> {
            String email = emailId.getEditText().getText().toString();
            String pwd = password.getEditText().getText().toString();

            pd.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if(email.isEmpty()){
                emailId.setError("E-mail não informado");
                emailId.requestFocus();

                pd.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }else if(pwd.isEmpty()){
                password.setError("Senha não informada");

                pd.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }else createUser(email, pwd);
        });

        tvSignIn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        });
    }

    public void createUser(String email, String password){
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, task -> {
            try {
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Campos incorretos, tente novamente", Toast.LENGTH_SHORT).show();
                    pd.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                else{
                    FirebaseUser user = task.getResult().getUser();

                    Map<String,Object> userHash = new HashMap<>();
                    userHash.put("uid",user.getUid());
                    userHash.put("displayName",user.getDisplayName()!=null?user.getDisplayName():"user");
                    userHash.put("email",user.getEmail());
                    userHash.put("imageUrl", user.getPhotoUrl()!=null?user.getPhotoUrl().toString():"");

                    addUserToFirestore(user, userHash);
                }
            }catch (Exception e){
                Log.e("cadastro",e.toString());
                e.printStackTrace();

                mFirebaseAuth.getCurrentUser().delete();
                mFirebaseAuth.signOut();
            }
        });
    }

    public void addUserToFirestore(FirebaseUser user, Map<String,Object> userHash){
        db.collection("users").document(user.getUid()).set(userHash).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else{
                user.delete();
                Toast.makeText(MainActivity.this,"Ocorreu um erro no cadastro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
