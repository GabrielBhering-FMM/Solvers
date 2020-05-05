package com.example.solvers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.CDATASection;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        Button btLogin = (Button) findViewById(R.id.btLogin);
        Button btCad = (Button) findViewById(R.id.btLogin2);

        btLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                TextView txtUser = (TextView) findViewById(R.id.txtUser);
                TextView txtPw = (TextView) findViewById(R.id.txtPw);
                String email = txtUser.getText().toString();
                String pw = txtPw.getText().toString();
                if(email.equals(RegisterActivity.email_geral) && pw.equals(RegisterActivity.senha_geral)){
                    alert("Logou!");
                }
                else{
                    alert("Não logou :(");
                }

            }
        });

        btCad.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LaunchActivity();
            }
        });


    }
    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
    public void LaunchActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

}
