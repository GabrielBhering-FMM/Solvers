package com.example.solvers_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.txt_email);
        password = findViewById(R.id.txt_senha);
        btnSignUp = findViewById(R.id.btn_cad);
        tvSignIn = findViewById(R.id.txt_ja_tem_cad);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = emailId.getText().toString();
            String pwd = password.getText().toString();
            if(email.isEmpty()){
                emailId.setError("E-mail não informado");
                emailId.requestFocus();
            }
            else if(pwd.isEmpty()){
                password.setError("Senha não informada");
            }
            else if(email.isEmpty() && pwd.isEmpty()){
                Toast.makeText(MainActivity.this,"Campos vazios", Toast.LENGTH_SHORT).show();
            }
            else if(!(email.isEmpty() && pwd.isEmpty())){
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Campos incorretos, tente novamente", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        }
                    }
                });
            }
            else{
                Toast.makeText(MainActivity.this,"Erro no login!", Toast.LENGTH_SHORT).show();

            }
            }
        });



        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
