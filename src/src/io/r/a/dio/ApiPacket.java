package io.r.a.dio;

import android.graphics.Bitmap;

import java.util.HashMap;

public class ApiPacket {
	public boolean online;
	public String np;
	public String list;
	public int kbps;
	public long start;
	public long end;
	public long cur;
	public String dj;
	public String djimg;
	public String djtext;
	public int thread;
	public Bitmap djImage;
	public String songName;
	public String artistName;

    public Tracks[] queue;
    public Tracks[] lastPlayed;

	public ApiPacket() {
		online = false;
		np = "unknown";
		list = "";
		kbps = 0;
		start = 0;
		end = 1;
		cur = 0;
		dj = "";
		djimg = "";
		djtext = "";
		thread = 0;
		songName = "";
		artistName = "";
        queue = null;
        lastPlayed = null;
	}

}
