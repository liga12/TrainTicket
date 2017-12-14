-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Дек 14 2017 г., 00:03
-- Версия сервера: 10.1.28-MariaDB
-- Версия PHP: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `tt5`
--

-- --------------------------------------------------------

--
-- Структура таблицы `station`
--

CREATE TABLE `station` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `station`
--

INSERT INTO `station` (`id`, `name`) VALUES
(44, 'chernihiv'),
(49, 'chernivtsi'),
(37, 'dnipro'),
(33, 'donetsk'),
(52, 'ivano-frankivsk'),
(38, 'kharkiv'),
(51, 'khmelnytskyi'),
(45, 'kiev'),
(40, 'kremenchuk'),
(39, 'kryvyi rih'),
(42, 'lubny'),
(32, 'luhansk'),
(55, 'lutsk'),
(53, 'lviv'),
(34, 'mariupol'),
(35, 'melitopol'),
(47, 'mykolaiv'),
(48, 'odessa'),
(41, 'poltava'),
(43, 'shostka'),
(46, 'uman'),
(56, 'uzhorod'),
(50, 'vinnytsia'),
(36, 'zaporizhia'),
(54, 'zhytomyr');

-- --------------------------------------------------------

--
-- Структура таблицы `station_neighbor`
--

CREATE TABLE `station_neighbor` (
  `id` int(11) NOT NULL,
  `id_city` int(11) NOT NULL,
  `id_neighbor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `station_neighbor`
--

INSERT INTO `station_neighbor` (`id`, `id_city`, `id_neighbor`) VALUES
(83, 44, 43),
(84, 44, 45),
(85, 49, 48),
(86, 49, 50),
(87, 49, 56),
(88, 37, 39),
(89, 37, 36),
(90, 37, 33),
(91, 37, 38),
(92, 33, 32),
(93, 33, 34),
(94, 33, 37),
(95, 52, 56),
(96, 52, 51),
(97, 38, 32),
(98, 38, 41),
(99, 38, 37),
(100, 51, 52),
(101, 51, 55),
(102, 51, 50),
(103, 45, 42),
(104, 45, 44),
(105, 45, 54),
(106, 45, 46),
(107, 40, 41),
(108, 40, 39),
(109, 39, 37),
(110, 39, 46),
(111, 39, 47),
(112, 42, 45),
(113, 42, 41),
(114, 32, 33),
(115, 32, 38),
(116, 55, 53),
(117, 55, 51),
(118, 55, 54),
(119, 53, 55),
(120, 53, 56),
(121, 34, 35),
(122, 34, 33),
(123, 35, 34),
(124, 35, 36),
(125, 35, 47),
(126, 47, 35),
(127, 47, 39),
(128, 47, 48),
(129, 48, 46),
(130, 48, 49),
(131, 48, 47),
(132, 41, 38),
(133, 41, 43),
(134, 41, 42),
(135, 41, 40),
(136, 43, 44),
(137, 43, 41),
(138, 46, 45),
(139, 46, 50),
(140, 46, 48),
(141, 46, 39),
(142, 56, 53),
(143, 56, 52),
(144, 56, 49),
(145, 50, 46),
(146, 50, 54),
(147, 50, 51),
(148, 50, 49),
(149, 36, 37),
(150, 36, 35),
(151, 54, 45),
(152, 54, 50),
(153, 54, 55);

-- --------------------------------------------------------

--
-- Структура таблицы `train`
--

CREATE TABLE `train` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `train`
--

INSERT INTO `train` (`id`, `name`) VALUES
(69, '1-20'),
(56, '1-21'),
(62, '10-24'),
(66, '14-1'),
(59, '16-1'),
(70, '19-15'),
(58, '19-5'),
(63, '2-13'),
(60, '20-1'),
(65, '22-7'),
(61, '23-8'),
(68, '24-2'),
(57, '25-13'),
(67, '5-18'),
(64, '5-21');

-- --------------------------------------------------------

--
-- Структура таблицы `train_way`
--

CREATE TABLE `train_way` (
  `id` int(11) NOT NULL,
  `cost` int(11) DEFAULT NULL,
  `departure` time NOT NULL,
  `stopping_time` time NOT NULL,
  `city_id` int(11) NOT NULL,
  `id_train` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `train_way`
--

INSERT INTO `train_way` (`id`, `cost`, `departure`, `stopping_time`, `city_id`, `id_train`) VALUES
(1386, 70, '04:10:00', '00:00:00', 32, 69),
(1396, 65, '05:16:00', '00:11:00', 33, 69),
(1406, 130, '08:15:00', '00:10:00', 34, 69),
(1416, 170, '12:18:00', '00:05:00', 35, 69),
(1426, 90, '13:06:00', '00:06:00', 47, 69),
(1436, 350, '17:18:00', '00:08:00', 48, 69),
(1446, 165, '19:18:00', '00:13:00', 49, 69),
(1456, 110, '20:30:00', '00:15:00', 50, 69),
(1466, 0, '22:10:00', '00:00:00', 54, 69),
(1467, 70, '07:15:00', '00:00:00', 32, 56),
(1475, 150, '08:30:00', '00:05:00', 33, 56),
(1483, 85, '09:31:00', '00:08:00', 37, 56),
(1491, 160, '11:00:00', '00:13:00', 39, 56),
(1499, 150, '14:10:00', '00:14:00', 46, 56),
(1507, 80, '15:46:00', '00:21:00', 50, 56),
(1515, 0, '16:35:00', '00:00:00', 51, 56),
(1516, 60, '01:50:00', '00:00:00', 40, 62),
(1525, 60, '02:15:00', '00:05:00', 41, 62),
(1534, 160, '03:21:00', '00:15:00', 42, 62),
(1543, 140, '06:10:00', '00:14:00', 45, 62),
(1552, 110, '09:11:00', '00:11:00', 54, 62),
(1561, 80, '12:12:00', '00:06:00', 50, 62),
(1570, 135, '15:04:00', '00:04:00', 51, 62),
(1579, 0, '16:55:00', '00:00:00', 52, 62),
(1580, 110, '05:05:00', '00:00:00', 45, 66),
(1586, 170, '08:40:00', '00:10:00', 44, 66),
(1592, 290, '13:14:00', '00:25:00', 43, 66),
(1598, 130, '16:10:00', '00:03:00', 41, 66),
(1604, 0, '17:40:00', '00:00:00', 38, 66),
(1605, 160, '10:03:00', '00:00:00', 46, 59),
(1613, 110, '12:14:00', '00:14:00', 39, 59),
(1621, 170, '14:30:00', '00:10:00', 47, 59),
(1629, 130, '15:35:00', '00:04:00', 35, 59),
(1637, 65, '16:59:00', '00:07:00', 34, 59),
(1645, 70, '19:11:00', '00:11:00', 33, 59),
(1653, 0, '22:22:00', '00:00:00', 32, 59),
(1654, 165, '02:09:00', '00:00:00', 49, 70),
(1660, 150, '04:11:00', '00:10:00', 50, 70),
(1666, 200, '07:10:00', '00:17:00', 46, 70),
(1672, 110, '09:03:00', '00:17:00', 45, 70),
(1678, 0, '13:11:00', '00:00:00', 44, 70),
(1679, 350, '07:47:00', '00:00:00', 49, 58),
(1686, 90, '08:59:00', '00:05:00', 48, 58),
(1693, 110, '11:15:00', '00:05:00', 47, 58),
(1700, 85, '13:50:00', '00:02:00', 39, 58),
(1707, 190, '16:14:00', '00:31:00', 37, 58),
(1714, 0, '20:09:00', '00:00:00', 38, 58),
(1715, 150, '01:10:00', '00:00:00', 33, 63),
(1721, 190, '03:17:00', '00:13:00', 37, 63),
(1727, 130, '05:14:00', '00:15:00', 38, 63),
(1733, 60, '07:11:00', '00:10:00', 41, 63),
(1739, 0, '10:13:00', '00:00:00', 42, 63),
(1740, 140, '11:11:00', '00:00:00', 54, 60),
(1748, 110, '13:11:00', '00:04:00', 45, 60),
(1756, 170, '14:50:00', '00:14:00', 44, 60),
(1764, 290, '17:16:00', '00:11:00', 43, 60),
(1772, 130, '20:14:00', '00:13:00', 41, 60),
(1780, 320, '22:11:00', '00:17:00', 38, 60),
(1788, 0, '23:14:00', '00:00:00', 32, 60),
(1789, 280, '10:05:00', '00:00:00', 55, 65),
(1798, 140, '11:47:00', '00:07:00', 54, 65),
(1807, 200, '13:59:00', '00:15:00', 45, 65),
(1816, 160, '15:33:00', '00:05:00', 46, 65),
(1825, 85, '17:07:00', '00:15:00', 39, 65),
(1834, 60, '19:38:00', '00:15:00', 37, 65),
(1843, 75, '20:49:00', '00:17:00', 36, 65),
(1852, 0, '23:14:00', '00:00:00', 35, 65),
(1853, 150, '12:14:00', '00:00:00', 53, 61),
(1861, 260, '14:11:00', '00:08:00', 55, 61),
(1869, 80, '16:18:00', '00:13:00', 51, 61),
(1877, 165, '18:46:00', '00:09:00', 50, 61),
(1885, 350, '20:09:00', '00:16:00', 49, 61),
(1893, 90, '21:35:00', '00:13:00', 48, 61),
(1901, 0, '23:15:00', '00:00:00', 47, 61),
(1902, 135, '03:08:00', '00:00:00', 52, 68),
(1913, 80, '05:14:00', '00:16:00', 51, 68),
(1924, 150, '09:08:00', '00:15:00', 50, 68),
(1935, 160, '12:05:00', '00:03:00', 46, 68),
(1946, 85, '14:14:00', '00:09:00', 39, 68),
(1957, 60, '16:12:00', '00:18:00', 37, 68),
(1968, 75, '18:16:00', '00:10:00', 36, 68),
(1979, 130, '19:39:00', '00:09:00', 35, 68),
(1990, 65, '21:14:00', '00:10:00', 34, 68),
(2001, 0, '23:13:00', '00:00:00', 33, 68),
(2002, 145, '04:02:00', '00:00:00', 56, 57),
(2010, 135, '05:12:00', '00:02:00', 52, 57),
(2018, 80, '06:44:00', '00:05:00', 51, 57),
(2026, 110, '09:28:00', '00:14:00', 50, 57),
(2034, 140, '11:15:00', '00:16:00', 54, 57),
(2042, 160, '13:15:00', '00:10:00', 45, 57),
(2050, 0, '15:36:00', '00:00:00', 42, 57),
(2051, 190, '10:12:00', '00:00:00', 38, 67),
(2057, 85, '12:48:00', '00:11:00', 37, 67),
(2063, 160, '14:58:00', '00:04:00', 39, 67),
(2069, 150, '15:57:00', '00:04:00', 46, 67),
(2075, 0, '20:00:00', '00:00:00', 50, 67),
(2076, 190, '05:46:00', '00:00:00', 33, 64),
(2083, 85, '07:14:00', '00:13:00', 37, 64),
(2090, 160, '10:49:00', '00:04:00', 39, 64),
(2097, 150, '13:09:00', '00:01:00', 46, 64),
(2104, 80, '15:10:00', '00:08:00', 50, 64),
(2111, 0, '18:12:00', '00:00:00', 51, 64);

-- --------------------------------------------------------

--
-- Структура таблицы `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `role` varchar(255) NOT NULL,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `user`
--

INSERT INTO `user` (`id`, `role`, `login`, `password`) VALUES
(1, 'ROLE_ADMIN', '11', 'ecfd942640fa510e3ec8d5656443b098');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `station`
--
ALTER TABLE `station`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_qsstlki7ni5ovaariyy9u8y79` (`name`);

--
-- Индексы таблицы `station_neighbor`
--
ALTER TABLE `station_neighbor`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsgfdlxt1d2auepjsxlpdfreni` (`id_neighbor`),
  ADD KEY `FKrj1eoftje6hq3te7sirjmli36` (`id_city`);

--
-- Индексы таблицы `train`
--
ALTER TABLE `train`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_oxxbrkoath6yyjbigbdtf1ts9` (`name`);

--
-- Индексы таблицы `train_way`
--
ALTER TABLE `train_way`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfku8ny81oen0iai9yf804j10m` (`id_train`),
  ADD KEY `FK47qqan3kxtfkmij3rtwtm5yfl` (`city_id`);

--
-- Индексы таблицы `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ew1hvam8uwaknuaellwhqchhb` (`login`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `station`
--
ALTER TABLE `station`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT для таблицы `station_neighbor`
--
ALTER TABLE `station_neighbor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=154;

--
-- AUTO_INCREMENT для таблицы `train`
--
ALTER TABLE `train`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT для таблицы `train_way`
--
ALTER TABLE `train_way`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2112;

--
-- AUTO_INCREMENT для таблицы `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `station_neighbor`
--
ALTER TABLE `station_neighbor`
  ADD CONSTRAINT `FKbb832xtiw1ld7i77ryfaha942` FOREIGN KEY (`id_neighbor`) REFERENCES `station` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FKfg4wal0sn0nd8h02uakg6lsdv` FOREIGN KEY (`id_city`) REFERENCES `station` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FKi0mkv6lfkmwlsnoa4ax4s82wq` FOREIGN KEY (`id_city`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FKjg0904an2db8sklnsoh4iw1bx` FOREIGN KEY (`id_neighbor`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FKrj1eoftje6hq3te7sirjmli36` FOREIGN KEY (`id_city`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FKsgfdlxt1d2auepjsxlpdfreni` FOREIGN KEY (`id_neighbor`) REFERENCES `station` (`id`);

--
-- Ограничения внешнего ключа таблицы `train_way`
--
ALTER TABLE `train_way`
  ADD CONSTRAINT `FK47qqan3kxtfkmij3rtwtm5yfl` FOREIGN KEY (`city_id`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FKb9lsqvruudp3ewe5aqhnaoh8u` FOREIGN KEY (`city_id`) REFERENCES `station` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKfku8ny81oen0iai9yf804j10m` FOREIGN KEY (`id_train`) REFERENCES `train` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
