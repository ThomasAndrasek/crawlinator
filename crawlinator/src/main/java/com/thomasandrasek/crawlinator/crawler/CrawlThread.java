package com.thomasandrasek.crawlinator.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

import com.thomasandrasek.crawlinator.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CrawlThread extends Thread {
    
    private ArrayList<String> toVisit;
    private ArrayList<String> visited;
    private ArrayList<Document> pages;

    private boolean runThread;

    private ArrayList<Semaphore> semaphores;

    private boolean empty;

    public CrawlThread(ArrayList<Semaphore> semaphores, ArrayList<String> toVisit, ArrayList<String> visited, ArrayList<Document> pages) {
        super();

        this.runThread = true;
        this.toVisit = toVisit;
        this.visited = visited;
        this.pages = pages;

        this.semaphores = semaphores;
    }

    private Document getDocument(String currentURL) {
        Document doc = null;
		try {
			doc = Jsoup.connect(currentURL).get();
		} catch (IOException e) {
            if (App.displayOutput) {
                System.err.println("Invalid URL: " + currentURL);
            }
		} catch (IllegalArgumentException e) {
            if (App.displayOutput) {
                System.err.println("Invalid URL: " + currentURL);
            }
        }

		if (doc == null) {
			if (App.displayOutput) {
                System.err.println("Could Not Get Page: " + currentURL);
            }
            return null;
		}

        return doc;
    }

    @Override
    public void run() {

        while (runThread) {
            while (!App.runCrawler) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            try {
                semaphores.get(0).acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (toVisit.size() == 0) {
                semaphores.get(0).release();
                empty = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (toVisit.size() > 0) {
                empty = false;
                String url = toVisit.remove(0);
                if (App.displayOutput) {
                    System.out.println("URL: " + url);
                }
                semaphores.get(0).release();
                try {
                    semaphores.get(1).acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                visited.add(url);
                semaphores.get(1).release();

                Document doc = getDocument(url);
                if (doc != null) {
                    try {
                        semaphores.get(2).acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pages.add(doc);
                    semaphores.get(2).release();
                }   
            }

            semaphores.get(0).release();
            semaphores.get(1).release();
            semaphores.get(2).release();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Crawling finished.");
    }

    public void stopThread() {
        this.runThread = false;
    }

    public boolean isEmpty() {
        return empty;
    }
}
