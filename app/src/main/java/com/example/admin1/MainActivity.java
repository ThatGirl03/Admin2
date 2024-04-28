package com.example.admin1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText identificationNumber;
    private EditText password;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Ensure your XML file is named activity_main.xml

        identificationNumber = findViewById(R.id.IdentificationNumber);
        password = findViewById(R.id.Password);
        Button loginButton = findViewById(R.id.loginButton);
        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String idNum = identificationNumber.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (!idNum.isEmpty() && pass.equals("@DutClinic2024")) {
            if (dbHelper.checkStaff(idNum, pass)) {
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Intent to navigate to another Activity or perform other actions
                Intent intent = new Intent(MainActivity.this, welcome.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Invalid Identification Number", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Identification Number and Password cannot be empty and must be correct", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}

