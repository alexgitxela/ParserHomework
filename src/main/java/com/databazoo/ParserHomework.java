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

    private ParserConfig parseArgs(String[] args) {
        ParserConfig config = new ParserConfig();
        // todo: parseArgs into config
        return config;
    }


}
