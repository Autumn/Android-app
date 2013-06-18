package io.r.a.dio;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.support.v4.view.ViewPager;


public class MainActivity extends Activity {

    HomePagerAdapter mainPagerAdapter;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment);
        mainPagerAdapter = new HomePagerAdapter(getFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.home_pager);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                ((HomeFragment)mainPagerAdapter.getItem(1)).shareTrack();
                return true;
            case R.id.menu_settings:
                // do nothing
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
