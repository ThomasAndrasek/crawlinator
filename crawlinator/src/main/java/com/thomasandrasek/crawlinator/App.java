package com.thomasandrasek.crawlinator;

import java.io.File;

import com.thomasandrasek.crawlinator.crawler.Crawler;
import com.thomasandrasek.crawlinator.crawler.LinkChecker;
import com.thomasandrasek.crawlinator.gui.Window;

/**
 * Hello world!
 *
 */
public class App 
{
	public static Crawler crawler;

	public static Window window;

	public static boolean runCrawler = false;

	public static boolean displayOutput = true;

	public static boolean condenseOutput = false;

	public static File dataFile = null;
    public static void main(String[] args) {
		LinkChecker.loadExtensions();
		
		crawler = new Crawler("https://www.thomasandrasek.com/");

		window = new Window();
	}
}
