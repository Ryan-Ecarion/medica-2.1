-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 29 Nov 2024 pada 07.26
-- Versi server: 10.4.22-MariaDB
-- Versi PHP: 8.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `medica`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `dokter`
--

CREATE TABLE `dokter` (
  `id_dokter` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `no_telepon` varchar(15) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `jenis_kelamin` enum('L','P') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `dokter`
--

INSERT INTO `dokter` (`id_dokter`, `nama`, `no_telepon`, `alamat`, `jenis_kelamin`) VALUES
(1, 'Dr. Andi Putra', '+62 8134567890', 'Jl. Mahakam No. 12, Banjarmasin, Kalimantan Selatan', 'L'),
(2, 'Dr. Siti Aisyah', '+62 8123456789', 'Jl. A. Yani No. 34, Banjarbaru, Kalimantan Selatan', 'P'),
(3, 'Dr. Budi Santoso', '+62 8135678901', 'Jl. Ir. Juanda No. 56, Martapura, Kalimantan Selatan', 'L'),
(4, 'Dr. Yusuf', '+62 86464535', 'Kalteng', 'L'),
(5, 'Dr. Agus Sinddia', '+62 8137890123', 'Jl. Pahlawan No. 9, Tapin, Kalimantan Selatan', 'L'),
(6, 'Dr. Rina Fitriani', '+62 8128901234', 'Jl. Kertak Hanyar No. 21, Banjarmasin, Kalimantan Selatan', 'P'),
(20, 'Dr. Ahmad Rasyid', '087123456789', 'Banjarmasin, Kalimantan Selatan', 'L'),
(21, 'Dr. Fitriyana Sari', '087234567890', 'Banjarbaru, Kalimantan Selatan', 'P'),
(23, 'Dr. Arini Putri', '087456789012', 'Tanjung, Kalimantan Selatan', 'P');

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwal_perawatan`
--

CREATE TABLE `jadwal_perawatan` (
  `id_jadwal` int(11) NOT NULL,
  `id_pasien` int(11) NOT NULL,
  `id_dokter` int(11) NOT NULL,
  `id_perawatan` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `status` enum('selesai','terjadwal','dibatalkan') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `jadwal_perawatan`
--

INSERT INTO `jadwal_perawatan` (`id_jadwal`, `id_pasien`, `id_dokter`, `id_perawatan`, `tanggal`, `status`) VALUES
(8, 30, 2, 2, '2024-11-07', 'dibatalkan'),
(9, 33, 1, 5, '2024-11-30', 'terjadwal'),
(11, 28, 1, 5, '2024-10-24', 'selesai'),
(12, 35, 1, 5, '2024-10-24', 'terjadwal'),
(14, 34, 1, 5, '2024-10-09', 'dibatalkan'),
(16, 32, 5, 5, '2024-10-09', 'dibatalkan'),
(17, 30, 2, 1, '2024-11-05', 'terjadwal'),
(18, 31, 3, 4, '2024-11-13', 'terjadwal'),
(19, 30, 2, 1, '2024-11-14', 'terjadwal'),
(20, 30, 2, 1, '2024-11-05', 'terjadwal');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pasien`
--

CREATE TABLE `pasien` (
  `id_pasien` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `jenis_kelamin` enum('L','P') NOT NULL,
  `no_telepon` varchar(15) DEFAULT NULL,
  `id_perawatan` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `pasien`
--

INSERT INTO `pasien` (`id_pasien`, `nama`, `alamat`, `jenis_kelamin`, `no_telepon`, `id_perawatan`) VALUES
(28, 'Liza', 'Handil', 'P', '08726362713', NULL),
(30, 'Aisyah', 'Barabai', 'P', '0831377374', NULL),
(31, 'Aluh', 'Barabai', 'P', '0831377374', NULL),
(32, 'Warta', 'Barabai', 'P', '0831377374', NULL),
(33, 'Sirani', 'Barabai', 'P', '0831377374', NULL),
(34, 'Arul', 'Tamban', 'L', '0874687464', NULL),
(35, 'Amrullah', 'Handil Badak', 'L', '086512348763', NULL),
(36, 'Alan', 'Kumogakure', 'L', '087632524', NULL),
(37, 'Anang', 'Kapuas', 'L', '086387236497', NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `perawatan`
--

CREATE TABLE `perawatan` (
  `id_perawatan` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `deskripsi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `perawatan`
--

INSERT INTO `perawatan` (`id_perawatan`, `nama`, `deskripsi`) VALUES
(1, 'Pembersihan Gigi (Scaling)', 'Pembersihan karang gigi dan plak pada gigi.'),
(2, 'Tambal Gigi', 'Proses menambal gigi yang berlubang.'),
(3, 'Cabut Gigi', 'Prosedur pencabutan gigi yang rusak atau tidak dapat diselamatkan.'),
(4, 'Pemasangan Kawat Gigi (Braces)', 'Proses pemasangan kawat gigi untuk merapikan susunan gigi.'),
(5, 'Pengobatan Saluran Akar (Root Canal)', 'Perawatan untuk mengobati infeksi pada saluran akar gigi.'),
(6, 'Pemasangan Implan Gigi', 'Pemasangan implan untuk menggantikan gigi yang hilang.'),
(7, 'Pemutihan Gigi (Bleaching)', 'Proses pemutihan gigi untuk tampilan yang lebih cerah.');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `role` enum('admin','resepsionis','dokter') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `nama`, `role`) VALUES
(2, 'ryan', 'ryan123', 'Ryan', 'admin'),
(7, 'admin', 'admin123', 'admin', 'admin');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`id_dokter`);

--
-- Indeks untuk tabel `jadwal_perawatan`
--
ALTER TABLE `jadwal_perawatan`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `id_pasien` (`id_pasien`),
  ADD KEY `id_dokter` (`id_dokter`),
  ADD KEY `id_perawatan` (`id_perawatan`);

--
-- Indeks untuk tabel `pasien`
--
ALTER TABLE `pasien`
  ADD PRIMARY KEY (`id_pasien`);

--
-- Indeks untuk tabel `perawatan`
--
ALTER TABLE `perawatan`
  ADD PRIMARY KEY (`id_perawatan`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `dokter`
--
ALTER TABLE `dokter`
  MODIFY `id_dokter` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT untuk tabel `jadwal_perawatan`
--
ALTER TABLE `jadwal_perawatan`
  MODIFY `id_jadwal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT untuk tabel `pasien`
--
ALTER TABLE `pasien`
  MODIFY `id_pasien` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT untuk tabel `perawatan`
--
ALTER TABLE `perawatan`
  MODIFY `id_perawatan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `jadwal_perawatan`
--
ALTER TABLE `jadwal_perawatan`
  ADD CONSTRAINT `jadwal_perawatan_ibfk_1` FOREIGN KEY (`id_pasien`) REFERENCES `pasien` (`id_pasien`),
  ADD CONSTRAINT `jadwal_perawatan_ibfk_2` FOREIGN KEY (`id_dokter`) REFERENCES `dokter` (`id_dokter`),
  ADD CONSTRAINT `jadwal_perawatan_ibfk_3` FOREIGN KEY (`id_perawatan`) REFERENCES `perawatan` (`id_perawatan`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `pasien`
--
ALTER TABLE `pasien`
  ADD CONSTRAINT `pasien_ibfk_1` FOREIGN KEY (`id_perawatan`) REFERENCES `perawatan` (`id_perawatan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
