package com.thomasandrasek.crawlinator.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.thomasandrasek.crawlinator.App;
import com.thomasandrasek.crawlinator.graph.Graph;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {
    
    private String seedURL;

    private Graph webGraph;

    private boolean runCrawler;

    public Crawler(String seedURL) {
        this.seedURL = seedURL;

        this.runCrawler = true;
    }

    public void setSeedURL(String seedURL) {
        this.seedURL = seedURL;
    }

    public String getSeedURL() {
        return seedURL;
    }
    
    public void crawl() {
        Thread crawlThread = new Thread() {
            @Override
            public void run() {
                webGraph = new Graph();
                webGraph.addNode(seedURL);
                
                Set<String> visited = new HashSet<String>();
                ArrayList<String> toVisit = new ArrayList<String>();
                toVisit.add(seedURL);

                long visitedCount = 0;

                long totalCount = 0;

                while (toVisit.size() > 0) {
                    while (!runCrawler) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    visitedCount++;
                    App.window.getControlPanel().updateVisitedCount(visitedCount);

                    String currentURL = toVisit.remove(0);
                    App.window.getControlPanel().addVisitedURL(currentURL);
                    App.window.getControlPanel().scrollToBottom();

                    ArrayList<String> links = getLinks(currentURL);
                    for (String link : links) {
                        if (!visited.contains(link)) {
                            webGraph.addNode(link);
                            webGraph.addEdge(currentURL, link);

                            totalCount++;
                            App.window.getControlPanel().updateTotalCount(totalCount);

                            if (link.indexOf("#") != -1) {
                                link = link.substring(0, link.indexOf("#"));
                            }

                            if (!visited.contains(link) && link.endsWith(".pdf") == false && link.endsWith(".html") == false && link.endsWith(".htm") == false) {
                                toVisit.add(link);
                                visited.add(link);
                            }
                        }
                    }
                }

                System.out.println("Crawling finished.");
            }
        };
        
        crawlThread.start();
    }

    private String getBody(String html) {
        int start = html.indexOf("<body>");
        int end = html.indexOf("</body>");
        if (start == -1 || end == -1) {
            return "";
        }
        return html.substring(start + 6, end);
    }

    private ArrayList<String> getATags(Document doc) {
        ArrayList<String> links = new ArrayList<String>();
        for (org.jsoup.nodes.Element link : doc.select("a")) {
            links.add(link.attr("abs:href"));
        }
        return links;
    }

    private ArrayList<String> getLinks(String currentURL) {
        ArrayList<String> links = new ArrayList<String>();

        Document doc = null;
		try {
			doc = Jsoup.connect(currentURL).get();
		} catch (IOException e) {
			System.err.println("Invalid URL: " + currentURL);
		} catch (IllegalArgumentException e) {
            System.err.println("Invalid URL: " + currentURL);
        }

		if (doc == null) {
			System.err.println("Failed to get page!");
            return links;
		}

        ArrayList<String> aTags = getATags(doc);
        for (String link : aTags) {
            if (link.startsWith("http")) {
                links.add(link);
            }
        }

        return links;
    }

    public void stopCrawler() {
        runCrawler = false;
    }

    public void startCrawler() {
        runCrawler = true;
    }
}
