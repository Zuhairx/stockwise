-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 11 Jan 2026 pada 11.22
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `stockwise2_db`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `products`
--

CREATE TABLE `products` (
  `product_id` varchar(11) NOT NULL,
  `product_category` varchar(100) DEFAULT NULL,
  `product_name` varchar(100) NOT NULL,
  `price` int(11) NOT NULL,
  `stock` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `products`
--

INSERT INTO `products` (`product_id`, `product_category`, `product_name`, `price`, `stock`, `created_at`, `updated_at`) VALUES
('PR-001', 'minuman', 'pop ice', 12000, 37, '2026-01-10 17:31:27', '2026-01-11 09:22:20'),
('PR-002', 'adad', 'adadw', 1333, 1, '2026-01-10 18:21:15', '2026-01-10 18:30:09'),
('PR-003', 'adwaw', 'adaw', 123, NULL, '2026-01-11 09:22:30', '2026-01-11 09:22:30');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` varchar(11) NOT NULL,
  `product_id` varchar(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `type` enum('IN','OUT') DEFAULT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `product_id`, `quantity`, `type`, `transaction_date`) VALUES
('TR-001', 'PR-001', 1, 'IN', '2026-01-10 18:16:26'),
('TR-002', 'PR-002', 1, 'IN', '2026-01-10 18:21:21'),
('TR-003', 'PR-001', 2, 'IN', '2026-01-10 18:24:12'),
('TR-004', 'PR-001', 11, 'IN', '2026-01-11 03:44:20'),
('TR-005', 'PR-001', 1, 'IN', '2026-01-11 09:22:20');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','STAFF') DEFAULT 'STAFF',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`) VALUES
(2, 'admin', '$2a$10$WKGxOpeQ.PjaOHIzig6MfeIEOn/PLzeoQSICE/yxvwxzs1qkoFyCW', 'ADMIN', '2026-01-10 13:09:24'),
(3, 'zuhair', '$2a$10$M7pEl4HvNErbNoYeRqM/CemSwGfsmLKVHCjobccTnXzbxJemWZzOS', 'STAFF', '2026-01-11 05:59:06'),
(4, 'ada', '$2a$10$QKt/zMyNZ.GL6dQPPUNc8OAOhI18pYzYV/BH3i7Lelzpaw2MZRymO', 'ADMIN', '2026-01-11 06:27:47'),
(5, 'test', '$2a$10$YIIZYmNoit/ItVZc./qDteSAB0PWrq0.80mfsUYOKs/9RDQjPhGVu', 'STAFF', '2026-01-11 06:30:40'),
(6, 'debby', '$2a$10$kgbKwflaSOFEm4d/GuQUS.Oz4t0xEo8FJrR1CZ7t7cDtbtGuc3I7C', 'ADMIN', '2026-01-11 09:23:21'),
(7, 'zuhairx', '$2a$10$rY2uaGbCjfYmJfvGgWKWr.guI6zs2IZolGNw6rAMAG9Av4knVgLcS', 'ADMIN', '2026-01-11 09:51:12'),
(8, 'adwwaw', '$2a$10$cdrvyI1HkT9eHswfX5I8..ufoTf6YGyMQFRFmJP0IPjxZVuqxp1nK', 'STAFF', '2026-01-11 09:58:57');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- Indeks untuk tabel `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `product_id` (`product_id`);

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
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
