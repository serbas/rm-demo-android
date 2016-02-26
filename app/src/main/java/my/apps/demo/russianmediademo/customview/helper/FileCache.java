package my.apps.demo.russianmediademo.customview.helper;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class FileCache {

    private static final String TAG = "FileCache";
    private File cacheDir;
    
    public FileCache(Context context){
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"PlaylistImagesCache");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String url){
        String filename = String.valueOf(url.hashCode()) + ".jpg";

        Log.i(TAG, url + " -> " + filename);

        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public void clear(){
        File[] files = cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}