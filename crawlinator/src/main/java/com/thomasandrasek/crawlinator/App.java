package com.thomasandrasek.crawlinator;

import java.io.IOException;

import com.thomasandrasek.crawlinator.crawler.Crawler;
import com.thomasandrasek.crawlinator.graph.Graph;
import com.thomasandrasek.crawlinator.gui.Window;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App 
{
	public static Crawler crawler;

	public static Window window;
    public static void main(String[] args) {
		
		crawler = new Crawler("https://www.thomasandrasek.com/");

		window = new Window();
	}
}
