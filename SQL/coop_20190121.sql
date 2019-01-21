CREATE DATABASE  IF NOT EXISTS `coop` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `coop`;
-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: coop
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `admin` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `max_teams` int(11) NOT NULL,
  `max_channels` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,5,5);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `channels`
--

DROP TABLE IF EXISTS `channels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `channels` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `team_idx` int(11) NOT NULL,
  `user_idx` int(11) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `star_flag` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idx`),
  KEY `channels_team_idx_idx` (`team_idx`),
  KEY `channels_user_idx_idx` (`user_idx`),
  CONSTRAINT `channels_team_idx` FOREIGN KEY (`team_idx`) REFERENCES `teams` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `channels_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `channels`
--

LOCK TABLES `channels` WRITE;
/*!40000 ALTER TABLE `channels` DISABLE KEYS */;
/*!40000 ALTER TABLE `channels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comments` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `msg_idx` int(11) NOT NULL,
  `user_idx` int(11) NOT NULL,
  `send_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`),
  KEY `comments_msg_idx_idx` (`msg_idx`),
  KEY `comments_user_idx_idx` (`user_idx`),
  CONSTRAINT `comments_msg_idx` FOREIGN KEY (`msg_idx`) REFERENCES `messages` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comments_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `file` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(100) NOT NULL,
  `type` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `messages` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `channel_idx` int(11) NOT NULL,
  `user_idx` int(11) NOT NULL,
  `send_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `file_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `message_channel_idx_idx` (`channel_idx`),
  KEY `message_user_idx_idx` (`user_idx`),
  KEY `message_file_idx_idx` (`file_idx`),
  CONSTRAINT `message_channel_idx` FOREIGN KEY (`channel_idx`) REFERENCES `channels` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `message_file_idx` FOREIGN KEY (`file_idx`) REFERENCES `file` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `message_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `teams` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_team`
--

DROP TABLE IF EXISTS `user_has_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_has_team` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `user_idx` int(11) NOT NULL,
  `team_idx` int(11) NOT NULL,
  `join_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner_flag` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idx`),
  KEY `user_has_team_user_idx_idx` (`user_idx`),
  KEY `user_has_team_team_idx_idx` (`team_idx`),
  CONSTRAINT `user_has_team_team_idx` FOREIGN KEY (`team_idx`) REFERENCES `teams` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_has_team_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_team`
--

LOCK TABLES `user_has_team` WRITE;
/*!40000 ALTER TABLE `user_has_team` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_has_team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(45) NOT NULL,
  `pwd` varchar(80) NOT NULL,
  `salt` varchar(45) NOT NULL,
  `nickname` varchar(45) NOT NULL,
  `gender` tinyint(4) NOT NULL,
  `role` tinyint(4) NOT NULL DEFAULT '0',
  `status` varchar(45) NOT NULL DEFAULT 'active',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `access_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-21  9:41:45
