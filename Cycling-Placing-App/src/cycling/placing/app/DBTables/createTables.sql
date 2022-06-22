CREATE TABLE `Inscritos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `dataNascimento` DATE	NOT NULL,
  `telemovel` varchar(15) NOT NULL,
  `idade` tinyint NULL,	
  `escalao` varchar(10) NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)	
);

CREATE TABLE `Geral` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `dataNascimento` DATE	NOT NULL,
  `telemovel` varchar(15) NOT NULL,
  `idade` tinyint NULL,	
  `escalao` varchar(10) NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `Utilizadores` (
  `id` INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(256) NOT NULL,
  `password` text NOT NULL,
  `modified` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `Provas` (
  `id` INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `userID` INT(11) NOT NULL,
  `modified` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
