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
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `channels`
--

LOCK TABLES `channels` WRITE;
/*!40000 ALTER TABLE `channels` DISABLE KEYS */;
INSERT INTO `channels` VALUES (96,'general',84,'2019-02-12 11:20:49','2019-02-12 11:20:49'),(98,'testChannel',84,'2019-02-14 16:33:07','2019-02-14 16:33:07');
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
INSERT INTO `messages` VALUES (0,96,'joined #general',12,'2019-02-14 14:36:27',NULL),(0,98,'joined #testChannel',13,'2019-02-14 16:37:55',NULL),(1,96,'hello',12,'2019-02-14 16:37:55',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (84,'yunjae-naver-testTeam','2019-02-12 11:20:49','2019-02-12 11:20:49',1);
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
  `user_has_last_idx` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  KEY `user_has_channel_channel_idx_idx` (`channel_idx`),
  KEY `user_has_channel_user_idx_idx` (`user_idx`),
  CONSTRAINT `user_has_channel_channel_idx` FOREIGN KEY (`channel_idx`) REFERENCES `channels` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_has_channel_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_channel`
--

LOCK TABLES `user_has_channel` WRITE;
/*!40000 ALTER TABLE `user_has_channel` DISABLE KEYS */;
INSERT INTO `user_has_channel` VALUES (109,96,12,0,1,0),(111,96,13,0,1,0),(113,98,13,0,1,0),(114,98,12,0,1,0);
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
  `invite_flag` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idx`),
  KEY `user_has_team_user_idx_idx` (`user_idx`),
  KEY `user_has_team_team_idx_idx` (`team_idx`),
  CONSTRAINT `user_has_team_team_idx` FOREIGN KEY (`team_idx`) REFERENCES `teams` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_has_team_user_idx` FOREIGN KEY (`user_idx`) REFERENCES `users` (`idx`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_team`
--

LOCK TABLES `user_has_team` WRITE;
/*!40000 ALTER TABLE `user_has_team` DISABLE KEYS */;
INSERT INTO `user_has_team` VALUES (119,84,12,'2019-02-12 11:20:49',1,1,1),(121,84,13,'2019-02-14 14:42:30',0,1,1),(123,84,14,'2019-02-14 16:46:20',0,1,1),(124,84,15,'2019-02-14 16:46:20',0,1,1),(125,84,16,'2019-02-14 16:47:10',0,1,1),(126,84,17,'2019-02-14 16:47:10',0,1,1),(127,84,18,'2019-02-14 16:47:10',0,1,1),(128,84,19,'2019-02-14 16:47:10',0,1,1),(129,84,20,'2019-02-14 16:47:10',0,1,1),(130,84,21,'2019-02-14 16:47:10',0,1,1),(131,84,22,'2019-02-14 16:47:10',0,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10,'Starever222@gmail.com','62a1700c62a9c5dbe137fb446ff3809400bed32733e38bf07075cfb28142bd9d','0089566902375800','garam',0,0,1,'2019-02-08 10:45:21','2019-02-14 14:32:57','2019-02-08 10:45:21'),(11,'dmsal2525@gmail.com','289c1fc2803a23a3a175d706da4223db4a6028e834c0262dc779f6b83a873fb1','6105129553065625','mooming',0,0,1,'2019-02-08 16:37:51','2019-02-14 14:32:43','2019-02-08 16:37:51'),(12,'yunjea0312@naver.com','b953909066c1d14e4db84183d8115fc4c9960ca0369dfa3f78ed5dda8f8b1a36','6097780492983663','yunyun',1,0,1,'2019-02-11 15:30:41','2019-02-14 15:02:00','2019-02-11 15:30:41'),(13,'yunjea031296@gmail.com','db04f3e5655fe618b6c34502318d124593b9c238b50f4540dbc36d0b37ce0943','3831778083439950','yunyun2',1,0,1,'2019-02-14 14:42:04','2019-02-15 12:26:24','2019-02-14 14:42:04'),(14,'aaa','1234','1234','aaa',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(15,'bbb','1234','1234','bbb',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(16,'ccc','1234','1234','ccc',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(17,'ddd','1234','1234','ddd',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(18,'eee','1234','1234','eee',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(19,'fff','1234','1234','fff',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(20,'ggg','1234','1234','ggg',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(21,'hhh','1234','1234','hhh',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16'),(22,'iii','1234','1234','iii',1,0,1,'2019-02-14 16:44:16','2019-02-14 16:44:16','2019-02-14 16:44:16');
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

-- Dump completed on 2019-02-15 15:20:42
