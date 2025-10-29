package carbonfp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;

public class TrackerFrame extends JFrame implements ActionListener {

    JButton CalculateButton, ClearButton, SaveButton, HistoryButton;
    JTextArea ResultArea;
//personal tab    
    JTextField NameField, HeightField, WeightField;
    JComboBox<String> GenderBox, DietBox, SocialActivityBox;
//travel tab
    JComboBox<String> TransportModeBox, VehicleTypeBox, AirTravelBox;
    JTextField VehicleKmField; 
//waste tab
    JComboBox<String> WasteBagSizeBox;
    JTextField WasteBagCountField;
    JCheckBox PlasticBox, PaperBox,GlassBox, MetalBox;
//energy tab
    JComboBox<String> CookingSourceBox, EnergyEffBox;
    JCheckBox MicrowaveBox, OvenBox, GrillBox, AirfryerBox, StoveBox;
    JTextField TvField;
    JTextField NetField;
//consumption
    JComboBox<String> ShowerFrequencyBox;
    JTextField GroceryField;
    JTextField ClothesField;

    FootprintCalculator calculator;
    Record record;
    CarbonRecord lastRecord = null;

    public TrackerFrame() {
        String DB_URL = "jdbc:mysql://localhost:3306/carbon";
        String USER = "root";
        String PASS = "";
        record = new Record(DB_URL, USER, PASS);
        calculator = new FootprintCalculator();

        setTitle("Carbon Footprint Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Carbon Footprint Calculator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);
//tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Personal", createPersonalPanel());
        tabbedPane.addTab("Travel", createTravelPanel());
        tabbedPane.addTab("Waste", createWastePanel());
        tabbedPane.addTab("Energy", createEnergyPanel());
        tabbedPane.addTab("Consumption", createConsumptionPanel());
        add(tabbedPane, BorderLayout.CENTER);
//bottompanel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        CalculateButton = new JButton("Calculate");
        ClearButton = new JButton("Clear");
        SaveButton = new JButton("Save");
        HistoryButton = new JButton("History");

        CalculateButton.addActionListener(this);
        ClearButton.addActionListener(this);
        SaveButton.addActionListener(this);
        HistoryButton.addActionListener(this);

        buttonPanel.add(CalculateButton);
        buttonPanel.add(ClearButton);
        buttonPanel.add(SaveButton);
        buttonPanel.add(HistoryButton);
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        ResultArea = new JTextArea();
        ResultArea.setEditable(false);
        ResultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(ResultArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    
    private JPanel createPersonalPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        NameField = new JTextField();
        HeightField = new JTextField("");
        WeightField = new JTextField("");
        GenderBox = new JComboBox<>(new String[]{"Female", "Male"});
        DietBox = new JComboBox<>(new String[]{"Omnivore", "Vegetarian", "Vegan"});
        SocialActivityBox = new JComboBox<>(new String[]{"Never", "Sometimes", "Often"});
        
        panel.add(new JLabel("Your Name:"));
        panel.add(NameField);
        panel.add(new JLabel("Height(cm):"));
        panel.add(HeightField);
        panel.add(new JLabel("Weight(kg):"));
        panel.add(WeightField);
        panel.add(new JLabel("Gender:"));
        panel.add(GenderBox);
        panel.add(new JLabel("Diet:"));
        panel.add(DietBox);
        panel.add(new JLabel("Social Activity:"));
        panel.add(SocialActivityBox);
        
        return panel;
    }

    private JPanel createTravelPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        TransportModeBox = new JComboBox<>(new String[]{"Public", "Private", "Walk/Bicycle"});
        VehicleTypeBox = new JComboBox<>(new String[]{"Petrol", "Diesel", "Hybrid", "LPG", "Electric"});
        AirTravelBox = new JComboBox<>(new String[]{"Never", "Rarely (1-4 Hours)", "Frequently (5-10 Hours)", "Very Frequently (10+ Hours)"});

        TransportModeBox.addActionListener(e -> {
            VehicleTypeBox.setEnabled(TransportModeBox.getSelectedItem().equals("Private"));
        });
        VehicleTypeBox.setEnabled(false); 

        VehicleKmField = new JTextField(""); 
        
        panel.add(new JLabel("Transportation:"));
        panel.add(TransportModeBox);
        panel.add(new JLabel("Vehicle Type (if private):"));
        panel.add(VehicleTypeBox);
        panel.add(new JLabel("Monthly Vehicle Distance (km):"));
        panel.add(VehicleKmField);
        panel.add(new JLabel("Monthly Air Travel:"));
        panel.add(AirTravelBox);

        return panel;
    }

    private JPanel createWastePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        WasteBagSizeBox = new JComboBox<>(new String[]{"Small (approx. 2kg)", "Medium (approx. 5kg)", "Large (approx. 10kg)", "Extra Large (approx. 15kg)" });
        WasteBagCountField = new JTextField(""); 
        
        panel.add(new JLabel("Waste Bag Size:"));
        panel.add(WasteBagSizeBox);
        panel.add(new JLabel("Waste Bags per Week:"));
        panel.add(WasteBagCountField);
        
        panel.add(new JLabel("Materials Recycled:"));
        JPanel recyclePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        PlasticBox = new JCheckBox("Plastic");
        PaperBox = new JCheckBox("Paper");
        GlassBox = new JCheckBox("Glass");
        MetalBox = new JCheckBox("Metal");
        recyclePanel.add(PlasticBox);
        recyclePanel.add(PaperBox);
        recyclePanel.add(GlassBox);
        recyclePanel.add(MetalBox);
        panel.add(recyclePanel);
        
        return panel;
    }

