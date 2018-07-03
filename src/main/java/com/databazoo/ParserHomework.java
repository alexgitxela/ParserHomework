package com.databazoo;

import com.databazoo.logFileParser.IParser;
import com.databazoo.logFileParser.ParserImpl;
import com.databazoo.util.DbFactory;
import com.databazoo.util.Duration;
import com.databazoo.util.OutputType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserHomework {

    private static final String PARAM_DURATION = "--duration=";
    private static final String PARAM_START_DATE = "--startDate=";
    private static final String PARAM_THRESHOLD = "--threshold=";
    private static final String PARAM_FILENAME = "--accesslog=";
    private static final String PARAM_OUTPUT_TYPE = "--outputType=";
    private static final String PARAM_OUTPUT_FILE = "--outputFile=";

    // 2017-01-01.13:00:00
    private static final SimpleDateFormat FORMAT_START_DATE = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

    private static final String BAD_IP_TABLE_NAME = "badiplist";
    private static final String BAD_IP_FILE_NAME = "badIpList.txt"; // TARGET folder
    private static final String DELIMITER = "\t";

    public static void main(String[] args) {
        ParserHomework parserHomework = new ParserHomework();
        ParserConfig config = parserHomework.parseArgs(args);
        ParserResult result = parserHomework.parse(config);
        parserHomework.output(result, config);
    }

    private void output(ParserResult result, ParserConfig config) {
        switch (config.getOutputType()) {
            case file:
                try (FileOutputStream output = new FileOutputStream(config.getOutputFileName())) {
                    for (String key : result.getBadIp().keySet()) {
                        int value = result.getBadIp().get(key);
                        String line = key + DELIMITER + value + DELIMITER + String.valueOf(config.getDuration())
                                + DELIMITER + "Above the threshold (" + config.getThreshold() + ")\n";
                        // string to bytes
                        byte[] buffer = line.getBytes();
                        output.write(buffer, 0, buffer.length);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(BAD_IP_FILE_NAME + " A write error has occurred.", e);
                }
                break;
            case console:
                for (String key : result.getBadIp().keySet()) {
                    int value = result.getBadIp().get(key);
                    System.out.println(key + DELIMITER + value + DELIMITER + String.valueOf(config.getDuration())
                            + DELIMITER + "Above the threshold (" + config.getThreshold() + ")");
                }

//                for (Map.Entry<String, Integer> badIpLine : result.getBadIp().entrySet()) {
//                    System.out.println(badIpLine.getKey() + ": " + badIpLine.getValue());
//                }
                break;
            case database:
                try {
                    String sql = "TRUNCATE " + BAD_IP_TABLE_NAME;
                    PreparedStatement statement = DbFactory.getConnection().prepareStatement(sql);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new IllegalStateException("Could not truncate " + BAD_IP_TABLE_NAME, e);
                }
                for (String key : result.getBadIp().keySet()) {
                    int value = result.getBadIp().get(key);
                    // System.out.println(key + ": " + value);
                    Date date = new Date();
                    String sql = "INSERT INTO " + BAD_IP_TABLE_NAME + " (id, ip, cntr, duration, banTime, comment) VALUES (?, ?, ?, ?, ?, ?)";
                    try {
                        PreparedStatement statement = DbFactory.getConnection().prepareStatement(sql);
                        statement.setInt(1, 0);
                        statement.setString(2, key);
                        statement.setInt(3, value);
                        statement.setString(4, String.valueOf(config.getDuration()));
                        statement.setLong(5, date.getTime());
                        statement.setString(6, "Above the threshold (" + config.getThreshold() + ")");
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        throw new IllegalStateException("Could not insert into " + BAD_IP_TABLE_NAME, e);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown output type");
        }
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
                    throw new IllegalArgumentException("Start Date: Illegal format ", e);
                }

            } else if (arg.startsWith(PARAM_THRESHOLD)) {
                String substring = arg.substring(PARAM_THRESHOLD.length());
                config.setThreshold(Integer.parseInt(substring));

            } else if (arg.startsWith(PARAM_FILENAME)) {
                String substring = arg.substring(PARAM_FILENAME.length());
                config.setFileName(substring);
            }

            else if (arg.startsWith(PARAM_OUTPUT_TYPE)) {
                String substring = arg.substring(PARAM_OUTPUT_TYPE.length());
                config.setOutputType(OutputType.valueOf(substring));
            }

            else if (arg.startsWith(PARAM_OUTPUT_FILE)) {
                String substring = arg.substring(PARAM_OUTPUT_FILE.length());
                if (substring.length() == 0) {substring = BAD_IP_FILE_NAME;}
                config.setOutputFileName(substring);
            }

        }
        return config;
    }


}
