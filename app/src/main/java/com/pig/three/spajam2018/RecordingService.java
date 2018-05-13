package com.pig.three.spajam2018;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class RecordingService {
    private MediaRecorder rec;
    private MediaPlayer mp;

    public void StartPlay(String filepath) {
        startPlay(filepath);
    }

    public void StartRecord(File file) {
        startRecord(file);
    }

    public void StopRecord() {
        stopRecord();
    }

    private void startPlay(String filepath) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(filepath);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRecord(File file) {
        File wavFile = file;
        if (wavFile.exists()) {
            wavFile.delete();
        }
        wavFile = null;
        try {
            rec = new MediaRecorder();
            rec.setAudioSource(MediaRecorder.AudioSource.MIC);
            rec.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            rec.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            rec.setOutputFile(file.getPath());

            rec.prepare();
            rec.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        try {
            rec.stop();
            rec.reset();
            rec.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void StartBell(MediaPlayer bell) {
        startBell(bell);
    }

    private void startBell(MediaPlayer bell) {
        bell.start();
    }

    public void StartDonpafu(MediaPlayer donpafu) {
        startDonpafu(donpafu);
    }

    private void startDonpafu(MediaPlayer donpafu) {
        donpafu.start();
    }
}
