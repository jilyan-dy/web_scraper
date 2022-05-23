import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebScraper {

    public static ArrayList<String> visited = new ArrayList<String>();
    public static void main(String[] args) throws Exception {
        final String url = "https://americanliterature.com/short-stories-for-children";
        System.out.println("Begin crawling from " + url);
        crawl(1, url);

        System.out.println("Finished Crawling, will proceed with scraping");

        for (int i = 1; i < visited.size(); ++i) {
            scrape(visited.get(i));
        }
    }

    private static void crawl(int level, String url) {
        if (level <= 2) {
            Document doc = request(level, url);

            if (doc != null) {
                for (Element link: doc.select("figure.imgcs a[href]")) {
                    String next_link = link.absUrl("href");
                    if (visited.contains(next_link) == false) {
                        crawl(level + 1, next_link);
                    }
                }
            }
        }
    }

    private static Document request(int level, String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
                visited.add(url);

                return doc;
            }
            System.out.println("Connection Failed");
            return null;
            
        } catch (IOException e) {
            System.out.println("Request Failed");
            return null;
        }
        
    }

    private static void scrape(String url) {
        try {
            final Document document = Jsoup.connect(url).get();
            
            String title = document.title();
            
            File dir = new File("stories/");
            dir.mkdirs();
            File file = new File(dir, title + ".txt");
            file.createNewFile();
            System.out.println(file.getAbsolutePath());

            PrintWriter pw = new PrintWriter(file);

            for (Element par : document.select("p,pre")) {
                if (!par.text().equals("")) {
                    pw.println(par.text());
                }
            }

            pw.close();
        }
        catch (Exception ex) {
            System.out.println("Scrape Failed");
        }
    }
}
