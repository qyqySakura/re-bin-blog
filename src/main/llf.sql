-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: llf
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `about`
--

DROP TABLE IF EXISTS `about`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `about` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '关于我内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `about`
--

LOCK TABLES `about` WRITE;
/*!40000 ALTER TABLE `about` DISABLE KEYS */;
/*!40000 ALTER TABLE `about` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unio` (`username`,`password`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'彬彬','123','123123','1234556667','1234567890','/avatar/1752578127555_1_-___.jpg'),(2,'不帅','2232','12300','65748392@bb.com','992349','/avatar/1752577312899_1.jpg'),(13,'帅哥','223231234','1233','6574839@bb.com','992349','/avatar/1752577587959_1.jpg');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `post_count` int DEFAULT '0' COMMENT '文章数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'技术分享','分享技术相关的文章',0,4,'2025-07-25 16:58:03','2025-08-04 15:38:49'),(2,'生活随笔','记录生活中的点点滴滴',0,1,'2025-07-25 16:58:03','2025-07-31 17:25:33'),(3,'学习笔记','学习过程中的心得体会',0,0,'2025-07-25 16:58:03','2025-07-25 16:58:03');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL COMMENT '文章ID',
  `user_id` int NOT NULL COMMENT '评论用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `parent_id` int DEFAULT NULL COMMENT '父评论ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint DEFAULT '1' COMMENT '状态 1-正常 0-隐藏',
  `like_count` int DEFAULT '0' COMMENT '点赞数量',
  `reply_to_user_id` int DEFAULT NULL COMMENT '被回复的用户ID',
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  KEY `idx_post_create_time` (`post_id`,`create_time` DESC),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_reply_to_user_id` (`reply_to_user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`reply_to_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,1,2,'很棒的博客，期待更多精彩内容！',NULL,'2025-01-01 11:00:00',1,2,NULL),(2,2,2,'这个Spring Boot教程很实用，谢谢分享！',NULL,'2025-01-02 15:00:00',1,0,NULL),(3,2,1,'谢谢支持！后续会继续更新更多教程。',2,'2025-01-02 16:00:00',1,0,NULL),(4,3,1,'生活需要用心感受，很有感触的文章。',NULL,'2025-01-03 19:00:00',1,1,NULL),(5,2,2,'你好',NULL,'2025-07-25 20:45:15',1,0,NULL),(6,3,2,'1',NULL,'2025-07-30 14:56:42',1,1,NULL),(7,5,1,'这是一条测试评论，看起来很不错！',NULL,'2025-08-05 16:54:04',1,2,NULL),(8,5,1,'这是对第一条评论的回复',7,'2025-08-05 16:54:04',1,2,NULL),(9,5,1,'又一条主评论，用来测试评论功能',NULL,'2025-08-05 16:54:04',1,2,NULL),(10,2,1,'你好',5,'2025-08-05 17:19:05',1,0,NULL),(11,5,2,'这是sakura对第一条评论的回复',7,'2025-08-05 17:28:45',1,2,NULL),(12,5,1,'这是对sakura回复的再次回复',7,'2025-08-05 17:28:45',1,2,NULL),(13,1,1,'谢谢',1,'2025-08-05 17:30:44',1,2,NULL),(14,2,1,'🙃',5,'2025-08-05 17:36:11',1,0,NULL),(15,5,2,'很好  评论区很不错🥰',NULL,'2025-08-05 20:28:16',1,2,NULL),(16,5,1,'😓',7,'2025-08-05 21:17:14',1,0,NULL),(17,5,1,'好的😯',7,'2025-08-05 21:29:26',1,1,2),(18,1,2,'😍',1,'2025-08-06 17:33:42',1,2,1),(19,5,2,'aaa',7,'2025-08-06 19:31:36',1,0,1),(20,2,2,'很棒😊',NULL,'2025-08-06 19:32:10',1,0,NULL),(21,5,2,'你好',NULL,'2025-08-06 19:40:42',1,0,NULL),(22,5,2,'好的',7,'2025-08-06 19:40:55',1,0,1),(23,3,2,'test2😊',NULL,'2025-08-06 19:59:27',1,0,NULL),(24,1,2,'test',NULL,'2025-08-06 20:02:03',1,0,NULL),(25,1,2,'111',NULL,'2025-08-06 20:02:55',1,0,NULL),(26,5,2,'test',NULL,'2025-08-06 20:03:28',1,0,NULL),(27,5,2,'test',7,'2025-08-06 20:04:43',1,0,1),(28,5,2,'😊',NULL,'2025-08-06 20:10:56',1,0,NULL),(29,5,2,'哈哈哈🤤',7,'2025-08-06 20:11:12',1,1,1),(30,5,1,'你想干嘛',7,'2025-08-06 20:11:49',1,0,2),(31,5,2,'？？？',7,'2025-08-06 20:12:47',1,0,1),(32,5,1,'谢谢',15,'2025-08-06 20:13:56',1,0,2),(33,5,1,'哈哈',7,'2025-08-06 20:14:14',1,0,2),(34,5,1,'🤤',7,'2025-08-06 20:14:49',1,0,2),(35,5,2,'哈哈',7,'2025-08-06 20:15:26',1,0,1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_comment_insert_update_post` AFTER INSERT ON `comment` FOR EACH ROW BEGIN
  CALL UpdatePostStats(NEW.post_id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_comment_delete_update_post` AFTER DELETE ON `comment` FOR EACH ROW BEGIN
  CALL UpdatePostStats(OLD.post_id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `comment_like`
--

DROP TABLE IF EXISTS `comment_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_like` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comment_id` int NOT NULL COMMENT '评论ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`,`user_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_comment_like_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论点赞表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_like`
--

LOCK TABLES `comment_like` WRITE;
/*!40000 ALTER TABLE `comment_like` DISABLE KEYS */;
INSERT INTO `comment_like` VALUES (6,8,2,'2025-08-05 19:25:27'),(7,11,2,'2025-08-05 19:25:28'),(8,12,2,'2025-08-05 19:25:29'),(9,9,2,'2025-08-05 19:25:30'),(17,7,2,'2025-08-05 19:25:35'),(18,1,2,'2025-08-05 20:27:08'),(20,11,1,'2025-08-05 20:27:23'),(21,12,1,'2025-08-05 20:27:24'),(22,7,1,'2025-08-05 20:27:27'),(23,15,2,'2025-08-05 20:28:18'),(24,8,1,'2025-08-06 17:02:42'),(25,9,1,'2025-08-06 17:02:45'),(26,15,1,'2025-08-06 17:02:46'),(27,13,2,'2025-08-06 17:15:07'),(28,4,2,'2025-08-06 17:15:23'),(29,6,2,'2025-08-06 17:15:27'),(30,18,2,'2025-08-06 17:33:45'),(31,13,1,'2025-08-06 17:34:12'),(32,1,1,'2025-08-06 17:34:14'),(33,18,1,'2025-08-06 17:34:15'),(34,17,2,'2025-08-06 19:40:47'),(35,29,1,'2025-08-06 20:11:42');
/*!40000 ALTER TABLE `comment_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_link`
--

DROP TABLE IF EXISTS `friend_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_link` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '网站名',
  `url` varchar(255) NOT NULL COMMENT '链接',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_link`
--

LOCK TABLES `friend_link` WRITE;
/*!40000 ALTER TABLE `friend_link` DISABLE KEYS */;
INSERT INTO `friend_link` VALUES (1,'binbin','www.baidu.com','百科全书'),(2,'github','https://gitee.com/sakurafengbinbin','my gitee');
/*!40000 ALTER TABLE `friend_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `recipient_id` int NOT NULL COMMENT '接收者用户ID',
  `sender_id` int NOT NULL COMMENT '发送者用户ID',
  `type` varchar(20) NOT NULL COMMENT '通知类型：LIKE, COMMENT, REPLY, FOLLOW',
  `title` varchar(100) NOT NULL COMMENT '通知标题',
  `content` text COMMENT '通知内容',
  `related_id` int DEFAULT NULL COMMENT '相关对象ID（文章ID、评论ID等）',
  `related_type` varchar(20) DEFAULT NULL COMMENT '相关对象类型：POST, COMMENT',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`),
  KEY `idx_recipient_id` (`recipient_id`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_is_read` (`is_read`),
  CONSTRAINT `fk_notification_recipient` FOREIGN KEY (`recipient_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_notification_sender` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,2,1,'COMMENT','收到新的评论','彬彬 评论了你的文章《aa》：有人点赞了你的评论',5,'POST',1,'2025-08-06 17:02:46','2025-08-06 17:15:31'),(2,1,2,'LIKE','收到新的点赞','sakura 点赞了你的文章《欢迎来到我的博客》',1,'POST',1,'2025-08-06 17:15:05','2025-08-06 17:15:59'),(3,1,2,'COMMENT','收到新的评论','sakura 评论了你的文章《欢迎来到我的博客》：有人点赞了你的评论',1,'POST',1,'2025-08-06 17:15:08','2025-08-06 17:18:29'),(4,1,2,'COMMENT','收到新的评论','sakura 评论了你的文章《今天的生活感悟》：有人点赞了你的评论',3,'POST',1,'2025-08-06 17:15:24','2025-08-06 17:18:28'),(5,1,2,'LIKE','收到新的点赞','sakura 点赞了你的文章《今天的生活感悟》',3,'POST',1,'2025-08-06 17:15:26','2025-08-06 17:18:27'),(6,2,1,'COMMENT','收到新的评论','彬彬 评论了你的文章《欢迎来到我的博客》：有人点赞了你的评论',1,'POST',1,'2025-08-06 17:34:15','2025-08-06 19:58:58'),(7,2,1,'COMMENT','收到新的评论','彬彬 评论了你的文章《欢迎来到我的博客》：有人点赞了你的评论',1,'POST',1,'2025-08-06 17:34:16','2025-08-06 19:58:58'),(8,1,2,'LIKE','收到新的点赞','sakura 点赞了你的文章《aa》',5,'POST',1,'2025-08-06 19:31:54','2025-08-06 19:32:38'),(9,1,2,'LIKE','收到新的点赞','sakura 点赞了你的文章《aa》',5,'POST',1,'2025-08-06 19:40:45','2025-08-06 19:58:47'),(10,1,2,'COMMENT','收到新的评论','sakura 评论了你的文章《aa》：有人点赞了你的评论',5,'POST',1,'2025-08-06 19:40:48','2025-08-06 19:58:13'),(11,1,2,'LIKE','收到新的点赞','sakura 点赞了你的文章《欢迎来到我的博客》',1,'POST',0,'2025-08-06 20:01:27',NULL),(12,1,2,'LIKE','收到新的点赞','sakura 点赞了你的文章《aa》',5,'POST',0,'2025-08-06 20:03:37',NULL),(13,1,2,'LIKE','收到新的点赞','sakura 点赞了你的文章《aa》',5,'POST',0,'2025-08-06 20:03:40',NULL),(14,1,2,'COMMENT','收到新的评论','sakura 评论了你的文章《aa》：😊',5,'POST',0,'2025-08-06 20:10:57',NULL),(15,1,2,'REPLY','收到新的回复','sakura 回复了你在《aa》的评论：哈哈哈🤤',5,'POST',1,'2025-08-06 20:11:12','2025-08-06 20:11:33'),(16,2,1,'COMMENT','收到新的评论','彬彬 评论了你的文章《aa》：有人点赞了你的评论',5,'POST',1,'2025-08-06 20:11:42','2025-08-06 20:12:15'),(17,1,2,'REPLY','收到新的回复','sakura 回复了你在《aa》的评论：？？？',5,'POST',0,'2025-08-06 20:12:48',NULL),(18,2,1,'REPLY','收到新的回复','彬彬 回复了你在《aa》的评论：谢谢',5,'POST',0,'2025-08-06 20:13:57',NULL),(19,1,2,'REPLY','收到新的回复','sakura 回复了你在《aa》的评论：哈哈',5,'POST',0,'2025-08-06 20:15:27',NULL);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `popular_posts`
--

DROP TABLE IF EXISTS `popular_posts`;
/*!50001 DROP VIEW IF EXISTS `popular_posts`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `popular_posts` AS SELECT 
 1 AS `id`,
 1 AS `user_id`,
 1 AS `category_id`,
 1 AS `title`,
 1 AS `content`,
 1 AS `summary`,
 1 AS `cover`,
 1 AS `status`,
 1 AS `create_time`,
 1 AS `update_time`,
 1 AS `view_count`,
 1 AS `like_count`,
 1 AS `comment_count`,
 1 AS `author_name`,
 1 AS `author_avatar`,
 1 AS `category_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '作者ID',
  `category_id` int DEFAULT NULL COMMENT '分类ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `status` tinyint DEFAULT '1' COMMENT '状态 1-发布 0-草稿',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `view_count` int DEFAULT '0' COMMENT '浏览次数',
  `like_count` int DEFAULT '0' COMMENT '点赞次数',
  `comment_count` int DEFAULT '0' COMMENT '评论次数',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  KEY `idx_status_create_time` (`status`,`create_time` DESC),
  KEY `idx_category_status` (`category_id`,`status`),
  KEY `idx_user_status` (`user_id`,`status`),
  FULLTEXT KEY `idx_title_content` (`title`,`content`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `post_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,1,1,'欢迎来到我的博客','这是我的第一篇博客文章，欢迎大家来到我的个人博客！在这里我会分享技术心得、生活感悟和学习笔记。','欢迎来到我的博客，这里会分享各种有趣的内容','https://pic3.zhimg.com/v2-dae74e490ddc79d3ef21b09203084102_r.jpg',1,'2025-01-01 10:00:00','2025-08-06 20:02:58',16,1,5),(2,1,1,'Spring Boot 入门指南','Spring Boot 是一个基于 Spring 框架的快速开发框架，它简化了 Spring 应用的配置和部署过程。本文将介绍如何快速上手 Spring Boot 开发。1','Spring Boot 快速入门教程，适合初学者','https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop',1,'2025-01-02 14:30:00','2025-08-06 19:59:02',3,0,6),(3,1,2,'今天的生活感悟','<div style=\"text-align: left;\"><br></div><div style=\"text-align: left;\">生活就像一杯茶，需要慢慢品味。今天在公园散步时，看到了美丽的夕阳，让我想起了很多美好的回忆。</div>','记录今天的生活感悟和心情','https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop',1,'2025-01-03 18:00:00','2025-08-06 20:03:14',8,1,3),(4,1,1,'a','aaaasddddddddd','aaa','/api/files/c3aa19454df94fe1a56c1782a1385107.jpg',1,'2025-08-04 15:20:47','2025-08-06 19:40:23',7,1,0),(5,1,1,'aa','aaaaaaaaaaaaaaaaaaaaaaaa','a','/api/files/9c2bdedaf12a48908d603d581eac307f.jpg',1,'2025-08-04 15:38:49','2025-08-06 20:15:26',39,1,21);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_post_insert_update_category` AFTER INSERT ON `post` FOR EACH ROW BEGIN
  IF NEW.category_id IS NOT NULL AND NEW.status = 1 THEN
    CALL UpdateCategoryStats(NEW.category_id);
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_post_update_update_category` AFTER UPDATE ON `post` FOR EACH ROW BEGIN
  -- 如果分类改变了
  IF OLD.category_id != NEW.category_id THEN
    IF OLD.category_id IS NOT NULL THEN
      CALL UpdateCategoryStats(OLD.category_id);
    END IF;
    IF NEW.category_id IS NOT NULL THEN
      CALL UpdateCategoryStats(NEW.category_id);
    END IF;
  -- 如果状态改变了
  ELSEIF OLD.status != NEW.status AND NEW.category_id IS NOT NULL THEN
    CALL UpdateCategoryStats(NEW.category_id);
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary view structure for view `post_archive`
--

DROP TABLE IF EXISTS `post_archive`;
/*!50001 DROP VIEW IF EXISTS `post_archive`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `post_archive` AS SELECT 
 1 AS `archive_date`,
 1 AS `archive_name`,
 1 AS `post_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_like` (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL COMMENT '文章ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`,`user_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_post_like_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_post_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章点赞表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_like`
--

LOCK TABLES `post_like` WRITE;
/*!40000 ALTER TABLE `post_like` DISABLE KEYS */;
INSERT INTO `post_like` VALUES (1,4,2,'2025-08-05 21:07:52'),(3,3,2,'2025-08-06 17:15:26'),(6,1,2,'2025-08-06 20:01:27'),(8,5,2,'2025-08-06 20:03:39');
/*!40000 ALTER TABLE `post_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_tag`
--

DROP TABLE IF EXISTS `post_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_tag` (
  `post_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `post_tag_ibfk_2` (`tag_id`),
  CONSTRAINT `post_tag_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `post_tag_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_tag`
--

LOCK TABLES `post_tag` WRITE;
/*!40000 ALTER TABLE `post_tag` DISABLE KEYS */;
INSERT INTO `post_tag` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(1,2),(2,2),(3,2),(4,2),(3,3),(3,4),(3,5),(2,6),(3,6),(4,6),(3,7);
/*!40000 ALTER TABLE `post_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_view`
--

DROP TABLE IF EXISTS `post_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_view` (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL COMMENT '文章ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID（可为空，支持匿名浏览）',
  `ip_address` varchar(45) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `view_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_ip_time` (`ip_address`,`view_time`),
  CONSTRAINT `fk_post_view_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章浏览记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_view`
--

LOCK TABLES `post_view` WRITE;
/*!40000 ALTER TABLE `post_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_desc` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
INSERT INTO `system_config` VALUES (1,'site_name','RE-BIN博客','网站名称','2025-07-25 16:58:03','2025-07-25 16:58:03'),(2,'site_description','一个基于Spring Boot + Vue的个人博客系统','网站描述','2025-07-25 16:58:03','2025-07-25 16:58:03'),(3,'site_keywords','Java,Spring Boot,Vue,博客','网站关键词','2025-07-25 16:58:03','2025-07-25 16:58:03'),(4,'site_author','RE-BIN','网站作者','2025-07-25 16:58:03','2025-07-25 16:58:03'),(5,'site_icp','','ICP备案号','2025-07-25 16:58:03','2025-07-25 16:58:03'),(6,'comment_audit','0','评论是否需要审核 0-不需要 1-需要','2025-07-25 16:58:03','2025-07-25 16:58:03'),(7,'register_enabled','1','是否允许注册 0-不允许 1-允许','2025-07-25 16:58:03','2025-07-25 16:58:03');
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '标签名',
  `post_count` int DEFAULT '0' COMMENT '文章数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'Java',0,'2025-07-25 16:58:03'),(2,'Spring Boot',0,'2025-07-25 16:58:03'),(3,'Vue.js',0,'2025-07-25 16:58:03'),(4,'MySQL',0,'2025-07-25 16:58:03'),(5,'前端',0,'2025-07-25 16:58:03'),(6,'后端',0,'2025-07-25 16:58:03'),(7,'全栈开发',0,'2025-07-25 16:58:03');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT '状态 1-正常 0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bio` text COMMENT '个人简介',
  `website` varchar(255) DEFAULT NULL COMMENT '个人网站',
  `location` varchar(100) DEFAULT NULL COMMENT '所在地',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  KEY `idx_status_create_time` (`status`,`create_time` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'彬彬','123123','123123','2692433610@qq.com','1234567890',NULL,1,'2025-01-01 10:00:00','2025-07-31 16:41:58',NULL,NULL,NULL),(2,'sakura','sakura','123123','123123@qq.com','123123','/avatar/1753929361545_2.jpg',1,'2025-01-01 10:00:00','2025-07-31 10:36:01','111','无','长沙');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `popular_posts`
--

/*!50001 DROP VIEW IF EXISTS `popular_posts`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `popular_posts` AS select `p`.`id` AS `id`,`p`.`user_id` AS `user_id`,`p`.`category_id` AS `category_id`,`p`.`title` AS `title`,`p`.`content` AS `content`,`p`.`summary` AS `summary`,`p`.`cover` AS `cover`,`p`.`status` AS `status`,`p`.`create_time` AS `create_time`,`p`.`update_time` AS `update_time`,`p`.`view_count` AS `view_count`,`p`.`like_count` AS `like_count`,`p`.`comment_count` AS `comment_count`,`u`.`name` AS `author_name`,`u`.`avatar` AS `author_avatar`,`c`.`name` AS `category_name` from ((`post` `p` left join `user` `u` on((`p`.`user_id` = `u`.`id`))) left join `category` `c` on((`p`.`category_id` = `c`.`id`))) where (`p`.`status` = 1) order by `p`.`view_count` desc,`p`.`like_count` desc,`p`.`comment_count` desc limit 10 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `post_archive`
--

/*!50001 DROP VIEW IF EXISTS `post_archive`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `post_archive` AS select date_format(`post`.`create_time`,'%Y-%m') AS `archive_date`,date_format(`post`.`create_time`,'%Y年%m月') AS `archive_name`,count(0) AS `post_count` from `post` where (`post`.`status` = 1) group by date_format(`post`.`create_time`,'%Y-%m') order by `archive_date` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-06 20:16:48
