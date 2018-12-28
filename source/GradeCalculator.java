/*
1. Initialize the object
2. Set the bounds
3. Add to panel
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GradeCalculator extends JFrame {

    private static final long serialVersionUID = 1L;
    ArrayList<AssignmentData> data = new ArrayList<AssignmentData>();

    public GradeCalculator() {
        initializeUI();
    }

    public void initializeUI() {

        // Window Setup
        setSize(750, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Grade Calculator");
        setResizable(false);
        
        // Panel Setup
        JPanel panel = new JPanel(null);
        int tableXPosition = 12;
        int buttonWidth = 50;
        int buttonHeight = 20;
        int inputPositionX = 575;
        int inputLabels = 102;

        // UOS Title
        JLabel titleLabel = new JLabel("UOS: ");
        titleLabel.setBounds(tableXPosition, 15, 50, 10);
        panel.add(titleLabel);
        JTextField textField = new JTextField();
        textField.setBounds(tableXPosition + 38, 10, 100, 20);
        panel.add(textField);

        // UOS Label
        JLabel intCompLabel = new JLabel("Internal Composition: ");
        intCompLabel.setBounds(tableXPosition, 50, 125, 15);
        panel.add(intCompLabel);

        // Internal Mark Cap Textfield Label
        JTextField internalTextField = new JTextField();
        internalTextField.setBounds(tableXPosition + 130, 50, 20, 20);
        panel.add(internalTextField);

        // TextArea Assessment Display
        int scrollPaneX = 12;
        int scrollPaneY = 100;
        int scrollPaneWidth = 550;
        int scrollPaneHeight = 300;

        JFrame tableFrame = new JFrame();
        JTable tableArrList = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane pane = new JScrollPane(tableArrList);
        pane.setBounds(scrollPaneX, scrollPaneY, scrollPaneWidth, scrollPaneHeight);
        panel.add(pane);

        Object[] colNames = {"Assessment Title", "Mark Received", "Max Mark", "% Received", "Weighting"};        
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colNames);
        tableArrList.setModel(model);

        // Calculate Button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(tableXPosition, scrollPaneY + scrollPaneHeight, buttonWidth + 50, buttonHeight);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (data.size() == 0) {
                    JOptionPane.showMessageDialog(null, "Please add data", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double pass;
                double credit;
                double distinction;
                double highDistinction;

                double totalPercentageReceived = 0;
                double examWorth = 100.0 - Double.valueOf(internalTextField.getText());

                for (AssignmentData ad: data) {
                    totalPercentageReceived += ad.getPercentReceived();
                }

                pass = 50.0 - totalPercentageReceived;
                credit = 65.0 - totalPercentageReceived;
                distinction = 75.0 - totalPercentageReceived;
                highDistinction = 85.0 - totalPercentageReceived;

                JOptionPane.showMessageDialog(null, textField.getText() + " EXAM: \n" +
                                                    "PASS: " + String.format("%.2f", (pass / examWorth) * 100) + "% \n" +
                                                    "CREDIT: " + String.format("%.2f", (credit / examWorth) * 100) + "% \n" +
                                                    "DISTINCTION: " + String.format("%.2f", (distinction / examWorth) * 100) + "% \n" +
                                                    "HIGH DISTINCTION : " + String.format("%.2f", (highDistinction / examWorth) * 100) + "% \n",
                                                    "Percentage to get in the Exam",
                                                    JOptionPane.PLAIN_MESSAGE 
                );

            }
        });
        panel.add(calculateButton);

        int totalXPosition = inputPositionX - 300;
        // Accumulated Percentage Label
        JLabel accumulatedPercentage = new JLabel("Accumulated Percentage: 0%");
        accumulatedPercentage.setBounds(totalXPosition, 15, 200, 15);
        panel.add(accumulatedPercentage);

        // Max Percentage Accumulated Label 
        JLabel maxAccumulatedPercentage = new JLabel("Max Percentage Accumulated: 0%");
        maxAccumulatedPercentage.setBounds(totalXPosition, 45, 500, 15);
        panel.add(maxAccumulatedPercentage);

        // Reset Button 
        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(tableXPosition + 478, scrollPaneY + scrollPaneHeight, buttonWidth + 20, buttonHeight);
        resetButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data.clear();
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                System.out.println("The table has been reset");
                accumulatedPercentage.setText("Accumulated Percentage: 0%");
                maxAccumulatedPercentage.setText("Max Percentage Accumulated: 0%");
            }
        });
        panel.add(resetButton);

        // Title Input 
        JLabel assTitleInputLabel = new JLabel("Assessment Title:");
        assTitleInputLabel.setBounds(inputPositionX, 100, inputLabels, 10);        
        JTextField assTitleInputText = new JTextField();
        assTitleInputText.setBounds(inputPositionX, 120, 120, 20);
        panel.add(assTitleInputLabel);
        panel.add(assTitleInputText);

        JLabel markReceivedLabel = new JLabel("Mark Received:");
        markReceivedLabel.setBounds(inputPositionX, 150, inputLabels, 10);        
        JTextField assInputText = new JTextField();
        assInputText.setBounds(inputPositionX, 170, 120, 20);
        panel.add(markReceivedLabel);
        panel.add(assInputText);

        JLabel maxMarkLabel = new JLabel("Max Mark:");
        maxMarkLabel.setBounds(inputPositionX, 200, inputLabels, 10);        
        JTextField maxMarkInput = new JTextField();
        maxMarkInput.setBounds(inputPositionX, 220, 120, 20);
        panel.add(maxMarkLabel);
        panel.add(maxMarkInput);

        JLabel percentWorthLabel = new JLabel("Weighting:");
        percentWorthLabel.setBounds(inputPositionX, 250, inputLabels, 15);        
        JTextField percentWorthInput = new JTextField();
        percentWorthInput.setBounds(inputPositionX, 270, 120, 20);
        panel.add(percentWorthLabel);
        panel.add(percentWorthInput);

        JButton addToList = new JButton("ADD");
        addToList.setBounds(inputPositionX + 30, 310, 60, 20);
        Object[] row = new Object[5];
        addToList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (internalTextField.getText().isEmpty()) {
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
                    int internalComposition = Integer.parseInt(internalTextField.getText());

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
                    model.addRow(row);
                } catch(NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Some or all fields have incorrect data type", "Error", JOptionPane.ERROR_MESSAGE);
                } catch(IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(null, "Maximum Internal Composition Reached", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(addToList);
        setContentPane(panel);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GradeCalculator().setVisible(true);
            }
        });
    }

}