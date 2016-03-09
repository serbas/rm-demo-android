package my.apps.demo.russianmediademo.customview.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Utils {
    private static final String TAG = "Utils";

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

	public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
		ActivityManager manager = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				Log.i("isMyServiceRunning", "Service already running");
				return true;
			}
		}
		Log.i("isMyServiceRunning", "Service is not running");
		return false;
	}

    public static String TimeString(int position, int duration) {
        int s1 = position/1000;
        int s2 = duration/1000;
        int m1 = s1 / 60;
        int m2 = s2 / 60;
        return String.format("%02d:%02d/%02d:%02d", m1, s1 % 60, m2, s2 % 60);
    }

    public static String HttpsContent(String url) {
        DataLoader dl = new DataLoader();
        try {
            HttpResponse response = dl.secureLoadData(url);

            StringBuilder sb = new StringBuilder();
            sb.append("HEADERS:\n\n");

            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Header h = headers[i];
                sb.append(h.getName()).append(":\t").append(h.getValue()).append("\n");
            }

            InputStream is = response.getEntity().getContent();
            StringBuilder out = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (String line = br.readLine(); line != null; line = br.readLine())
                out.append(line);
            br.close();

            return out.toString();
        }
        catch(Exception e){
            Log.e(TAG, "HttpsContent:" + e.getMessage());
        }
        return null;
    }



}