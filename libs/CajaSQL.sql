-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema pvpizza
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pvpizza
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pvpizza` DEFAULT CHARACTER SET utf8 ;
USE `pvpizza` ;

-- -----------------------------------------------------
-- Table `pvpizza`.`caja`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pvpizza`.`caja` (
  `idCaja` INT(11) NOT NULL AUTO_INCREMENT,
  `inicioDelDia` DOUBLE NULL DEFAULT '0',
  `finalDelDia` DOUBLE NULL DEFAULT NULL,
  `fecha` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idCaja`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
