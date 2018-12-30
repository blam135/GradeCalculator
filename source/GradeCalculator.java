/*
1. Initialize the object
2. Set the bounds
3. Add to panel
*/
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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
    JButton removeButton;
    JButton updateButton;
    
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
        intCompTextField.setBounds(tableXPosition + 130, 50, 40, 20);
        panel.add(intCompTextField);
        intCompTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!isCorrectNumberFormat(e.getKeyChar(), intCompTextField.getText())) {
                    e.consume();
                }
            }
        });

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
        Object[] colNames = {"Assessment Title", "Mark Received", "Max Mark", "% Received", "% Weighting"};        
        modelAssessment = (DefaultTableModel)tableOfAssessment.getModel();
        modelAssessment.setColumnIdentifiers(colNames);
        tableOfAssessment.setRowHeight(30);
        tableOfAssessment.setFont(new Font("", 1, 15));
        tableOfAssessment.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = tableOfAssessment.getSelectedRow();
                assTitleInputText.setText(modelAssessment.getValueAt(i, 0).toString());
                assInputText.setText(modelAssessment.getValueAt(i, 1).toString());
                maxMarkInput.setText(modelAssessment.getValueAt(i, 2).toString());
                percentWorthInput.setText(modelAssessment.getValueAt(i, 4).toString());
            }
        });

        // Accumulated and Max Percentage Accumulated Labels
        int totalXPosition = inputPositionX - 300;
        int totalHeight = 15;
        accumulatedPercentage = new JLabel("Accumulated Percentage: 0.00%");
        accumulatedPercentage.setBounds(totalXPosition, 15, 200, totalHeight);
        panel.add(accumulatedPercentage);
        // -----
        maxAccumulatedPercentage = new JLabel("Max Accumulated Percentage: 0.00%");
        maxAccumulatedPercentage.setBounds(totalXPosition, 45, 500, totalHeight);
        panel.add(maxAccumulatedPercentage);

        // Calculate Button
        calculateButton = new JButton("Calculate");
        calculateButton.setBounds(tableXPosition, scrollPaneY + scrollPaneHeight, buttonWidth + 50, buttonHeight);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
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
        assInputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!isCorrectNumberFormat(e.getKeyChar(), assInputText.getText())) {
                    e.consume();
                }
            }
        });
        panel.add(markReceivedLabel);
        panel.add(assInputText);

        // Max Mark Input 
        maxMarkLabel = new JLabel("Max Mark:");
        maxMarkLabel.setBounds(inputPositionX, 200, inputLabels, 10);        
        maxMarkInput = new JTextField();
        maxMarkInput.setBounds(inputPositionX, 220, 120, 20);
        maxMarkInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!isCorrectNumberFormat(e.getKeyChar(), maxMarkInput.getText())) {
                    e.consume();
                }
            }
        });
        panel.add(maxMarkLabel);
        panel.add(maxMarkInput);

        // Weighting Input 
        percentWorthLabel = new JLabel("% Weighting:");
        percentWorthLabel.setBounds(inputPositionX, 250, inputLabels, 15);        
        percentWorthInput = new JTextField();
        percentWorthInput.setBounds(inputPositionX, 270, 120, 20);
        percentWorthInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!isCorrectNumberFormat(e.getKeyChar(), percentWorthInput.getText())) {
                    e.consume();
                }
            }
        });
        panel.add(percentWorthLabel);
        panel.add(percentWorthInput);

        // Add Button 
        addToListButton = new JButton("ADD");
        addToListButton.setBounds(inputPositionX + 30, 300, 60, 20);
        addToListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToList(-1);
            }
        });
        panel.add(addToListButton);

        // Remove Button 
        removeButton = new JButton("REMOVE");
        removeButton.setBounds(inputPositionX + 15, 330, 90, 20);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove();
            }
        });
        panel.add(removeButton);
        
        // Update Button 
        updateButton = new JButton("UPDATE");
        updateButton.setBounds(inputPositionX + 15, 360, 88, 20);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToList(tableOfAssessment.getSelectedRow());
            }
        });
        panel.add(updateButton);

        setContentPane(panel);
    }

    // For the textfield inputs, checks if the numbers typed are actually double numbers:
    public boolean isCorrectNumberFormat(char charToAppend, String origin) {
        if (origin.length() == 3 && origin.indexOf('.') == -1) {
            return false;
        }

        if (charToAppend == '.' && origin.length() == 0) {
            return false;
        } else if (origin.indexOf(charToAppend) != -1 && charToAppend == '.') {
            return false;
        } else if (origin.length() > 4) {
            return false;
        }

        try {   
            if (charToAppend == '.') {
                return true;
            }
            Integer.parseInt(String.valueOf(charToAppend));
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // Function for reset button
    public void reset() {
        data.clear();
        while (modelAssessment.getRowCount() > 0) {
            modelAssessment.removeRow(0);
        }
        System.out.println("The table has been reset");
        calculateAccumulatedPercentage();
    }

    // Function for the calculate button 
    public void calculate() {
        if (intCompTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please cap your marks", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (Double.parseDouble(intCompTextField.getText()) < 0) {
            JOptionPane.showMessageDialog(null, "Cannot have an internal mark of less than 0", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (Double.parseDouble(intCompTextField.getText()) > 100) {
            JOptionPane.showMessageDialog(null, "Cannot have an internal mark of more than 100", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double[] listOfMark = calculateGrades();
        String outputText = uosTextField.getText() + " EXAM: \n";

        if (listOfMark[0] <= 0) {
            outputText += "PASS: You have already passed the course \n";
        } else if (listOfMark[0] > 100) {
            outputText += "Sorry you have failed the course \n";
        } else {
            outputText += "PASS : " + String.format("%.2f", listOfMark[0]) + "% \n";
        }

        if (listOfMark[1] <= 0) {
            outputText += "CREDIT: You have already gained a credit\n";
        } else if (listOfMark[1] > 100) {
            outputText += "CREDIT: Sorry, you cannot get a credit or higher\n";
        } else {
            outputText += "CREDIT : " + String.format("%.2f", listOfMark[1]) + "% \n";
        }

        if (listOfMark[2] <= 0) {
            outputText += "DISTINCTION: You have already gained a Distinction\n";
        } else if (listOfMark[2] > 100) {
            outputText += "DISTINCTION: Sorry, you cannot get a Distinction or higher\n";
        } else {
            outputText += "DISTINCTION : " + String.format("%.2f", listOfMark[2]) + "% \n";
        }

        if (listOfMark[3] <= 0) {
            outputText += "HIGH DISTINCTION: You have already gained a High Distinction\n";
        } else if (listOfMark[3] > 100) {
            outputText += "HIGH DISTINCTION: Sorry, you cannot get a High Distinction\n";
        } else {
            outputText += "HIGH DISTINCTION : " + String.format("%.2f", listOfMark[3]) + "% \n";
        }

        Object[] options = {"Close", "Export to CSV"};

        int option = JOptionPane.showOptionDialog(null, outputText, "Percentage in Exam", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (option == 1) {
            exportToCSV();
            return;
        }
        
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

        returnList[0] = ((50.0 - totalMark) / examWorth) * 100;
        returnList[1] = ((65.0 - totalMark) / examWorth) * 100;
        returnList[2] = ((75.0 - totalMark) / examWorth) * 100;
        returnList[3] = ((85.0 - totalMark) / examWorth) * 100;
        return returnList;
    }

    // Function for the Add Button (-1) and Update Button (corresponding index) 
    public void addToList(int index) {
        try {
            if (intCompTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please cap your internal marks", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (assTitleInputText.getText().isEmpty() || assInputText.getText().isEmpty() || maxMarkInput.getText().isEmpty() || percentWorthInput.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "One or more areas in your assessment details are missing", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (Double.parseDouble(intCompTextField.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "Cannot have an internal mark of less than 0%", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (Double.parseDouble(intCompTextField.getText()) > 100) {
                JOptionPane.showMessageDialog(null, "Cannot have an internal mark of more than 100%", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } 

            String assessmentTitle = assTitleInputText.getText();
            double markReceived = Double.parseDouble(assInputText.getText());
            double maxMark = Double.parseDouble(maxMarkInput.getText());
            double weighting = Double.parseDouble(percentWorthInput.getText());
            double internalComposition = Double.parseDouble(intCompTextField.getText());

            if (markReceived > maxMark) {
                markReceived = maxMark;             
            } else if (weighting > 100) {
                JOptionPane.showMessageDialog(null, "Your assessment cannot be more than 100%", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (weighting < 0) {
                JOptionPane.showMessageDialog(null, "Your assessment cannot be less than 0%", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalPercentCalculated = 0; // The Total Percentage Max

            AssignmentData newData = new AssignmentData(assessmentTitle, markReceived, maxMark, weighting);
            
            if (index == -1) {
                data.add(newData);
            } else {
                data.set(index, newData);
            }

            for (AssignmentData ad : data) {
                totalPercentCalculated += ad.getWeighting();
                if (totalPercentCalculated > internalComposition) {
                    if (index == -1) {
                        data.remove(data.size() - 1);                    
                    } else {
                        data.remove(index);
                    }
                    throw new IllegalArgumentException();
                }
            }
            Object[] row = new Object[5];
            row[0] = assessmentTitle;
            row[1] = String.format("%.2f", markReceived);
            row[2] = String.format("%.2f", maxMark);
            row[3] = String.format("%.2f", newData.getPercentReceived());
            row[4] = String.format("%.2f", weighting);

            if (index == -1) {
                modelAssessment.addRow(row);
                calculateAccumulatedPercentage();
            } else {
                modelAssessment.setValueAt(row[0], index, 0);
                modelAssessment.setValueAt(row[1], index, 1);
                modelAssessment.setValueAt(row[2], index, 2);
                modelAssessment.setValueAt(row[3], index, 3);
                modelAssessment.setValueAt(row[4], index, 4);
            }

            System.out.println("Values have been added");
        } catch(NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Some or all fields have incorrect data type", "Error", JOptionPane.ERROR_MESSAGE);
        } catch(IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Maximum Internal Composition Reached", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Function for the Labels Accumulated % and Max Accumulated % Labels. Also returns their values as well
    public double[] calculateAccumulatedPercentage() {
        double[] returnValue = {0.0, 0.0};
        
        for (AssignmentData ad : data) {
            returnValue[0] += ad.getPercentReceived();
            returnValue[1] += ad.getWeighting();
        }

        accumulatedPercentage.setText("Accumulated Percentage: " + String.format("%.2f", returnValue[0]) + "%");
        maxAccumulatedPercentage.setText("Max Accumulated Percentage: " + String.format("%.2f", returnValue[1]) + "%");
        return returnValue;
    }

    // Function for the remove button 
    public void remove() {
        int i = tableOfAssessment.getSelectedRow();
        if (i >= 0) {
            modelAssessment.removeRow(i);
            data.remove(i);
            System.out.println("Index " + i + " has been removed");
            calculateAccumulatedPercentage();
        } else {
            System.out.println("Remove error");
        }
    }

    // Function for export to CSV  
    public void exportToCSV() {
        String content = "";
        
        if (data.size() != 0) {
            content += "Assessment Title,Mark Received,Max Mark,% Received,Weighting\n";
            for (AssignmentData ad : data) {
                content += ad.getAssessmentTitle() + ",";
                content += String.format("%.2f", ad.getMarkReceived()) + ",";
                content += String.format("%.2f", ad.getMaxMark()) + ",";
                content += String.format("%.2f", ad.getPercentReceived()) + ",";
                content += String.format("%.2f", ad.getWeighting()) + "\n";
            }
            content += "\n";
        }

        content += "PERCENTAGE IN EXAM:\nPASS,CREDIT,DISTINCTION,HIGH DISTINCTION\n";
        double[] examMarks = calculateGrades();
        for (int i = 0; i < examMarks.length; i++) {
            content += examMarks[i] + ",";
        }
        content += "\n\nINTERNAL ASSESSMENT WORTH:\n";
        content += intCompTextField.getText() + "\n";

        String directoryPath = "";
        String filename = JOptionPane.showInputDialog(null, "Enter a filename");
        if (filename == null) {
            System.out.println("User cancelled Action Input Dialog");
            return;
        } else if (!filename.isEmpty()) {
            filename += ".csv";
        } else {
            filename += uosTextField.getText() + "Results.csv";
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
            JOptionPane.showMessageDialog(null, "Failed to Write to File", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Failed to export to CSV");            
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