CREATE DATABASE  IF NOT EXISTS `coop` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `coop`;
-- MySQL dump 10.13  Distrib 8.0.13, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: coop
-- ------------------------------------------------------
-- Server version	8.0.12

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
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `channels`
--

LOCK TABLES `channels` WRITE;
/*!40000 ALTER TABLE `channels` DISABLE KEYS */;
INSERT INTO `channels` VALUES (7,'general',15,'2019-01-22 13:30:29','2019-01-22 13:30:29'),(8,'general',16,'2019-01-22 13:30:39','2019-01-22 13:30:39'),(9,'love',15,'2019-01-22 15:42:52','2019-01-22 15:42:52'),(40,'general',47,'2019-01-24 13:23:48','2019-01-24 13:23:48'),(42,'aomc-test',16,'2019-01-28 18:31:13','2019-01-28 18:31:13'),(44,'aomc-test2',16,'2019-01-28 19:06:08','2019-01-28 19:06:08'),(45,'aomc-test3',16,'2019-01-28 19:54:14','2019-01-28 19:54:14'),(48,'aomc-test4',16,'2019-01-29 09:51:54','2019-01-29 09:51:54'),(49,'general',48,'2019-01-31 18:41:58','2019-01-31 18:41:58'),(50,'general',49,'2019-01-31 18:56:01','2019-01-31 18:56:01'),(51,'general',50,'2019-01-31 18:58:10','2019-01-31 18:58:10'),(52,'general',51,'2019-01-31 19:02:31','2019-01-31 19:02:31'),(53,'general',52,'2019-01-31 19:03:11','2019-01-31 19:03:11'),(54,'general',53,'2019-01-31 19:04:19','2019-01-31 19:04:19'),(55,'general',54,'2019-01-31 19:06:38','2019-01-31 19:06:38'),(56,'general',55,'2019-01-31 19:14:10','2019-01-31 19:14:10'),(57,'general',56,'2019-01-31 19:16:14','2019-01-31 19:16:14'),(58,'general',57,'2019-01-31 19:17:15','2019-01-31 19:17:15'),(59,'general',58,'2019-01-31 19:21:05','2019-01-31 19:21:05'),(60,'general',59,'2019-01-31 19:22:16','2019-01-31 19:22:16'),(61,'general',60,'2019-01-31 19:23:45','2019-01-31 19:23:45'),(62,'general',61,'2019-01-31 19:26:37','2019-01-31 19:26:37'),(63,'general',62,'2019-01-31 19:47:42','2019-01-31 19:47:42'),(64,'general',63,'2019-01-31 19:48:33','2019-01-31 19:48:33'),(65,'general',64,'2019-01-31 19:51:37','2019-01-31 19:51:37'),(66,'general',65,'2019-01-31 19:53:36','2019-01-31 19:53:36'),(74,'general',72,'2019-02-01 11:08:31','2019-02-01 11:08:31');
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
  `msg_idx` int(11) NOT NULL,
  `channel_idx` int(11) NOT NULL,
  `user_idx` int(11) NOT NULL,
  `content` text NOT NULL,
  `send_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`),
  KEY `comments_user_idx_idx` (`user_idx`),
  KEY `comments_message_idx_idx` (`msg_idx`),
  KEY `comments_channel_idx_idx` (`channel_idx`),
  CONSTRAINT `comments_channel_idx` FOREIGN KEY (`channel_idx`) REFERENCES `messages` (`channel_idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comments_message_idx` FOREIGN KEY (`msg_idx`) REFERENCES `messages` (`message_idx`) ON DELETE CASCADE ON UPDATE CASCADE,
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
  `message_idx` int(11) NOT NULL,
  `channel_idx` int(11) NOT NULL,
  `content` text NOT NULL,
  `user_idx` int(11) NOT NULL,
  `send_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `file_idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`message_idx`,`channel_idx`),
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
INSERT INTO `messages` VALUES (1,7,'joined #general',5,'2019-01-22 13:30:29',NULL),(2,8,'joined #general',6,'2019-01-22 13:30:39',NULL),(33,40,'joined #general',4,'2019-01-24 13:23:48',NULL),(34,9,'hi',4,'2019-01-25 16:12:04',NULL),(78,40,'hi',4,'2019-01-25 19:11:12',NULL),(91,42,'joined #aomc-test',4,'2019-01-28 18:31:13',NULL),(93,44,'joined #aomc-test2',5,'2019-01-28 19:06:08',NULL),(94,45,'joined #aomc-test3',5,'2019-01-28 19:54:14',NULL),(97,48,'joined #aomc-test4',5,'2019-01-29 09:51:54',NULL),(98,8,'hello1',4,'2019-01-29 10:48:13',NULL),(99,8,'helloo2',4,'2019-01-29 10:48:30',NULL),(100,8,'helloo3',4,'2019-01-29 10:48:34',NULL),(101,8,'helloo4',4,'2019-01-29 10:48:35',NULL),(102,8,'hellooooo5',4,'2019-01-29 10:48:40',NULL),(103,8,'helloooooooooooo6',5,'2019-01-29 13:54:59',NULL),(104,8,'helloooooooooooo7',5,'2019-01-29 13:57:51',NULL),(105,8,'helloooooooooooo8',5,'2019-01-29 13:57:56',NULL),(106,8,'helloooooooooooo9',5,'2019-01-29 13:57:56',NULL),(107,8,'helloooooooooooo10',5,'2019-01-29 13:57:56',NULL),(108,8,'helloooooooooooo11',5,'2019-01-29 13:57:56',NULL),(109,8,'helloooooooooooo12',5,'2019-01-29 13:57:56',NULL),(110,8,'helloooooooooooo13',5,'2019-01-29 13:57:57',NULL),(111,8,'helloooooooooooo14',5,'2019-01-29 13:57:57',NULL),(112,8,'helloooooooooooo15',5,'2019-01-29 13:57:57',NULL),(113,8,'helloooooooooooo16',5,'2019-01-29 13:57:57',NULL),(114,8,'helloooooooooooo17',5,'2019-01-29 13:57:58',NULL),(115,8,'helloooooooooooo18',5,'2019-01-29 13:57:58',NULL),(116,8,'helloooooooooooo19',5,'2019-01-29 13:57:58',NULL),(117,8,'helloooooooooooo20',5,'2019-01-29 13:57:58',NULL),(118,8,'helloooooooooooo21',5,'2019-01-29 13:57:58',NULL),(119,8,'helloooooooooooo22',5,'2019-01-29 13:57:59',NULL),(120,8,'helloooooooooooo23',5,'2019-01-29 13:57:59',NULL),(121,8,'helloooooooooooo24',5,'2019-01-29 13:57:59',NULL),(122,8,'helloooooooooooo25',5,'2019-01-29 13:57:59',NULL),(123,8,'helloooooooooooo26',5,'2019-01-29 13:58:00',NULL),(124,8,'helloooooooooooo27',5,'2019-01-29 13:58:00',NULL),(125,8,'helloooooooooooo28',5,'2019-01-29 13:58:00',NULL),(126,8,'helloooooooooooo29',5,'2019-01-29 13:58:00',NULL),(127,8,'helloooooooooooo30',5,'2019-01-29 13:58:01',NULL),(128,8,'helloooooooooooo31',5,'2019-01-29 13:58:01',NULL),(129,8,'helloooooooooooo32',5,'2019-01-29 13:58:01',NULL),(130,8,'helloooooooooooo33',5,'2019-01-29 13:58:01',NULL),(131,8,'helloooooooooooo34',5,'2019-01-29 13:58:01',NULL),(132,8,'helloooooooooooo35',5,'2019-01-29 13:58:02',NULL),(133,8,'helloooooooooooo36',5,'2019-01-29 13:58:02',NULL),(134,8,'helloooooooooooo37',5,'2019-01-29 13:58:02',NULL),(135,8,'helloooooooooooo38',5,'2019-01-29 13:58:02',NULL),(136,8,'helloooooooooooo39',5,'2019-01-29 13:58:02',NULL),(137,8,'helloooooooooooo40',5,'2019-01-29 13:58:02',NULL),(138,8,'41',5,'2019-01-29 13:58:03',NULL),(139,8,'helloooooooooooo42',5,'2019-01-29 13:58:03',NULL),(140,42,'zzzzz',5,'2019-01-30 10:52:28',NULL),(141,42,'aomc-testtest\n',5,'2019-01-30 11:03:33',NULL),(142,44,'할루~~~~\n',5,'2019-01-30 11:14:16',NULL),(143,42,'hihihihi\n',5,'2019-01-31 14:53:33',NULL),(144,49,'joined #general',4,'2019-01-31 18:41:58',NULL),(145,50,'joined #general',4,'2019-01-31 18:56:01',NULL),(146,51,'joined #general',4,'2019-01-31 18:58:10',NULL),(147,52,'joined #general',4,'2019-01-31 19:02:31',NULL),(148,53,'joined #general',4,'2019-01-31 19:03:11',NULL),(149,54,'joined #general',4,'2019-01-31 19:04:19',NULL),(150,55,'joined #general',4,'2019-01-31 19:06:38',NULL),(151,56,'joined #general',4,'2019-01-31 19:14:10',NULL),(152,57,'joined #general',4,'2019-01-31 19:16:14',NULL),(153,58,'joined #general',4,'2019-01-31 19:17:15',NULL),(154,59,'joined #general',4,'2019-01-31 19:21:05',NULL),(155,60,'joined #general',4,'2019-01-31 19:22:16',NULL),(156,61,'joined #general',4,'2019-01-31 19:23:45',NULL),(157,62,'joined #general',4,'2019-01-31 19:26:37',NULL),(158,63,'joined #general',4,'2019-01-31 19:47:42',NULL),(159,64,'joined #general',4,'2019-01-31 19:48:33',NULL),(160,65,'joined #general',4,'2019-01-31 19:51:37',NULL),(161,66,'joined #general',4,'2019-01-31 19:53:36',NULL),(169,74,'joined #general',5,'2019-02-01 11:08:31',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (15,'winterdev','2019-01-22 13:30:29','2019-01-22 14:27:04',1),(16,'aomc','2019-01-22 13:30:39','2019-01-22 14:27:01',1),(47,'testTeam','2019-01-24 13:23:48','2019-01-24 13:23:48',1),(48,'aomc','2019-01-31 18:41:58','2019-01-31 18:41:58',1),(49,'aomc','2019-01-31 18:56:01','2019-01-31 18:56:01',1),(50,'aomc','2019-01-31 18:58:10','2019-01-31 18:58:10',1),(51,'aomc','2019-01-31 19:02:31','2019-01-31 19:02:31',1),(52,'aomc','2019-01-31 19:03:11','2019-01-31 19:03:11',1),(53,'aomc','2019-01-31 19:04:19','2019-01-31 19:04:19',1),(54,'aomc','2019-01-31 19:06:38','2019-01-31 19:06:38',1),(55,'aomc','2019-01-31 19:14:10','2019-01-31 19:14:10',1),(56,'aomc','2019-01-31 19:16:14','2019-01-31 19:16:14',1),(57,'aomc-2','2019-01-31 19:17:15','2019-01-31 19:17:15',1),(58,'aomc-2','2019-01-31 19:21:05','2019-01-31 19:21:05',1),(59,'aomc-2','2019-01-31 19:22:16','2019-01-31 19:22:16',1),(60,'aomc-2','2019-01-31 19:23:45','2019-01-31 19:23:45',1),(61,'aomc-2','2019-01-31 19:26:37','2019-01-31 19:26:37',1),(62,'aomc-2','2019-01-31 19:47:42','2019-01-31 19:47:42',1),(63,'aomc-2','2019-01-31 19:48:33','2019-01-31 19:48:33',1),(64,'aomc-5','2019-01-31 19:51:37','2019-01-31 19:51:37',1),(65,'aomc-0','2019-01-31 19:53:36','2019-01-31 19:53:36',1),(72,'halohalo','2019-02-01 11:08:31','2019-02-01 11:08:31',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_channel`
--

LOCK TABLES `user_has_channel` WRITE;
/*!40000 ALTER TABLE `user_has_channel` DISABLE KEYS */;
INSERT INTO `user_has_channel` VALUES (1,7,6,0,1),(2,7,4,0,0),(3,7,5,0,1),(4,8,6,0,1),(5,8,4,0,1),(6,8,5,0,1),(7,9,4,0,0),(8,9,5,0,1),(42,7,5,0,1),(43,7,6,0,1),(44,7,7,0,1),(45,42,4,0,1),(46,42,5,0,1),(50,44,5,0,1),(51,44,4,0,1),(52,44,6,0,1),(53,45,5,0,1),(54,45,4,0,1),(55,45,6,0,1),(60,48,5,0,1),(61,48,4,0,1),(70,74,5,0,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_team`
--

LOCK TABLES `user_has_team` WRITE;
/*!40000 ALTER TABLE `user_has_team` DISABLE KEYS */;
INSERT INTO `user_has_team` VALUES (8,15,6,'2019-01-22 13:30:29',1,1,0),(9,15,4,'2019-01-22 13:30:29',0,1,1),(11,16,6,'2019-01-22 13:30:39',1,1,0),(12,16,4,'2019-01-22 13:30:39',0,1,0),(13,16,5,'2019-01-22 13:30:39',0,1,0),(14,15,7,'2019-01-22 15:34:01',0,1,1),(54,47,4,'2019-01-24 13:23:48',1,1,0),(69,15,5,'2019-01-28 14:51:51',0,1,1),(72,48,4,'2019-01-31 18:41:58',1,1,0),(73,49,4,'2019-01-31 18:56:01',1,1,0),(74,50,4,'2019-01-31 18:58:10',1,1,0),(75,51,4,'2019-01-31 19:02:31',1,1,0),(76,52,4,'2019-01-31 19:03:11',1,1,0),(77,53,4,'2019-01-31 19:04:19',1,1,0),(78,54,4,'2019-01-31 19:06:38',1,1,0),(79,55,4,'2019-01-31 19:14:10',1,1,0),(80,56,4,'2019-01-31 19:16:14',1,1,0),(81,57,4,'2019-01-31 19:17:15',1,1,0),(82,58,4,'2019-01-31 19:21:05',1,1,0),(83,59,4,'2019-01-31 19:22:16',1,1,0),(84,60,4,'2019-01-31 19:23:45',1,1,0),(85,61,4,'2019-01-31 19:26:37',1,1,0),(86,62,4,'2019-01-31 19:47:42',1,1,0),(87,63,4,'2019-01-31 19:48:33',1,1,0),(88,64,4,'2019-01-31 19:51:37',1,1,0),(89,65,4,'2019-01-31 19:53:36',1,1,0),(98,72,5,'2019-02-01 11:08:31',1,1,0);
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

-- Dump completed on 2019-02-01 15:58:10
