package com.example.admin1;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Appointments extends AppCompatActivity {
    private DatabaseHelper2 dbHelper;
    private LinearLayout appointmentsLayout;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointments);

        dbHelper = new DatabaseHelper2(this);
        appointmentsLayout = findViewById(R.id.LinearLayout); // Ensure this ID matches your LinearLayout ID in XML
        addHeaders();
        loadAppointments();
    }

    private void addHeaders() {
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.addView(createHeaderTextView("User Number", 2.5f));
        headerLayout.addView(createHeaderTextView("Datetime", 3.5f));
        appointmentsLayout.addView(headerLayout);
    }

    private TextView createHeaderTextView(String text, float weight) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(params);
        return textView;
    }

    private void loadAppointments() {
        executorService.execute(() -> {
            Cursor cursor = dbHelper.getAllAppointments();
            if (cursor == null || cursor.getCount() == 0) {
                initializeDemoAppointments();
            }
            cursor = dbHelper.getAllAppointments();
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(0);
                    String userId = cursor.getString(1);
                    String dateTime = cursor.getString(2);
                    handler.post(() -> addAppointmentView(id, userId, dateTime));
                } while (cursor.moveToNext());
                cursor.close();
            } else {
                handler.post(() -> Toast.makeText(Appointments.this, "No appointments to display", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void initializeDemoAppointments() {
        dbHelper.insertAppointment("22238460", "2024-04-25 09:00");
        dbHelper.insertAppointment("22345776", "2024-04-25 10:00");
        dbHelper.insertAppointment("21889003", "2024-04-25 11:00");
        dbHelper.insertAppointment("22347811", "2024-04-25 12:00");
        dbHelper.insertAppointment("22239905", "2024-04-25 13:00");
    }

    private void addAppointmentView(String id, String userId, String dateTime) {
        LinearLayout appointmentView = new LinearLayout(this);
        appointmentView.setOrientation(LinearLayout.HORIZONTAL);
        appointmentView.addView(createTextView(userId, 2.5f));
        appointmentView.addView(createTextView(dateTime, 3.5f));
        appointmentView.addView(createCheckBox(id, userId));
        appointmentView.addView(createDeleteButton(id));
        appointmentsLayout.addView(appointmentView);
    }

    private TextView createTextView(String content, float weight) {
        TextView textView = new TextView(this);
        textView.setText(content);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(params);
        return textView;
    }

    private CheckBox createCheckBox(String id, String userId) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(Appointments.this, "Attendance confirmed for " + userId, Toast.LENGTH_SHORT).show();
            }
        });
        return checkBox;
    }

    private Button createDeleteButton(String id) {
        Button deleteButton = new Button(this);
        Drawable deleteIcon = ContextCompat.getDrawable(this, android.R.drawable.ic_delete);
        if (deleteIcon != null) {
            deleteIcon.setBounds(0, 0, 40, 40); // Smaller icon size to fully appear
            deleteButton.setBackground(deleteIcon);
        }
        deleteButton.setOnClickListener(v -> confirmDeletion(id, v.getParent()));
        return deleteButton;
    }

    private void confirmDeletion(String id, Object parent) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this appointment?")
                .setPositiveButton("Yes", (dialog, which) -> deleteAppointment(id, (LinearLayout) parent))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAppointment(String id, LinearLayout appointmentView) {
        executorService.execute(() -> {
            if (dbHelper.deleteAppointment(id)) {
                handler.post(() -> {
                    appointmentsLayout.removeView(appointmentView);
                    Toast.makeText(Appointments.this, "Appointment Deleted", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
