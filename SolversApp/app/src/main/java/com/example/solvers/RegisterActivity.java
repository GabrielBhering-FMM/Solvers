package com.example.solvers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    public static String username_geral;
    public static String email_geral;
    public static String senha_geral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btCadastrarUser = (Button) findViewById(R.id.btCadastrarUser);
        btCadastrarUser.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                TextView txtNome = (TextView) findViewById(R.id.txtUsername);
                TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
                TextView txtSenha = (TextView) findViewById(R.id.pwUser);
                TextView txtSenhaDnv = (TextView) findViewById(R.id.pwUserDnv);

                String NomeCad = txtNome.getText().toString();
                String EmailCad = txtEmail.getText().toString();
                String PwCad = txtSenha.getText().toString();
                String PwDnvCad = txtSenhaDnv.getText().toString();

                if(PwCad.equals(PwDnvCad)){
                    username_geral = NomeCad;
                    email_geral = EmailCad;
                    senha_geral = PwCad;
                    LaunchActivity();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Erro no Cadastro",Toast.LENGTH_LONG).show();
                }


            }
            private void LaunchActivity() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
