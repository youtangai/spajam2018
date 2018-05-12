package com.pig.three.spajam2018;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "com.pig.three.spajam2018";
    private SimpleDateFormat dataFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataFormat = new SimpleDateFormat("hh:mm");

        TextView timerText = findViewById(R.id.timer);
        timerText.setText(dataFormat.format(0));
    }

    private void testFireBaseDataBase() {
        //fbservice instacne
        FirebaseService fbservice = new FirebaseService();

        //get reference firebase object
        DatabaseReference myRef = fbservice.GetReference("message");

        //set change listener on reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        myRef.setValue("hello world");
        myRef.setValue("Hello, World!");
    }
}
