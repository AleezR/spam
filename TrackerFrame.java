package carbonfp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrackerFrame extends JFrame implements ActionListener {
  
    JTextField TransportField, ElectricityField, GarbageField, NameField;
    JButton CalculateButton, ClearButton, SaveButton, HistoryButton;
    JTextArea ResultArea;
    JComboBox<String> TransportModeBox;
    FootprintCalculator calculator;
    Record record;
    CarbonRecord lastRecord = null;

    public TrackerFrame() {

    	//database connection and declaration enu vendi
        String DB_URL = "jdbc:mysql://localhost:3306/carbon";
        String USER = "root";
        String PASS = "";     
        record = new Record(DB_URL, USER, PASS);
        calculator = new FootprintCalculator();

     
        setTitle("Carbon Footprint Tracker");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Carbon Footprint Calculator");
        titleLabel.setBounds(100, 20, 250, 25);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel);

        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setBounds(40, 60, 150, 25);
        add(nameLabel);

        NameField = new JTextField();
        NameField.setBounds(220, 60, 150, 25);
        add(NameField);

        JLabel modeLabel = new JLabel("Transport Mode:");
        modeLabel.setBounds(40, 100, 150, 25);
        add(modeLabel);

        String[] modes = {"Car", "Bus", "Bike", "Walk"};
        TransportModeBox = new JComboBox<>(modes);
        TransportModeBox.setBounds(220, 100, 150, 25);
        add(TransportModeBox);

        JLabel transportLabel = new JLabel("Transportation (km/day):");
        transportLabel.setBounds(40, 140, 180, 25);
        add(transportLabel);

        TransportField = new JTextField();
        TransportField.setBounds(220, 140, 150, 25);
        add(TransportField);

        JLabel electricityLabel = new JLabel("Electricity (kWh/day):");
        electricityLabel.setBounds(40, 180, 180, 25);
        add(electricityLabel);

        ElectricityField = new JTextField();
        ElectricityField.setBounds(220, 180, 150, 25);
        add(ElectricityField);

        JLabel garbageLabel = new JLabel("Garbage (kg/day):");
        garbageLabel.setBounds(40, 220, 180, 25);
        add(garbageLabel);

        GarbageField = new JTextField();
        GarbageField.setBounds(220, 220, 150, 25);
        add(GarbageField);

        CalculateButton = new JButton("Calculate");
        CalculateButton.setBounds(40, 270, 90, 30);
        CalculateButton.addActionListener(this);
        add(CalculateButton);

        ClearButton = new JButton("Clear");
        ClearButton.setBounds(140, 270, 90, 30);
        ClearButton.addActionListener(this);
        add(ClearButton);

        SaveButton = new JButton("Save");
        SaveButton.setBounds(240, 270, 90, 30);
        SaveButton.addActionListener(this);
        add(SaveButton);

        HistoryButton = new JButton("History");
        HistoryButton.setBounds(340, 270, 90, 30);
        HistoryButton.addActionListener(this);
        add(HistoryButton);

        ResultArea = new JTextArea();
        ResultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ResultArea);
        scrollPane.setBounds(40, 320, 360, 220);
        add(scrollPane);

        setVisible(true);
    }
//Event handling

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ClearButton) {
            TransportField.setText("");
            ElectricityField.setText("");
            GarbageField.setText("");
            NameField.setText("");
            ResultArea.setText("");
            lastRecord = null;
        } else if (e.getSource() == CalculateButton) {
            handleCalculate();
        } else if (e.getSource() == SaveButton) {
            handleSave();
        } else if (e.getSource() == HistoryButton) {
            handleShowHistory();
        }
    }

   
    void handleCalculate() {
        try {
            String name = NameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name field cannot be empty.");
            }
            
            String transportText = TransportField.getText().trim();
            double currentTransport = transportText.isEmpty() ? 0 : Double.parseDouble(transportText);
            if (currentTransport < 0) { throw new NumberFormatException("Transportation cannot be negative."); }

            String electricityText = ElectricityField.getText().trim();
            double currentElectricity = electricityText.isEmpty() ? 0 : Double.parseDouble(electricityText);
            if (currentElectricity < 0) { throw new NumberFormatException("Electricity cannot be negative."); }

            String garbageText = GarbageField.getText().trim();
            double currentGarbage = garbageText.isEmpty() ? 0 : Double.parseDouble(garbageText);
            if (currentGarbage < 0) { throw new NumberFormatException("Garbage cannot be negative."); }

            String mode = (String) TransportModeBox.getSelectedItem();
            
            lastRecord = calculator.calculate(name, mode, currentTransport, currentElectricity, currentGarbage);
            
            double transportEmission = lastRecord.transportKm * 0.21;
            double electricityEmission = lastRecord.electricityKwh * 0.5;
            double garbageEmission = lastRecord.garbageKg * 0.3;
            double totalEmission = lastRecord.totalCo2;

            String result = "Daily Carbon Footprint:\n";
            result += "Transport Mode: " + mode + "\n";
            result += "Transportation CO2: " + String.format("%.2f", transportEmission) + " kg\n";
            result += "Electricity CO2: " + String.format("%.2f", electricityEmission) + " kg\n";
            result += "Garbage CO2: " + String.format("%.2f", garbageEmission) + " kg\n";
            result += "Total: " + String.format("%.2f", totalEmission) + " kg CO2/day\n\n";

            result += "Tips:\n";
            if (totalEmission < 5) {
                result += "Good.\n";
            } else if (totalEmission < 10) {
                result += "Average, but can improve.\n";
            } else {
                result += "Bad, have to improve.\n";
            }
            if (transportEmission > 2) {
                result += "Use public transport\n";
            }
            if (electricityEmission > 5) {
                result += "Reduce electricity usage\n";
            }

            ResultArea.setText(result);

        } catch (NumberFormatException ex) {
            ResultArea.setText("Error: Enter valid details! Details: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
             ResultArea.setText("Error: " + ex.getMessage());
        }
    }
    
   
    void handleSave() {
        if (lastRecord == null) {
            JOptionPane.showMessageDialog(this, "Error: Calculate a footprint before saving!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = record.saveRecord(lastRecord);

        if (success) {
            JOptionPane.showMessageDialog(this, "Record saved to your local database!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error while saving! Check database connection.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  
    void handleShowHistory() {
        String records = record.getHistory();

        if (records.startsWith("Error:")) {
            JOptionPane.showMessageDialog(this, records, "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextArea area = new JTextArea(records);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scroll, "History from Local Database", JOptionPane.PLAIN_MESSAGE);
    }
}