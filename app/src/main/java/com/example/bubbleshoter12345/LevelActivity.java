package com.example.bubbleshoter12345;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LevelActivity extends AppCompatActivity {

    private Button level1Button;
    private Button level2Button;
    private Button level3Button;
    private Button level4Button;
    private Button level5Button;
    private Button level6Button;
    private Button level7Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level); // Corrected layout file name

        level1Button = findViewById(R.id.level1Button);
        level2Button = findViewById(R.id.level2Button);
        level3Button = findViewById(R.id.level3Button);
        level4Button = findViewById(R.id.level4Button);
        level5Button = findViewById(R.id.level5Button);
        level6Button = findViewById(R.id.level6Button);
        level7Button = findViewById(R.id.level7Button);

        level1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the GameActivity
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        level2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the GameActivity
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        level3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the GameActivity
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        level4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        level5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        level6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        level7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        // Add click listeners and logic for other buttons
    }
}
