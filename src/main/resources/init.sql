
--
-- Table structure for table `loglines`
--

CREATE TABLE `loglines` (
  `requestTime` varchar(13) default NULL,
  `requestIp` varchar(15) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
--
--

--
-- Table structure for table `badiplist`
--

CREATE TABLE `badiplist` (
  `ip` varchar(15) NOT NULL,
  `cntr` int(11) NOT NULL,
  `duration` varchar(6) NOT NULL,
  `bantime` bigint(20) default NULL,
  `comment` varchar(64) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `badiplist`
--
