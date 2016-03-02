package my.apps.demo.russianmediademo.customview;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
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

        if(intent == null)
            intent = new Intent(BROADCAST_ACTION);

        player = new MediaPlayer();
        player.setLooping(false);
        player.setVolume(100, 100);

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
            if(playing) {
                try {
                    intent.putExtra("position", CurrentPosition());
                    intent.putExtra("duration", GetDuration());
                } catch (Exception e) {

                }
                sendBroadcast(intent);
            }

            handler.postDelayed(this, 1000);
        }
    };

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if(intent == null) return 0;

        Bundle extras = intent.getExtras();
        String url = extras.getString("url");
        Log.d(TAG, "onStartCommand, url=" + url);

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

        startMediaPlayer(url);

        startPlayProgressUpdater();

        return 1;
    }

    private void startMediaPlayer(final String url) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "startMediaPlayer...");
                    playing = true;
                    player.setDataSource(url);
                    player.prepare();
                    player.start();
                    Log.i(TAG, "startMediaPlayer started!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
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

    public int GetDuration(){
        if(player != null && player.isPlaying())
            return player.getDuration();
        else return 0;
    }

    public int CurrentPosition(){
        if(player != null && player.isPlaying() && playing)
            return player.getCurrentPosition();
        else return 0;
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