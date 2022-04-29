package com.thomasandrasek.crawlinator.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

import com.thomasandrasek.crawlinator.App;

import org.jsoup.nodes.Document;

public class Crawler {
    
    private String seedURL;

    private int threadCount;

    private ArrayList<String> output;

    private File autoSaveFile;

    public Crawler(String seedURL) {
        this.seedURL = seedURL;

        this.threadCount = 1;

        this.autoSaveFile = null;

        output = new ArrayList<String>();
    }

    public void setAutoSaveFile(File autoSaveFile) {
        this.autoSaveFile = autoSaveFile;
    }

    public void setSeedURL(String seedURL) {
        this.seedURL = seedURL;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getSeedURL() {
        return seedURL;
    }

    private static void loadData(File dataFile, Set<String> visited, Set<String> actuallyVisited, ArrayList<String> toVisit) {
        FileReader fr = null;
        try {
            fr = new FileReader(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (fr == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        int c;
        try {
            while ((c = fr.read()) != -1) {
                if (c == '\n') {
                    String line = sb.toString();
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        actuallyVisited.add(parts[0]);
                        visited.add(parts[0]);

                        if (parts[1].indexOf("#") != -1) {
                            parts[1] = parts[1].substring(0, parts[1].indexOf("#"));
                        }

                        visited.add(parts[1]);
                    }
                    sb = new StringBuilder();
                } else {
                    sb.append((char) c);
                }
            }

            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String url : visited) {
            if (!actuallyVisited.contains(url)) {
                toVisit.add(url);
            }
        }
    }

    public void crawl() {
        Thread crawlThread = new Thread() {
            @Override
            public void run() {
                Set<String> visited = new HashSet<String>();

                ArrayList<CrawlThread> threads = new ArrayList<CrawlThread>();
                ArrayList<ArrayList<Semaphore>> semaphores = new ArrayList<ArrayList<Semaphore>>();
                ArrayList<ArrayList<String>> toVisit = new ArrayList<ArrayList<String>>();
                ArrayList<ArrayList<String>> visitedLinks = new ArrayList<ArrayList<String>>();
                ArrayList<ArrayList<Document>> documents = new ArrayList<ArrayList<Document>>();
                for (int i = 0; i < threadCount; i++) {
                    semaphores.add(new ArrayList<Semaphore>());
                    semaphores.get(i).add(new Semaphore(1));
                    semaphores.get(i).add(new Semaphore(1));
                    semaphores.get(i).add(new Semaphore(1));
                    toVisit.add(new ArrayList<String>());
                    visitedLinks.add(new ArrayList<String>());
                    documents.add(new ArrayList<Document>());
                    CrawlThread thread = new CrawlThread(semaphores.get(i), toVisit.get(i), visitedLinks.get(i), documents.get(i));
                    threads.add(thread);
                }

                ArrayList<ArrayList<String>> toVisitTemp = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < threadCount; i++) {
                    toVisitTemp.add(new ArrayList<String>());
                }

                Set<String> actuallyVisitedLinks = new HashSet<String>();

                if (App.dataFile == null) {
                    toVisit.get(0).add(seedURL);
                } else {
                    ArrayList<String> tempLinks = new ArrayList<String>();
                    loadData(App.dataFile, visited, actuallyVisitedLinks, tempLinks);

                    int i = 0;
                    for (String link : tempLinks) {
                        toVisit.get(i % threadCount).add(link);
                        i++;
                    }
                }
                

                for (int i = 0; i < threadCount; i++) {
                    threads.get(i).start();
                }

                long visitedCount = actuallyVisitedLinks.size();

                long totalCount = visited.size();

                int saveCount = 1000;

                while (true) {
                    while (!App.runCrawler) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    boolean allEmpty = true;
                    for (CrawlThread thread : threads) {
                        if (!thread.isEmpty()) {
                            allEmpty = false;
                        }
                    }

                    if (allEmpty) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayList<String> links = new ArrayList<String>();

                    ArrayList<String> urls = new ArrayList<String>();
                    int z = 0;
                    for (int i = 0; i < threads.size(); i++) {
                        try {
                            semaphores.get(i).get(2).acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            semaphores.get(i).get(1).acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while (documents.get(i).size() > 0) {
                            Document doc = documents.get(i).remove(0);
                            if (doc != null) {
                                if (visitedLinks.get(i).size() > 0) {
                                    String justVisited = visitedLinks.get(i).remove(0);
                                    if (!actuallyVisitedLinks.contains(justVisited)) {
                                        actuallyVisitedLinks.add(justVisited);
                                        urls.add(justVisited);
                                        links.addAll(getLinks(doc, urls.get(z)));
                                        links.add("GAMEREPICSWAGKEY");
                                        z++;
                                    }
                                }
                            }
                        }
                        semaphores.get(i).get(1).release();
                        semaphores.get(i).get(2).release();
                    }

                    if (links.size() > 0) {
                        if (App.displayOutput) {
                            System.out.println("Found " + links.size() + " links");
                        }
                    }

                    visitedCount += urls.size();

                    App.window.getControlPanel().updateVisitedCount(visitedCount);

                    int j = 0;
                    if (urls.size() > 0) {
                        output.add(urls.get(j));
                    }
                    for (int i = 0; i < links.size(); i++) {
                        if (links.get(i).equals("GAMEREPICSWAGKEY")) {
                            j++;
                            output.add("GAMEREPICSWAGKEY");
                            if (urls.size() > j) {
                                output.add(urls.get(j));
                            }
                            continue;
                        }
                        output.add(links.get(i));
                        totalCount++;

                        if (!visited.contains(links.get(i))) {  
                            if (links.get(i).indexOf("#") != -1) {
                                links.set(i, links.get(i).substring(0, links.get(i).indexOf("#")));
                            }

                            if (!visited.contains(links.get(i))) {
                                toVisitTemp.get((i + j) % threadCount).add(links.get(i));
                                visited.add(links.get(i));
                            }
                        }
                    }

                    App.window.getControlPanel().updateTotalCount(totalCount);

                    long toVisitCount = 0;
                    for (int i = 0; i < toVisitTemp.size(); i++) {
                        try {
                            semaphores.get(i).get(0).acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        toVisit.get(i).addAll(toVisitTemp.get(i));
                        toVisitCount += toVisit.get(i).size();
                        semaphores.get(i).get(0).release();
                    }
                    App.window.getControlPanel().updateToVisitCount(toVisitCount);

                    for (ArrayList<String> temp : toVisitTemp) {
                        temp.clear();
                    }

                    // App.window.getControlPanel().updateBackUpCount(unVisitedLinks.size());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (allEmpty) {
                        System.out.println("All threads are empty");
                        System.out.println("Visits: " + visited.size());
                        System.out.println("Actual Visits: " + actuallyVisitedLinks.size());
                        ArrayList<ArrayList<String>> toVisitTemp2 = new ArrayList<ArrayList<String>>();

                        for (int i = 0; i < threadCount; i++) {
                            toVisitTemp2.add(new ArrayList<String>());
                        }

                        int k = 0;
                        visited.removeAll(actuallyVisitedLinks);

                        for (String url : visited) {
                            if (url.indexOf("#") != -1) {
                                url = url.substring(0, url.indexOf("#"));
                            }

                            toVisitTemp2.get(k % threadCount).add(url);
                            k++;
                        }

                        for (int i = 0; i < threadCount; i++) {
                            try {
                                semaphores.get(i).get(0).acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            toVisit.get(i).addAll(toVisitTemp2.get(i));
                            semaphores.get(i).get(0).release();
                        }

                        visited.clear();

                        visited.addAll(actuallyVisitedLinks);
                    }

                    if (autoSaveFile != null) {
                        if (actuallyVisitedLinks.size() > saveCount) {
                            System.out.println("Saving...");
                            saveCount += 1000;

                            saveCrawledData(autoSaveFile);
                            output.clear();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Saved");
                        }
                    }
                }
            }
        };
        
        crawlThread.start();
    }

    private ArrayList<String> getATags(Document doc) {
        ArrayList<String> links = new ArrayList<String>();
        for (org.jsoup.nodes.Element link : doc.select("a")) {
            links.add(link.attr("abs:href"));
        }
        return links;
    }

    private ArrayList<String> getLinks(Document document, String currentURL) {
        ArrayList<String> links = new ArrayList<String>();

        ArrayList<String> aTags = getATags(document);
        for (String link : aTags) {
            if (link.startsWith("http")) {
                if (LinkChecker.doesLinkMatchSettings(currentURL, link)) {
                    links.add(link);
                }
            }
        }

        return links;
    }

    public void stopCrawler() {
        App.runCrawler = false;
    }

    public void startCrawler() {
        App.runCrawler = true;
    }

    public String encodeString(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void saveCrawledData(File file) {
        try {
            FileWriter fw = new FileWriter(file, true);
            long size = 0;
            if (App.condenseOutput) {
                fw.write(OutputChecker.check(output.get(0)));
                for (int i = 1; i < output.size(); i++) {
                    if (output.get(i) == null) {
                        continue;
                    }
                    if (output.get(i).equals("GAMEREPICSWAGKEY")) {
                        fw.write("\n");
                        i++;
                        if (i < output.size()) {
                            fw.write(OutputChecker.check(output.get(i)));
                        }
                    }
                    else {
                        fw.write(";" + OutputChecker.check(output.get(i)));
                    }
                    size++;
                }
            }
            else {
                int baseURLIndex = 0;
                for (int i = 1; i < output.size(); i++) {
                    if (output.get(i) == null) {
                        continue;
                    }
                    if (output.get(i).equals("GAMEREPICSWAGKEY")) {
                        baseURLIndex = i + 1;
                        continue;
                    }
                    fw.write(OutputChecker.check(output.get(baseURLIndex)) + ";" + OutputChecker.check(output.get(i)) + "\n");
                    size++;
                }
            }
            fw.close();
            System.out.println("Total: " + size);
            // webGraph.saveGraph(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
