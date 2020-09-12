package me.sujeetthakur.awesomewallaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FullScreenWallpaperActivity extends AppCompatActivity {

    String originalUrl = "";
    PhotoView photoView;
    ProgressBar progressBar;
    InterstitialAd interstitialAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }

        interstitialAd= new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad));
        AdRequest adRequest1 = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest1);

        progressBar = findViewById(R.id.progressBar2);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        originalUrl = intent.getStringExtra("originalUrl");

        photoView = findViewById(R.id.photo_view);

        Glide.with(this).load(originalUrl).into(photoView);
        //progressBar.setVisibility(View.GONE);
    }

    public void SetWallPaper(View view) {

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

        Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();

        try {
            Toast.makeText(getApplicationContext(),"Successful!!!",Toast.LENGTH_LONG).show();
            wallpaperManager.setBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void shareImageToWhatsapp(View view) {

        Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
        Uri bmpUri = getLocalBitmapUri(bitmap);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, bmpUri);
        share.setPackage("com.whatsapp");//package name of the app
        startActivity(Intent.createChooser(share, "Share Image"));
    }
    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void saveToGallery(View view) {

        final int random = new Random().nextInt(61) + 20;
        Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Awesome Wallpaper!";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + random + ".png";
        File file = new File(myDir, fname);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(FullScreenWallpaperActivity.this, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);

        Toast.makeText(getApplicationContext(),"Saved to the gallery",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });
        }else{
            super.onBackPressed();
        }

    }
}