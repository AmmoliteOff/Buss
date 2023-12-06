package org.hackathon.buss.service;

import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.Stop;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class IntegrationService {

    public int getPeopleCount() {
        Random random = new Random();
        return random.nextInt(1, 30);
    }

    public int getPeopleCount(Stop stop){
        Random random = new Random();
        return generateCustomValue(stop.getPeopleCount(), random);
    }

    private static int generateCustomValue(int initialValue, Random random) {
        double probability = random.nextDouble();

        if (probability < 0.65) {
            return (int) ((-1 * initialValue) + (1.5 * initialValue + 1) * random.nextDouble());
        } else if (probability < 0.9) {
            return (int) ((0.5 * initialValue) + (initialValue - 0.5 * initialValue + 1) * random.nextDouble());
        } else {
            int upperBound = Math.min((int) (1.5 * initialValue + 1), 30);
            return (int) ((initialValue) + (upperBound - initialValue) * random.nextDouble());
        }
    }

    public int getScore(Route route) {
        List<Integer> list = getScores(route);
        return (int) Math.ceil(list.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0));}

    public List<Integer> getScores(Route route) {
        List<Integer> trafficPoints = new ArrayList<>();
        int n = route.getWaypoints().size();
        double mean = 5.0;
        double standardDeviation = 2.0;

        Random random = new Random();

        for (int i = 0; i < n; i++) {
            double value = generateRandomValue(mean, standardDeviation, random);
            int boundedValue = boundValue(value, 0, 10);
            trafficPoints.add(boundedValue);
        }
        return trafficPoints;
    }

    private static double generateRandomValue(double mean, double standardDeviation, Random random) {
        return mean + standardDeviation * random.nextGaussian();
    }

    private static int boundValue(double value, int minValue, int maxValue) {
        return Math.min(Math.max((int) Math.round(value), minValue), maxValue);
    }
}
