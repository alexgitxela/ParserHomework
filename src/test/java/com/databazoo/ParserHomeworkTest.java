package com.databazoo;

import com.databazoo.util.Duration;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserHomeworkTest {

    @Test
    @Ignore
    public void main() {
        ParserHomework.main(new String[]{"--startDate=2017-01-01.13:00:00", "--duration=daily", "--threshold=250", "--accesslog=E:\\git\\ParserHomework\\src\\test\\resources\\access.log"});
    }

    @Test
    public void parseArgs() {
        String[] args = {"--startDate=2017-01-01.13:00:00", "--duration=daily", "--threshold=250", "--accesslog=/path/to/file"};

        ParserHomework parserHomework = new ParserHomework();
        ParserConfig config = parserHomework.parseArgs(args);

        assertEquals(Duration.daily, config.getDuration());
        assertEquals("Sun Jan 01 13:00:00 CET 2017", config.getStartDate().toString());
        assertEquals(250, config.getThreshold());
        assertEquals("/path/to/file", config.getFileName());
    }

    @Test
    public void parseDurationValid() {
        String[] args = {"--startDate=2017-01-01.13:00:00", "--duration=daily", "--threshold=250"};

        ParserHomework parserHomework = new ParserHomework();

        ParserConfig config = parserHomework.parseArgs(args);
        assertEquals(Duration.daily, config.getDuration());

        config = parserHomework.parseArgs(new String[]{"--duration=hourly"});
        assertEquals(Duration.hourly, config.getDuration());
    }

    @Test (expected = IllegalArgumentException.class)
    public void parseDurationInvalid() {
        String[] args = {"--startDate=2017-01-01.13:00:00", "--duration=weekly", "--threshold=250"};

        ParserHomework parserHomework = new ParserHomework();
        parserHomework.parseArgs(args);

    }
}