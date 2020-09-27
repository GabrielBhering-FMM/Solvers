package com.example.solvers_login;

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

        edtEmail = (EditText) findViewById(R.id.txt_email_forgotpw);
        btnResetPassword = (Button) findViewById(R.id.btn_forgotpw);
        btnBack = (TextView) findViewById(R.id.view_voltar_pw);
        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Informe seu e-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassActivity.this, "Verifique seu e-mail para redefinir sua senha", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPassActivity.this, "Falha ao enviar o e-mail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
