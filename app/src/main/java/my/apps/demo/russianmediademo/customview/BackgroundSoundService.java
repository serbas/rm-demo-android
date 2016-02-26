package my.apps.demo.russianmediademo.customview;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BackgroundSoundService extends Service {
    private static final String TAG = "BgSoundService";
    public static final String BROADCAST_ACTION = "my.apps.demo.russianmediademo.updatedata";
    MediaPlayer player;

    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    private boolean playing = false;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service", "onCreate");
        player = new MediaPlayer();
        player.setLooping(false); // Set looping
        player.setVolume(100, 100);

        if(intent == null)
            intent = new Intent(BROADCAST_ACTION);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i(TAG, "onCompletion");
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i(TAG, "onPrepared");
            }
        });

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                Log.i(TAG, "onBufferingUpdate");
            }
        });
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000);
        }
    };

    private void DisplayLoggingInfo() {
        if(playing) {
            intent.putExtra("time", new Date().toLocaleString());
            intent.putExtra("counter", String.valueOf(++counter));
            try {
                intent.putExtra("position", CurrentPosition());
                intent.putExtra("duration", GetDuration());
            } catch (Exception e) {

            }
            sendBroadcast(intent);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if(intent == null) return 0;

        Bundle extras = intent.getExtras();
        String url = extras.getString("url");
        Log.d(TAG, "onStartCommand, url=" + url);

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

        try {
            playing = true;

            startPlayProgressUpdater();

            player.setDataSource(url);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int GetDuration(){
        if(player != null && player.isPlaying())
        {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = sharedPrefs.edit();
            edit.putInt("duration", player.getDuration());
            edit.commit();

            return player.getDuration();
        }
        else return 0;
    }

    public int CurrentPosition(){
        if(player != null && player.isPlaying() && playing)
        {
            int position = player.getCurrentPosition();
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = sharedPrefs.edit();
            edit.putInt("currentPosition", position);
            edit.commit();

            return position;
        }
        else return 0;
    }

    public void startPlayProgressUpdater() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    while(playing)
                    {
                        int duration = GetDuration();
                        int pos = CurrentPosition();
                        Log.i(TAG, playing + " " + pos + " / " + duration);

                        TimeUnit.MILLISECONDS.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void onStart(Intent intent, int startId) {
    }

    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {
    }

    public void onPause() {
    }

    @Override
    public void onDestroy() {
        playing = false;
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}