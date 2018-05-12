package com.pig.three.spajam2018;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "com.pig.three.spajam2018";
    private SimpleDateFormat dispdateformat;
    private final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat dateformat;
    private Calendar dispcalendar;
    private TextView timerText;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set standard dateformat
        dateformat = new SimpleDateFormat(DATE_PATTERN);

        //init firebase
        FirebaseService fbservice = new FirebaseService();
        myRef = fbservice.GetReference("end");

        //set changelistener on firebase end reference
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

        //set timer 00:00
        dispdateformat = new SimpleDateFormat("HH:mm");
        timerText = findViewById(R.id.timer);
        dispcalendar = Calendar.getInstance();
        dispcalendar.set(Calendar.HOUR_OF_DAY, 0);
        dispcalendar.set(Calendar.MINUTE, 0);
        dispcalendar.set(Calendar.SECOND, 0);
        dispcalendar.set(Calendar.MILLISECOND, 0);
        timerText.setText(dispdateformat.format(dispcalendar.getTime()));

        //set clicklistenr on up and down
        ImageButton hourUp = findViewById(R.id.hourUp);
        ImageButton hourDown = findViewById(R.id.hourDown);
        ImageButton minutesUp = findViewById(R.id.minutesUp);
        ImageButton minutesDown = findViewById(R.id.minutesDown);

        hourUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispcalendar.add(Calendar.HOUR_OF_DAY, 1);
                timerText.setText(dispdateformat.format(dispcalendar.getTime()));
            }
        });

        hourDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispcalendar.add(Calendar.HOUR_OF_DAY, -1);
                timerText.setText(dispdateformat.format(dispcalendar.getTime()));
            }
        });

        minutesUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispcalendar.add(Calendar.MINUTE, 1);
                timerText.setText(dispdateformat.format(dispcalendar.getTime()));
            }
        });

        minutesDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispcalendar.add(Calendar.MINUTE, -1);
                timerText.setText(dispdateformat.format(dispcalendar.getTime()));
            }
        });

        //set clicklistener on start button
        Button startButton = findViewById(R.id.startbutton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = dispcalendar.get(Calendar.HOUR_OF_DAY);
                int minute = dispcalendar.get(Calendar.MINUTE);
                if (hour == 0 && minute == 0) {
                    Toast toast = Toast.makeText(MainActivity.this, "時間と分を設定してください", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // end setting
                    Calendar nowCal = Calendar.getInstance();
                    nowCal.add(Calendar.HOUR_OF_DAY, hour);
                    nowCal.add(Calendar.MINUTE, minute);
                    String end = dateformat.format(nowCal.getTime());

                    // push firebase end string
                    myRef.setValue(end);
                }
            }
        });
    }

    private Calendar datestringToCalendar(String stringdate) {
        java.util.Date date = null;

        try {
            date = new java.util.Date( dateformat.parse(stringdate).getTime());
        } catch (java.text.ParseException e) {
            Log.d("時間のパースエラー", e.toString());
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
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
