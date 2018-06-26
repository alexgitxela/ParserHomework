package com.databazoo;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserHomeworkTest {

    @Test
    @Ignore
    public void main() {
        ParserHomework.main(new String[]{"--startDate=2017-01-01.13:00:00", "--duration=daily", "--threshold=250"});
    }

    @Test
    public void parseArgs() {
        String[] args = {"--startDate=2017-01-01.13:00:00", "--duration=daily", "--threshold=250"};

        ParserHomework parserHomework = new ParserHomework();
        ParserConfig config = parserHomework.parseArgs(args);

        assertEquals("daily", config.getDuration());
        assertEquals("2017-01-01.13:00:00", config.getStartDate());
        assertEquals(250, config.getThreshold());
    }
}