package com.databazoo;

import com.databazoo.logFileParser.IParser;
import com.databazoo.logFileParser.ParserImpl;

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

    ParserConfig parseArgs(String[] args) {
        ParserConfig config = new ParserConfig();
        for (String arg : args) {
            if (arg.startsWith("--duration=")) {
                config.setDuration(arg.substring("--duration=".length()));

            } else if (arg.startsWith("--startDate=")) {
                //todo
                config.setStartDate(arg.substring("--startDate=".length()));

            } else if (arg.startsWith("--threshold=")) {
                String substring = arg.substring("--threshold=".length());
                config.setThreshold(Integer.parseInt(substring));
            }
        }
        return config;
    }


}
