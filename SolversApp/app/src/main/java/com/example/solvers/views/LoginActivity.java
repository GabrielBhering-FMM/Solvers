package com.example.solvers.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solvers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    TextView esquecisenha;

    ProgressBar pd;

    private  FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        pd = findViewById(R.id.progressBar);
        pd.setIndeterminate(true);

        emailId = findViewById(R.id.txt_email_login);
        password = findViewById(R.id.txt_senha_login);
        btnSignIn = findViewById(R.id.btn_login);
        tvSignUp = findViewById(R.id.txt_nao_tem_cad);
        esquecisenha = findViewById(R.id.txt_esqueci_senha);

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if(mFirebaseUser != null){
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                LoginActivity.this.finish();
            }
        };

        setEvents();
    }

    public void setEvents(){
        btnSignIn.setOnClickListener(v -> {
            String email = emailId.getText().toString();
            String pwd = password.getText().toString();

            pd.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if(email.isEmpty()){
                emailId.setError("E-mail não informado");
                emailId.requestFocus();
            }
            else if(pwd.isEmpty()){
                password.setError("Senha não informada");
            }
            else {
                mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Erro de autenticação", Toast.LENGTH_SHORT).show();
                            pd.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                        else{
                            Intent intToHome = new Intent(getApplicationContext(), HomeActivity.class);
                            intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intToHome);
                            LoginActivity.this.finish();
                        }
                    }
                });
            }
        });

        tvSignUp.setOnClickListener(v -> finish());

        esquecisenha.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, ForgotPassActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
