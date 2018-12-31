import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class Scraper {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a subject from CUSP:");
        String subject = scan.nextLine();
        System.out.println("Chosen subject: " + subject);
        Document document;
        try {
            document = Jsoup.connect("https://cusp.sydney.edu.au/students/view-unit-page/alpha/" + subject).get();
        } catch (IOException ioe) {
            System.out.println("Unable to connect to the server");
            return;
        }

        Elements ele = document.select("div#assessment .t_no_b tbody tr .t_b tbody tr");
        ArrayList<AssignmentData> finalData = new ArrayList<AssignmentData>();

        if (ele.text().length() == 0) {
            System.out.println("Course code does not exist in CUSP");
            return;
        }

        for (int i = 1; i < ele.size(); i++) {
            if (ele.get(i).select("td").get(0).text().equals("Grade Type")) {
                break;
            }
            // This part to edit later for GUI Use. Assumes perfect correct use
            System.out.println("What did you get for the assessment: " + ele.get(i).select("td").get(1).text().replace("*", "") + "?");
            Double markReceived = Double.parseDouble(scan.nextLine());
            System.out.println("What was it out of?");
            Double maxMark = Double.parseDouble(scan.nextLine());
            // -------------------------------------
            AssignmentData newData = new AssignmentData(ele.get(i).select("td").get(1).text().replace("*", ""), markReceived, maxMark, Double.parseDouble(ele.get(i).select("td").get(3).text()));
            finalData.add(newData);
        }
        finalData.remove(finalData.size() - 1);

        for (AssignmentData ad : finalData) {
            System.out.println("For " + ad.getAssessmentTitle() + ", you scored " + ad.getMarkReceived() + " out of " + ad.getMaxMark() + " and scored a percentage of " + ad.getPercentReceived() + "out of " + ad.getWeighting());
        }
        // Remove the last element

    }
}
