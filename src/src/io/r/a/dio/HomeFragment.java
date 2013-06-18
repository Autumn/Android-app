package io.r.a.dio;

import android.app.Fragment;
import android.content.ComponentName;
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
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aki on 18/05/13.
 */

public class HomeFragment extends Fragment {

    View rootView = null;

    RadioService service;
    private TextView songName;
    private TextView artistName;
    private TextView djName;
    private ProgressBar songProgressBar;
    private Timer progressBarTimer;
    private ImageView djImage;
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

    HomePagerAdapter mainPagerAdapter;
    ViewPager viewPager;


    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ApiUtil.NPUPDATE) {
                ApiPacket packet = (ApiPacket) msg.obj;
                HomeFragment.this.updateNP(packet);
            }
        }
    };

    // FIX ME
    public void shareTrack() {
        String shareHeading = "Share track title.";

        String shareText = songName.getText() + " - " + artistName.getText();
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(i, shareHeading));

    }

    private String lastDjImg = "";

    private void updateNP(ApiPacket packet) {
        int progress = (int)(packet.cur - packet.start);
        songName.setText(packet.songName);
        artistName.setText(packet.artistName);
        djName.setText(packet.dj);
        songProgressBar.setMax((int)(packet.end - packet.start));
        songProgressBar.setProgress(progress);
        if (!lastDjImg.equals(packet.djimg)) {
            lastDjImg = packet.djimg;
            DJImageLoader imageLoader = new DJImageLoader();
            imageLoader.execute(packet);

        }
    }

    private class DJImageLoader extends AsyncTask<ApiPacket, Void, Void> {
        private Bitmap image;

        @Override
        protected Void doInBackground(ApiPacket... params) {
            ApiPacket pack = params[0];
            URL url;
            try {
                url = new URL("http://r-a-d.io" + pack.djimg);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                image = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
                image = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (image != null) {
                djImage.setImageBitmap(image);
                service.updateNotificationImage(image);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_layout, container, false);
        this.rootView = rootView;

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        songName = (TextView) getView().findViewById(R.id.main_SongName);
        artistName = (TextView) getView().findViewById(R.id.main_ArtistName);
        djName = (TextView) getView().findViewById(R.id.main_DjName);
        djImage = (ImageView) getView().findViewById(R.id.main_DjImage);
        songProgressBar = (ProgressBar) getView().findViewById(R.id.main_SongProgress);
        Intent servIntent = new Intent(getActivity(), RadioService.class);
        //bindService(servIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(servIntent);
        progressBarTimer = new Timer();
        progressBarTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                songProgressBar.setProgress(songProgressBar.getProgress() + 1);
            }

        }, 0, 1000);

    }
}
