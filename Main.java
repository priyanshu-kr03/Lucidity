import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        DeliveryOptimizer deliveryOptimizer = new DeliveryOptimizer();

        // Define the locations
        deliveryOptimizer.locations.put("Aman", new Location(12.9357, 77.6101));  // Aman's location in Koramangala
        deliveryOptimizer.locations.put("R1", new Location(12.9279, 77.6271));    // Restaurant 1
        deliveryOptimizer.locations.put("R2", new Location(12.9354, 77.6221));    // Restaurant 2
        deliveryOptimizer.locations.put("C1", new Location(12.9345, 77.6139));    // Consumer 1
        deliveryOptimizer.locations.put("C2", new Location(12.9380, 77.6145));    // Consumer 2

        // Define preparation times at each restaurant (in minutes)
        deliveryOptimizer.prepTimes.put("R1", 15);
        deliveryOptimizer.prepTimes.put("R2", 20);

        deliveryOptimizer.n = deliveryOptimizer.locations.size();
        deliveryOptimizer.VISITED_ALL = (1 << deliveryOptimizer.n) - 1;

        deliveryOptimizer.calculateDistances();
        List<Integer> path = deliveryOptimizer.solveTSP();

        System.out.println("Optimal path:");
        for (int i : path) {
            System.out.println(deliveryOptimizer.locations.keySet().toArray()[i]);
        }

        double totalTime = deliveryOptimizer.calculateTotalTime(path);
        System.out.println("Total time required: " + totalTime + " hours");
    }
}
