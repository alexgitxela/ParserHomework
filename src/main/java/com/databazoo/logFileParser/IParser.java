package com.databazoo.logFileParser;

import com.databazoo.ParserConfig;
import com.databazoo.ParserResult;

public interface IParser {
    ParserResult parse(ParserConfig config);
}
