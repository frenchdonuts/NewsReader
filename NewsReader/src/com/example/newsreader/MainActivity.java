package com.example.newsreader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	private static final String URL = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DownloadNewsTask dlNews = new DownloadNewsTask();
    	List<Article> articles;
        try {
			dlNews.execute(URL);
			articles = dlNews.get();
			
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
}
