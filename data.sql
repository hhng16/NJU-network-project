/*
 Navicat Premium Dump SQL

 Source Server         : mysql_B
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : localhost:3306
 Source Schema         : tiangongmarket

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 02/12/2025 17:34:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `userid` int NOT NULL,
  `goodsid` int NOT NULL,
  `rate` tinyint NOT NULL,
  `message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userid`, `goodsid`) USING BTREE,
  INDEX `fk_comment_goods`(`goodsid` ASC) USING BTREE,
  CONSTRAINT `fk_comment_goods` FOREIGN KEY (`goodsid`) REFERENCES `goods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 846563, 3, '测试3', '2025-11-24 22:37:58');
INSERT INTO `comment` VALUES (122597, 846563, 1, '测试2', '2025-11-24 22:36:06');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int NOT NULL COMMENT '主键',
  `goodsname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '货物名称',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `storage` int NOT NULL COMMENT '库存',
  `authorid` int NOT NULL COMMENT '作者 id',
  `tag` int NOT NULL COMMENT '标签',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片路径或URL',
  `rate_sum` double NULL DEFAULT NULL COMMENT '评分总和',
  `rate_count` int NULL DEFAULT NULL COMMENT '评分次数',
  `rate_avg` double NULL DEFAULT NULL COMMENT '平均评分',
  PRIMARY KEY (`id` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (846563, '测试商品1', 12.00, 12, 122597, 1, 'R-C.png', 4, 2, 2);
INSERT INTO `goods` VALUES (450153, ' 测试商品2', 114514.00, 1, 122597, 3, '屏幕截图 2025-03-23 101730.png', NULL, NULL, NULL);
INSERT INTO `goods` VALUES (10007, '测试商品3', 19.00, 5, 1, 4, '屏幕截图 2025-08-09 212828.png', 3, 2, 1.5);
INSERT INTO `goods` VALUES (10006, '测试商品4', 1.00, 14, 1, 5, '屏幕截图 2024-12-17 161320.png', 1, 1, 1);
INSERT INTO `goods` VALUES (10005, '测试商品5', 19.00, 9, 1231231, 1, '2', 5, 2, 2.5);
INSERT INTO `goods` VALUES (10004, '测试商品6', 11.45, 1, 1, 2, '2', 1, 1, 1);
INSERT INTO `goods` VALUES (10003, '测试商品7', 2.00, 3, 112312, 3, '2', 1, 1, 1);
INSERT INTO `goods` VALUES (10002, '测试商品8', 40.00, 5, 1, 4, '2', 1, 1, 1);
INSERT INTO `goods` VALUES (10001, '测试商品9', 30.00, 4, 1, 4, '2', 1, 1, 1);
INSERT INTO `goods` VALUES (10000, '测试商品10', 5.00, 10, 1, 3, '屏幕截图 2024-12-17 161320.png', 1, 1, 1);

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`  (
  `user_id` int NOT NULL,
  `goods_id` int NOT NULL,
  `reserve_num` int NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `goods_id`) USING BTREE,
  INDEX `fk_reserve_goods`(`goods_id` ASC) USING BTREE,
  CONSTRAINT `fk_reserve_goods` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reserve_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reservation_chk_1` CHECK (`reserve_num` > 0)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `no` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '账号',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名字',
  `password` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `account` int NOT NULL COMMENT '余额',
  `roleid` int NOT NULL COMMENT '角色 0超级管理员 1管理员 2用户',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像',
  `number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122598 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', '张三', '123456', 500, 3, 'TGQY .jpg', '11111111111');
INSERT INTO `user` VALUES (2, '2', '李四', '2', 2, 3, NULL, '111');
INSERT INTO `user` VALUES (122597, 'TGQY', '管理', 'th18x06', 1213, 1, '屏幕截图 2024-12-17 161320.png', NULL);

SET FOREIGN_KEY_CHECKS = 1;
