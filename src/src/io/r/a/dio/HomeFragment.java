package io.r.a.dio;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    private TextView songNameView;
    private TextView artistNameView;
    private TextView djNameView;
    private ProgressBar songProgressBar;
    private Timer progressBarTimer;
    private ImageView djImageView;

    public String songName;
    public String artistName;
    public String djName;
    public String djImageUrl;
    public long songStart;
    public long songCur;
    public long songEnd;


    // FIX ME
    public void shareTrack() {
        String shareHeading = "Share track title.";

        String shareText = songNameView.getText() + " - " + artistNameView.getText();
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(i, shareHeading));

    }

    private String lastDjImg = "";

    @Override
    public void onResume() {
        super.onResume();
        int progress = (int)(songCur - songStart);
        songNameView.setText(songName);
        artistNameView.setText(artistName);
        djNameView.setText(djName);
        songProgressBar.setMax((int) (songEnd - songStart));
        songProgressBar.setProgress(progress);
        if (!lastDjImg.equals(djImageUrl)) {
            lastDjImg = djImageUrl;
            DJImageLoader imageLoader = new DJImageLoader();
            imageLoader.execute(djImageUrl);
        }
    }

    public void updateNP(ApiPacket packet) {
        int progress = (int)(packet.cur - packet.start);
        View v = getView();
        songNameView.setText(packet.songName);
        artistNameView.setText(packet.artistName);
        djNameView.setText(packet.dj);
        songProgressBar.setMax((int) (packet.end - packet.start));
        songProgressBar.setProgress(progress);
        if (!lastDjImg.equals(packet.djimg)) {
            lastDjImg = packet.djimg;
            DJImageLoader imageLoader = new DJImageLoader();
            imageLoader.execute(djImageUrl);
        }
    }

    private class DJImageLoader extends AsyncTask<String, Void, Void> {
        private Bitmap image;

        @Override
        protected Void doInBackground(String... params) {
            String imageUrl = params[0];
            URL url;
            try {
                url = new URL("http://r-a-d.io" + imageUrl);
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
                djImageView.setImageBitmap(image);
                MainActivity activity = (MainActivity) getActivity();
                activity.service.updateNotificationImage(image);
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
        songNameView = (TextView) this.getView().findViewById(R.id.main_SongName);
        artistNameView = (TextView) getView().findViewById(R.id.main_ArtistName);
        djNameView = (TextView) getView().findViewById(R.id.main_DjName);
        djImageView = (ImageView) getView().findViewById(R.id.main_DjImage);
        songProgressBar = (ProgressBar) getView().findViewById(R.id.main_SongProgress);
        progressBarTimer = new Timer();
        progressBarTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                songProgressBar.setProgress(songProgressBar.getProgress() + 1);
            }

        }, 0, 1000);

    }
}
