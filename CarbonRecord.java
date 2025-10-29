package carbonfp;

public class CarbonRecord {
    public double totalCo2; 
    
    public double dietEmission;
    public double transportEmission;
    public double wasteEmission;
    public double energyEmission;
    public double consumptionEmission;

    public String name;
    public String transportMode;
    public double height;
    public double weight;
    public String bodyType;
    public String gender;
    public String diet;
    public String socialActivity;
    
    public String vehicleType;
    public double vehicleMonthlyKm;
    public String airTravelFrequency;
    
    public String wasteBagSize;
    public int wasteBagCount;
    public String recycledMaterials;
    
    public String cookingSource; 
    public String cookingSystems; 
    public String energyEfficiency;
    public int pcTvHours;
    public int internetHours;
    
    public String showerFrequency;
    public double groceryBill;
    public int clothesMonthly;

    public CarbonRecord(String name, double totalCo2, 
                        double dietEmission, double transportEmission, double wasteEmission, double energyEmission, double consumptionEmission,
                        double height, double weight,
                        String bodyType, String gender, String diet, String socialActivity,
                        String transportMode, String vehicleType, double vehicleMonthlyKm,
                        String airTravelFrequency, String wasteBagSize, int wasteBagCount,
                        String recycledMaterials, String cookingSource, String cookingSystems,
                        String energyEfficiency, int pcTvHours, int internetHours,
                        String showerFrequency, double groceryBill, int clothesMonthly) {
        
        this.name = name;
        this.totalCo2 = totalCo2;

        this.dietEmission = dietEmission;
        this.transportEmission = transportEmission;
        this.wasteEmission = wasteEmission;
        this.energyEmission = energyEmission;
        this.consumptionEmission = consumptionEmission;

        this.height = height;
        this.weight = weight;
        this.bodyType = bodyType;
        this.gender = gender;
        this.diet = diet;
        this.socialActivity = socialActivity;
        this.transportMode = transportMode;
        this.vehicleType = vehicleType;
        this.vehicleMonthlyKm = vehicleMonthlyKm;
        this.airTravelFrequency = airTravelFrequency;
        this.wasteBagSize = wasteBagSize;
        this.wasteBagCount = wasteBagCount;
        this.recycledMaterials = recycledMaterials;
        this.cookingSource = cookingSource;
        this.cookingSystems = cookingSystems;
        this.energyEfficiency = energyEfficiency;
        this.pcTvHours = pcTvHours;
        this.internetHours = internetHours;
        this.showerFrequency = showerFrequency;
        this.groceryBill = groceryBill;
        this.clothesMonthly = clothesMonthly;
    }
}
