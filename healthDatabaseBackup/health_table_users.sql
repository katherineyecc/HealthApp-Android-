
-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userID` int(11) NOT NULL,
  `userEmail` varchar(50) NOT NULL,
  `userPassword` varchar(50) NOT NULL,
  `userType` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `userEmail`, `userPassword`, `userType`) VALUES
(1, 'katherineyecc@163.com', 'taylorswift1989', 'user'),
(2, 'xiaobei2@qq.com', '123456', 'user'),
(3, 'luyu@qq.com', '123456', 'user'),
(4, 'admin@163.com', 'admin', 'admin');
