package com.pig.three.spajam2018;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;

public class RecordingService {
    private MediaRecorder rec;
    static final String filepath = Environment.getExternalStorageDirectory() + "/sample.wav";

    public void StartRecord() {
        startRecord();
    }

    public void StopRecord() {
        stopRecord();
    }

    private void startRecord() {
        File wavFile = new File(filepath);
        if (wavFile.exists()) {
            wavFile.delete();
        }
        wavFile = null;
        try {
            rec = new MediaRecorder();
            rec.setAudioSource(MediaRecorder.AudioSource.MIC);
            rec.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            rec.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            rec.setOutputFile(filepath);

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
}
