package my.apps.demo.russianmediademo;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.apps.demo.russianmediademo.customview.BackgroundSoundService;
import my.apps.demo.russianmediademo.customview.PlaylistAdapter;
import my.apps.demo.russianmediademo.customview.PlaylistItem;
import my.apps.demo.russianmediademo.customview.PlaylistView;
import my.apps.demo.russianmediademo.customview.helper.RestClient;
import my.apps.demo.russianmediademo.customview.helper.Utils;

public class MainActivity extends Activity implements RecyclerView.OnClickListener {

    private static final String TAG = "MainActivity";
    private String service_url = "https://backend.soundstream.media/API/v1.5/?action=get_podcasts";
    private Fragment newFragment;
    private Button stop_btn;
    PlaylistView pv;

    private PlaylistAdapter adapter;

    private Intent intent;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        int duration = intent.getIntExtra("duration", 0);
        int position = intent.getIntExtra("position", 0);

        PlaylistItem pi = App.Instance().PlayingItem();
        pi.SetData(Utils.TimeString(position, duration));
        pi.SetPosition(position, duration);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stop_btn = (Button)findViewById(R.id.stop_btn);
        stop_btn.setEnabled(true);

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopMedia();
            }
        });

        pv = (PlaylistView)findViewById(R.id.playlist);

        if(App.Instance().Playlist() == null)
            (new AsyncListViewLoader()).execute(service_url);

        intent = new Intent(this, BroadcastService.class);

        if(!Utils.isMyServiceRunning(BackgroundSoundService.class, this))
        {
            try {
                startService(intent);
                registerReceiver(broadcastReceiver, new IntentFilter(BackgroundSoundService.BROADCAST_ACTION));
            }
            catch(Exception e){
                Log.e(TAG, e.getMessage());
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private void StopMedia() {
        Intent svc = new Intent(this, BackgroundSoundService.class);
        stopService(svc);
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<PlaylistItem>> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        private String result;
        @Override
        protected void onPostExecute(List<PlaylistItem> result) {
            super.onPostExecute(result);
            dialog.dismiss();

            App.Instance().SetPlaylists(result);

            UpdateAdapter();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setMessage("Loading playlist...");
            dialog.show();
        }

        @Override
        protected List<PlaylistItem> doInBackground(String... params) {
            List<PlaylistItem> data = LoadData(params[0]);
            return data;
        }
    }

    private void UpdateAdapter() {
        adapter = new PlaylistAdapter(this, App.Instance().Playlist());
        pv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<PlaylistItem> LoadData(String url){

        List<PlaylistItem> result = new ArrayList<>();

        RestClient rc = new RestClient();
        try {
            rc.Execute(RestClient.RequestMethod.GET, url, null, null, null);

            JSONObject jResponse = new JSONObject(rc.response);
            boolean is_ok = jResponse.getBoolean("success");

            if(is_ok) {
                JSONArray jData = jResponse.getJSONArray("data");
                for (int i=0; i<jData.length(); i++) {
                    JSONObject jo = jData.getJSONObject(i);

                    String id = jo.getString("id");
                    String name = jo.getString("name");
                    String image = jo.getString("image");
                    JSONObject play = jo.getJSONObject("play");
                    String high = play.getString("high");

                    PlaylistItem pli = new PlaylistItem(id, name, image, high, "00:00");
                    result.add(pli);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
