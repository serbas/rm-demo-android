package my.apps.demo.russianmediademo.customview;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import my.apps.demo.russianmediademo.App;
import my.apps.demo.russianmediademo.R;
import my.apps.demo.russianmediademo.customview.helper.ImageLoader;
import my.apps.demo.russianmediademo.customview.helper.Utils;

public class PlaylistAdapter extends ArrayAdapter<PlaylistItem> {
    private static final String TAG = "PlaylistAdapter";
    private final Activity context;
    private final ImageLoader imageLoader;
    private List<PlaylistItem> items;
    private PlaylistView.OnListFragmentInteractionListener mListener;
    private ViewHolder viewHolder;

    static class ViewHolder {
        public TextView mTitle;
        public ImageView mImage;
        public TextView mPositionText;
        public SeekBar mSeekbar;
    }

    public PlaylistAdapter(Activity context, List<PlaylistItem> items) {
        super(context, R.layout.playlist_item, items);
        this.context = context;
        this.items = items;
        imageLoader = new ImageLoader(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.playlist_item, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mPositionText = (TextView) convertView.findViewById(R.id.position_text);
            viewHolder.mImage = (ImageView) convertView.findViewById(R.id.playlist_image);
            viewHolder.mSeekbar = (SeekBar) convertView.findViewById(R.id.progress);

            imageLoader.DisplayImage(items.get(position).ImageUrl(), viewHolder.mImage);

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        final PlaylistItem pi = items.get(position);

        if(!pi.equals(App.Instance().PlayingItem()))
        {
            pi.SetPosition(0, 0);
        }

        viewHolder.mTitle.setText(pi.Name());
        viewHolder.mPositionText.setText(pi.Data());
        viewHolder.mSeekbar.setMax(pi.GetDuration());
        viewHolder.mSeekbar.setProgress(pi.GetPosition());
        viewHolder.mSeekbar.setSecondaryProgress(0);

        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "item clicked:" + pi.Name());
                App.Instance().SetPlaying(pi);

                Intent svc = new Intent(context, BackgroundSoundService.class);

                if(Utils.isMyServiceRunning(BackgroundSoundService.class, context))
                    context.stopService(svc);

                svc.putExtra("url", pi.Url());
                context.startService(svc);

                if (null != mListener) {
                    mListener.onListFragmentInteraction(pi);
                }
            }
        });

        return convertView;
    }
}
