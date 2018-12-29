package source;

/*
1. Initialize the object
2. Set the bounds
3. Add to panel
*/

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class GradeCalculator extends JFrame {

    // GUI and data Components for the JFrame
    ArrayList<AssignmentData> data = new ArrayList<AssignmentData>();
    JPanel panel; 
    JLabel uosLabel;
    JTextField uosTextField;
    JLabel intCompLabel;
    JTextField intCompTextField;
    JTable tableOfAssessment;
    DefaultTableModel modelAssessment;
    JButton calculateButton; 
    JLabel accumulatedPercentage; 
    JScrollPane scrollPaneAssessment;
    JLabel maxAccumulatedPercentage;
    JLabel assTitleInputLabel;
    JTextField assTitleInputText;
    JLabel markReceivedLabel;
    JTextField assInputText;
    JLabel maxMarkLabel;
    JTextField maxMarkInput;
    JLabel percentWorthLabel;
    JTextField percentWorthInput;
    JButton addToListButton;
    
    // Constructor
    public GradeCalculator() {
        // Window Setup
        setSize(750, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Grade Calculator");
        setResizable(false);
        
        // Panel Setup and variable Setups
        panel = new JPanel(null);
        int tableXPosition = 12;
        int buttonWidth = 50;
        int buttonHeight = 20;
        int inputPositionX = 575;
        int inputLabels = 102;
        int scrollPaneX = 12;
        int scrollPaneY = 100;
        int scrollPaneWidth = 550;
        int scrollPaneHeight = 300;

        // UOS Title Label and Textfield Initialization
        uosLabel = new JLabel("UOS: ");
        uosLabel.setBounds(tableXPosition, 15, 50, 10);
        panel.add(uosLabel);
        uosTextField = new JTextField();
        uosTextField.setBounds(tableXPosition + 38, 10, 100, 20);
        panel.add(uosTextField);

        // Internal Composition Label and Textfield Initialization
        intCompLabel = new JLabel("Internal Composition: ");
        intCompLabel.setBounds(tableXPosition, 50, 125, 15);
        panel.add(intCompLabel);
        intCompTextField = new JTextField();
        intCompTextField.setBounds(tableXPosition + 130, 50, 20, 20);
        panel.add(intCompTextField);

        // Table of Assessment Display
        tableOfAssessment = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the cell non editable 
            }
        };
        scrollPaneAssessment = new JScrollPane(tableOfAssessment);
        scrollPaneAssessment.setBounds(scrollPaneX, scrollPaneY, scrollPaneWidth, scrollPaneHeight);
        panel.add(scrollPaneAssessment);
        Object[] colNames = {"Assessment Title", "Mark Received", "Max Mark", "% Received", "Weighting"};        
        modelAssessment = new DefaultTableModel();
        modelAssessment.setColumnIdentifiers(colNames);
        tableOfAssessment.setModel(modelAssessment);
        
        // Accumulated and Max Percentage Accumulated Labels
        int totalXPosition = inputPositionX - 300;
        int totalHeight = 15;
        accumulatedPercentage = new JLabel("Accumulated Percentage: 0%");
        accumulatedPercentage.setBounds(totalXPosition, 15, 200, totalHeight);
        panel.add(accumulatedPercentage);
        // -----
        maxAccumulatedPercentage = new JLabel("Max Percentage Accumulated: 0%");
        maxAccumulatedPercentage.setBounds(totalXPosition, 45, 500, totalHeight);
        panel.add(maxAccumulatedPercentage);

        // Calculate Button
        calculateButton = new JButton("Calculate");
        calculateButton.setBounds(tableXPosition, scrollPaneY + scrollPaneHeight, buttonWidth + 50, buttonHeight);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
                exportToCSV();
            }
        });
        panel.add(calculateButton);

        // Reset Button 
        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(tableXPosition + 478, scrollPaneY + scrollPaneHeight, buttonWidth + 20, buttonHeight);
        resetButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        panel.add(resetButton);

        // Title Input 
        assTitleInputLabel = new JLabel("Assessment Title:");
        assTitleInputLabel.setBounds(inputPositionX, 100, inputLabels, 10);        
        assTitleInputText = new JTextField();
        assTitleInputText.setBounds(inputPositionX, 120, 120, 20);
        panel.add(assTitleInputLabel);
        panel.add(assTitleInputText);

        // Mark Received Input 
        markReceivedLabel = new JLabel("Mark Received:");
        markReceivedLabel.setBounds(inputPositionX, 150, inputLabels, 10);        
        assInputText = new JTextField();
        assInputText.setBounds(inputPositionX, 170, 120, 20);
        panel.add(markReceivedLabel);
        panel.add(assInputText);

        // Max Mark Input 
        maxMarkLabel = new JLabel("Max Mark:");
        maxMarkLabel.setBounds(inputPositionX, 200, inputLabels, 10);        
        maxMarkInput = new JTextField();
        maxMarkInput.setBounds(inputPositionX, 220, 120, 20);
        panel.add(maxMarkLabel);
        panel.add(maxMarkInput);

        // Weighting Input 
        percentWorthLabel = new JLabel("Weighting:");
        percentWorthLabel.setBounds(inputPositionX, 250, inputLabels, 15);        
        percentWorthInput = new JTextField();
        percentWorthInput.setBounds(inputPositionX, 270, 120, 20);
        panel.add(percentWorthLabel);
        panel.add(percentWorthInput);

        // Add Button 
        addToListButton = new JButton("ADD");
        addToListButton.setBounds(inputPositionX + 30, 310, 60, 20);
        addToListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToList();
            }
        });
        panel.add(addToListButton);

        setContentPane(panel);
    }

    // Resets inputted data
    public void reset() {
        data.clear();
        while (modelAssessment.getRowCount() > 0) {
            modelAssessment.removeRow(0);
        }
        System.out.println("The table has been reset");
        accumulatedPercentage.setText("Accumulated Percentage: 0%");
        maxAccumulatedPercentage.setText("Max Percentage Accumulated: 0%");
    }

    // Function for the calculate button 
    public void calculate() {
        if (data.size() == 0) {
            JOptionPane.showMessageDialog(null, "Please add data", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (intCompTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please cap your marks", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double[] listOfMark = calculateGrades();
        String outputText = uosTextField.getText() + " EXAM: \n";

        if (listOfMark[0] < 0) {
            outputText += "PASS: You have already passed the course \n";
        } else if (listOfMark[0] > 100) {
            outputText += "Sorry you have failed the course \n";
            JOptionPane.showMessageDialog(null, outputText, "Oops", JOptionPane.PLAIN_MESSAGE); 
            return;
        } else {
            outputText += "PASS : " + String.format("%.2f", listOfMark[0]) + "% \n";
        }

        if (listOfMark[1] < 0) {
            outputText += "CREDIT: You have already gained a credit\n";
        } else if (listOfMark[1] > 100) {
            outputText += "CREDIT: Sorry, you cannot get a credit or higher\n";
            JOptionPane.showMessageDialog(null, outputText, "Percentage to get in the Exam", JOptionPane.PLAIN_MESSAGE); 
            return;
        } else {
            outputText += "CREDIT : " + String.format("%.2f", listOfMark[1]) + "% \n";
        }

        if (listOfMark[2] < 0) {
            outputText += "DISTINCTION: You have already gained a Distinction\n";
        } else if (listOfMark[2] > 100) {
            outputText += "DISTINCTION: Sorry, you cannot get a Distinction or higher\n";
            JOptionPane.showMessageDialog(null, outputText, "Percentage to get in the Exam", JOptionPane.PLAIN_MESSAGE); 
            return;
        } else {
            outputText += "DISTINCTION : " + String.format("%.2f", listOfMark[2]) + "% \n";
        }

        if (listOfMark[3] < 0) {
            outputText += "HIGH DISTINCTION: You have already gained a High Distinction\n";
        } else if (listOfMark[3] > 100) {
            outputText += "HIGH DISTINCTION: Sorry, you cannot get a High Distinction\n";
            JOptionPane.showMessageDialog(null, outputText, "Percentage to get in the Exam", JOptionPane.PLAIN_MESSAGE); 
            return;
        } else {
            outputText += "HIGH DISTINCTION : " + String.format("%.2f", listOfMark[3]) + "% \n";
        }

        JOptionPane.showMessageDialog(null, outputText, "Percentage to get in the Exam", JOptionPane.PLAIN_MESSAGE); 
    }

    // Calculates % needed in exam to get a certain grade 
    public double[] calculateGrades() {
        /*
        This will return an array of corresponding grades where:
        index 0: Pass
        index 1: Credit
        index 2: Distinction 
        index 3: High Distinction 
        (in terms of what % you need to get in the exam) 
        - Assumes intCompTextField has been filled in already 
        - Can return values that are abnormal i.e. outside the range 0 - 100
        */
        double examWorth = 100.0 - Double.valueOf(intCompTextField.getText());

        double[] returnList = new double[4];
        double totalMark = 0;
        for (AssignmentData ad : data) {
            totalMark += ad.getPercentReceived();
        }

        System.out.println("total Mark evaluates to: " + Double.toString(totalMark));

        returnList[0] = ((50.0 - totalMark) / examWorth) * 100;
        returnList[1] = ((65.0 - totalMark) / examWorth) * 100;
        returnList[2] = ((75.0 - totalMark) / examWorth) * 100;
        returnList[3] = ((85.0 - totalMark) / examWorth) * 100;

        for (int i = 0; i < 4; i++) {
            System.out.println(i + " evaluates to " + returnList[i]);
        }

        return returnList;
    }

    // Function for the Add Button 
    public void addToList() {
        Object[] row = new Object[5];
        try {
            if (intCompTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please cap your internal marks", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (assTitleInputText.getText().isEmpty() || assInputText.getText().isEmpty() || maxMarkInput.getText().isEmpty() || percentWorthInput.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "One or more areas in your assessment details are missing", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String assessmentTitle = assTitleInputText.getText();
            int markReceived = Integer.parseInt(assInputText.getText());
            int maxMark = Integer.parseInt(maxMarkInput.getText());
            int weighting = Integer.parseInt(percentWorthInput.getText());
            int internalComposition = Integer.parseInt(intCompTextField.getText());

            double totalPercentCalculated = 0; // The Total Percentage Max
            double totalAccumulatedPercentage = 0; // The Total Percentage received

            AssignmentData newData = new AssignmentData(assessmentTitle, markReceived, maxMark, weighting);
            data.add(newData);

            for (AssignmentData ad : data) {
                totalPercentCalculated += ad.getWeighting();
                if (totalPercentCalculated > internalComposition) {
                    data.remove(data.size() - 1);
                    throw new IllegalArgumentException();
                }
                totalAccumulatedPercentage += ad.getPercentReceived();
            }
            row[0] = assessmentTitle;
            row[1] = markReceived;
            row[2] = maxMark;
            row[3] = newData.getPercentReceived();
            row[4] = weighting;

            accumulatedPercentage.setText("Accumulated Percentage: " + totalAccumulatedPercentage + "%");
            maxAccumulatedPercentage.setText("Max Accumulated Percentage: " + totalPercentCalculated + "%");
            modelAssessment.addRow(row);
        } catch(NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Some or all fields have incorrect data type", "Error", JOptionPane.ERROR_MESSAGE);
        } catch(IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Maximum Internal Composition Reached", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TODO: Not yet implemented, tested through the Calculate Button 
    // Exports assessment data as well as the calculated grades data in exam to CSV
    // BUGS: Calculated grades need to be rounded to 2DP 
    // Additional Functionalities needed: Its own new button in the calculate grade button 
    public void exportToCSV() {
        if (data.size() == 0) {
            // Print error; 
            return;
        }
        try {
            FileWriter filewriter = new FileWriter("DATA.csv");
            filewriter.append("Assessment Title,Mark Received,Max Mark,% Received,Weighting");
            filewriter.append("\n");
            for (AssignmentData ad : data) {
                filewriter.append(ad.getAssessmentTitle() + ",");
                filewriter.append(ad.getMarkReceived() + ",");
                filewriter.append(ad.getMaxMark() + ",");
                filewriter.append(ad.getPercentReceived() + ",");
                filewriter.append(ad.getWeighting() + "\n");
            }
            filewriter.append("\nPERCENTAGE IN EXAM:\nPASS,CREDIT,DISTINCTION,HIGH DISTINCTION\n");

            double[] examMarks = calculateGrades();
            for (int i = 0; i < examMarks.length; i++) {
                filewriter.append(examMarks[i] + ",");
            }
            filewriter.append("\n");
            filewriter.close();
        } catch (Exception e) {
            // Print error
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GradeCalculator().setVisible(true);
            }
        });
    }

}