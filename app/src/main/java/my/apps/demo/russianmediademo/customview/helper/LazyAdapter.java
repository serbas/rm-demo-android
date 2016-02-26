package my.apps.demo.russianmediademo.customview.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import my.apps.demo.russianmediademo.R;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<String> images;
    private List<String> titles;
    private List<String> delays;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, List<String> i, List<String> t, List<String> d) {
        activity = a;
        images = i;
        titles = t;
        delays = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.playlist_item, null);

        TextView text=(TextView)vi.findViewById(R.id.text);;
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        text.setText(position + ". ["+delays.get(position)+"] " + titles.get(position));
        imageLoader.DisplayImage(images.get(position), image);
        return vi;
    }
}
