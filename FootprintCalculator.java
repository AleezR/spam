package carbonfp;

import java.util.List;

public class FootprintCalculator {

    static final double DIET_OMNIVORE_KG = 2500;
    static final double DIET_VEGETARIAN_KG = 1500;
    static final double DIET_VEGAN_KG = 1000;

    static final double VEHICLE_PETROL_KG_KM = 0.192;
    static final double VEHICLE_DIESEL_KG_KM = 0.171;
    static final double VEHICLE_HYBRID_KG_KM = 0.105;
    static final double VEHICLE_LPG_KG_KM = 0.150;
    static final double VEHICLE_ELECTRIC_KG_KM = 0.050; 
    static final double VEHICLE_PUBLIC_KG_KM = 0.080;
    static final double VEHICLE_WALK_BIKE_KG_KM = 0.0;

    static final double AIR_NEVER_KG = 0;
    static final double AIR_RARELY_KG = 400;  // 1-4 hours
    static final double AIR_FREQUENTLY_KG = 1600; // 5-10 hours
    static final double AIR_VERY_FREQUENTLY_KG = 5000; // 10+ hours

    static final double BAG_SMALL_KG = 2.0;
    static final double BAG_MEDIUM_KG = 5.0;
    static final double BAG_LARGE_KG = 10.0;
    static final double BAG_EXTRA_LARGE_KG = 15.0;
    static final double WASTE_FACTOR_KG = 0.6; 
    static final double RECYCLE_CREDIT_KG = -50;

    static final double COOKING_GAS_KG = 2000;
    static final double COOKING_ELECTRICITY_KG = 1200;
    static final double COOKING_WOOD_KG = 500;
    static final double COOKING_COAL_KG = 3500;
    
    private static final double COOKING_APPLIANCE_KG = 50; 
    private static final double PC_TV_FACTOR_KG_HR = 0.04; 
    private static final double INTERNET_FACTOR_KG_HR = 0.01; 

    private static final double SHOWER_FACTOR_KG = 150; 
    private static final double GROCERY_FACTOR_KG = 0.00125;
    private static final double CLOTHES_FACTOR_KG = 8; 

    
    public CarbonRecord calculate(
//personal
            String name, String gender, String bodyType, String diet, String socialActivity,
            double height, double weight,
//travel
            String transportMode, String vehicleType, double vehicleMonthlyKm, String airTravelFrequency,
//waste
            String wasteBagSize, int wasteBagCount, List<String> recycledMaterials,
//energy
            String cookingSource, List<String> cookingSystems, String energyEfficiency,
            int pcTvHours, int internetHours,
//consumption
            String showerFrequency, double groceryBill, int clothesMonthly
    ) {
        double dietTotal = 0;
        double transportTotal = 0;
        double wasteTotal = 0;
        double energyTotal = 0;
        double consumptionTotal = 0;
switch (diet) {
            case "Omnivore": dietTotal += DIET_OMNIVORE_KG; break;
            case "Vegetarian": dietTotal += DIET_VEGETARIAN_KG; break;
            case "Vegan": dietTotal += DIET_VEGAN_KG; break;
        }
double vehicleFactor = 0;
        switch (transportMode) {
            case "Public": vehicleFactor = VEHICLE_PUBLIC_KG_KM; break;
            case "Walk/Bicycle": vehicleFactor = VEHICLE_WALK_BIKE_KG_KM; break;
            case "Private":
                switch (vehicleType) {
                    case "Petrol": vehicleFactor = VEHICLE_PETROL_KG_KM; break;
                    case "Diesel": vehicleFactor = VEHICLE_DIESEL_KG_KM; break;
                    case "Hybrid": vehicleFactor = VEHICLE_HYBRID_KG_KM; break;
                    case "LPG": vehicleFactor = VEHICLE_LPG_KG_KM; break;
                    case "Electric": vehicleFactor = VEHICLE_ELECTRIC_KG_KM; break;
                }
                break;
        }
        transportTotal += (vehicleMonthlyKm * 12) * vehicleFactor;

        switch (airTravelFrequency) {
            case "Never": transportTotal += AIR_NEVER_KG; break;
            case "Rarely (1-4 Hours)": transportTotal += AIR_RARELY_KG; break;
            case "Frequently (5-10 Hours)": transportTotal += AIR_FREQUENTLY_KG; break;
            case "Very Frequently (10+ Hours)": transportTotal += AIR_VERY_FREQUENTLY_KG; break;
        }
        double bagWeight = 0;
        switch (wasteBagSize) {
            case "Small (approx. 2kg)": bagWeight = BAG_SMALL_KG; break;
            case "Medium (approx. 5kg)": bagWeight = BAG_MEDIUM_KG; break;
            case "Large (approx. 10kg)": bagWeight = BAG_LARGE_KG; break;
            case "Extra Large (approx. 15kg)": bagWeight = BAG_EXTRA_LARGE_KG; break;
        }
        wasteTotal += (wasteBagCount * 52) * bagWeight * WASTE_FACTOR_KG;
        wasteTotal += (recycledMaterials.size() * RECYCLE_CREDIT_KG);

        switch (cookingSource) {
            case "Natural Gas": energyTotal += COOKING_GAS_KG; break;
            case "Electricity": energyTotal += COOKING_ELECTRICITY_KG; break;
            case "Wood": energyTotal += COOKING_WOOD_KG; break;
            case "Coal": energyTotal += COOKING_COAL_KG; break;
        }
        
        energyTotal += (cookingSystems.size() * COOKING_APPLIANCE_KG);
        energyTotal += (pcTvHours * 365) * PC_TV_FACTOR_KG_HR;
        energyTotal += (internetHours * 365) * INTERNET_FACTOR_KG_HR;
        
        if (energyEfficiency.equals("Yes")) {
            energyTotal *= 0.85; 
        } else if (energyEfficiency.equals("Sometimes")) {
            energyTotal *= 0.95;
        }
        double showerBase = SHOWER_FACTOR_KG;
        if (showerFrequency.equals("Twice a day")) showerBase *= 1.5;
        else if (showerFrequency.equals("More frequently")) showerBase *= 2.0;
        else if (showerFrequency.equals("Less frequently")) showerBase *= 0.7;
        consumptionTotal += showerBase;

        consumptionTotal += (groceryBill * 12) * GROCERY_FACTOR_KG;
        consumptionTotal += (clothesMonthly * 12) * CLOTHES_FACTOR_KG;

        double totalAnnualEmission = dietTotal + transportTotal + wasteTotal + energyTotal + consumptionTotal;
        
        String recycledStr = String.join(", ", recycledMaterials);
        String cookingStr = String.join(", ", cookingSystems);
        
        return new CarbonRecord(
            name, totalAnnualEmission, 
            dietTotal, transportTotal, wasteTotal, energyTotal, consumptionTotal,
            height, weight, bodyType, gender, diet,
            socialActivity, transportMode, vehicleType, vehicleMonthlyKm,
            airTravelFrequency, wasteBagSize, wasteBagCount, recycledStr,
            cookingSource, cookingStr, energyEfficiency, pcTvHours, internetHours,
            showerFrequency, groceryBill, clothesMonthly
        );
    }
}
