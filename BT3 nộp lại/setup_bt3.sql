-- Database rieng cho BT3. Chi chay mot lan; dung lai neu database da ton tai.
CREATE DATABASE bt03_jpa CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE bt03_jpa;
-- MySQL dump 10.13  Distrib 26.7.0, for Win64 (x86_64)
--
-- Host: localhost    Database: categorycrud
-- ------------------------------------------------------
-- Server version	26.7.0

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
-- GTID state at the beginning of the backup
--


--
-- Table structure for table `category`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `cate_id` int NOT NULL AUTO_INCREMENT,
  `cate_name` varchar(255) NOT NULL,
  `icons` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (3,'b','category/1787719554013.jpg');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `image` varchar(500) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `quantity` int NOT NULL,
  `cate_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa5189yusf8nb268s842chc62p` (`cate_id`),
  CONSTRAINT `FKa5189yusf8nb268s842chc62p` FOREIGN KEY (`cate_id`) REFERENCES `category` (`cate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'2026-09-06 16:49:42.669004','Th├┤ng sß╗æ kß╗╣ thuß║¡t\r\nPin	\r\n2 pin AAA, tuß╗òi thß╗ì pin 24 th├íng.\r\n\r\nT╞░╞íng th├¡ch	\r\nWindows, MacOS, ChromeOS, iOS, Android, PadOS\r\n\r\nC├ích kß║┐t nß╗æi	\r\nBluetooth\r\n\r\n─Éß╗Ö d├ái d├óy / Khoß║úng c├ích kß║┐t nß╗æi	\r\n10 m\r\n\r\nK├¡ch th╞░ß╗¢c b├án ph├¡m	\r\nTenkeyless\r\n\r\n─É├¿n nß╗ün LED	\r\nKh├┤ng\r\n\r\nH├úng sß║ún xuß║Ñt	\r\nLogitech','product/1788713382662-ban-phim-bluetooth-logitech-k380.webp','Logitech K380',750000.00,12,NULL),(2,'2026-09-06 16:50:32.133620','T╞░╞íng th├¡ch	\r\nWindows┬« 7 trß╗ƒ l├¬n\r\nmacOS 10.13 trß╗ƒ l├¬n\r\nChromeOS\r\n\r\n─Éß╗Ö ph├ón giß║úi	\r\n200 - 8000 DPI\r\n\r\nKß║┐t nß╗æi	\r\nD├óy kß║┐t nß╗æi USB\r\n\r\nKhoß║úng c├ích kß║┐t nß╗æi (─Éß╗Ö d├ái d├óy)	\r\n2.1 m\r\n\r\n─É├¿n LED	\r\nRGB LIGHTSYNC\r\n\r\nH├úng sß║ún xuß║Ñt	\r\nLogitech','product/1788713432130-chuot-choi-game-co-day-logitech-g102-lightsync-8000dpi-1_1_2.webp','Logitech G102',400.00,3,NULL),(3,'2026-09-06 16:51:18.186016','Th├┤ng sß╗æ kß╗╣ thuß║¡t\r\nT╞░╞íng th├¡ch	\r\nWindows\r\n\r\nC├ích kß║┐t nß╗æi	\r\nD├óy cß║»m USB\r\n\r\n─Éß╗Ö d├ái d├óy / Khoß║úng c├ích kß║┐t nß╗æi	\r\n2.1 m\r\n\r\n─É├¿n nß╗ün LED	\r\nC├│\r\n\r\nH├úng sß║ún xuß║Ñt	\r\nRazer\r\n\r\nT├¡nh n─âng kh├íc	\r\n5 n├║t bß║Ñm\r\n10 triß╗çu lß║ºn nhß║Ñn','product/1788713478181-_0000_screenshot_2021-06-22_084125_bc9_1.webp','Razer DeathAdder Essential',799.00,5,NULL),(4,'2026-09-06 16:52:57.087551','H├úng sß║ún xuß║Ñt:	HyperX\r\nModel:	Cloud II \r\nTß║ºn sß╗æ ─æ├íp ß╗⌐ng:	15Hz - 25kHz\r\nTrß╗ƒ kh├íng :	60 Ohm\r\nNg╞░ß╗íng ├íp suß║Ñt ├óm : 	98 (+/-) 3 dB\r\n─Éß╗Ö d├ái d├óy c├íp :	1m \r\n─Éß╗Ö d├ái d├óy c├íp mß╗ƒ rß╗Öng :	2m ( Soundcard )\r\nKß║┐t nß╗æi th├┤ng qua jack:	3.5mm\r\nPhß╗Ñ kiß╗çn ─æi k├¿m:	Microphone, ─æß╗çm tai, souncard USB\r\n( Bß║ún nobox sß║╜ kh├┤ng c├│ soundcard )\r\nTß║ºn sß╗æ ─æ├íp ß╗⌐ng Microphone :	50-18kHz\r\nNg╞░ß╗íng ├íp suß║Ñt ├óm Microphone :	105dB\r\nTrß╗ƒ kh├íng Microphone :	<2.2kOhm\r\n├ém thanh v├▓m:	7.1\r\n','product/1788713577087-do-web.webp','HyperX Cloud II',750000.00,9,NULL),(5,'2026-09-06 16:54:31.482773','Th╞░╞íng hiß╗çu	\r\nKeychron\r\n\r\nBß║úo h├ánh	\r\n12 Th├íng\r\n\r\nKiß╗âu b├án ph├¡m	\r\nB├án ph├¡m c╞í\r\n\r\nLayout ph├¡m	\r\nLayout 8x\r\n\r\nChß║Ñt liß╗çu	\r\nVß╗Å nh├┤m, Vß╗Å nhß╗▒a\r\n\r\nKiß╗âu Switch	\r\nGateron Blue, Gateron Brown, Gateron Red\r\n\r\nKeycap	\r\nABS Doubleshot, OEM Profile\r\n\r\nC├┤ng nghß╗ç kß║┐t nß╗æi	\r\nC├│ d├óy, Kh├┤ng d├óy\r\n\r\nKß║┐t nß╗æi	\r\nBluetooth, Type-C\r\n\r\n─É├¿n LED	\r\nLED ─É╞ín, RGB\r\n\r\nM├áu sß║»c	\r\nBlack\r\n\r\n','product/1788713671478-Keychron-K2-wireless-mechanical-keyboard-for-Mac-Windows-iOS-Gateron-switch-red-with-type-C-RGB-white-backlight-aluminum-frame.webp','Keychron K2',1690000.00,1,NULL),(6,'2026-09-06 16:55:09.388200','Th├┤ng sß╗æ kß╗╣ thuß║¡t\r\nCß╗òng kß║┐t nß╗æi	\r\n3.5mm\r\n\r\nCß╗òng giao tiß║┐p	\r\nJack cß║»m 3.5mm\r\n\r\nT├¡nh n─âng kh├íc	\r\nC├┤ng nghß╗ç ├óm thanh JBL QuantumSOUND Signature\r\n\r\nH├úng sß║ún xuß║Ñt	\r\nJBL','product/1788713709385-jbl_quantum_100_product_image_hero_blue_02_2.webp','JBL Quantum 100',990000.00,2,NULL),(7,'2026-09-06 16:56:08.414697','Th├┤ng sß╗æ kß╗╣ thuß║¡t\r\nC├┤ng suß║Ñt sß║íc	\r\n100W\r\n\r\nSß╗¡ dß╗Ñng tß╗æi ─æa	\r\n6 thiß║┐t bß╗ï\r\n\r\n─Éß║ºu v├áo	\r\nType-C\r\n\r\n─Éß║ºu ra	\r\n1 x HDMI, 1 x USB-C (PD), 1 x RJ45, 3 x USB-A\r\n\r\nTiß╗çn ├¡ch	\r\nSß║íc nhanh\r\nTruyß╗ün dß╗» liß╗çu tß╗æc ─æß╗Ö cao\r\nT╞░╞íng th├¡ch hß╗ç ─æiß╗üu h├ánh Windows, Apple OS, Linux, Vista\r\n\r\nC├┤ng nghß╗ç/─Éß║ít chß╗⌐ng nhß║¡n	\r\nC├┤ng nghß╗ç Power Delivery\r\n\r\nChß║Ñt l╞░ß╗úng nß╗Öi dung	\r\n─Éß╗Ö ph├ón giß║úi 4K@60Hz\r\n\r\nChß╗⌐c n─âng	\r\nTß╗æc ─æß╗Ö USB 3.0 5Gbps\r\nCß╗òng RJ45 1000Mbps\r\n\r\nChiß╗üu d├ái d├óy	\r\n22cm\r\n\r\nH├úng sß║ún xuß║Ñt	\r\nBaseus','product/1788713768412-hub-baseus-ultrajoy-series-6-in-1.webp','HUB Baseus Ultrajoy Series 6 in 1',350000.00,5,NULL),(8,'2026-09-06 16:59:28.866447','Th├┤ng sß╗æ kß╗╣ thuß║¡t\r\nC├┤ng suß║Ñt sß║íc	\r\n240W\r\n\r\nSß╗¡ dß╗Ñng tß╗æi ─æa	\r\n1 thiß║┐t bß╗ï\r\n\r\n─Éß║ºu v├áo	\r\nType C\r\n\r\n─Éß║ºu ra	\r\nType C\r\n\r\nTiß╗çn ├¡ch	\r\nSß║íc nhanh\r\nTß╗æc ─æß╗Ö truyß╗ün dß╗» liß╗çu 480Mbps\r\nChß╗ïu ─æ╞░ß╗úc h╞ín 10.000 lß║ºn uß╗æn cong\r\n─Éß║ºu Cß║»m Mß╗Ång chß╗ë 5,8 mm\r\nLß╗¢p phß╗º chß╗æng b├ím bß║⌐n\r\n\r\nChiß╗üu d├ái d├óy	\r\n1 m├⌐t\r\n\r\nH├úng sß║ún xuß║Ñt	\r\nAnker','product/1788713968862-cap-type-c-to-type-c-anker-zolo-a8060-240w-1m_5_.webp','C├íp Anker Zolo USB-C to USB-C 240W d├ái 1m A8060',140.00,3,NULL),(9,'2026-09-06 17:00:18.596049','Th├┤ng sß╗æ kß╗╣ thuß║¡t\r\n─Éß║ºu v├áo	\r\nHDMI\r\n\r\n─Éß║ºu ra	\r\nHDMI\r\n\r\nChß║Ñt l╞░ß╗úng nß╗Öi dung	\r\n8K@60Hz, 4K@120Hz\r\n\r\nChß╗⌐c n─âng	\r\nB─âng th├┤ng 48 Gbps\r\n├ém thanh Dolby True HD 7.1 / DTS HD / Dolby Atmos / DTS: X\r\nT╞░╞íng th├¡ch ng╞░ß╗úc vß╗¢i HDMI\r\nHß╗ù trß╗ú tß╗╖ lß╗ç khung h├¼nh video s├ón khß║Ñu g├│c rß╗Öng\r\nL├¬n ─æß║┐n 32 k├¬nh ├óm thanh\r\n\r\nChiß╗üu d├ái d├óy	\r\n1.5m\r\n\r\nH├úng sß║ún xuß║Ñt	\r\nUgreen','product/1788714018592-group_563_7__3.webp','C├íp dß╗» liß╗çu Ugreen HDMI 8K 2.1 HD135 70320 1.5M',280.00,12,NULL),(10,'2026-09-06 17:00:58.535631','Th├┤ng sß╗æ kß╗╣ thuß║¡t\r\nH├úng sß║ún xuß║Ñt	\r\nUgreen\r\n\r\nChß╗⌐c n─âng	\r\nXuß║Ñt h├¼nh ß║únh, Truyß╗ün dß╗» liß╗çu\r\n\r\n─Éß║ºu ra	\r\nDisplayPort\r\n\r\n─Éß║ºu v├áo	\r\nDisplayPort\r\n\r\nChß║Ñt l╞░ß╗úng nß╗Öi dung	\r\n4K@60Hz','product/1788714058532-frame_1_59__1.webp','C├íp dß╗» liß╗çu Ugreen Displayport To Displayport Dp102 10211 2m',180.00,2,NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(50) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `email` varchar(150) NOT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_expiry` datetime(6) DEFAULT NULL,
  `reset_otp` varchar(255) DEFAULT NULL,
  `reset_otp_expiry` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','ADMIN',_binary '','admin@example.com',NULL,NULL,NULL,NULL),(3,'user','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','USER',_binary '','user@email.com',NULL,NULL,NULL,NULL);
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

-- Dump completed on 2026-09-07  0:19:53
