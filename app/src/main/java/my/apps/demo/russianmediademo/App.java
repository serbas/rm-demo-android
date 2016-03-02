package my.apps.demo.russianmediademo;

import java.util.List;

import my.apps.demo.russianmediademo.customview.PlaylistItem;

public class App {

    private static final String TAG = "App";
    private static App instance;
    private List<PlaylistItem> _playlist;
    private PlaylistItem playing_item;
    private boolean _busy;

    private App() {
    }

    public static App Instance() {
        if (instance == null)
            instance = new App();
        return instance;
    }

    public void SetPlaylists(List<PlaylistItem> playlist) {
        _playlist = playlist;
    }

    public List<PlaylistItem> Playlist(){
        return _playlist;
    }

    public void SetPlaying(PlaylistItem pi) {
        playing_item = pi;
    }

    public PlaylistItem PlayingItem(){return playing_item;}

    public void SetBusy(boolean busy){
        _busy = busy;
    }

    public boolean IsBusy(){
        return  _busy;
    }
}
