-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              5.6.16 - MySQL Community Server (GPL)
-- S.O. server:                  Win32
-- HeidiSQL Versione:            8.3.0.4753
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
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `password` char(64) NOT NULL COMMENT 'hash in SHA256',
  `email` varchar(105) NOT NULL,
  `authcode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- L’esportazione dei dati non era selezionata.


-- Dump della struttura di tabella mathchallenger.amico
CREATE TABLE IF NOT EXISTS `amico` (
  `id_utente` int(10) unsigned DEFAULT NULL,
  `id_amico` int(10) unsigned DEFAULT NULL,
  KEY `FK_amico_id_utente` (`id_utente`),
  KEY `FK_amico_id_amico` (`id_amico`),
  CONSTRAINT `FK_amico_id_amico` FOREIGN KEY (`id_amico`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_amico_id_utente` FOREIGN KEY (`id_utente`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- L’esportazione dei dati non era selezionata.


-- Dump della struttura di tabella mathchallenger.partite
CREATE TABLE IF NOT EXISTS `partite` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_utente_1` int(10) unsigned NOT NULL,
  `id_utente_2` int(10) unsigned NOT NULL,
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

-- L’esportazione dei dati non era selezionata.


-- Dump della struttura di tabella mathchallenger.statistiche
CREATE TABLE IF NOT EXISTS `statistiche` (
  `id_utente` int(10) unsigned NOT NULL,
  `partite_giocate` int(10) unsigned NOT NULL DEFAULT '0',
  `vinte` int(10) unsigned NOT NULL DEFAULT '0',
  `perse` int(10) unsigned NOT NULL DEFAULT '0',
  `pareggiate` int(10) unsigned NOT NULL DEFAULT '0',
  `punteggio` int(10) unsigned NOT NULL DEFAULT '0',
  `abbandonate` int(10) unsigned NOT NULL DEFAULT '0',
  KEY `FK__account` (`id_utente`),
  CONSTRAINT `FK__account` FOREIGN KEY (`id_utente`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- L’esportazione dei dati non era selezionata.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
