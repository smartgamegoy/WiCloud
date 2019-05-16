package com.jetec.wicloud.Pageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.jetec.wicloud.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static android.content.Context.VIBRATOR_SERVICE;
import static java.lang.Thread.sleep;

public class PageTwoView extends PageView {

    private Uri uri = Uri.parse("http://www.jetec.com.tw/#services1");
    private Bitmap preview_bitmap;
    private Vibrator vibrator;
    private int flag = 0;

    public PageTwoView(final Context context) {
        super(context);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.pageone, null);
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        ImageView imageView = view.findViewById(R.id.imageView);

        Runnable getimage = () -> {
            String imageUri = "http://www.jetec.com.tw/W8_Banner1/images/gallery/20181024_BT240A.png";
            preview_bitmap = fetchImage(imageUri);
            flag = 1;
        };
        new Thread(getimage).start();
        for(;flag == 0;){
            try{
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(preview_bitmap);

        imageView.setOnClickListener(v -> {
            vibrator.vibrate(100);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

        addView(view);
    }

    private Bitmap fetchImage( String urlstr ) {
        try {
            URL url;
            url = new URL(urlstr);
            HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
            c.setDoInput( true );
            c.connect();
            InputStream is = c.getInputStream();
            Bitmap img;
            img = BitmapFactory.decodeStream(is);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
