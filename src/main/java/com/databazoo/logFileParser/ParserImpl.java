package com.databazoo.logFileParser;

import com.databazoo.DbFactory;
import com.databazoo.ParserConfig;
import com.databazoo.ParserResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserImpl implements IParser {

    // 2017-01-01 00:00:11.763
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public ParserResult parse(ParserConfig config) {
        ParserResult result = new ParserResult();
        clearLogTable();
        parseLogFile(config.getFileName());
        return result;
    }

    private void clearLogTable() {
        // todo
    }

    private void parseLogFile(String fileName) {
        InputStream input;
        if (fileName == null) {
            input = this.getClass().getResourceAsStream("/access.log");
        } else {
            input = null;
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

        String sql = "INSERT INTO loglines (requestTime, requestIp) VALUES (?, ?)";
        PreparedStatement statement = DbFactory.getConnection().prepareStatement(sql);
        statement.setLong(1, requestTime.getTime());
        statement.setString(2, requestIP);
        statement.executeUpdate();
    }
}
