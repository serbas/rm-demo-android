package my.apps.demo.russianmediademo.customview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

import my.apps.demo.russianmediademo.R;

/**
 * TODO: document your custom view class.
 */
public class PlaylistView extends ListView {
    private static final String TAG = "PlaylistView";
    private String mPodcastUrl;
    private Intent intent;

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PlaylistItem item);
    }

    public PlaylistView(Context context) {
        super(context);
        init(null, 0);
    }

    public PlaylistView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlaylistView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PlaylistView, defStyle, 0);

        mPodcastUrl = a.getString(R.styleable.PlaylistView_playlist_url);

        a.recycle();
    }

    public String getPodcastUrl() {
        return mPodcastUrl;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

}
