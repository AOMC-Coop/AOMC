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
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`),
  KEY `channels_team_idx_idx` (`team_idx`),
  CONSTRAINT `channels_team_idx` FOREIGN KEY (`team_idx`) REFERENCES `teams` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `channels`
--

LOCK TABLES `channels` WRITE;
/*!40000 ALTER TABLE `channels` DISABLE KEYS */;
INSERT INTO `channels` VALUES (7,'general',15,'2019-01-22 13:30:29','2019-01-22 13:30:29'),(8,'general',16,'2019-01-22 13:30:39','2019-01-22 13:30:39'),(9,'love',15,'2019-01-22 15:42:52','2019-01-22 15:42:52'),(16,'general',23,'2019-01-23 21:25:16','2019-01-23 21:25:16'),(22,'general',29,'2019-01-23 21:31:26','2019-01-23 21:31:26'),(26,'general',33,'2019-01-24 10:06:26','2019-01-24 10:06:26'),(32,'general',39,'2019-01-24 12:45:56','2019-01-24 12:45:56'),(36,'general',43,'2019-01-24 12:57:36','2019-01-24 12:57:36'),(38,'general',45,'2019-01-24 13:11:21','2019-01-24 13:11:21'),(39,'general',46,'2019-01-24 13:13:08','2019-01-24 13:13:08'),(40,'general',47,'2019-01-24 13:23:48','2019-01-24 13:23:48');
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
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'joined #general',7,6,'2019-01-22 13:30:29',NULL),(2,'joined #general',8,6,'2019-01-22 13:30:39',NULL),(3,'joined #general',7,4,'2019-01-22 14:45:14',NULL),(10,'joined #general',16,6,'2019-01-23 21:25:16',NULL),(16,'joined #general',22,6,'2019-01-23 21:31:26',NULL),(20,'joined #general',26,4,'2019-01-24 10:06:26',NULL),(26,'joined #general',32,4,'2019-01-24 12:45:56',NULL),(30,'joined #general',36,4,'2019-01-24 12:57:36',NULL),(31,'joined #general',38,4,'2019-01-24 13:11:21',NULL),(32,'joined #general',39,4,'2019-01-24 13:13:08',NULL),(33,'joined #general',40,4,'2019-01-24 13:23:48',NULL);
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
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (15,'winterdev','2019-01-22 13:30:29','2019-01-22 14:27:04',1),(16,'aomc','2019-01-22 13:30:39','2019-01-22 14:27:01',1),(23,'testTeam','2019-01-23 21:25:16','2019-01-23 21:25:16',1),(29,'testTeam','2019-01-23 21:31:26','2019-01-23 21:31:26',1),(33,'testTeam','2019-01-24 10:06:26','2019-01-24 10:06:26',1),(39,'testTeam','2019-01-24 12:45:56','2019-01-24 12:45:56',1),(43,'testTeam','2019-01-24 12:57:36','2019-01-24 12:57:36',1),(45,'testTeam','2019-01-24 13:11:21','2019-01-24 13:11:21',1),(46,'testTeam','2019-01-24 13:13:08','2019-01-24 13:13:08',1),(47,'testTeam','2019-01-24 13:23:48','2019-01-24 13:23:48',1);
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_channel`
--

DROP TABLE IF EXISTS `user_has_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_has_channel` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `channel_idx` int(11) NOT NULL,
  `user_idx` int(11) NOT NULL,
  `star_flag` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idx`),
  KEY `user_has_channel_channel_idx_idx` (`channel_idx`),
  KEY `user_has_channel_user_idx_idx` (`user_idx`),
  CONSTRAINT `user_has_channel_channel_idx` FOREIGN KEY (`channel_idx`) REFERENCES `channels` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_has_channel_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_channel`
--

