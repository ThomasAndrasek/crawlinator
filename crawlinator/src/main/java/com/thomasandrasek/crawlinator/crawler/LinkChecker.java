package com.thomasandrasek.crawlinator.crawler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LinkChecker {
    
    public static String baseLink = "https://thomasandrasek.com";

    public static boolean matchDomain = false;
    public static boolean matchSubdomain = false;
    public static boolean linkToCurrentPage = false;

    public static boolean matchExtension = false;
    public static ArrayList<String> extensions = new ArrayList<String>();

    public static boolean useRegex = false;
    public static String regex = "";

    private static Set<String> validExtensions;

    private static ArrayList<String> split(String str, String delim) {
        ArrayList<String> result = new ArrayList<String>();
        int pos = 0;
        int lastPos = 0;
        while ((pos = str.indexOf(delim, lastPos)) != -1) {
            result.add(str.substring(lastPos, pos));
            lastPos = pos + delim.length();
        }
        result.add(str.substring(lastPos));

        return result;
    }

    public static void loadExtensions() {
        File file = new File("./src/res/domains.txt");
        
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (Exception e) {
            System.err.println("Could not load extensions.");
            return;
        }

        validExtensions = new HashSet<>();

        String line = "";
        int c = -1;
        try {
            c = reader.read();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while (c != -1) {
            if (c == '\n') {
                validExtensions.add(line);
                line = "";
            } else {
                line += (char) c;
            }
            try {
                c = reader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidExtension(String extension) {
        return validExtensions.contains(extension);
    }

    private static boolean doesLinkMatchDomain(String linkToMatch) {
        String baseDomain = baseLink.substring(baseLink.indexOf("//") + 2);
        if (baseDomain.indexOf("/") == -1) {
            baseDomain += "/";
            baseLink += "/";
        }
        baseDomain = baseDomain.substring(0, baseDomain.indexOf("/"));

        int indexOfDoubleSlash = linkToMatch.indexOf("//");
        if (indexOfDoubleSlash == -1) {
            return false;
        }
        String linkDomain = linkToMatch.substring(indexOfDoubleSlash + 2);
        int indexOfSingleSlash = linkDomain.indexOf("/");
        if (indexOfSingleSlash == -1) {
            return false;
        }

        linkDomain = linkDomain.substring(0, linkDomain.indexOf("/"));
        if (baseDomain.equals(linkDomain)) {
            return true;
        }

        return false;
    }


    private static boolean doesBaseLinkMatchDomain(String linkToMatch) {
        String baseDomain = baseLink.substring(baseLink.indexOf("//") + 2);
        if (baseDomain.indexOf("/") == -1) {
            baseDomain += "/";
            baseLink += "/";
        }
        baseDomain = baseDomain.substring(0, baseDomain.indexOf("/"));
        ArrayList<String> baseDomainParts = split(baseDomain, ".");
        String subBaseDomain = baseDomainParts.get(baseDomainParts.size() - 2) + "." + baseDomainParts.get(baseDomainParts.size() - 1);

        int indexOfDoubleSlash = linkToMatch.indexOf("//");
        if (indexOfDoubleSlash == -1) {
            return false;
        }
        String linkDomain = linkToMatch.substring(indexOfDoubleSlash + 2);
        int indexOfSingleSlash = linkDomain.indexOf("/");
        if (indexOfSingleSlash == -1) {
            return false;
        }
        linkDomain = linkDomain.substring(0, linkDomain.indexOf("/"));

        ArrayList<String> linkDomainParts = split(linkDomain, ".");
        if (linkDomainParts.size() < 2) {
            System.out.println(linkDomain);
            return false;
        }
        String subLinkDomain = linkDomainParts.get(linkDomainParts.size() - 2) + "." + linkDomainParts.get(linkDomainParts.size() - 1);

        if (subBaseDomain.equals(subLinkDomain)) {
            return true;
        }

        return false;

    }

    public static boolean doesLinkLinkToCurrentPage(String currentPage, String link) {
        if (link.indexOf("#") != -1) {
            link = link.substring(0, link.indexOf("#"));

            if (link.equals(currentPage)) {
                return true;
            }
        }

        return false;
    }

    public static boolean doesLinkMatchRegex(String linkToMatch) {
        if (useRegex) {
            if (linkToMatch.matches(regex)) {
                return true;
            }
        }

        return false;
    }

    public static boolean doesExtensionMatch(String link) {
        String extension = "";
        link = link.substring(link.indexOf("//") + 2);
        if (link.indexOf("www") != -1) {
            link = link.substring(link.indexOf("www") + 4);
        }
        if (link.indexOf("/") != -1) {
            link = link.substring(0, link.indexOf("/"));
        }
        if (link.indexOf(".") != -1) {
            extension = link.substring(link.indexOf(".") + 1).toUpperCase();
        }

        if (extension.isEmpty()) {
            return false;
        }

        return extensions.contains(extension);
    }

    public static boolean doesLinkMatchSettings(String currentLink, String link) {
        int doubleSlashIndex = link.indexOf("//");
        int finalSlash = link.lastIndexOf("/");
        if (finalSlash > doubleSlashIndex+1) {
            int finalDot = link.lastIndexOf(".");
            if (finalDot > finalSlash && !(link.endsWith("html") || link.endsWith("htm"))) {
                return false;
            }
        }

        if (!linkToCurrentPage) {
            if (doesLinkLinkToCurrentPage(currentLink, link)) {
                return false;
            }
        }

        if (useRegex) {
            if (!doesLinkMatchRegex(link)) {
                return false;
            }
        }

        if (matchDomain) {
            if (!doesLinkMatchDomain(link)) {
                return false;
            }
        }

        if (matchSubdomain) {
            if (!doesBaseLinkMatchDomain(link)) {
                return false;
            }
        }

        if (matchExtension) {
            if (!doesExtensionMatch(link)) {
                return false;
            }
        }

        return true;
    }

}
