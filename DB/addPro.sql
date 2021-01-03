-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: addressbook
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `addressNo` int NOT NULL AUTO_INCREMENT,
  `user_userId` varchar(45) NOT NULL,
  `addressName` varchar(45) DEFAULT NULL,
  `addressPhone` varchar(45) DEFAULT NULL,
  `addressGroup` varchar(45) DEFAULT NULL,
  `addressEmail` varchar(45) DEFAULT NULL,
  `addressText` varchar(45) DEFAULT NULL,
  `addressBirth` varchar(45) DEFAULT NULL,
  `addressImage` varchar(45) DEFAULT NULL,
  `addressStar` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`addressNo`,`user_userId`),
  UNIQUE KEY `addressNo_UNIQUE` (`addressNo`),
  KEY `FK_address_user1_idx` (`user_userId`),
  CONSTRAINT `FK_address_user1` FOREIGN KEY (`user_userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (27,'aaa','박지은','01033333333','회사','tjmac@gmail.com','','','20210104038437.jpg',NULL),(28,'aaa','tv','01098746541','지정안함','','','','202101031748253.jpg',NULL),(29,'aaa','차종한123','01001234567','가족','','','1992/06/10','2021010317504272.jpg',NULL),(31,'aaa','박세미','01023235648','지정안함','','','','20210104038567.jpg',NULL),(32,'aaa','이강후','01066458797','회사','','','','2021010404047.jpg',NULL),(33,'aaa','코로나','01043456587','지정안함','','','','2021010404835.peg',NULL);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userId` varchar(45) NOT NULL,
  `userPw` varchar(45) DEFAULT NULL,
  `userName` varchar(45) DEFAULT NULL,
  `userPhone` varchar(45) DEFAULT NULL,
  `userEmail` varchar(45) DEFAULT NULL,
  `userDeletedata` datetime DEFAULT '2020-01-01 00:00:00',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `userId_UNIQUE` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('aaa','1234','1홍길동','01011111111','null','2020-01-01 00:00:00'),('abc','1234','honggildong','010123412345','abc@naver.com','2020-01-01 00:00:00'),('abcd','1234','dsdfsdsdfs','010123412345','abc@naver.com',NULL),('acb','1234','kkk','01012312345','abc@naver.com',NULL),('bbb','1234','신사임당','010123412345','abc@naver.com',NULL),('ccc','1234','kanghoo','010112222','asd@naver',NULL),('cccc','1234','choi','010343265432','choi@naver.com','2020-01-01 00:00:00'),('dsds','fdsfdfsd','fsdfs','010312312312','dsf@naver.com',NULL),('fff','1234','kim','0101111111','kim@naver.com','2020-01-01 00:00:00'),('ffffff','1234','kim','0101111111','kim@naver.com','2020-01-01 00:00:00'),('hhh','1234','qwer','010123123','er@naver.com','2020-01-01 00:00:00'),('hhhh','1234','park','0102223333','park@naver.com','2020-01-01 00:00:00'),('oooo','1234','lee','0102342345','lee@naver.com','2020-01-01 00:00:00'),('q','qq','q','1','q',NULL),('qqqq','1234','brown','01012341234','brown@naver.com','2020-01-01 00:00:00'),('uuuu','1234','sanyi','01033212345','sanyi@naver.com','2020-01-01 00:00:00');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-04  1:54:14
