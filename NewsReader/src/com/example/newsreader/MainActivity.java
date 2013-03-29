package com.example.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	private static final String URL = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	List<Article> articles;
        try {
			articles = loadNews();
			
		} catch (XmlPullParserException e) {
			 articles = new ArrayList<Article>();
			 articles.add(new Article("Did", "not", "work"));
			 Log.e("MainActivity", e.toString());
			 
		} catch (IOException e) {
			articles = new ArrayList<Article>();
			articles.add(new Article("Did", "not", "work"));
			Log.e("MainActivity", e.toString());
			 
		} catch (ExecutionException e) {
			articles = new ArrayList<Article>();
			articles.add(new Article("Did", "not", "work"));
			Log.e("MainActivity", e.toString());
			
		} catch (InterruptedException e) {
			articles = new ArrayList<Article>();
			articles.add(new Article("Did", "not", "work"));
			Log.e("MainActivity", e.toString());
			
		}
       
        ArrayAdapter<Article> adapter = new ArrayAdapter<Article>
        (this, android.R.layout.simple_list_item_1, articles);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private class DownloadUrlTask extends AsyncTask<String, Void, InputStream> {

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
        
		@Override
		protected InputStream doInBackground(String... urls) {
			try {
				return downloadUrl(urls[0]);
			}  catch(IOException e) {
				Log.e("MainActivity", "doInBackground" + e.toString());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(InputStream in) {
			
		}
    	
    }
    
    public List<Article> loadNews() throws XmlPullParserException, IOException, InterruptedException, ExecutionException {
    	DownloadUrlTask downloadRssTask = new DownloadUrlTask();
    	XmlParser xmlParser = new XmlParser();
    	
    	try {
    		downloadRssTask.execute(URL);
    		return xmlParser.parse(downloadRssTask.get());
    	} finally {
    		if(downloadRssTask.get() != null)
    			downloadRssTask.get().close();
    	}
    }
}
