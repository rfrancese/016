-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              5.6.16 - MySQL Community Server (GPL)
-- S.O. server:                  Win32
-- HeidiSQL Versione:            8.3.0.4766
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dump della struttura del database mathchallenger
CREATE DATABASE IF NOT EXISTS `mathchallenger` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `mathchallenger`;


-- Dump della struttura di tabella mathchallenger.account
CREATE TABLE IF NOT EXISTS `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `password` char(64) NOT NULL COMMENT 'hash in SHA256',
  `email` varchar(105) NOT NULL,
  `authcode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella mathchallenger.account: ~0 rows (circa)
DELETE FROM `account`;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;

INSERT INTO `account` (`id`, `username`) VALUES	(0, 'bot');


-- Dump della struttura di tabella mathchallenger.amico
CREATE TABLE IF NOT EXISTS `amico` (
  `id_utente` int(10) DEFAULT NULL,
  `id_amico` int(10) DEFAULT NULL,
  KEY `FK_amico_id_utente` (`id_utente`),
  KEY `FK_amico_id_amico` (`id_amico`),
  CONSTRAINT `FK_amico_id_amico` FOREIGN KEY (`id_amico`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_amico_id_utente` FOREIGN KEY (`id_utente`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella mathchallenger.amico: ~0 rows (circa)
DELETE FROM `amico`;
/*!40000 ALTER TABLE `amico` DISABLE KEYS */;
/*!40000 ALTER TABLE `amico` ENABLE KEYS */;


-- Dump della struttura di tabella mathchallenger.bot
CREATE TABLE IF NOT EXISTS `bot` (
  `id` int(11) NOT NULL,
  `nome` char(30) NOT NULL,
  `percentuale_successo` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella mathchallenger.bot: ~21 rows (circa)
DELETE FROM `bot`;
/*!40000 ALTER TABLE `bot` DISABLE KEYS */;
INSERT INTO `bot` (`id`, `nome`, `percentuale_successo`) VALUES
	(-21, 'Randy', 20),
	(-20, 'Hope', 50),
	(-19, 'Johnny', 10),
	(-18, 'Dexter', 70),
	(-17, 'Bruce', 70),
	(-16, 'Gennaro', 78),
	(-15, 'Angelica', 50),
	(-14, 'Marco', 25),
	(-13, 'Belen', 5),
	(-12, 'Penny', 30),
	(-11, 'Francesco', 15),
	(-10, 'Shepard', 50),
	(-9, 'Rachel', 60),
	(-8, 'Tony', 75),
	(-7, 'Vicky', 45),
	(-6, 'Sheldon', 80),
	(-5, 'Rita', 65),
	(-4, 'Hermione', 60),
	(-3, 'Bart', 20),
	(-2, 'Hal', 55),
	(-1, 'Lisa', 65);
/*!40000 ALTER TABLE `bot` ENABLE KEYS */;


-- Dump della struttura di tabella mathchallenger.partite
CREATE TABLE IF NOT EXISTS `partite` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_utente_1` int(10) NOT NULL,
  `id_utente_2` int(10) NOT NULL,
  `stato_partita` tinyint(2) unsigned NOT NULL DEFAULT '0',
  `domanda1` varchar(100) NOT NULL,
  `risposta1_esatta` float NOT NULL,
  `risposta1_alternativa1` float NOT NULL,
  `risposta1_alternativa2` float NOT NULL,
  `risposta1_alternativa3` float NOT NULL,
  `utente1_risposta1` tinyint(1) DEFAULT '-1',
  `utente2_risposta1` tinyint(1) DEFAULT '-1',
  `domanda2` varchar(100) NOT NULL,
  `risposta2_esatta` float NOT NULL,
  `risposta2_alternativa1` float NOT NULL,
  `risposta2_alternativa2` float NOT NULL,
  `risposta2_alternativa3` float NOT NULL,
  `utente1_risposta2` tinyint(1) DEFAULT '-1',
  `utente2_risposta2` tinyint(1) DEFAULT '-1',
  `domanda3` varchar(100) NOT NULL,
  `risposta3_esatta` float NOT NULL,
  `risposta3_alternativa1` float NOT NULL,
  `risposta3_alternativa2` float NOT NULL,
  `risposta3_alternativa3` float NOT NULL,
  `utente1_risposta3` tinyint(1) DEFAULT '-1',
  `utente2_risposta3` tinyint(1) DEFAULT '-1',
  `domanda4` varchar(100) NOT NULL,
  `risposta4_esatta` float NOT NULL,
  `risposta4_alternativa1` float NOT NULL,
  `risposta4_alternativa2` float NOT NULL,
  `risposta4_alternativa3` float NOT NULL,
  `utente1_risposta4` tinyint(1) DEFAULT '-1',
  `utente2_risposta4` tinyint(1) DEFAULT '-1',
  `domanda5` varchar(100) NOT NULL,
  `risposta5_esatta` float NOT NULL,
  `risposta5_alternativa1` float NOT NULL,
  `risposta5_alternativa2` float NOT NULL,
  `risposta5_alternativa3` float NOT NULL,
  `utente1_risposta5` tinyint(1) DEFAULT '-1',
  `utente2_risposta5` tinyint(1) DEFAULT '-1',
  `domanda6` varchar(100) NOT NULL,
  `risposta6_esatta` float NOT NULL,
  `risposta6_alternativa1` float NOT NULL,
  `risposta6_alternativa2` float NOT NULL,
  `risposta6_alternativa3` float NOT NULL,
  `utente1_risposta6` tinyint(1) DEFAULT '-1',
  `utente2_risposta6` tinyint(1) DEFAULT '-1',
  `inizio` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fine` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `FK_partite_account` (`id_utente_1`),
  KEY `FK_partite_account_2` (`id_utente_2`),
  CONSTRAINT `FK_partite_account` FOREIGN KEY (`id_utente_1`) REFERENCES `account` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `FK_partite_account_2` FOREIGN KEY (`id_utente_2`) REFERENCES `account` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella mathchallenger.partite: ~0 rows (circa)
DELETE FROM `partite`;
/*!40000 ALTER TABLE `partite` DISABLE KEYS */;
/*!40000 ALTER TABLE `partite` ENABLE KEYS */;


-- Dump della struttura di tabella mathchallenger.statistiche
CREATE TABLE IF NOT EXISTS `statistiche` (
  `id_utente` int(10) NOT NULL,
  `partite_giocate` int(10) unsigned NOT NULL DEFAULT '0',
  `vinte` int(10) unsigned NOT NULL DEFAULT '0',
  `perse` int(10) unsigned NOT NULL DEFAULT '0',
  `pareggiate` int(10) unsigned NOT NULL DEFAULT '0',
  `punteggio` int(10) unsigned NOT NULL DEFAULT '0',
  `abbandonate` int(10) unsigned NOT NULL DEFAULT '0',
  KEY `FK__account` (`id_utente`),
  CONSTRAINT `FK__account` FOREIGN KEY (`id_utente`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella mathchallenger.statistiche: ~0 rows (circa)
DELETE FROM `statistiche`;
/*!40000 ALTER TABLE `statistiche` DISABLE KEYS */;
/*!40000 ALTER TABLE `statistiche` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
