import java.io.File;
import java.io.PrintWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebScraper {
    public static void main(String[] args) throws Exception {
        final String url = "https://americanliterature.com/childrens-stories/the-three-little-pigs";

        try {
            final Document document = Jsoup.connect(url).get();
            
            String title = document.title();
            
            File dir = new File("stories/");
            dir.mkdirs();
            File file = new File(dir, title + ".txt");
            file.createNewFile();
            System.out.println(file.getAbsolutePath());

            PrintWriter pw = new PrintWriter(file);

            for (Element par : document.select("p,pre.childspoem")) {
                if (!par.text().equals("")) {
                    pw.println(par.text());
                }
            }

            pw.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
