
package co.roomapp.room.activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import co.roomapp.room.R;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.content.Intent;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.widget.TextView;
import android.os.Message;
import javax.security.auth.callback.Callback;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.TimerTask;
import java.util.Timer;

public class RAAudioRecorderActivity extends Activity implements OnCompletionListener, View.OnClickListener {
    private static final String TAG = "RAAudioRecorderActivity";

    private static final String OUT_FILE_NAME = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/audio-recorder-output.m4a";

    private ImageButton btnHold;

    private ImageButton btnBack;

    private ImageButton btnDone;

    private TextView timeLabel;

    private MediaRecorder mediaRecorder = null;

    private MediaPlayer mediaPlayer = null;

    private File file;

    Handler handler;
    ScheduledThreadPoolExecutor executor;

    long starttime = 0;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        super.setContentView(R.layout.activity_rarecord_audio);
        this.btnHold = (ImageButton)findViewById(R.id.btnHold);
        this.btnBack= (ImageButton)findViewById(R.id.btnBack);
        this.btnDone = (ImageButton)findViewById(R.id.btnDone);
        this.timeLabel = (TextView)findViewById(R.id.timeLabel);



        btnHold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                System.out.println("Start recording");

                System.out.println("Path: " + OUT_FILE_NAME);

                record(null);


                btnHold.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {

                            System.out.println("Stop recording");
                            stopRecording(null);
                            btnHold.setOnTouchListener(null);
                            return true;
                        }
                        else
                            return false;
                    }
                });

                return false;
            }

        });

        btnDone.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        this.file = new File(OUT_FILE_NAME);

    }

    public void record(View v) {
        Log.d(TAG, "record");
        String state = android.os.Environment.getExternalStorageState();

        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            System.out.println("SD Card is not mounted.  It is " + state
                    + ".");
        }

        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioChannels(1);
        this.mediaRecorder.setAudioSamplingRate(44100);
        this.mediaRecorder.setAudioEncodingBitRate(64000);
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        this.mediaRecorder.setOutputFile(this.file.getAbsolutePath());
        this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            this.mediaRecorder.prepare();
            this.mediaRecorder.start();
        } catch (IOException e) {
            Log.e(TAG, "Failed to record()", e);
        }
    }

    public void play(View v) {
        Log.d(TAG, "play()");
        if (this.file.exists()) {
            this.mediaPlayer = new MediaPlayer();
            try {
                this.mediaPlayer.setDataSource(this.file.getAbsolutePath());
                this.mediaPlayer.prepare();
                this.mediaPlayer.setOnCompletionListener(this);
                this.mediaPlayer.start();
                // update the buttons
            } catch (IOException e) {
                Log.e(TAG, "Failed to play()", e);
            }
        } else {

        }
    }

    public void stopRecording(View v) {
        Log.d(TAG, "stop()");
//        if (this.mediaPlayer != null) {
//            // stop/release the media player
//            this.mediaPlayer.stop();
//            this.mediaPlayer.release();
//            this.mediaPlayer = null;
//        }
//        else
        if (this.mediaRecorder != null) {
            // stop/release the media recorder
            this.mediaRecorder.stop();
            this.mediaRecorder.release();
            this.mediaRecorder = null;
        }
        // update the buttons
    }

    public void delete(View v) {
        Log.d(TAG, "delete()");
        this.file.delete();
        // update the buttons
    }

    @Override
    public void onPause() {
        super.onPause();
        this.stopRecording(null);
    }

    // called when the playback is done
    public void onCompletion(MediaPlayer mp) {
        this.stopRecording(null);
    }

    public void onClick(View v)
    {
        ImageButton btnClicked = (ImageButton)v;
        if(btnClicked == btnDone)
        {
            Bundle bundle = new Bundle();
            bundle.putString("status", "Done");
            bundle.putString("audioPath", OUT_FILE_NAME);
            Intent mIntent = new Intent();
            mIntent.putExtras(bundle);
            setResult(Activity.RESULT_OK, mIntent);
            finish();
        }
        else if(btnClicked == btnBack)
        {
            Bundle bundle = new Bundle();
            bundle.putString("status", "Canceled");
            Intent mIntent = new Intent();
            mIntent.putExtras(bundle);
            setResult(Activity.RESULT_CANCELED, mIntent);
            finish();
        }
    }
}
