
-- --------------------------------------------------------

--
-- Table structure for table `ordinaryuser`
--

CREATE TABLE `ordinaryuser` (
  `userID` int(11) NOT NULL,
  `userPassword` varchar(50) DEFAULT NULL,
  `height` float DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `sex` varchar(20) DEFAULT NULL,
  `userType` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ordinaryuser`
--

INSERT INTO `ordinaryuser` (`userID`, `userPassword`, `height`, `weight`, `age`, `sex`, `userType`) VALUES
(1, 'taylorswift1989', 173, 65, 21, 'female', 'user'),
(2, '123456', 1, 1, 1, 'nan', 'user'),
(3, '123456', 165, 48, 22, 'female', 'user');
