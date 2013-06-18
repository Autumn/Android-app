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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.support.v4.view.ViewPager;


public class MainActivity extends Activity {

    HomePagerAdapter mainPagerAdapter;
    ViewPager viewPager;

    public RadioService service;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName arg0, IBinder binder) {
            service = ((RadioService.LocalBinder) binder).getService();
            Message m = Message.obtain();
            m.what = ApiUtil.ACTIVITYCONNECTED;
            m.replyTo = mMessenger;
            try {
                service.getMessenger().send(m);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            service.updateApiData();
        }

        public void onServiceDisconnected(ComponentName name) {
            // Activity disconnected never sent
            service = null;
        }
    };
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ApiUtil.NPUPDATE) {
                ApiPacket packet = (ApiPacket) msg.obj;
                MainActivity.this.update(packet);
                /*LastPlayedFragment.updateLastPlayed(packet);
                QueueFragment.updateQueue(packet);*/
            }
        }
    };

    private void update(ApiPacket packet) {
        HomeFragment homeFragment = (HomeFragment) mainPagerAdapter.getItem(1);
        QueueFragment queueFragment = (QueueFragment) mainPagerAdapter.getItem(2);
        LastPlayedFragment lastPlayedFragment = (LastPlayedFragment) mainPagerAdapter.getItem(0);
        //homeFragment.updateNP(packet);
        homeFragment.songName = packet.songName;
        homeFragment.artistName = packet.artistName;
        homeFragment.djName = packet.dj;
        homeFragment.songStart = packet.start;
        homeFragment.songCur = packet.cur;
        homeFragment.songEnd = packet.end;
        homeFragment.djImageUrl = packet.djimg;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment);
        Intent servIntent = new Intent(this, RadioService.class);

        bindService(servIntent, serviceConnection, Context.BIND_AUTO_CREATE);

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
