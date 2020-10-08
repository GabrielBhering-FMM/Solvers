package com.example.solvers.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solvers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    EditText edtEmail;
    Button btnResetPassword;
    TextView btnBack;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        edtEmail = findViewById(R.id.txt_email_forgotpw);
        btnResetPassword = findViewById(R.id.btn_forgotpw);
        btnBack = findViewById(R.id.view_voltar_pw);
        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Informe seu e-mail", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassActivity.this, "Verifique seu e-mail para redefinir sua senha", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPassActivity.this, "Falha ao enviar o e-mail", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(ForgotPassActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

    }
}
