package my.apps.demo.russianmediademo;

import java.util.List;

import my.apps.demo.russianmediademo.customview.PlaylistItem;

public class App {

    private static final String TAG = "App";
    private static App instance;
    private List<PlaylistItem> _playlist;
    private PlaylistItem playing_playlist;

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
        playing_playlist = pi;
    }

    public PlaylistItem PlayingItem(){return playing_playlist;}
}
