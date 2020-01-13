
-- --------------------------------------------------------

--
-- Table structure for table `experience`
--

CREATE TABLE `experience` (
  `experienceID` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `publishTime` date DEFAULT NULL,
  `title` varchar(50) NOT NULL,
  `content` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `experience`
--

INSERT INTO `experience` (`experienceID`, `userID`, `publishTime`, `title`, `content`) VALUES
(1, 2, '2018-10-23', 'xiaobei', ''),
(2, 2, '2018-10-23', 'xiaobei2222222', ''),
(4, 1, '2018-10-24', 'bbb', 'cccc'),
(5, 3, '2018-10-24', 'aaa', 'ccc'),
(6, 1, '2018-11-13', 'experience', 'helloworld'),
(7, 1, '2018-11-19', 'myAPP ', 'ON AN ACTUAL DEVICE.');
