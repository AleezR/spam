package carbonfp;

public class FootprintCalculator {
    
   
    public CarbonRecord calculate(String name, String mode, double transport, double electricity, double garbage) {
        
        double transportFactor = 0.21;
        if (mode.equals("Bus")) {
            transportFactor = 0.089;
        } else if (mode.equals("Bike") || mode.equals("Walk")) {
            transportFactor = 0.0;
        }
        double transportEmission = transport * transportFactor;
        double electricityEmission = electricity * 0.5;
        double garbageEmission = garbage * 0.3;
        double totalEmission = transportEmission + electricityEmission + garbageEmission;
        return new CarbonRecord(name, mode,transport,electricity,garbage,totalEmission );
    }
}