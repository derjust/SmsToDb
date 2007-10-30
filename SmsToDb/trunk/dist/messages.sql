CREATE TABLE IF NOT EXISTS `Messages` (
  `Id` int(10) unsigned NOT NULL auto_increment,
  `Time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `Originator` varchar(128) NOT NULL,
  `Recipient` varchar(128) NOT NULL,
  `Text` text NOT NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;