LOCK TABLES `user_has_channel` WRITE;
/*!40000 ALTER TABLE `user_has_channel` DISABLE KEYS */;
INSERT INTO `user_has_channel` VALUES (1,7,6,0,1),(2,7,4,0,0),(3,7,5,0,1),(4,8,6,0,1),(5,8,4,0,1),(6,8,5,0,1),(7,9,4,0,0),(8,9,5,0,1),(10,16,6,0,1),(11,16,4,0,1),(12,16,5,0,1),(16,22,6,0,1),(17,22,4,0,1),(18,22,5,0,1),(21,26,4,0,1),(22,26,5,0,1),(23,32,4,0,1),(28,36,5,0,1),(29,38,5,0,1),(30,39,5,0,1);
/*!40000 ALTER TABLE `user_has_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_team`
--

DROP TABLE IF EXISTS `user_has_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_has_team` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `team_idx` int(11) NOT NULL,
  `user_idx` int(11) NOT NULL,
  `join_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner_flag` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `certification_flag` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idx`),
  KEY `user_has_team_user_idx_idx` (`user_idx`),
  KEY `user_has_team_team_idx_idx` (`team_idx`),
  CONSTRAINT `user_has_team_team_idx` FOREIGN KEY (`team_idx`) REFERENCES `teams` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_has_team_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_team`
--

LOCK TABLES `user_has_team` WRITE;
/*!40000 ALTER TABLE `user_has_team` DISABLE KEYS */;
INSERT INTO `user_has_team` VALUES (8,15,6,'2019-01-22 13:30:29',1,1,0),(9,15,4,'2019-01-22 13:30:29',0,1,1),(10,15,5,'2019-01-22 13:30:29',0,1,0),(11,16,6,'2019-01-22 13:30:39',1,1,0),(12,16,4,'2019-01-22 13:30:39',0,1,0),(13,16,5,'2019-01-22 13:30:39',0,1,0),(14,15,7,'2019-01-22 15:34:01',0,1,0),(19,23,6,'2019-01-23 21:25:16',1,1,0),(20,23,4,'2019-01-23 21:25:16',0,1,0),(21,23,5,'2019-01-23 21:25:16',0,1,0),(30,29,6,'2019-01-23 21:31:26',1,1,0),(31,29,4,'2019-01-23 21:31:26',0,1,0),(32,29,5,'2019-01-23 21:31:26',0,1,0),(37,33,4,'2019-01-24 10:06:26',1,1,0),(38,33,5,'2019-01-24 10:06:26',0,1,0),(39,39,4,'2019-01-24 12:45:56',1,1,0),(40,39,4,'2019-01-24 12:45:56',0,1,0),(48,43,4,'2019-01-24 12:57:36',1,1,0),(49,43,5,'2019-01-24 12:57:36',0,1,0),(50,45,4,'2019-01-24 13:11:21',1,1,0),(51,45,5,'2019-01-24 13:11:21',0,1,0),(52,46,4,'2019-01-24 13:13:08',1,1,0),(53,46,5,'2019-01-24 13:13:08',0,1,0),(54,47,4,'2019-01-24 13:23:48',1,1,0);
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
  `role` char(5) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `access_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (4,'dmsal7325@naver.com','1234','1234','mooming',0,'0',1,'2019-01-22 10:34:16','2019-01-22 10:34:16','2019-01-22 10:34:16'),(5,'yunjea031296@gmail.com','1234','1234','yunyun',1,'0',1,'2019-01-22 10:35:08','2019-01-22 10:35:08','2019-01-22 10:35:08'),(6,'garamdaStarever222@gmail.com ','1234','1234','catman',1,'0',1,'2019-01-22 10:35:29','2019-01-22 10:35:29','2019-01-22 10:35:29'),(7,'dmsal2525@gmail.com','1234','1234','love',1,'0',1,'2019-01-22 14:51:27','2019-01-22 14:51:27','2019-01-22 14:51:27');
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

-- Dump completed on 2019-01-29 10:16:02
