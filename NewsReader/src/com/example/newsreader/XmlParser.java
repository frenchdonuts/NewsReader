package com.example.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class XmlParser {
	private static final String ns = null;
	private static final String TAG = "XmlParser";
	
	// Instantiate parser and start the parsing
	public List<Article> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			// Instantiate new xml parser
			XmlPullParser parser = Xml.newPullParser();
			// Not using any optional features
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			// Set input and encoding type(none)
			parser.setInput(in, null);
			// Start parsing process by going to first tag(<rss>)
			parser.nextTag();
			// Start reading xml feed.
			return readRss(parser);
		} finally {
			//Notice that this will run even after a return statement
			in.close();
		}
	}
	
	// Read <rss>...</rss> entries
	private List<Article> readRss(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "rss");
		List<Article> articles = null;
		
		while(parser.next() != XmlPullParser.END_TAG) {
			if(parser.getEventType() != XmlPullParser.START_TAG)
				continue;
			
			String name = parser.getName();
			if(name.equals("channel")) {
				articles = readChannel(parser);
			} else {
				skip(parser);
			}
		}
		if(articles == null) {
			Log.e(TAG, "readRss: List of articles is null.");
		}
		return articles;
	}
	
	// Read <channel>...</channel> entries
	private List<Article> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
		// See if current tag is a start tag. Throw exception if it isn't
		parser.require(XmlPullParser.START_TAG, ns, "channel");
		List<Article> articles = new ArrayList<Article>();
		
		// Loop until you hit an end tag (</feed>)
		while (parser.next() != XmlPullParser.END_TAG) {
			// If the current tag is a start tag, go to next tag
			if (parser.getEventType() != XmlPullParser.START_TAG)
				//continue - Go back and run condition in while loop
				continue;
			
			// Get current tag name
			String name = parser.getName();
			// If current tag is <item> read it using readItem
			if (name.equals("item")) {
				articles.add(readItem(parser));
			} else {
				skip(parser);
			}
		}
		return articles;
	}
	
	// Read <item>...</item> entries
	private Article readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
		// See if current tag is a <item>. Throw exception if it isn't
		parser.require(XmlPullParser.START_TAG, ns, "item");
		String title = null;
		String url = null;
		String description = null;
		
		// Loop until you reach </item>
		while(parser.next() != XmlPullParser.END_TAG) {
			// If current tag is <item>, go to next tag
			if(parser.getEventType() != XmlPullParser.START_TAG)
				continue;
			
			String name = parser.getName();
			if(name.equals("title")) {
				title = readTitle(parser);
			} else if(name.equals("link")) {
				url = readUrl(parser);
			} else if(name.equals("description")) {
				description = readDescription(parser);
			} else {
				skip(parser);
			}
		}		
		return new Article(title, url, description);
	}
	
	// Read <title>...</title> entries
	private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
		// Check if current tag is <title>. Throw exception otherwise
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		// Check if current tag is </title>. Throw exception otherwise
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}
	
	// Read <link>...</link> entries
	private String readUrl(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "link");
		String url = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "link");
		return url;
	}
	
	// Read <description>...</description> entries
	private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "description");
		String description = readText(parser).split("&lt", 2)[0];
		parser.require(XmlPullParser.END_TAG, ns, "description");
		return description;
	}
	
	// Read the actual text between tags
	private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}
	
	// Method for skipping unwanted tags. Simply consumes them, aka hit parser.next() til you hit
	// the unwanted tag's corresponding end tag
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		// Notice that if we are in this method, we have hit a START TAG exactly once. Hence depth = 1
		// relative to the tag we want to skip
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
		// By the time you exit the while loop, the parser is exactly on the end tag of the unwanted tag
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
