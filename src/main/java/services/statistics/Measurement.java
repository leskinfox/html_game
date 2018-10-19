package services.statistics;

public class Measurement {
    long pageTime;
    long dbTime;
    long pageTimeStart;
    long dbTimeStart;
    int dbRequestCount;

    public void reset() {
        pageTime = dbTime = pageTimeStart = dbTimeStart = dbRequestCount = 0;
    }
}
