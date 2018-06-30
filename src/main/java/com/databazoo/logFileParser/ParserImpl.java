package com.databazoo.logFileParser;

import com.databazoo.DbFactory;
import com.databazoo.ParserConfig;
import com.databazoo.ParserResult;
import org.apache.commons.lang3.time.DateUtils;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        checkThreshold(config, result);
        return result;
    }

    private void checkThreshold(ParserConfig config, ParserResult result) {
        // TODO: select IPs by time and threshold. Values over threshold must be reported into a new table and ParseResult.

        Date timeFrom = config.getStartDate();
        Date timeTo = (Date) config.getStartDate().clone();
        timeTo = DateUtils.addHours(timeTo, config.getDuration().getHours());

        String sql = "SELECT requestIp, COUNT(*) AS CNT \n" +
                "         FROM `loglines` \n" +
                "         WHERE (requestTime >= ? AND requestTime <= ?) \n" +
                "         GROUP BY requestIp \n" +
                "         HAVING CNT > ?\n" +
                "         ORDER BY CNT DESC";

        try {
            PreparedStatement statement = DbFactory.getConnection().prepareStatement(sql);
            statement.setLong(1, timeFrom.getTime());
            statement.setLong(2, timeTo.getTime());
            statement.setInt(3, config.getThreshold());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
            }

        } catch (SQLException e) {
            System.out.println("Could not read: " + e.getMessage());
        }
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
        //System.out.println("time: " + requestTime + " IP: " + requestIP);

        String sql = "INSERT INTO " + LOG_TABLE_NAME + " (requestTime, requestIp) VALUES (?, ?)";
        PreparedStatement statement = DbFactory.getConnection().prepareStatement(sql);
        statement.setLong(1, requestTime.getTime());
        statement.setString(2, requestIP);
        statement.executeUpdate();
    }

}
