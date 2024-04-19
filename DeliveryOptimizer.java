import java.lang.reflect.Array;
import java.util.*;

public class DeliveryOptimizer {
    Map<String, Location> locations = new HashMap<>();
    Map<String, Integer> prepTimes = new HashMap<>();

    double[][] distances;
    int n;
    int VISITED_ALL;

    // Function to calculate distance between two locations using Haversine formula
    double haversine(Location loc1, Location loc2) {
        double R = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(loc2.latitude - loc1.latitude);
        double dLon = Math.toRadians(loc2.longitude - loc1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(loc1.latitude)) * Math.cos(Math.toRadians(loc2.latitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Calculate distances between all pairs of locations
    void calculateDistances() {
        distances = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Location loc1 = locations.get(locations.keySet().toArray()[i]);
                Location loc2 = locations.get(locations.keySet().toArray()[j]);
                distances[i][j] = haversine(loc1, loc2);
            }
        }
    }

    // Define the TSP function
    List<Integer> solveTSP() {
        double[][] dp = new double[n][1 << n];
        int[][] parent = new int[n][1 << n];

        for (double[] row : dp) {
            Arrays.fill(row, -1);
        }

        for (int[] row : parent) {
            Arrays.fill(row, -1);
        }

        return tsp(0, 1, dp, parent);
    }

    List<Integer> tsp(int currentIndex, int mask, double[][] dp, int[][] parent) {
        if (mask == VISITED_ALL) {
            List<Integer> path = new ArrayList<>();
            path.add(currentIndex);
            return path;
        }
        if (dp[currentIndex][mask] != -1) {
            return constructPath(currentIndex, mask, parent);
        }

        List<Integer> minPath = null;
        double minDistance = Double.MAX_VALUE;

        for (int nextIndex = 0; nextIndex < n; nextIndex++) {
            if ((mask & (1 << nextIndex)) == 0) {
                double distance = distances[currentIndex][nextIndex];
                List<Integer> path = tsp(nextIndex, mask | (1 << nextIndex), dp, parent);
                double totalDistance = distance + calculateTotalDistance(path);
                if (totalDistance < minDistance) {
                    minDistance = totalDistance;
                    minPath = path;
                    minPath.add(0, currentIndex);
                }
            }
        }

        dp[currentIndex][mask] = minDistance;
        parent[currentIndex][mask] = minPath.get(1); // Next index in the path

        return minPath;
    }

    List<Integer> constructPath(int currentIndex, int mask, int[][] parent) {
        List<Integer> path = new ArrayList<>();
        path.add(currentIndex);

        while (parent[currentIndex][mask] != -1) {
            int nextIndex = parent[currentIndex][mask];
            path.add(nextIndex);
            mask |= (1 << nextIndex);
            currentIndex = nextIndex;
        }

        return path;
    }

    double calculateTotalDistance(List<Integer> path) {
        double totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int loc1 = path.get(i);
            int loc2 = path.get(i + 1);
            totalDistance += distances[loc1][loc2];
        }
        return totalDistance;
    }

    double calculateTotalTime(List<Integer> path) {
        double totalTime = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String loc1 = (String) locations.keySet().toArray()[path.get(i)];
            String loc2 = (String) locations.keySet().toArray()[path.get(i + 1)];
            totalTime += distances[path.get(i)][path.get(i + 1)] / 20; // Assuming average speed of 20 km/hr
            if (prepTimes.containsKey(loc2)) {
                totalTime += prepTimes.get(loc2) / 60.0; // Convert preparation time from minutes to hours
            }
        }
        return totalTime;
    }

}