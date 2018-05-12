package com.pig.three.spajam2018;

import android.os.Bundle;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "com.pig.three.spajam2018";

    //set up formatter
    private final String DISP_TIMER_PATTERN = "HH:mm";
    private SimpleDateFormat DispTimerFormatter;
    private final String DISP_TIMER_SECOND_PATTERN = "ss";
    private SimpleDateFormat DispTimerSecondFormatter;
    private final String STANDARD_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat StandardDateFormatter;

    //timer表示用のカレンダー変数
    private Calendar DispCalendar;

    //タイマーのTextView
    private TextView DispTimerText;
    private TextView DispTimerSecondText;

    //endのfirebaseリファレンス
    private DatabaseReference myRef;

    //ハンドラー
    private Handler myHandler;

    //タイマー
    private Timer CountDownTimer;
    private Timer Vibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create handler
        myHandler = new Handler();

        //init firebase
        FirebaseService fbservice = new FirebaseService();
        myRef = fbservice.GetReference("end");

        //set timer 00:00:00
        DispTimerFormatter = new SimpleDateFormat(DISP_TIMER_PATTERN);
        DispTimerSecondFormatter = new SimpleDateFormat(DISP_TIMER_SECOND_PATTERN);
        DispTimerText = findViewById(R.id.timer);
        DispTimerSecondText = findViewById(R.id.timerSecond);

        //表示するカレンダーの初期化
        DispCalendar = Calendar.getInstance();
        DispCalendar.set(Calendar.HOUR_OF_DAY, 0);
        DispCalendar.set(Calendar.MINUTE, 0);
        DispCalendar.set(Calendar.SECOND, 0);
        DispCalendar.set(Calendar.MILLISECOND, 0);

        //hhとmmの初期化
        DispTimerText.setText(DispTimerFormatter.format(DispCalendar.getTime()));

        //ssの初期化
        DispTimerSecondText.setText(DispTimerSecondFormatter.format(DispCalendar.getTime()));

        //set standard dateformat
        StandardDateFormatter = new SimpleDateFormat(STANDARD_DATE_PATTERN);

        //set changelistener on firebase end reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);

                //終了時刻と現在時刻を取得
                Calendar endCal = datestringToCalendar(value);
                Calendar nowCal = Calendar.getInstance();

                int hour = nowCal.get(Calendar.HOUR_OF_DAY);
                int minute = nowCal.get(Calendar.MINUTE);
                int sec = nowCal.get(Calendar.SECOND);

                //差分を計算
                endCal.add(Calendar.HOUR_OF_DAY, -hour);
                endCal.add(Calendar.MINUTE, -minute);
                endCal.add(Calendar.SECOND, -sec);
                DispCalendar = endCal;
                DispTimerText.setText(DispTimerFormatter.format(DispCalendar.getTime()));
                DispTimerSecondText.setText(DispTimerSecondFormatter.format(DispCalendar.getTime()));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //set clicklistenr on up and down
        ImageButton hourUp = findViewById(R.id.hourUp);
        ImageButton hourDown = findViewById(R.id.hourDown);
        ImageButton minutesUp = findViewById(R.id.minutesUp);
        ImageButton minutesDown = findViewById(R.id.minutesDown);

        hourUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DispCalendar.add(Calendar.HOUR_OF_DAY, 1);
                DispTimerText.setText(DispTimerFormatter.format(DispCalendar.getTime()));
            }
        });

        hourDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DispCalendar.add(Calendar.HOUR_OF_DAY, -1);
                DispTimerText.setText(DispTimerFormatter.format(DispCalendar.getTime()));
            }
        });

        minutesUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DispCalendar.add(Calendar.MINUTE, 1);
                DispTimerText.setText(DispTimerFormatter.format(DispCalendar.getTime()));
            }
        });

        minutesDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DispCalendar.add(Calendar.MINUTE, -1);
                DispTimerText.setText(DispTimerFormatter.format(DispCalendar.getTime()));
            }
        });

        //set clicklistener on start button
        Button startButton = findViewById(R.id.startbutton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = DispCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = DispCalendar.get(Calendar.MINUTE);
                int sec = DispCalendar.get(Calendar.SECOND);

                if (hour == 0 && minute == 0) {
                    Toast toast = Toast.makeText(MainActivity.this, "時間と分を設定してください", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // end setting
                    Calendar nowCal = Calendar.getInstance();
                    nowCal.add(Calendar.HOUR_OF_DAY, hour);
                    nowCal.add(Calendar.MINUTE, minute);
                    nowCal.add(Calendar.SECOND, sec);
                    String end = StandardDateFormatter.format(nowCal.getTime());

                    // push firebase end string
                    myRef.setValue(end);

                    CountDownTimer = new Timer();
                    TimerTask countDownTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (!(DispCalendar.get(Calendar.HOUR_OF_DAY) == 0 && DispCalendar.get(Calendar.MINUTE) == 0 && DispCalendar.get(Calendar.SECOND) == 0)){
                                DispCalendar.add(Calendar.SECOND, -1);
                                myHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        DispTimerSecondText.setText(DispTimerSecondFormatter.format(DispCalendar.getTime()));
                                        DispTimerText.setText(DispTimerFormatter.format(DispCalendar.getTime()));
                                    }
                                });
                            } else {
                                CountDownTimer.cancel();
                            }

                        }
                    };
                    CountDownTimer.scheduleAtFixedRate(countDownTask, 0, 1000); //1000ms = 1sec
                }
            }
        });
    }

    private Calendar datestringToCalendar(String stringdate) {
        java.util.Date date = null;

        try {
            date = new java.util.Date( StandardDateFormatter.parse(stringdate).getTime());
        } catch (java.text.ParseException e) {
            Log.d("時間のパースエラー", e.toString());
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
