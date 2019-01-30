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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `channels`
--

LOCK TABLES `channels` WRITE;
/*!40000 ALTER TABLE `channels` DISABLE KEYS */;
INSERT INTO `channels` VALUES (7,'general',15,'2019-01-22 13:30:29','2019-01-22 13:30:29'),(8,'general',16,'2019-01-22 13:30:39','2019-01-22 13:30:39'),(9,'love',15,'2019-01-22 15:42:52','2019-01-22 15:42:52'),(40,'general',47,'2019-01-24 13:23:48','2019-01-24 13:23:48'),(42,'aomc-test',16,'2019-01-28 18:31:13','2019-01-28 18:31:13'),(44,'aomc-test2',16,'2019-01-28 19:06:08','2019-01-28 19:06:08'),(45,'aomc-test3',16,'2019-01-28 19:54:14','2019-01-28 19:54:14'),(48,'aomc-test4',16,'2019-01-29 09:51:54','2019-01-29 09:51:54');
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
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'joined #general',7,5,'2019-01-22 13:30:29',NULL),(2,'joined #general',8,6,'2019-01-22 13:30:39',NULL),(33,'joined #general',40,4,'2019-01-24 13:23:48',NULL),(34,'hi',9,4,'2019-01-25 16:12:04',NULL),(78,'hi',40,4,'2019-01-25 19:11:12',NULL),(91,'joined #aomc-test',42,4,'2019-01-28 18:31:13',NULL),(93,'joined #aomc-test2',44,5,'2019-01-28 19:06:08',NULL),(94,'joined #aomc-test3',45,5,'2019-01-28 19:54:14',NULL),(97,'joined #aomc-test4',48,5,'2019-01-29 09:51:54',NULL),(98,'hello1',8,4,'2019-01-29 10:48:13',NULL),(99,'helloo2',8,4,'2019-01-29 10:48:30',NULL),(100,'helloo3',8,4,'2019-01-29 10:48:34',NULL),(101,'helloo4',8,4,'2019-01-29 10:48:35',NULL),(102,'hellooooo5',8,4,'2019-01-29 10:48:40',NULL),(103,'helloooooooooooo6',8,5,'2019-01-29 13:54:59',NULL),(104,'helloooooooooooo7',8,5,'2019-01-29 13:57:51',NULL),(105,'helloooooooooooo8',8,5,'2019-01-29 13:57:56',NULL),(106,'helloooooooooooo9',8,5,'2019-01-29 13:57:56',NULL),(107,'helloooooooooooo10',8,5,'2019-01-29 13:57:56',NULL),(108,'helloooooooooooo11',8,5,'2019-01-29 13:57:56',NULL),(109,'helloooooooooooo12',8,5,'2019-01-29 13:57:56',NULL),(110,'helloooooooooooo13',8,5,'2019-01-29 13:57:57',NULL),(111,'helloooooooooooo14',8,5,'2019-01-29 13:57:57',NULL),(112,'helloooooooooooo15',8,5,'2019-01-29 13:57:57',NULL),(113,'helloooooooooooo16',8,5,'2019-01-29 13:57:57',NULL),(114,'helloooooooooooo17',8,5,'2019-01-29 13:57:58',NULL),(115,'helloooooooooooo18',8,5,'2019-01-29 13:57:58',NULL),(116,'helloooooooooooo19',8,5,'2019-01-29 13:57:58',NULL),(117,'helloooooooooooo20',8,5,'2019-01-29 13:57:58',NULL),(118,'helloooooooooooo21',8,5,'2019-01-29 13:57:58',NULL),(119,'helloooooooooooo22',8,5,'2019-01-29 13:57:59',NULL),(120,'helloooooooooooo23',8,5,'2019-01-29 13:57:59',NULL),(121,'helloooooooooooo24',8,5,'2019-01-29 13:57:59',NULL),(122,'helloooooooooooo25',8,5,'2019-01-29 13:57:59',NULL),(123,'helloooooooooooo26',8,5,'2019-01-29 13:58:00',NULL),(124,'helloooooooooooo27',8,5,'2019-01-29 13:58:00',NULL),(125,'helloooooooooooo28',8,5,'2019-01-29 13:58:00',NULL),(126,'helloooooooooooo29',8,5,'2019-01-29 13:58:00',NULL),(127,'helloooooooooooo30',8,5,'2019-01-29 13:58:01',NULL),(128,'helloooooooooooo31',8,5,'2019-01-29 13:58:01',NULL),(129,'helloooooooooooo32',8,5,'2019-01-29 13:58:01',NULL),(130,'helloooooooooooo33',8,5,'2019-01-29 13:58:01',NULL),(131,'helloooooooooooo34',8,5,'2019-01-29 13:58:01',NULL),(132,'helloooooooooooo35',8,5,'2019-01-29 13:58:02',NULL),(133,'helloooooooooooo36',8,5,'2019-01-29 13:58:02',NULL),(134,'helloooooooooooo37',8,5,'2019-01-29 13:58:02',NULL),(135,'helloooooooooooo38',8,5,'2019-01-29 13:58:02',NULL),(136,'helloooooooooooo39',8,5,'2019-01-29 13:58:02',NULL),(137,'helloooooooooooo40',8,5,'2019-01-29 13:58:02',NULL),(138,'41',8,5,'2019-01-29 13:58:03',NULL),(139,'helloooooooooooo42',8,5,'2019-01-29 13:58:03',NULL);
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
INSERT INTO `teams` VALUES (15,'winterdev','2019-01-22 13:30:29','2019-01-22 14:27:04',1),(16,'aomc','2019-01-22 13:30:39','2019-01-22 14:27:01',1),(47,'testTeam','2019-01-24 13:23:48','2019-01-24 13:23:48',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_channel`
--

LOCK TABLES `user_has_channel` WRITE;
/*!40000 ALTER TABLE `user_has_channel` DISABLE KEYS */;
INSERT INTO `user_has_channel` VALUES (1,7,6,0,1),(2,7,4,0,0),(3,7,5,0,1),(4,8,6,0,1),(5,8,4,0,1),(6,8,5,0,1),(7,9,4,0,0),(8,9,5,0,1),(42,7,5,0,1),(43,7,6,0,1),(44,7,7,0,1),(45,42,4,0,1),(46,42,5,0,1),(50,44,5,0,1),(51,44,4,0,1),(52,44,6,0,1),(53,45,5,0,1),(54,45,4,0,1),(55,45,6,0,1),(60,48,5,0,1),(61,48,4,0,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_team`
--

LOCK TABLES `user_has_team` WRITE;
/*!40000 ALTER TABLE `user_has_team` DISABLE KEYS */;
INSERT INTO `user_has_team` VALUES (8,15,6,'2019-01-22 13:30:29',1,1,0),(9,15,4,'2019-01-22 13:30:29',0,1,1),(11,16,6,'2019-01-22 13:30:39',1,1,0),(12,16,4,'2019-01-22 13:30:39',0,1,0),(13,16,5,'2019-01-22 13:30:39',0,1,0),(14,15,7,'2019-01-22 15:34:01',0,1,1),(54,47,4,'2019-01-24 13:23:48',1,1,0),(69,15,5,'2019-01-28 14:51:51',0,1,1),(70,15,6,'2019-01-28 15:09:10',0,1,0),(71,15,7,'2019-01-28 15:09:14',0,1,1);
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
INSERT INTO `users` VALUES (4,'dmsal7325@naver.com','1234','1234','mooming',0,0,1,'2019-01-22 10:34:16','2019-01-22 10:34:16','2019-01-22 10:34:16'),(5,'yunjea0312@naver.com','1234','1234','yunyun',1,0,1,'2019-01-22 10:35:08','2019-01-22 10:35:08','2019-01-22 10:35:08'),(6,'garamdaStarever222@gmail.com ','1234','1234','catman',1,0,1,'2019-01-22 10:35:29','2019-01-22 10:35:29','2019-01-22 10:35:29'),(7,'dmsal2525@gmail.com','1234','1234','love',1,0,1,'2019-01-22 14:51:27','2019-01-22 14:51:27','2019-01-22 14:51:27');
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

-- Dump completed on 2019-01-29 19:00:30
