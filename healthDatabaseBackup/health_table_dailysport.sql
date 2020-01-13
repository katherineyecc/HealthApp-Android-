
-- --------------------------------------------------------

--
-- Table structure for table `dailysport`
--

CREATE TABLE `dailysport` (
  `sportid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `Cdate` date NOT NULL,
  `sport` varchar(50) DEFAULT NULL,
  `sportDuration` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `dailysport`
--

INSERT INTO `dailysport` (`sportid`, `userid`, `Cdate`, `sport`, `sportDuration`) VALUES
(1, 2, '2018-10-23', '2_', '183.34'),
(2, 2, '2018-10-23', '5_', '176.0'),
(3, 2, '2018-10-23', '3_', '176.0'),
(6, 1, '2018-10-24', '7_', '1200.0'),
(7, 3, '2018-10-24', '3_', '960.0'),
(8, 1, '2018-11-19', '4_', '566.66'),
(11, 3, '2018-11-21', '3_', '480.0');
