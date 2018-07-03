
--
-- Table structure for table `loglines`
--

CREATE TABLE `loglines` (
  `id` int(11) NOT NULL auto_increment,
  `requestTime` varchar(13) default NULL,
  `requestIp` varchar(15) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--
-- Table structure for table `badiplist`
--

CREATE TABLE `badiplist` (
  `id` int(11) NOT NULL auto_increment,
  `ip` varchar(15) NOT NULL,
  `cntr` int(11) NOT NULL,
  `duration` varchar(6) NOT NULL,
  `banTime` bigint(20) default NULL,
  `comment` varchar(64) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--
--
--
