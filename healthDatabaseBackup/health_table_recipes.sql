
-- --------------------------------------------------------

--
-- Table structure for table `recipes`
--

CREATE TABLE `recipes` (
  `recipeID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `shareTime` date DEFAULT NULL,
  `breakfast` varchar(50) DEFAULT NULL,
  `lunch` varchar(50) DEFAULT NULL,
  `dinner` varchar(50) DEFAULT NULL,
  `breakfastServe` varchar(100) DEFAULT NULL,
  `lunchServe` varchar(100) DEFAULT NULL,
  `dinnerServe` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `recipes`
--

INSERT INTO `recipes` (`recipeID`, `userID`, `shareTime`, `breakfast`, `lunch`, `dinner`, `breakfastServe`, `lunchServe`, `dinnerServe`) VALUES
(12, 2, '2018-10-23', '0_', '', '', '93.0', '0.0', '0.0'),
(13, 2, '2018-10-23', '10_7_4_', '5_', '9_', '38.78', '14.3', '11.22'),
(15, 1, '2018-10-24', '4_', '11_', '12_', '60.0', '232.0', '1568.0'),
(16, 3, '2018-10-24', '0_4_', '7_', '12_', '113.0', '240.0', '1176.0'),
(19, 1, '2018-11-19', '14_', '20_', '13_', '446.0', '970.0', '168.0'),
(20, 3, '2018-11-21', '6_', '7_', '8_', '102.0', '24.0', '828.0');
