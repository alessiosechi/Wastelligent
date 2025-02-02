-- --------------------------------------------------------
-- Host:                         localhost
-- Versione server:              11.3.0-MariaDB - mariadb.org binary distribution
-- S.O. server:                  Win64
-- HeidiSQL Versione:            12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dump della struttura del database wastelligent
CREATE DATABASE IF NOT EXISTS `wastelligent` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `wastelligent`;

-- Dump della struttura di tabella wastelligent.assegnazioni
CREATE TABLE IF NOT EXISTS `assegnazioni` (
  `id_assegnazione` int(11) NOT NULL AUTO_INCREMENT,
  `id_segnalazione` int(11) DEFAULT NULL,
  `id_operatore` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_assegnazione`),
  KEY `segnalazione_id` (`id_segnalazione`),
  KEY `operatore_id` (`id_operatore`),
  CONSTRAINT `assegnazioni_ibfk_1` FOREIGN KEY (`id_segnalazione`) REFERENCES `segnalazioni` (`id_segnalazione`),
  CONSTRAINT `assegnazioni_ibfk_3` FOREIGN KEY (`id_operatore`) REFERENCES `utenti` (`id_utente`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella wastelligent.punti_utenti
CREATE TABLE IF NOT EXISTS `punti_utenti` (
  `id_utente` int(11) NOT NULL,
  `punti` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_utente`),
  CONSTRAINT `punti_utenti_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`id_utente`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella wastelligent.riscatti
CREATE TABLE IF NOT EXISTS `riscatti` (
  `id_riscatto` int(11) NOT NULL AUTO_INCREMENT,
  `id_utente` int(11) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `descrizione` text DEFAULT NULL,
  `codice_riscatto` varbinary(255) DEFAULT NULL,
  `valore` decimal(10,2) NOT NULL,
  `data_riscatto` date NOT NULL,
  `data_scadenza` date NOT NULL,
  `punti_utilizzati` int(11) NOT NULL,
  PRIMARY KEY (`id_riscatto`),
  KEY `utente_id` (`id_utente`),
  CONSTRAINT `riscatti_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`id_utente`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella wastelligent.ruoli
CREATE TABLE IF NOT EXISTS `ruoli` (
  `id_ruolo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_ruolo`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella wastelligent.segnalazioni
CREATE TABLE IF NOT EXISTS `segnalazioni` (
  `id_segnalazione` int(11) NOT NULL AUTO_INCREMENT,
  `id_utente` int(11) DEFAULT NULL,
  `descrizione` text DEFAULT NULL,
  `foto` blob DEFAULT NULL,
  `stato` enum('Ricevuta','In corso','Risolta','Riscontrata') NOT NULL,
  `latitudine` decimal(9,6) DEFAULT NULL,
  `longitudine` decimal(9,6) DEFAULT NULL,
  `punti_assegnati` int(11) DEFAULT 0,
  PRIMARY KEY (`id_segnalazione`),
  KEY `utente_id` (`id_utente`),
  CONSTRAINT `segnalazioni_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`id_utente`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella wastelligent.utenti
CREATE TABLE IF NOT EXISTS `utenti` (
  `id_utente` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `tipo_utente` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_utente`),
  UNIQUE KEY `username` (`username`),
  KEY `tipo_utente` (`tipo_utente`),
  CONSTRAINT `utenti_ibfk_1` FOREIGN KEY (`tipo_utente`) REFERENCES `ruoli` (`id_ruolo`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- L’esportazione dei dati non era selezionata.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