    private JPanel createEnergyPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        CookingSourceBox = new JComboBox<>(new String[]{"Natural Gas", "Electricity", "Wood", "Coal"});
        EnergyEffBox = new JComboBox<>(new String[]{"No", "Yes", "Sometimes"});
        
        TvField = new JTextField("");
        NetField = new JTextField("");
        
        panel.add(new JLabel("Energy Efficiency?"));
        panel.add(EnergyEffBox);
        panel.add(new JLabel("Daily Screen/TV Hours:"));
        panel.add(TvField);
        
        panel.add(new JLabel("Cooking Systems Used:"));
        JPanel cookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        MicrowaveBox = new JCheckBox("Microwave");
        OvenBox = new JCheckBox("Oven");
        GrillBox = new JCheckBox("Grill");
        AirfryerBox = new JCheckBox("Airfryer");
        StoveBox = new JCheckBox("Stove");
        cookPanel.add(MicrowaveBox);
        cookPanel.add(OvenBox);
        cookPanel.add(GrillBox);
        cookPanel.add(AirfryerBox);
        cookPanel.add(StoveBox);
        panel.add(cookPanel);

        panel.add(new JLabel("Cooking Source:")); 
        panel.add(CookingSourceBox);
        
        panel.add(new JLabel("Daily Internet Hours:"));
        panel.add(NetField); 
        
        return panel;
    }
    
    private JPanel createConsumptionPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        ShowerFrequencyBox = new JComboBox<>(new String[]{"Daily", "Twice a day", "More frequently", "Less frequently"});
        GroceryField = new JTextField("");
        ClothesField = new JTextField("");
        
        panel.add(new JLabel("Shower Frequency:"));
        panel.add(ShowerFrequencyBox);
       
        panel.add(new JLabel("Monthly Grocery (₹):"));
        panel.add(GroceryField);
        panel.add(new JLabel("Clothes Bought Monthly:"));
        panel.add(ClothesField);

        return panel;
    }
