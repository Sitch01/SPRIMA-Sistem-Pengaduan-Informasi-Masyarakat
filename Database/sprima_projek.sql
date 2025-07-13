-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 13, 2025 at 09:59 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sprima_projek`
--

-- --------------------------------------------------------

--
-- Table structure for table `pengaduan`
--

CREATE TABLE `pengaduan` (
  `id` int(11) NOT NULL,
  `judul_laporan` varchar(100) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `foto_path` varchar(255) DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `status` enum('Diajukan','Diproses','Selesai') DEFAULT 'Diajukan',
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengaduan`
--

INSERT INTO `pengaduan` (`id`, `judul_laporan`, `deskripsi`, `foto_path`, `tanggal`, `status`, `user_id`) VALUES
(1, 'wweqwewqe', 'weweweea', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\WhatsApp Image 2025-02-27 at 00.58.18_5e72b7e4.jpg', '2025-06-10', 'Diproses', NULL),
(2, 'saakakska', 'daasfsfasfsa', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\141999-sung-jin-woo-solo-leveling-live-wallpaper.jpg', '2025-07-10', 'Selesai', NULL),
(3, 'Ayam', 'ada pantai di desa ini', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\60533fe4ecebf.jpg', '2025-04-04', 'Selesai', NULL),
(4, 'Ayam hilng', 'Ada ayam hilang di rumah ku', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\664d939784ea45f6b1249ccb11dbeae0.jpg', '2025-06-30', 'Diproses', NULL),
(5, 'banjir', 'kkkkkkk', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\60533fe4ecebf.jpg', '2025-07-05', 'Selesai', NULL),
(6, 'aku di rampok', 'di uncok ada kelas 4 G', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\logo.png', '2025-03-12', 'Diajukan', NULL),
(7, 'Pencurian Kelapa di Rumanya Opu', 'Saya melihatnya lari kedalam hutan', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\WhatsApp Image 2024-10-13 at 16.37.15_cb2f7fa8.jpg', '2025-07-22', 'Diproses', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `nik` varchar(16) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('admin','petugas','masyarakat') NOT NULL DEFAULT 'masyarakat',
  `tempat_lahir` varchar(100) DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `no_telpon` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `nama_lengkap`, `nik`, `username`, `password`, `role`, `tempat_lahir`, `tanggal_lahir`, `no_telpon`, `alamat`, `foto`) VALUES
(1, 'Admin Desa', NULL, 'admin01', 'admin123', 'admin', NULL, NULL, NULL, NULL, NULL),
(2, 'Petugas Surat', NULL, 'petugas01', 'petugas123', 'petugas', NULL, NULL, NULL, NULL, NULL),
(3, 'Sakti Putra Pratama', '7317081802050001', 'Sakti', 'ayang12', 'masyarakat', 'Batu', '2005-02-18', '082191982787', 'jl dusun bkklkkkka', 'C:\\Users\\MSI GF63\\OneDrive\\Pictures\\gambar\\gentil_satellite_92079.jpg'),
(13, 'Nyoman', '7373080506110002', 'ayam', 'qwqwwq', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(14, 'Memet Kambing', '1111111111111111', 'Memet', '2323', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(17, 'mullis1', '1111111111188', 'dabi', 'wwewwww', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(21, 'Rizky Hidayat', '7317010101010001', 'rizkyh', 'pass123', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(22, 'Putri Anindya', '7317020202020002', 'putri_a', 'putri456', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(23, 'Andi Kurniawan', '7317030303030003', 'andi_k', 'andiku', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(24, 'Dewi Sartika', '7317040404040004', 'dewis', 'dewis@789', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(25, 'Budi Santoso', '7317050505050005', 'budi_s', 'budi2024', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(27, 'Fajar Nugroho', '7317060606060006', 'fajar_n', 'fajarpass', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(28, 'Sinta Lestari', '7317070707070007', 'sinta_l', 'sinta321', 'masyarakat', NULL, NULL, NULL, NULL, NULL),
(29, 'Yoga Pratama', '7317080808080008', 'yogap', 'yoga!pass', 'masyarakat', NULL, NULL, NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pengaduan`
--
ALTER TABLE `pengaduan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `nik` (`nik`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pengaduan`
--
ALTER TABLE `pengaduan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pengaduan`
--
ALTER TABLE `pengaduan`
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
