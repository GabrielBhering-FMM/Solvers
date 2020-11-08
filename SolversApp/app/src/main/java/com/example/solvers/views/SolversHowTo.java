package com.example.solvers.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.solvers.R;

public class SolversHowTo extends AppCompatActivity {
    Button volta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvers_how_to);
        volta = findViewById(R.id.btnVolta);
    }

    public void setEvents() {
        volta.setOnClickListener(v -> {
            Intent intToHome = new Intent(getApplicationContext(), HomeActivity.class);
            intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intToHome);
            SolversHowTo.this.finish();
        });
    }
}