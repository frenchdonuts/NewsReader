package com.example.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadNewsTask extends AsyncTask<String, Void, List<Article>>{
	@Override
	protected List<Article> doInBackground(String... urls) {
		InputStream in = null;
		try {
			in = downloadUrl(urls[0]);
			XmlParser parser = new XmlParser();
			// Parse and return
			return parser.parse(in);
		} catch (XmlPullParserException e) {
			Log.e("DownloadNewsTask", e.toString());
		} catch (IOException e) {
			Log.e("DownlaodNewsTask", e.toString());
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					Log.e("DownloadNewsTask", "Could not close inputstream: " + e.toString());
				}
		}
		return null;
	}
	
    private InputStream downloadUrl(String urlString) throws IOException {
    	URL url = new URL(urlString);
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    	conn.setReadTimeout(10000 /* milliseconds */);
    	conn.setConnectTimeout(15000);
    	conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	// Starts the query
    	conn.connect();
    	return conn.getInputStream();
    }

}
