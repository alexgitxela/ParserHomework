package com.databazoo;

import com.databazoo.logFileParser.IParser;
import com.databazoo.logFileParser.ParserImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserHomework {

    private static final String PARAM_DURATION = "--duration=";
    private static final String PARAM_START_DATE = "--startDate=";
    private static final String PARAM_THRESHOLD = "--threshold=";

    // 2017-01-01.13:00:00
    private static final SimpleDateFormat FORMAT_START_DATE = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

    public static void main(String[] args) {
        ParserHomework parserHomework = new ParserHomework();
        ParserConfig config = parserHomework.parseArgs(args);
        ParserResult result = parserHomework.parse(config);
    }

    private IParser parserService = new ParserImpl();

    private ParserResult parse(ParserConfig config) {
        return parserService.parse(config);
    }

    ParserConfig parseArgs(String[] args) {
        ParserConfig config = new ParserConfig();
        for (String arg : args) {
            if (arg.startsWith(PARAM_DURATION)) {
                String stringDuration = arg.substring(PARAM_DURATION.length());
                config.setDuration(Duration.valueOf(stringDuration));

            } else if (arg.startsWith(PARAM_START_DATE)) {
                try {
                    Date startDateTime = FORMAT_START_DATE.parse(arg.substring(PARAM_START_DATE.length()));
                    config.setStartDate(startDateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (arg.startsWith(PARAM_THRESHOLD)) {
                String substring = arg.substring(PARAM_THRESHOLD.length());
                config.setThreshold(Integer.parseInt(substring));
            }
        }
        return config;
    }


}
