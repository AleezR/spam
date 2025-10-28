package carbonfp;


public class CarbonRecord {

  
    public String name;
    public String mode;
    public double transportKm;
    public double electricityKwh;
    public double garbageKg;
    public double totalCo2;

    public CarbonRecord(String name, String mode, double transportKm, double electricityKwh, double garbageKg, double totalCo2) {
        this.name = name;
        this.mode = mode;
        this.transportKm = transportKm;
        this.electricityKwh = electricityKwh;
        this.garbageKg = garbageKg;
        this.totalCo2 = totalCo2;
    }
}