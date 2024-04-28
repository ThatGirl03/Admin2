package com.example.admin1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class welcome extends AppCompatActivity {
    private Button appointmentsButton;
    private Button medicalRecordsButton;
    private Button healthTopicsButton;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome); // make sure your layout file is named welcome.xml

        // Initialize all buttons
        appointmentsButton = findViewById(R.id.AppointmentsButton);
        medicalRecordsButton = findViewById(R.id.MedicalRecordsButton);
        healthTopicsButton = findViewById(R.id.HealthTopicsButton);
        homeButton = findViewById(R.id.HomeButton);

        // Set up navigation for each button
        appointmentsButton.setOnClickListener(v -> navigateTo(Appointments.class));
        medicalRecordsButton.setOnClickListener(v -> navigateTo(MedicalRecords.class));
        healthTopicsButton.setOnClickListener(v -> navigateTo(HealthTopics.class));
        homeButton.setOnClickListener(v -> finish()); // Closes this activity
    }

    // Generic method to navigate to different activities
    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}



