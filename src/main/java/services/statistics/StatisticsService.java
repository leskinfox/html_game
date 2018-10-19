package services.statistics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticsService {
    private final Map<String, Measurement> users;

    public StatisticsService() {
        users = new ConcurrentHashMap<>();
    }

    private Measurement getMeasurement(String sessionId) {
        if(users.containsKey(sessionId))
            return users.get(sessionId);
        Measurement measurement = new Measurement();
        users.put(sessionId, measurement);
        return measurement;
    }

    public void reset(String sessionId) {
        getMeasurement(sessionId).reset();
    }

    public void startPageTimer(String sessionId) {
        Measurement measurement = getMeasurement(sessionId);
        measurement.pageTimeStart = System.currentTimeMillis();
    }

    public void startDBTimer(String sessionId) {
        Measurement measurement = getMeasurement(sessionId);
        measurement.dbTimeStart = System.currentTimeMillis();
    }

    public void stopPageTimer(String sessionId) {
        Measurement measurement = getMeasurement(sessionId);
        measurement.pageTime += System.currentTimeMillis() - measurement.pageTimeStart;
    }

    public void stopDBTimer(String sessionId) {
        Measurement measurement = getMeasurement(sessionId);
        measurement.dbTime += System.currentTimeMillis() - measurement.dbTimeStart;
    }

    public void addRequest(String sessionId) {
        Measurement measurement = getMeasurement(sessionId);
        measurement.dbRequestCount++;
    }

    public String getInfo(String sessionId) {
        Measurement measurement = getMeasurement(sessionId);
        return "page:" + measurement.pageTime + " ms, services.db:" + measurement.dbRequestCount + "req ("
                + measurement.dbTime + "ms)";
    }
}
