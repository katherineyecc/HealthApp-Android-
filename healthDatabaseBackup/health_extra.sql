
--
-- Indexes for dumped tables
--

--
-- Indexes for table `dailysport`
--
ALTER TABLE `dailysport`
  ADD PRIMARY KEY (`sportid`),
  ADD KEY `userid` (`userid`);

--
-- Indexes for table `experience`
--
ALTER TABLE `experience`
  ADD PRIMARY KEY (`experienceID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `foodcalorieinfo`
--
ALTER TABLE `foodcalorieinfo`
  ADD PRIMARY KEY (`foodID`);

--
-- Indexes for table `maxid`
--
ALTER TABLE `maxid`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `ordinaryuser`
--
ALTER TABLE `ordinaryuser`
  ADD PRIMARY KEY (`userID`);

--
-- Indexes for table `recipes`
--
ALTER TABLE `recipes`
  ADD PRIMARY KEY (`recipeID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `sportconsumecalorieinfo`
--
ALTER TABLE `sportconsumecalorieinfo`
  ADD PRIMARY KEY (`sportID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`,`userEmail`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `dailysport`
--
ALTER TABLE `dailysport`
  ADD CONSTRAINT `dailysport_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `ordinaryuser` (`userID`);

--
-- Constraints for table `experience`
--
ALTER TABLE `experience`
  ADD CONSTRAINT `experience_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `recipes`
--
ALTER TABLE `recipes`
  ADD CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`);
