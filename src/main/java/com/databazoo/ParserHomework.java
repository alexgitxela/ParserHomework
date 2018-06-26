package com.databazoo;

import com.databazoo.logFileParser.IParser;
import com.databazoo.logFileParser.ParserImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserHomework {

    public static void main(String[] args) {
        ParserHomework parserHomework = new ParserHomework();
        ParserConfig config = parserHomework.parseArgs(args);
        ParserResult result = parserHomework.parse(config);
    }

    private IParser parserService = new ParserImpl();

    private ParserResult parse(ParserConfig config) {
        return parserService.parse(config);
    }

    // 2017-01-01.13:00:00
    private static final SimpleDateFormat FORMAT_START_DATE = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");


    ParserConfig parseArgs(String[] args) {
        ParserConfig config = new ParserConfig();
        for (String arg : args) {
            if (arg.startsWith("--duration=")) {
                config.setDuration(arg.substring("--duration=".length()));

            } else if (arg.startsWith("--startDate=")) {
                // todo
                config.setStartDate(arg.substring("--startDate=".length()));
                try {
                    Date startDateTime = FORMAT_START_DATE.parse(arg.substring("--startDate=".length()));
                    System.out.println(startDateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (arg.startsWith("--threshold=")) {
                String substring = arg.substring("--threshold=".length());
                config.setThreshold(Integer.parseInt(substring));
            }
        }
        return config;
    }


}
