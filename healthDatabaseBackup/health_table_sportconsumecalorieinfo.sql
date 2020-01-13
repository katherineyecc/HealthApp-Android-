
-- --------------------------------------------------------

--
-- Table structure for table `sportconsumecalorieinfo`
--

CREATE TABLE `sportconsumecalorieinfo` (
  `sportID` int(11) NOT NULL,
  `sportName` varchar(50) NOT NULL,
  `consumingCaloriePerTimeUnit` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sportconsumecalorieinfo`
--

INSERT INTO `sportconsumecalorieinfo` (`sportID`, `sportName`, `consumingCaloriePerTimeUnit`) VALUES
(1, 'walk', 116.5),
(2, 'jog', 250),
(3, 'run', 240),
(4, 'dance', 170),
(5, 'bodybuilding', 240),
(6, 'rope skipping', 720),
(7, 'swimming', 300),
(8, 'golf', 125),
(9, 'sit_up', 432),
(10, 'tennis', 425),
(11, 'beach vollyball', 260),
(12, 'climb stairs', 194.5),
(13, 'walking race', 879),
(14, 'aerobic exercise', 275),
(15, 'table tennis', 300);
