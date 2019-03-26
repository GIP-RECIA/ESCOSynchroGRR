package fr.recia.grr.utils;

public class DateBatch {
    private String lastBatch;
    private String currentBatch;

    public DateBatch(String lastBatch, String currentBatch) {
        this.lastBatch = lastBatch;
        this.currentBatch = currentBatch;
    }

    public String getLastBatch() {
        return lastBatch;
    }

    public void setLastBatch(String lastBatch) {
        this.lastBatch = lastBatch;
    }

    public String getCurrentBatch() {
        return currentBatch;
    }

    public void setCurrentBatch(String currentBatch) {
        this.currentBatch = currentBatch;
    }
}
