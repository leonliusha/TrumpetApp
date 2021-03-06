-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: localhost    Database: trumpet
-- ------------------------------------------------------
-- Server version	5.6.25-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `broadcast`
--

DROP TABLE IF EXISTS `broadcast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `broadcast` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `broadcast_type` int(11) NOT NULL,
  `brief` varchar(55) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `expire_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `tags` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `broadcast`
--

LOCK TABLES `broadcast` WRITE;
/*!40000 ALTER TABLE `broadcast` DISABLE KEYS */;
INSERT INTO `broadcast` VALUES (1,1,'流量','呼啦啊',1,0,'2015-07-10 08:41:59','2015-07-10 08:42:40',31.135298,121.104702,NULL),(2,1,'212458元','老K去啦',1,0,'2015-07-10 09:51:44','2015-07-10 09:50:49',31.135339,121.104637,NULL),(3,1,'应该有个矩形','那是肯定的',1,0,'2015-07-10 13:12:58','2015-07-10 13:12:55',31.135339,121.104615,NULL),(16,1,'春色狼','才Joe',1,0,'2015-07-10 15:57:06','2015-07-10 15:57:03',31.139027,121.104813,NULL),(19,1,'城中南路','里拉',1,0,'2015-07-10 15:59:32','2015-07-10 15:59:29',31.146206,121.111732,NULL),(20,1,'这是呀鬼故事','地拉人',1,0,'2015-07-10 16:05:02','2015-07-10 16:04:59',31.146746,121.112953,NULL),(21,1,'加了Roy','计策图片我',1,0,'2015-07-10 16:06:22','2015-07-10 16:06:19',31.142962,121.118454,NULL),(22,1,'这是仁恒的呀','牛',1,0,'2015-07-10 16:07:15','2015-07-10 16:07:12',31.139477,121.129578,NULL),(23,1,'我在图书馆看书呢','黄书',1,0,'2015-07-10 16:07:45','2015-07-10 16:07:42',31.151051,121.13549,NULL),(24,1,'我在买菜呢','不好',1,0,'2015-07-10 16:08:22','2015-07-10 16:08:19',31.142876,121.095772,NULL),(25,1,'加热破','林家栋',1,0,'2015-07-10 16:08:41','2015-07-10 16:08:38',31.127396,121.087692,NULL),(26,1,'我在朱家角','怀念',1,0,'2015-07-10 16:09:05','2015-07-10 16:09:02',31.109791,121.057648,NULL),(27,1,'万隆','村',1,0,'2015-07-10 16:09:30','2015-07-10 16:09:27',31.105377,121.080315,NULL),(28,1,'农村','乱',1,0,'2015-07-10 16:10:55','2015-07-10 16:10:52',31.109793,121.116425,NULL),(29,1,'同德医院','我在同德医院看病',1,0,'2015-07-10 16:11:54','2015-07-10 16:11:51',31.160988,121.107712,NULL),(30,1,'袁家村','没来过',1,0,'2015-07-10 16:12:23','2015-07-10 16:12:20',31.169151,121.135323,NULL),(31,1,'这里我来过','送过饺子',1,0,'2015-07-10 16:13:05','2015-07-10 16:13:02',31.109539,121.175636,NULL),(32,1,'我在松江','这么远',1,0,'2015-07-10 19:16:47','2015-07-10 19:16:44',31.026005,121.210197,NULL),(33,1,'这里有一条河','小河',1,0,'2015-07-11 05:25:40','2015-07-11 05:25:37',31.13188,121.111595,NULL),(34,1,'这是哪里','为什不出现',1,0,'2015-07-11 05:26:06','2015-07-11 05:26:03',31.125681,121.098129,NULL),(35,1,'多彩水饺店退出新品','退出新品，干贝蘑菇水饺，采用新鲜干贝，新鲜蘑菇为材料',1,0,'2015-07-12 03:25:59','2015-07-12 03:25:54',31.215097,121.433067,NULL),(36,1,'此类OK去我glad接近我去啦嗯你就我去咯嗲咯谦虚啦额迷路Roy去啦很久URL金卡肾亏呢额李龙','',1,0,'2015-07-12 08:52:56','2015-07-12 08:52:54',31.138775,121.092598,NULL),(37,1,'刺激DHL呢pxl','佳儿哦五天',1,0,'2015-07-12 16:40:14','2015-07-12 16:38:48',31.135307,121.104699,NULL),(38,1,'希望可以成功','这是第一次过期时间测试',1,0,'2015-07-15 15:15:45','2015-07-20 15:13:00',31.135362,121.104598,NULL),(39,1,'哈哈','亏',1,0,'2015-07-15 15:40:18','2015-07-17 15:39:00',31.135348,121.104675,NULL),(40,1,'我这个是很长的一个演示，不知道会出现什么效果呢，我们自己试验一下吧','1、大白菜泡菜切碎，备用。2、将猪绞肉加入1/2茶匙的盐拌匀，摔打约2分钟，再加入步骤1的大白菜泡菜碎、姜末、葱末和调味料中的米酒、太白粉及砂糖搅拌拌匀，即为内馅。3、每张水饺皮约包入1大匙的内馅量，依序完成60个泡菜水饺。4、煮一锅水，滚沸后放入泡菜水饺，转小火续煮约3分钟至浮起后捞出即可。',1,0,'2015-07-17 03:54:40','2015-07-21 09:41:00',31.131184,121.103485,'水饺 新品'),(41,1,'日期，标签的实验','这是日期和标签的实验',1,0,'2015-07-18 06:04:11','2015-07-20 06:03:00',31.13888,121.103889,NULL),(42,0,'测试','全部信息测试',25,0,'2015-07-19 07:21:50','2015-07-20 07:09:00',31.136793,121.107101,'预定 新品 水饺 '),(43,0,'测试二','全部信息存储测试二',25,0,'2015-07-19 07:42:23','2015-07-20 07:35:00',31.136793,121.107101,'新品 水饺 '),(44,0,'测试三','全部数据存储测试三',25,0,'2015-07-19 07:57:00','2015-07-20 07:55:00',31.133276,121.106148,'团购 火锅 '),(45,0,'测试4','测试所有数据4',25,0,'2015-07-19 08:02:56','2015-07-20 08:01:00',31.132284,121.101936,'预定 水饺 '),(46,0,'测试五','测试全部数据存储五',20,0,'2015-07-19 08:14:25','2015-07-20 08:13:00',31.131735,121.107613,'预定 水饺 新品 '),(47,0,'测试六','全部数据存储测试六',23,0,'2015-07-19 08:17:00','2015-07-20 08:15:00',31.132948,121.10257,'新品 水饺 '),(48,0,'测试七','全部数据存储测试七',12,0,'2015-07-19 08:56:11','2015-07-20 06:54:00',31.132948,121.10257,'新品 水饺 '),(49,0,'测试八','算啊T1信息存储测试',13,0,'2015-07-19 11:26:03','2015-07-20 11:25:00',31.135283,121.104743,'新品 水饺 预定 '),(50,0,'多彩水饺店退出新品仙贝水饺',' 馅料的调制：肉的搅拌按一个方向搅拌上劲；馅料中可以加入适量的油；因为泡菜咸味比较重，所有可以适量少加点盐；馅料中的蔬菜搭配可以随意；\n2、蒸饺皮一般是半烫面； 擀成圆形后对折， 再将两端在前面固定， 韩式元宝型饺子就做好了。',50,0,'2015-07-19 15:01:43','2015-07-22 14:53:00',31.215086,121.43309,'预定 水饺 新品 '),(51,0,'多彩水饺店本周推出新品仙贝水饺',' 馅料的调制：肉的搅拌按一个方向搅拌上劲；馅料中可以加入适量的油；因为泡菜咸味比较重，所有可以适量少加点盐；馅料中的蔬菜搭配可以随意；\n2、蒸饺皮一般是半烫面； 擀成圆形后对折， 再将两端在前面固定， 韩式元宝型饺子就做好了。',50,11,'2015-07-21 07:14:41','2015-07-26 17:14:00',31.215042,121.43306,'新品 预定 水饺 '),(52,0,'仁寿堂江苏路店万艾可到货','正品新款进口万艾可到货了，五粒装特价优惠了。',100,0,'2015-07-19 17:22:24','2015-07-20 17:19:00',31.215546,121.432762,'促销 新品 '),(53,0,'世纪联华本周特惠商品','苹果，可口可乐，西瓜，百威啤酒，鸡蛋，糖果，饼干',100,0,'2015-07-19 17:45:32','2015-07-20 17:41:00',31.13438,121.101295,'促销 打折 ');
/*!40000 ALTER TABLE `broadcast` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-07-27 12:59:12
