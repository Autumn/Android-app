package io.r.a.dio;

/**
 * Created by aki on 18/06/13.
 */
public class Tracks {
    public String songName;
    public String artistName;
    public boolean isRequest;
    public Tracks(String songName, String artistName, boolean isRequest) {
        this.songName = songName;
        this.artistName = artistName;
        this.isRequest = isRequest;
    }
}
