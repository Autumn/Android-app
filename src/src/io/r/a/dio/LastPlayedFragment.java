package io.r.a.dio;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by aki on 18/05/13.
 */

public class LastPlayedFragment extends Fragment {

    View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.last_played_fragment, container, false);
        this.rootView = rootView;
        return rootView;
    }

    private void updateLastPlayed(ApiPacket packet) {
    }

}
