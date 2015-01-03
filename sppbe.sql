-- phpMyAdmin SQL Dump
-- version 4.2.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 03 Jan 2015 pada 22.18
-- Versi Server: 5.6.21
-- PHP Version: 5.4.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `sppbe`
--
CREATE DATABASE IF NOT EXISTS `sppbe` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `sppbe`;

-- --------------------------------------------------------

--
-- Struktur dari tabel `Admin`
--

CREATE TABLE IF NOT EXISTS `Admin` (
  `Id_Admin` varchar(6) NOT NULL,
  `Password` varchar(32) DEFAULT NULL,
  `Nama_Admin` varchar(40) DEFAULT NULL,
  `Alamat_Admin` varchar(50) DEFAULT NULL,
  `No_Hp_Admin` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `Admin`
--

INSERT INTO `Admin` (`Id_Admin`, `Password`, `Nama_Admin`, `Alamat_Admin`, `No_Hp_Admin`) VALUES
('admin', '21232f297a57a5a743894a0e4a801fc3', 'Yogi', 'Yogya', '08565656566');

-- --------------------------------------------------------

--
-- Struktur dari tabel `daemons`
--

CREATE TABLE IF NOT EXISTS `daemons` (
  `Start` text NOT NULL,
  `Info` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktur dari tabel `gammu`
--

CREATE TABLE IF NOT EXISTS `gammu` (
  `Version` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `gammu`
--

INSERT INTO `gammu` (`Version`) VALUES
(13);

-- --------------------------------------------------------

--
-- Struktur dari tabel `inbox`
--

CREATE TABLE IF NOT EXISTS `inbox` (
  `UpdatedInDB` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ReceivingDateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `Text` text NOT NULL,
  `SenderNumber` varchar(20) NOT NULL DEFAULT '',
  `Coding` enum('Default_No_Compression','Unicode_No_Compression','8bit','Default_Compression','Unicode_Compression') NOT NULL DEFAULT 'Default_No_Compression',
  `UDH` text NOT NULL,
  `SMSCNumber` varchar(20) NOT NULL DEFAULT '',
  `Class` int(11) NOT NULL DEFAULT '-1',
  `TextDecoded` text NOT NULL,
`ID` int(10) unsigned NOT NULL,
  `RecipientID` text NOT NULL,
  `Processed` enum('false','true') NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Trigger `inbox`
--
DELIMITER //
CREATE TRIGGER `inbox_timestamp` BEFORE INSERT ON `inbox`
 FOR EACH ROW BEGIN
    IF NEW.ReceivingDateTime = '0000-00-00 00:00:00' THEN
        SET NEW.ReceivingDateTime = CURRENT_TIMESTAMP();
    END IF;
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `Laporan`
--

CREATE TABLE IF NOT EXISTS `Laporan` (
  `Kode_Laporan` varchar(6) NOT NULL,
  `Kode_Perpanjang` varchar(6) DEFAULT NULL,
  `Jenis` varchar(30) DEFAULT NULL,
  `Sumber` varchar(40) DEFAULT NULL,
  `Tgl_Jatuh_Tempolp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktur dari tabel `outbox`
--

CREATE TABLE IF NOT EXISTS `outbox` (
  `UpdatedInDB` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `InsertIntoDB` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `SendingDateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `SendBefore` time NOT NULL DEFAULT '23:59:59',
  `SendAfter` time NOT NULL DEFAULT '00:00:00',
  `Text` text,
  `DestinationNumber` varchar(20) NOT NULL DEFAULT '',
  `Coding` enum('Default_No_Compression','Unicode_No_Compression','8bit','Default_Compression','Unicode_Compression') NOT NULL DEFAULT 'Default_No_Compression',
  `UDH` text,
  `Class` int(11) DEFAULT '-1',
  `TextDecoded` text NOT NULL,
`ID` int(10) unsigned NOT NULL,
  `MultiPart` enum('false','true') DEFAULT 'false',
  `RelativeValidity` int(11) DEFAULT '-1',
  `SenderID` varchar(255) DEFAULT NULL,
  `SendingTimeOut` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `DeliveryReport` enum('default','yes','no') DEFAULT 'default',
  `CreatorID` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Trigger `outbox`
--
DELIMITER //
CREATE TRIGGER `outbox_timestamp` BEFORE INSERT ON `outbox`
 FOR EACH ROW BEGIN
    IF NEW.InsertIntoDB = '0000-00-00 00:00:00' THEN
        SET NEW.InsertIntoDB = CURRENT_TIMESTAMP();
    END IF;
    IF NEW.SendingDateTime = '0000-00-00 00:00:00' THEN
        SET NEW.SendingDateTime = CURRENT_TIMESTAMP();
    END IF;
    IF NEW.SendingTimeOut = '0000-00-00 00:00:00' THEN
        SET NEW.SendingTimeOut = CURRENT_TIMESTAMP();
    END IF;
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `outbox_multipart`
--

CREATE TABLE IF NOT EXISTS `outbox_multipart` (
  `Text` text,
  `Coding` enum('Default_No_Compression','Unicode_No_Compression','8bit','Default_Compression','Unicode_Compression') NOT NULL DEFAULT 'Default_No_Compression',
  `UDH` text,
  `Class` int(11) DEFAULT '-1',
  `TextDecoded` text,
  `ID` int(10) unsigned NOT NULL DEFAULT '0',
  `SequencePosition` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktur dari tabel `Pajak`
--

CREATE TABLE IF NOT EXISTS `Pajak` (
  `Kode_Pajak` varchar(6) NOT NULL,
  `Id_Admin` varchar(6) NOT NULL,
  `No_Npwp` varchar(18) DEFAULT NULL,
  `Jenis_Pajak` varchar(20) DEFAULT NULL,
  `Sumber_Pajak` varchar(100) DEFAULT NULL,
  `Pokok_Pajak` varchar(20) DEFAULT NULL,
  `Tgl_Jatuh_Tempo_Pjk` datetime DEFAULT NULL,
  `sms_terkirim` varchar(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `Pajak`
--

INSERT INTO `Pajak` (`Kode_Pajak`, `Id_Admin`, `No_Npwp`, `Jenis_Pajak`, `Sumber_Pajak`, `Pokok_Pajak`, `Tgl_Jatuh_Tempo_Pjk`, `sms_terkirim`) VALUES
('oo1E', 'admin', 'qwkk', 'qw', 'qwqw', 'qw', '2014-11-28 00:00:00', '0'),
('qf', 'admin', 'qwkk', 'qw', 'qwqw', 'qw', '2015-12-27 00:00:00', '0'),
('qfd', 'admin', 'qwkk', 'qw', 'qwqw', 'qw', '2014-01-27 00:00:00', '1'),
('qw', 'admin', 'qwkk', 'qw', 'qwqw', 'qw', '2014-12-27 00:00:00', '1');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pbk`
--

CREATE TABLE IF NOT EXISTS `pbk` (
`ID` int(11) NOT NULL,
  `GroupID` int(11) NOT NULL DEFAULT '-1',
  `Name` text NOT NULL,
  `Number` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pbk_groups`
--

CREATE TABLE IF NOT EXISTS `pbk_groups` (
  `Name` text NOT NULL,
`ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktur dari tabel `Pengujian`
--

CREATE TABLE IF NOT EXISTS `Pengujian` (
  `Kode_Pengujian` varchar(6) NOT NULL,
  `Id_Admin` varchar(6) NOT NULL,
  `No_Pengujian` varchar(30) DEFAULT NULL,
  `Jenis_Pengujian` varchar(30) DEFAULT NULL,
  `Metode` varchar(30) DEFAULT NULL,
  `Tgl_Jatuh_Tempo_Pgjn` datetime DEFAULT NULL,
  `Sumber_Pengujian` varchar(100) DEFAULT NULL,
  `sms_terkirim` varchar(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `Pengujian`
--

INSERT INTO `Pengujian` (`Kode_Pengujian`, `Id_Admin`, `No_Pengujian`, `Jenis_Pengujian`, `Metode`, `Tgl_Jatuh_Tempo_Pgjn`, `Sumber_Pengujian`, `sms_terkirim`) VALUES
('ddd', 'admin', 'ddd', 'ddd', 'dsds', '2014-12-26 13:50:02', 'sdsds', '1'),
('qw', 'admin', 'qw', 'qw', 'qw', '2015-12-02 00:00:00', 'qwqw', '0');

-- --------------------------------------------------------

--
-- Struktur dari tabel `Perizinan`
--

CREATE TABLE IF NOT EXISTS `Perizinan` (
  `Kode_Perizinan` varchar(6) NOT NULL,
  `Id_Admin` varchar(6) NOT NULL,
  `No_Perizinan` varchar(30) DEFAULT NULL,
  `Jenis_Perizinan` varchar(20) DEFAULT NULL,
  `Kegunaan_Perizinan` varchar(40) DEFAULT NULL,
  `Sumber_Perizinan` varchar(100) DEFAULT NULL,
  `Tgl_Jatuh_Tempo_Przn` datetime DEFAULT NULL,
  `sms_terkirim` varchar(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `Perizinan`
--

INSERT INTO `Perizinan` (`Kode_Perizinan`, `Id_Admin`, `No_Perizinan`, `Jenis_Perizinan`, `Kegunaan_Perizinan`, `Sumber_Perizinan`, `Tgl_Jatuh_Tempo_Przn`, `sms_terkirim`) VALUES
('122', 'admin', '12', '12', '12', '12', '2014-12-01 00:00:00', '1');

-- --------------------------------------------------------

--
-- Struktur dari tabel `Perpanjang`
--

CREATE TABLE IF NOT EXISTS `Perpanjang` (
  `Kode_Perpanjang` varchar(6) NOT NULL,
  `Kode_Pajak` varchar(6) DEFAULT NULL,
  `Kode_Perizinan` varchar(6) DEFAULT NULL,
  `Kode_Pengujian` varchar(6) DEFAULT NULL,
  `Kode_Sertifikasi` varchar(6) DEFAULT NULL,
  `Id_Admin` varchar(6) DEFAULT NULL,
  `Jenis_Peringatan` varchar(40) DEFAULT NULL,
  `Tgl_Jatuh_Tempo` datetime DEFAULT NULL,
  `Tgl_Perpanjang` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktur dari tabel `phones`
--

CREATE TABLE IF NOT EXISTS `phones` (
  `ID` text NOT NULL,
  `UpdatedInDB` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `InsertIntoDB` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `TimeOut` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `Send` enum('yes','no') NOT NULL DEFAULT 'no',
  `Receive` enum('yes','no') NOT NULL DEFAULT 'no',
  `IMEI` varchar(35) NOT NULL,
  `Client` text NOT NULL,
  `Battery` int(11) NOT NULL DEFAULT '-1',
  `Signal` int(11) NOT NULL DEFAULT '-1',
  `Sent` int(11) NOT NULL DEFAULT '0',
  `Received` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Trigger `phones`
--
DELIMITER //
CREATE TRIGGER `phones_timestamp` BEFORE INSERT ON `phones`
 FOR EACH ROW BEGIN
    IF NEW.InsertIntoDB = '0000-00-00 00:00:00' THEN
        SET NEW.InsertIntoDB = CURRENT_TIMESTAMP();
    END IF;
    IF NEW.TimeOut = '0000-00-00 00:00:00' THEN
        SET NEW.TimeOut = CURRENT_TIMESTAMP();
    END IF;
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `sentitems`
--

CREATE TABLE IF NOT EXISTS `sentitems` (
  `UpdatedInDB` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `InsertIntoDB` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `SendingDateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `DeliveryDateTime` timestamp NULL DEFAULT NULL,
  `Text` text NOT NULL,
  `DestinationNumber` varchar(20) NOT NULL DEFAULT '',
  `Coding` enum('Default_No_Compression','Unicode_No_Compression','8bit','Default_Compression','Unicode_Compression') NOT NULL DEFAULT 'Default_No_Compression',
  `UDH` text NOT NULL,
  `SMSCNumber` varchar(20) NOT NULL DEFAULT '',
  `Class` int(11) NOT NULL DEFAULT '-1',
  `TextDecoded` text NOT NULL,
  `ID` int(10) unsigned NOT NULL DEFAULT '0',
  `SenderID` varchar(255) NOT NULL,
  `SequencePosition` int(11) NOT NULL DEFAULT '1',
  `Status` enum('SendingOK','SendingOKNoReport','SendingError','DeliveryOK','DeliveryFailed','DeliveryPending','DeliveryUnknown','Error') NOT NULL DEFAULT 'SendingOK',
  `StatusError` int(11) NOT NULL DEFAULT '-1',
  `TPMR` int(11) NOT NULL DEFAULT '-1',
  `RelativeValidity` int(11) NOT NULL DEFAULT '-1',
  `CreatorID` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Trigger `sentitems`
--
DELIMITER //
CREATE TRIGGER `sentitems_timestamp` BEFORE INSERT ON `sentitems`
 FOR EACH ROW BEGIN
    IF NEW.InsertIntoDB = '0000-00-00 00:00:00' THEN
        SET NEW.InsertIntoDB = CURRENT_TIMESTAMP();
    END IF;
    IF NEW.SendingDateTime = '0000-00-00 00:00:00' THEN
        SET NEW.SendingDateTime = CURRENT_TIMESTAMP();
    END IF;
END
//
DELIMITER ;
DELIMITER //
CREATE TRIGGER `sms_peringatan` AFTER INSERT ON `sentitems`
 FOR EACH ROW BEGIN
    DECLARE c1 VARCHAR(40);
    DECLARE cur CURSOR FOR SELECT Nama_User FROM User WHERE User.No_Hp_User = NEW.DestinationNumber;

    OPEN cur;
    FETCH cur INTO c1;
    INSERT INTO `SMSPeringatan` VALUES (NEW.ID, null, NEW.Status, CURRENT_TIMESTAMP(), NEW.TextDecoded, c1, NEW.DestinationNumber);
    CLOSE cur;
END
//
DELIMITER ;
DELIMITER //
CREATE TRIGGER `sms_peringatan_update` AFTER UPDATE ON `sentitems`
 FOR EACH ROW BEGIN
    UPDATE `SMSPeringatan` SET `Berita_Terkirim` = NEW.Status WHERE `Kode_SMS_Peringatan` = NEW.ID;
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `Sertifikasi`
--

CREATE TABLE IF NOT EXISTS `Sertifikasi` (
  `Kode_Sertifikasi` varchar(6) NOT NULL,
  `Id_Admin` varchar(6) NOT NULL,
  `No_Sertifikasi` varchar(30) DEFAULT NULL,
  `Jenis_Sertifikasi` varchar(30) DEFAULT NULL,
  `Sumber_Sertifikasi` varchar(100) DEFAULT NULL,
  `Tgl_Jatuh_Tempo_Srks` datetime DEFAULT NULL,
  `sms_terkirim` varchar(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `Sertifikasi`
--

INSERT INTO `Sertifikasi` (`Kode_Sertifikasi`, `Id_Admin`, `No_Sertifikasi`, `Jenis_Sertifikasi`, `Sumber_Sertifikasi`, `Tgl_Jatuh_Tempo_Srks`, `sms_terkirim`) VALUES
('1212', 'admin', '1212sasa', '1212', '1212', '2014-12-24 00:00:00', '1');

-- --------------------------------------------------------

--
-- Struktur dari tabel `SMSPeringatan`
--

CREATE TABLE IF NOT EXISTS `SMSPeringatan` (
  `Kode_SMS_Peringatan` varchar(6) NOT NULL,
  `Kode_Perpanjang` varchar(6) DEFAULT NULL,
  `Berita_Terkirim` varchar(40) DEFAULT 'Tunda',
  `Tgl_Pengiriman` datetime DEFAULT NULL,
  `Isi_SMS` text,
  `Nama` varchar(40) DEFAULT NULL,
  `No_Hp` decimal(12,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktur dari tabel `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `Id_User` varchar(6) NOT NULL,
  `Kode_SMS_Peringatan` varchar(6) DEFAULT NULL,
  `Nama_User` varchar(40) DEFAULT NULL,
  `No_Hp_User` varchar(12) DEFAULT NULL,
  `Alamat_User` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `User`
--

INSERT INTO `User` (`Id_User`, `Kode_SMS_Peringatan`, `Nama_User`, `No_Hp_User`, `Alamat_User`) VALUES
('baba', NULL, 'Ali Baba', '085654519941', 'Jl. Macan Kembang No. 40H');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Admin`
--
ALTER TABLE `Admin`
 ADD PRIMARY KEY (`Id_Admin`);

--
-- Indexes for table `inbox`
--
ALTER TABLE `inbox`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Laporan`
--
ALTER TABLE `Laporan`
 ADD PRIMARY KEY (`Kode_Laporan`), ADD KEY `Perpanjang_Laporan` (`Kode_Perpanjang`);

--
-- Indexes for table `outbox`
--
ALTER TABLE `outbox`
 ADD PRIMARY KEY (`ID`), ADD KEY `outbox_date` (`SendingDateTime`,`SendingTimeOut`), ADD KEY `outbox_sender` (`SenderID`);

--
-- Indexes for table `outbox_multipart`
--
ALTER TABLE `outbox_multipart`
 ADD PRIMARY KEY (`ID`,`SequencePosition`);

--
-- Indexes for table `Pajak`
--
ALTER TABLE `Pajak`
 ADD PRIMARY KEY (`Kode_Pajak`), ADD KEY `Admin_Pajak` (`Id_Admin`);

--
-- Indexes for table `pbk`
--
ALTER TABLE `pbk`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `pbk_groups`
--
ALTER TABLE `pbk_groups`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Pengujian`
--
ALTER TABLE `Pengujian`
 ADD PRIMARY KEY (`Kode_Pengujian`), ADD KEY `Admin_Pengujian` (`Id_Admin`);

--
-- Indexes for table `Perizinan`
--
ALTER TABLE `Perizinan`
 ADD PRIMARY KEY (`Kode_Perizinan`), ADD KEY `Admin_Perizinan` (`Id_Admin`);

--
-- Indexes for table `Perpanjang`
--
ALTER TABLE `Perpanjang`
 ADD PRIMARY KEY (`Kode_Perpanjang`), ADD KEY `Pajak_Perpanjang` (`Kode_Pajak`), ADD KEY `Perizinan_Perpanjang` (`Kode_Perizinan`), ADD KEY `Pengujian_Perpanjang` (`Kode_Pengujian`), ADD KEY `Sertifikasi_Perpanjang` (`Kode_Sertifikasi`), ADD KEY `Admin_Perpanjang` (`Id_Admin`);

--
-- Indexes for table `phones`
--
ALTER TABLE `phones`
 ADD PRIMARY KEY (`IMEI`);

--
-- Indexes for table `sentitems`
--
ALTER TABLE `sentitems`
 ADD PRIMARY KEY (`ID`,`SequencePosition`), ADD KEY `sentitems_date` (`DeliveryDateTime`), ADD KEY `sentitems_tpmr` (`TPMR`), ADD KEY `sentitems_dest` (`DestinationNumber`), ADD KEY `sentitems_sender` (`SenderID`);

--
-- Indexes for table `Sertifikasi`
--
ALTER TABLE `Sertifikasi`
 ADD PRIMARY KEY (`Kode_Sertifikasi`), ADD KEY `Admin_Sertifikasi` (`Id_Admin`);

--
-- Indexes for table `SMSPeringatan`
--
ALTER TABLE `SMSPeringatan`
 ADD PRIMARY KEY (`Kode_SMS_Peringatan`), ADD KEY `Perpanjang_SMS Peringatan` (`Kode_Perpanjang`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
 ADD PRIMARY KEY (`Id_User`), ADD KEY `SMS Peringatan_User` (`Kode_SMS_Peringatan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `inbox`
--
ALTER TABLE `inbox`
MODIFY `ID` int(10) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `outbox`
--
ALTER TABLE `outbox`
MODIFY `ID` int(10) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `pbk`
--
ALTER TABLE `pbk`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `pbk_groups`
--
ALTER TABLE `pbk_groups`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `Laporan`
--
ALTER TABLE `Laporan`
ADD CONSTRAINT `Perpanjang_Laporan` FOREIGN KEY (`Kode_Perpanjang`) REFERENCES `perpanjang` (`Kode_Perpanjang`);

--
-- Ketidakleluasaan untuk tabel `Perpanjang`
--
ALTER TABLE `Perpanjang`
ADD CONSTRAINT `Admin_Perpanjang` FOREIGN KEY (`Id_Admin`) REFERENCES `admin` (`Id_Admin`),
ADD CONSTRAINT `Pajak_Perpanjang` FOREIGN KEY (`Kode_Pajak`) REFERENCES `pajak` (`Kode_Pajak`),
ADD CONSTRAINT `Pengujian_Perpanjang` FOREIGN KEY (`Kode_Pengujian`) REFERENCES `pengujian` (`Kode_Pengujian`),
ADD CONSTRAINT `Perizinan_Perpanjang` FOREIGN KEY (`Kode_Perizinan`) REFERENCES `perizinan` (`Kode_Perizinan`),
ADD CONSTRAINT `Sertifikasi_Perpanjang` FOREIGN KEY (`Kode_Sertifikasi`) REFERENCES `sertifikasi` (`Kode_Sertifikasi`);

--
-- Ketidakleluasaan untuk tabel `SMSPeringatan`
--
ALTER TABLE `SMSPeringatan`
ADD CONSTRAINT `Perpanjang_SMS Peringatan` FOREIGN KEY (`Kode_Perpanjang`) REFERENCES `perpanjang` (`Kode_Perpanjang`);

--
-- Ketidakleluasaan untuk tabel `User`
--
ALTER TABLE `User`
ADD CONSTRAINT `SMS Peringatan_User` FOREIGN KEY (`Kode_SMS_Peringatan`) REFERENCES `smsperingatan` (`Kode_SMS_Peringatan`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
