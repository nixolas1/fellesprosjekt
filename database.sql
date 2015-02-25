-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u1
-- http://www.phpmyadmin.net
--
-- Vert: localhost
-- Generert den: 25. Feb, 2015 09:54 AM
-- Tjenerversjon: 5.5.41
-- PHP-Versjon: 5.4.36-0+deb7u3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `nixo_fp`
--

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `Appointment`
--

CREATE TABLE IF NOT EXISTS `Appointment` (
  `appointmentid` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(60) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `location` varchar(30) DEFAULT NULL,
  `starttime` datetime NOT NULL,
  `endtime` datetime NOT NULL,
  `repeatEndDate` date DEFAULT NULL,
  `repeat` int(11) DEFAULT NULL,
  `Calendar_calendarid` int(11) NOT NULL,
  `Room_roomid` int(11) NOT NULL,
  PRIMARY KEY (`appointmentid`,`Calendar_calendarid`),
  KEY `fk_Appointment_Calendar1_idx` (`Calendar_calendarid`),
  KEY `fk_Appointment_Room1_idx` (`Room_roomid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `Attendee`
--

CREATE TABLE IF NOT EXISTS `Attendee` (
  `Appointment_Calendar_calendarid` int(11) NOT NULL,
  `Appointment_appointmentid` int(11) NOT NULL,
  `User_domene` varchar(30) NOT NULL,
  `User_username` varchar(30) NOT NULL,
  `timeInvited` datetime DEFAULT NULL,
  `timeAnswered` datetime DEFAULT NULL,
  `willAttend` tinyint(1) DEFAULT NULL,
  `isOwner` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`Appointment_Calendar_calendarid`,`Appointment_appointmentid`,`User_domene`,`User_username`),
  KEY `fk_User_has_Appointment_Appointment1_idx` (`Appointment_appointmentid`,`Appointment_Calendar_calendarid`),
  KEY `fk_Attendee_User1_idx` (`User_domene`,`User_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `Calendar`
--

CREATE TABLE IF NOT EXISTS `Calendar` (
  `calendarid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(70) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  PRIMARY KEY (`calendarid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dataark for tabell `Calendar`
--

INSERT INTO `Calendar` (`calendarid`, `name`, `description`, `dateCreated`) VALUES
(1, 'Test', 'Hei det er en test.', '2015-02-25 15:22:36');

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `GroupCalendar`
--

CREATE TABLE IF NOT EXISTS `GroupCalendar` (
  `Calendar_calendarid` int(11) NOT NULL,
  `User_domene` varchar(30) NOT NULL,
  `User_username` varchar(30) NOT NULL,
  PRIMARY KEY (`Calendar_calendarid`,`User_domene`,`User_username`),
  KEY `fk_User_has_Calendar1_Calendar1_idx` (`Calendar_calendarid`),
  KEY `fk_GroupCalendar_User1_idx` (`User_domene`,`User_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `Notification`
--

CREATE TABLE IF NOT EXISTS `Notification` (
  `Appointment_appointmentid` int(11) NOT NULL,
  `Appointment_Calendar_calendarid` int(11) NOT NULL,
  `User_domene` varchar(30) NOT NULL,
  `User_username` varchar(30) NOT NULL,
  `text` varchar(200) DEFAULT NULL,
  `seen` tinyint(1) DEFAULT NULL,
  `sent` datetime DEFAULT NULL,
  PRIMARY KEY (`Appointment_appointmentid`,`Appointment_Calendar_calendarid`,`User_domene`,`User_username`),
  KEY `fk_User_has_Appointment_Appointment2_idx` (`Appointment_appointmentid`,`Appointment_Calendar_calendarid`),
  KEY `fk_Notification_User1_idx` (`User_domene`,`User_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `Room`
--

CREATE TABLE IF NOT EXISTS `Room` (
  `roomid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  PRIMARY KEY (`roomid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `Room_has_Utility`
--

CREATE TABLE IF NOT EXISTS `Room_has_Utility` (
  `Room_roomid` int(11) NOT NULL,
  `Utility_utilityid` int(11) NOT NULL,
  PRIMARY KEY (`Room_roomid`,`Utility_utilityid`),
  KEY `fk_Room_has_Utility_Utility1_idx` (`Utility_utilityid`),
  KEY `fk_Room_has_Utility_Room_idx` (`Room_roomid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `domene` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `passwordHash` varchar(256) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(60) NOT NULL,
  `phone` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`domene`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dataark for tabell `User`
--

INSERT INTO `User` (`domene`, `username`, `passwordHash`, `firstName`, `lastName`, `phone`) VALUES
('stud.ntnu.no', 'fuck', '38d0f91a99c57d189416439ce377ccdcd92639d0', 'Fuck', 'Your', 66666666),
('stud.ntnu.no', 'nicolaat', '85fe8de475bc9884da850bb5ac9dedaa50a5f850', 'Nicolas', 'Almagro Tonne', 48069930);

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `User_has_Calendar`
--

CREATE TABLE IF NOT EXISTS `User_has_Calendar` (
  `Calendar_calendarid` int(11) NOT NULL,
  `User_domene` varchar(30) NOT NULL,
  `User_username` varchar(30) NOT NULL,
  `color` varchar(7) DEFAULT NULL,
  `isVisible` tinyint(1) DEFAULT NULL,
  `isOwner` tinyint(1) DEFAULT NULL,
  `notifications` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`Calendar_calendarid`,`User_domene`,`User_username`),
  KEY `fk_User_has_Calendar_Calendar1_idx` (`Calendar_calendarid`),
  KEY `fk_User_has_Calendar_User1_idx` (`User_domene`,`User_username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dataark for tabell `User_has_Calendar`
--

INSERT INTO `User_has_Calendar` (`Calendar_calendarid`, `User_domene`, `User_username`, `color`, `isVisible`, `isOwner`, `notifications`) VALUES
(0, 'stud.ntnu.no', 'nicolaat', '#FF0000', 1, 1, 1);

-- --------------------------------------------------------

--
-- Tabellstruktur for tabell `Utility`
--

CREATE TABLE IF NOT EXISTS `Utility` (
  `utilityid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`utilityid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dataark for tabell `Utility`
--

INSERT INTO `Utility` (`utilityid`, `name`) VALUES
(1, 'Projektor');

--
-- Begrensninger for dumpede tabeller
--

--
-- Begrensninger for tabell `Appointment`
--
ALTER TABLE `Appointment`
  ADD CONSTRAINT `fk_Appointment_Calendar1` FOREIGN KEY (`Calendar_calendarid`) REFERENCES `Calendar` (`calendarid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Appointment_Room1` FOREIGN KEY (`Room_roomid`) REFERENCES `Room` (`roomid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Begrensninger for tabell `Attendee`
--
ALTER TABLE `Attendee`
  ADD CONSTRAINT `fk_User_has_Appointment_Appointment1` FOREIGN KEY (`Appointment_appointmentid`, `Appointment_Calendar_calendarid`) REFERENCES `Appointment` (`appointmentid`, `Calendar_calendarid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Attendee_User1` FOREIGN KEY (`User_domene`, `User_username`) REFERENCES `User` (`domene`, `username`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Begrensninger for tabell `GroupCalendar`
--
ALTER TABLE `GroupCalendar`
  ADD CONSTRAINT `fk_User_has_Calendar1_Calendar1` FOREIGN KEY (`Calendar_calendarid`) REFERENCES `Calendar` (`calendarid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_GroupCalendar_User1` FOREIGN KEY (`User_domene`, `User_username`) REFERENCES `User` (`domene`, `username`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Begrensninger for tabell `Notification`
--
ALTER TABLE `Notification`
  ADD CONSTRAINT `fk_User_has_Appointment_Appointment2` FOREIGN KEY (`Appointment_appointmentid`, `Appointment_Calendar_calendarid`) REFERENCES `Appointment` (`appointmentid`, `Calendar_calendarid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Notification_User1` FOREIGN KEY (`User_domene`, `User_username`) REFERENCES `User` (`domene`, `username`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Begrensninger for tabell `Room_has_Utility`
--
ALTER TABLE `Room_has_Utility`
  ADD CONSTRAINT `fk_Room_has_Utility_Room` FOREIGN KEY (`Room_roomid`) REFERENCES `Room` (`roomid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Room_has_Utility_Utility1` FOREIGN KEY (`Utility_utilityid`) REFERENCES `Utility` (`utilityid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Begrensninger for tabell `User_has_Calendar`
--
ALTER TABLE `User_has_Calendar`
  ADD CONSTRAINT `fk_User_has_Calendar_Calendar1` FOREIGN KEY (`Calendar_calendarid`) REFERENCES `Calendar` (`calendarid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_User_has_Calendar_User1` FOREIGN KEY (`User_domene`, `User_username`) REFERENCES `User` (`domene`, `username`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
