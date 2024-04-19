public class DistanceCalculator {
    static double calculateDistance(Coordinate coordinate1, Coordinate coordinate2) {
        double R = 6371;
        double dLat = Math.toRadians(coordinate2.latitude - coordinate1.latitude);
        double dLon = Math.toRadians(coordinate2.longitude - coordinate1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(coordinate1.latitude)) * Math.cos(Math.toRadians(coordinate1.latitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
