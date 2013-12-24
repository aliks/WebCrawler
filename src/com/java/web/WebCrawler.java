package com.java.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
	
	private BufferedReader in;
	private Queue<String> urlQueue;
	private List<Integer> hashCodeList;
	
	public WebCrawler() {
		urlQueue = new LinkedList<String>();
		hashCodeList = new ArrayList<Integer>();
		urlQueue.add( "http://tutorials.jenkov.com/java-collections/queue.html" );
	}
	
	public void crawlPage() {
		URL url = null;
		String urlText;
		InputStreamReader stream;
		try {
			while (( urlText = urlQueue.poll()) != null) {
				url = new URL(urlText);
				System.out.println( "Connecting to URL: " + url + "..." );
				try {
					stream = new InputStreamReader(url.openStream());
				} catch (Exception e) { continue; }
					in = new BufferedReader(stream);
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					parse(inputLine);
				}
				tidyUp();
			}
			System.out.println( "\nQueue is empty" );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
			    	in.close();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			}
		}
	}
	
	private void parse(String s) {
		Pattern p = Pattern.compile("<a href=\"");
		Matcher m = p.matcher(s);
		while (m.find()) 
		{
			String link = s.substring( m.end() );
			//System.out.println( link );
			try {
				String url = link.substring(0, link.indexOf("\""));
				if (url.startsWith("http://") || url.startsWith("www")) {
					//System.out.println(url);
					if (!hashCodeList.contains(url.hashCode())) {
						urlQueue.add(url);
						hashCodeList.add(url.hashCode());
					}
				}
			} catch (Exception e) {
				// TO-D0 url: http://www.bookdepository.com returns HTTP response error: 500 need to handle this
				System.out.println( "end index :" + link.indexOf("\"") );
				System.out.println( "LINK => " + link );
				
				return;
			}
		}
	}
	
	private void tidyUp() {
		if (hashCodeList.size() > 100)
			hashCodeList.clear();
	}
}