//event handling 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ClearButton) {
            handleClear();
        } else if (e.getSource() == CalculateButton) {
            handleCalculate();
        } else if (e.getSource() == SaveButton) {
            handleSave();
        } else if (e.getSource() == HistoryButton) {
            handleShowHistory();
        }
    }

    void handleClear() {
        
        NameField.setText("");
        HeightField.setText("");
        WeightField.setText("");
        GenderBox.setSelectedIndex(0);
        DietBox.setSelectedIndex(0);
        SocialActivityBox.setSelectedIndex(0);
        
        TransportModeBox.setSelectedIndex(0);
        VehicleTypeBox.setSelectedIndex(0);
        AirTravelBox.setSelectedIndex(0);
        VehicleKmField.setText("");
        
        WasteBagSizeBox.setSelectedIndex(0);
        WasteBagCountField.setText("");
        PlasticBox.setSelected(false);
        PaperBox.setSelected(false);
        GlassBox.setSelected(false);
        MetalBox.setSelected(false);
        
        CookingSourceBox.setSelectedIndex(0);
        EnergyEffBox.setSelectedIndex(0);
        TvField.setText("");
        NetField.setText("");
        MicrowaveBox.setSelected(false);
        OvenBox.setSelected(false);
        GrillBox.setSelected(false);
        AirfryerBox.setSelected(false);
        StoveBox.setSelected(false);

        ShowerFrequencyBox.setSelectedIndex(0);
        GroceryField.setText("");
        ClothesField.setText("");
        
        ResultArea.setText("");
        lastRecord = null;
    }

    void handleCalculate() {
        try {

            String name = NameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name field cannot be empty.");
            }
            if (!name.matches("^[a-zA-Z\\s]+$")) {
                throw new IllegalArgumentException("Name field must contain only letters and spaces.");
            }

            double height = getNumericInput(HeightField, "Height");
            double weight = getNumericInput(WeightField, "Weight");
            
            // Check for division by zero
            if (height <= 0) {
                throw new IllegalArgumentException("Height must be greater than zero.");
            }
            if (weight <= 0) {
                throw new IllegalArgumentException("Weight must be greater than zero.");
            }
            
            double bmi = weight / Math.pow(height / 100.0, 2);
            String bodyType = (bmi < 18.5) ? "Underweight" : (bmi < 25) ? "Normal" : (bmi < 30) ? "Overweight" : "Obese";
            
            String gender = (String) GenderBox.getSelectedItem();
            String diet = (String) DietBox.getSelectedItem();
            String social = (String) SocialActivityBox.getSelectedItem();
 //travel
            String transportMode = (String) TransportModeBox.getSelectedItem();
            String vehicleType = (String) VehicleTypeBox.getSelectedItem();
            double vehicleKm = getNumericInput(VehicleKmField, "Monthly Vehicle Distance");
            String airTravel = (String) AirTravelBox.getSelectedItem();
            
//waste
            String wasteBagSize = (String) WasteBagSizeBox.getSelectedItem();
            int wasteBagCount = (int) getNumericInput(WasteBagCountField, "Waste Bags per Week");
            List<String> recycled = getRecycledMaterials();
            //energy
            String cookingSource = (String) CookingSourceBox.getSelectedItem();
            String energyEffic = (String) EnergyEffBox.getSelectedItem();
            int pcHours = (int) getNumericInput(TvField, "Daily PC/TV Hours");
            int netHours = (int) getNumericInput(NetField, "Daily Internet Hours");
            List<String> cooking = getCookingSystems();
//consumption
            String shower = (String) ShowerFrequencyBox.getSelectedItem();
            double grocery = getNumericInput(GroceryField, "Monthly Grocery");
            int clothes = (int) getNumericInput(ClothesField, "Clothes Bought Monthly");

            //to run calculation
            lastRecord = calculator.calculate(
                name, gender, bodyType, diet, social, height, weight,
                transportMode, vehicleType, vehicleKm, airTravel,
                wasteBagSize, wasteBagCount, recycled,
                cookingSource, cooking, energyEffic, pcHours, netHours, 
                shower, grocery, clothes
            );
//display
            double totalAnnualCO2 = lastRecord.totalCo2;
            String result = "Annual Carbon Footprint Estimate\n";
            result += "User: " + lastRecord.name + "\n";
            result += "Body Type: " + lastRecord.bodyType + " (BMI: " + String.format("%.1f", bmi) + ")\n";
            result += "\n";
            result += String.format("  Transportation Footprint: %.2f kg\n", lastRecord.transportEmission);
            result += String.format("  Waste Footprint:          %.2f kg\n", lastRecord.wasteEmission);
            result += String.format("  Energy Footprint:         %.2f kg\n", lastRecord.energyEmission);
          
            result += "\n";
            result += String.format("  Total Annual Footprint:   %.2f kg CO2e\n", totalAnnualCO2);
            result += "\n";
            
            // tips
            if (totalAnnualCO2 < 4000) {
                result += "Low footprint. Great job!\n";
            } else if (totalAnnualCO2 < 8000) {
                result += "Below the global average. Keep it up!\n";
            } else if (totalAnnualCO2 < 15000) {
                result += "High footprint!\n";
            } else {
                result += "Very high footprint. Consider change.\n";
            }
            ResultArea.setText(result);

        } catch (IllegalArgumentException ex) {
            ResultArea.setText("Input Error: " + ex.getMessage());
            lastRecord = null;
        }
    }
    
    private List<String> getRecycledMaterials() {
        List<String> materials = new ArrayList<>();
        if (PlasticBox.isSelected()) materials.add("Plastic");
        if (PaperBox.isSelected()) materials.add("Paper");
        if (GlassBox.isSelected()) materials.add("Glass");
        if (MetalBox.isSelected()) materials.add("Metal");
        return materials;
    }
    
    private List<String> getCookingSystems() {
        List<String> systems = new ArrayList<>();
        if (MicrowaveBox.isSelected()) systems.add("Microwave");
        if (OvenBox.isSelected()) systems.add("Oven");
        if (GrillBox.isSelected()) systems.add("Grill");
        if (AirfryerBox.isSelected()) systems.add("Airfryer");
        if (StoveBox.isSelected()) systems.add("Stove");
        return systems;
    }
    
    private double getNumericInput(JTextField field, String fieldName) throws IllegalArgumentException {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            return 0; 
        }
        
        if (!text.matches("^[0-9]*\\.?[0-9]+$")) {
            throw new IllegalArgumentException("'" + fieldName + "' field contains invalid characters. " +
                                               "Please use numbers only (e.g., '2.5' or '10').");
        }
        
        try {
            double value = Double.parseDouble(text);
            if (value < 0) {
                throw new IllegalArgumentException("'" + fieldName + "' cannot be negative.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + fieldName + "' is not a valid number.");
        }
    }
    
    void handleSave() {
        if (lastRecord == null) {
            JOptionPane.showMessageDialog(this, "Error: Calculate a footprint before saving!", "Save Error", JOptionPane.ERROR_MESSAGE);
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
        JTextArea area = new JTextArea(records, 15, 40); 
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scroll, "History (Annual CO2)", JOptionPane.PLAIN_MESSAGE);
    }
}
