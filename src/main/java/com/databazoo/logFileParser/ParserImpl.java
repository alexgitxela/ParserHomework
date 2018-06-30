package com.databazoo.logFileParser;

import com.databazoo.DbFactory;
import com.databazoo.ParserConfig;
import com.databazoo.ParserResult;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserImpl implements IParser {

    // 2017-01-01 00:00:11.763
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final String LOG_TABLE_NAME = "loglines";

    public ParserResult parse(ParserConfig config) {
        ParserResult result = new ParserResult();
        clearLogTable();
        parseLogFile(config.getFileName(), result);
        //checkThreshold(config, result);
        return result;
    }

    private void checkThreshold(ParserConfig config, ParserResult result) {
        // TODO: select IPs by time and threshold. Values over threshold must be reported into a new table and ParseResult.

        // test
        final SimpleDateFormat FORMAT_START_DATE = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
        String timeStamp = "2017-01-01.13:00:00";
        Date stopDateInterval = null;
        Date startDateInterval = null;
        try {
            startDateInterval = FORMAT_START_DATE.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(startDateInterval);
        timeStamp = "2017-01-01.14:00:00";
        try {
            stopDateInterval = FORMAT_START_DATE.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(stopDateInterval);
        // SELECT requestIp, COUNT(*) FROM `loglines` WHERE (requestTime >= 1483225211763 AND requestTime <= 1483279200000) GROUP BY requestIp ORDER BY COUNT(*) DESC
    }

    private void clearLogTable() {
        try {
            String sql = "TRUNCATE " + LOG_TABLE_NAME;
            PreparedStatement statement = DbFactory.getConnection().prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Could not truncate " + LOG_TABLE_NAME + e.getMessage());
        }
    }

    private void parseLogFile(String fileName, ParserResult result) {
        InputStream input;
        if (fileName == null) {
            input = this.getClass().getResourceAsStream("/access.log");
        } else {
            try {
                input = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("File not found WTF", e);
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    processLine(line);
                } catch (ParseException | SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processLine(String line) throws ParseException, SQLException {
        String[] strings = line.split("\\|");
        Date requestTime = FORMAT.parse(strings[0]);
        String requestIP = strings[1];
        System.out.println("time: " + requestTime + " IP: " + requestIP);

        String sql = "INSERT INTO " + LOG_TABLE_NAME + " (requestTime, requestIp) VALUES (?, ?)";
        PreparedStatement statement = DbFactory.getConnection().prepareStatement(sql);
        statement.setLong(1, requestTime.getTime());
        statement.setString(2, requestIP);
        statement.executeUpdate();
    }

}
