package com.thomasandrasek.thecrawlinator;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thomasandrasek.thecrawlinator.graph.Graph;
import com.thomasandrasek.thecrawlinator.gui.Window;

public class App {

	public static void main(String[] args) {
		
		new Window();
		
//		Document doc = null;
//		try {
//			doc = Jsoup.connect(args[0]).get();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (doc == null) {
//			System.out.println("Failed to get page!");
//			return;
//		}
//		System.out.println(doc.title());
//		Elements newsHeadlines = doc.select("#mp-itn b a");
//		for (Element headline : newsHeadlines) {
//		  System.out.printf("%s\n\t%s", 
//		    headline.attr("title"), headline.absUrl("href"));
//		}
//		
//		System.out.println(doc.html());
		
		Graph g = new Graph();
		
		g.addEdge("a", "a");
		g.addEdge("a", "b");
		g.addEdge("a", "h");
		g.addEdge("a", "c");
		g.addEdge("a", "c");
		g.addEdge("a", "t");
		g.addEdge("a", "s");
		g.addEdge("a", "z");
		g.addEdge("a", "a");
		g.addEdge("a", "r");
	}
	
}
