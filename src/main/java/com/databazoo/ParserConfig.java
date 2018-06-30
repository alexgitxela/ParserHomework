package com.databazoo;

import java.util.Date;

public class ParserConfig {
    private String fileName;
    private Duration duration;
    private Date startDate;
    private int threshold;
    private OutputType outputType = OutputType.console;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }
}
