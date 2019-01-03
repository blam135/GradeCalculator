import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayCUSPData extends JFrame {

    ArrayList<AssignmentData> listOfAssessments = new ArrayList<>();
    ArrayList<JLabel> titleLabels = new ArrayList<>();
    ArrayList<JTextField> receivedTextFields = new ArrayList<>();
    ArrayList<JTextField> maxTextFields = new ArrayList<>();
    String unitOfStudy;
    Double examWeighting;
    JPanel panel;
    double[] returnList;

    public DisplayCUSPData() {

        if (getInitialData() == 1) {
            dispose();
            return;
        }
        int bottomRightX = 600;
        int bottomRightY = 600;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = (JPanel) getContentPane();
        panel.setLayout(null);

        int yPosition = 80;
        int initialXTextBoxPosition = 0;

        for (AssignmentData ad : listOfAssessments) {
            JLabel titleLabel = new JLabel(ad.getAssessmentTitle());
            titleLabel.setFont(titleLabel.getFont().deriveFont(16.0f));
            Dimension size = titleLabel.getPreferredSize();
            titleLabel.setBounds(30, yPosition, size.width, size.height);
            panel.add(titleLabel);
            yPosition += size.height + 10;

            int endXPosition = titleLabel.getX() + titleLabel.getWidth();

            if (endXPosition > initialXTextBoxPosition) {
                initialXTextBoxPosition = endXPosition;
            }

            titleLabels.add(titleLabel);
        }

        initialXTextBoxPosition += 30;
        int textBoxWidth = 40;
        int textBoxHeight = 25;

        JLabel receivedLabel = new JLabel("Received:");
        JLabel maxLabel = new JLabel("Max:");
        int receivedLabelwidth = (int) Math.round(receivedLabel.getPreferredSize().getWidth());
        int maxLabelWidth = (int) Math.round(maxLabel.getPreferredSize().getWidth());
        receivedLabel.setBounds(initialXTextBoxPosition, 60, receivedLabelwidth, 10);
        maxLabel.setBounds(initialXTextBoxPosition + textBoxWidth + 40, 60, receivedLabelwidth, 10);
        panel.add(receivedLabel);
        panel.add(maxLabel);

        for (int i = 0; i < titleLabels.size(); i++) {
            JTextField markReceived = new JTextField();
            JTextField maxMark = new JTextField();

            markReceived.setBounds(initialXTextBoxPosition, titleLabels.get(i).getY(), textBoxWidth, textBoxHeight);
            maxMark.setBounds(markReceived.getX() + markReceived.getWidth() + 40, titleLabels.get(i).getY(), textBoxWidth, textBoxHeight);
            panel.add(markReceived);
            panel.add(maxMark);

            maxTextFields.add(maxMark);
            receivedTextFields.add(markReceived);

            if (i == titleLabels.size() - 1) {
                bottomRightX = maxMark.getX() + maxMark.getWidth() + maxMark.getWidth();
                bottomRightY = maxMark.getY() + maxMark.getHeight() + 100;
            }
        }

        JLabel title = new JLabel(unitOfStudy.toUpperCase());
        title.setFont(new Font("", Font.BOLD, 32));
        int titleWidth = (int) Math.round(title.getPreferredSize().getWidth());
        int titleHeight = (int) Math.round(title.getPreferredSize().getHeight());
        int titleX = (int) Math.round(bottomRightX/2 - title.getPreferredSize().getWidth()/2);
        title.setBounds(titleX, 0 , titleWidth, titleHeight);
        panel.add(title);

        setSize(bottomRightX, bottomRightY);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(getWidth()/2 - ((int) Math.floor(calculateButton.getPreferredSize().getWidth())/2),
                getHeight() - 100 + 15,
                (int) Math.floor(calculateButton.getPreferredSize().getWidth()),
                (int) Math.floor(calculateButton.getPreferredSize().getHeight())
        );
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
        panel.add(calculateButton);

        setContentPane(panel);
    }

    public void calculate() {
        Double accumTotal = 0.00;
        Double max = 0.00;

        try{
            for (JTextField textfield : receivedTextFields) {
                accumTotal += Double.parseDouble(textfield.getText());
            }
            for (JTextField textfield : maxTextFields) {
                max += Double.parseDouble(textfield.getText());
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Must Enter correct numeric values", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (accumTotal > max) {
            accumTotal = max;
        }

        System.out.println("Your results: " + accumTotal + " " + max);

      /*
        This will return an array of corresponding grades where:
        index 0: Pass
        index 1: Credit
        index 2: Distinction
        index 3: High Distinction
        (in terms of what % you need to get in the exam)
        */
        returnList = new double[4];
        double totalMark = (accumTotal / max) * (100 - examWeighting);
        returnList[0] = ((50.0 - totalMark) / examWeighting) * 100;
        returnList[1] = ((65.0 - totalMark) / examWeighting) * 100;
        returnList[2] = ((75.0 - totalMark) / examWeighting) * 100;
        returnList[3] = ((85.0 - totalMark) / examWeighting) * 100;

        String outputText = "";
        String[] headers = {"PASS", "CREDIT", "DISTINCTION", "HIGH DISTINCTION"};
        for (int i = 0; i < returnList.length; i++) {
            outputText += headers[i] + ": ";
            if (returnList[i] < 0) {
                outputText += "You have already achieved this grade\n";
            } else if (returnList[i] > 100) {
                outputText += "Sorry you cannot achieve this grade\n";
            } else {
                outputText += String.format("%.2f", returnList[i]) + "%\n";
            }
        }

        Object[] options = {"Close", "Export to CSV"};
        int option = JOptionPane.showOptionDialog(null, outputText, "Percentage in Exam", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (option == 1) {
            // Export to CV
            exportToCSV();
        }

    }

    public int getInitialData() {
        String subject = "";
        while (subject.length() == 0) {
            subject = JOptionPane.showInputDialog("Type in a unit name");
            if (subject == null) {
                System.out.println("Exited Import from CUSP");
                return 1;
            } else if (subject.length() == 0) {
                JOptionPane.showMessageDialog(null, "A Subject cannot be empty!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        Document document;

        try {
            document = Jsoup.connect("https://cusp.sydney.edu.au/students/view-unit-page/alpha/" + subject).get();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Unable to connect to the server", "Error", JOptionPane.WARNING_MESSAGE);
            System.out.println("Unable to connect to the server");
            return 1;
        }

        Elements ele = document.select("div#assessment .t_no_b tbody tr .t_b tbody tr");
        ArrayList<AssignmentData> finalData = new ArrayList<AssignmentData>();

        if (ele.text().length() == 0) {
            JOptionPane.showMessageDialog(null, "Course code '" + subject + "' does not exist in CUSP", "Error", JOptionPane.WARNING_MESSAGE);
            return 1;
        }


        for (int i = 1; i < ele.size(); i++) {
            if (ele.get(i).select("td").get(0).text().equals("Grade Type")) {
                break;
            }
            AssignmentData newData = new AssignmentData(ele.get(i).select("td").get(1).text().replace("*", ""), 0, 1, Double.parseDouble(ele.get(i).select("td").get(3).text()));
            listOfAssessments.add(newData);
        }

        unitOfStudy = subject;

        boolean examFound = false;
        for (AssignmentData ad : listOfAssessments) {
            String thisExam = ad.getAssessmentTitle().toLowerCase();
            if (thisExam.equals("final exam") || thisExam.equals("final examination")) {
                examWeighting = ad.getWeighting();
                listOfAssessments.remove(ad);
                examFound = true;
                break;
            }
        }
        if (!examFound) {
            String thisExam = listOfAssessments.get(listOfAssessments.size() - 1).getAssessmentTitle().toLowerCase();
            if (thisExam.equals("exam")) {
                examWeighting = listOfAssessments.get(listOfAssessments.size() - 1).getWeighting();
                listOfAssessments.remove(listOfAssessments.size() - 1);
            } else {
                JOptionPane.showMessageDialog(null, "There is no exam for this particular unit", "Error", JOptionPane.WARNING_MESSAGE);
                return 1;
            }
        }

        System.out.println("Exam marks: " + String.format("%.2f", examWeighting));
        return 0;
    }

    public void exportToCSV() {

        String content = "Assessment Title,Mark Received,Max Mark,% Received,Weighting\n";

        for (int i = 0; i < titleLabels.size(); i++) {
            content += titleLabels.get(i).getText() + ",";
            double markReceived = Double.parseDouble(receivedTextFields.get(i).getText());
            double maxMark = Double.parseDouble(maxTextFields.get(i).getText());
            double this_weighting = listOfAssessments.get(i).getWeighting();
            content += String.format("%.2f", markReceived) + ",";
            content += String.format("%.2f", maxMark) + ",";
            content += String.format("%.2f", markReceived / maxMark * this_weighting) + ",";
            content += String.format("%.2f", this_weighting) + ",";
            content += "\n";
        }
        content += "\n";

        double[] examMarks;
        content += "PERCENTAGE IN EXAM:\nPASS,CREDIT,DISTINCTION,HIGH DISTINCTION\n";

        for (int i = 0; i < returnList.length; i++) {
            content += String.format("%.2f", returnList[i]) + ",";
        }

        content += "\n\nINTERNAL ASSESSMENT WORTH:\n";
        content += String.format("%.2f", (100.00 - examWeighting)) + "\n";

        String directoryPath = "";
        String filename = JOptionPane.showInputDialog(null, "Enter a filename");
        if (filename == null) {
            System.out.println("User cancelled Action Input Dialog");
            return;
        } else if (!filename.isEmpty()) {
            filename += ".csv";
        } else {
            filename += unitOfStudy + "Results.csv";
        }

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Save");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            directoryPath = selectedFile.getAbsolutePath();
        } else {
            System.out.println("User cancelled action");
            return;
        }

        try {
            File f = new File(directoryPath + "\\" + filename);
            FileWriter writer = new FileWriter(f);
            writer.write(content);
            writer.close();
            JOptionPane.showMessageDialog(null, "Export Done!");
            System.out.println("Exported to CSV");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Write to File, make sure only one GradeCalculator program is opened or\n" +
                    "if you're overwriting a file with the same name, make sure that file is closed", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.out.println("Failed to export to CSV");
        }
    }

}
