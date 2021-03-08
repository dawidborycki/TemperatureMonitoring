package com.temperaturemonitoring;

public class SensorReading {
    private String PartitionKey;
    private String RowKey;
    private double Temperature;    

    // PartitionKey
    public String getPartitionKey() { return this.PartitionKey; }
    public void setPartitionKey(String key) { this.PartitionKey = key; }

    // RowKey
    public String getRowKey() { return this.RowKey; }
    public void setRowKey(String key) { this.RowKey = key; }

    // Temperature
    public double getTemperature() { return this.Temperature; }
    public void setTemperature(double temperature) { this.Temperature = temperature; }
}