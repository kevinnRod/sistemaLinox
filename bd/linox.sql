-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 05-07-2025 a las 17:55:27
-- Versión del servidor: 9.1.0
-- Versión de PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `linox`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cargo`
--

DROP TABLE IF EXISTS `cargo`;
CREATE TABLE IF NOT EXISTS `cargo` (
  `id_cargo` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `id_estado` int NOT NULL,
  `nombre_cargo` varchar(100) NOT NULL,
  `update_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id_cargo`),
  UNIQUE KEY `UKbku282yk9xphnc54hugf4oqf8` (`nombre_cargo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cargo`
--

INSERT INTO `cargo` (`id_cargo`, `created_at`, `id_estado`, `nombre_cargo`, `update_at`) VALUES
(1, '2025-06-02 19:06:02.378514', 1, 'Vendedor', '2025-06-02 19:06:02.378514'),
(2, '2025-06-09 13:23:49.988733', 1, 'vendedor 1', '2025-06-09 13:23:49.988733');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_cliente`
--

DROP TABLE IF EXISTS `categoria_cliente`;
CREATE TABLE IF NOT EXISTS `categoria_cliente` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `id_estado` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `categoria_cliente`
--

INSERT INTO `categoria_cliente` (`id_categoria`, `created_at`, `id_estado`, `nombre`, `update_at`) VALUES
(1, '2025-06-02 19:02:14.477106', 1, 'Cliente Nuevo', '2025-06-07 22:30:54.505989'),
(2, '2025-06-07 22:28:07.150039', 1, 'Cliente Minorista', NULL),
(3, '2025-06-07 22:28:35.184669', 1, 'Cliente Mayorista', NULL),
(4, '2025-06-07 22:29:13.741435', 1, 'Cliente Preferencial', NULL),
(5, '2025-06-07 22:29:36.539841', 1, 'Cliente Gobierno', NULL),
(6, '2025-06-07 22:30:01.376682', 1, 'Cliente Internacional', NULL),
(7, '2025-06-07 22:30:18.356815', 1, 'Cliente VIP', NULL),
(8, '2025-06-07 22:31:18.301288', 1, 'Cliente Inactivo', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_producto`
--

DROP TABLE IF EXISTS `categoria_producto`;
CREATE TABLE IF NOT EXISTS `categoria_producto` (
  `id_categoria_p` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `descripcion` varchar(30) NOT NULL,
  `id_estado` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_categoria_p`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `categoria_producto`
--

INSERT INTO `categoria_producto` (`id_categoria_p`, `created_at`, `descripcion`, `id_estado`, `updated_at`) VALUES
(1, '2025-06-02 19:03:48.562942', 'Industrial', 1, '2025-06-02 19:03:48.562942'),
(2, '2025-06-07 22:44:02.177992', 'Aceros Planos', 1, '2025-06-07 22:44:02.177992'),
(3, '2025-06-07 22:48:35.237806', 'Aceros Largos', 1, '2025-06-07 22:48:35.237806'),
(4, '2025-06-07 22:48:45.746227', 'Tubos y Tuberías', 1, '2025-06-07 22:48:45.746227'),
(5, '2025-06-07 22:49:07.074036', 'Perfiles y Vigas', 1, '2025-06-07 22:49:07.074036'),
(6, '2025-06-07 22:49:25.833425', 'Accesorios de Unión', 1, '2025-06-07 22:49:25.833425'),
(7, '2025-06-07 22:50:06.622242', 'Aceros Inoxidables', 1, '2025-06-07 22:50:06.622242'),
(8, '2025-06-07 22:50:17.330871', 'Mallas y Alambrón', 1, '2025-06-07 22:50:17.330871'),
(9, '2025-06-07 22:50:31.970050', 'Chapas y Bobinas', 1, '2025-06-07 22:50:31.970050'),
(10, '2025-06-07 22:50:54.287069', 'Aceros Especiales', 1, '2025-06-07 22:50:54.287069'),
(11, '2025-06-08 00:06:49.065250', 'Barras de acero', 1, '2025-06-08 00:06:49.065250'),
(12, '2025-06-08 00:06:57.369711', 'Tubos de acero', 1, '2025-06-08 00:06:57.369711');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE IF NOT EXISTS `cliente` (
  `cod_cliente` varchar(8) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_categoria_c` int DEFAULT NULL,
  `tipo_cliente` varchar(31) NOT NULL,
  PRIMARY KEY (`cod_cliente`),
  KEY `FKdajda7wildpob82xutyb4qh9y` (`id_categoria_c`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`cod_cliente`, `created_at`, `id_estado`, `updated_at`, `id_categoria_c`, `tipo_cliente`) VALUES
('CLI-0001', '2025-06-02 19:02:49.922105', 1, '2025-06-02 19:02:49.922105', 1, 'natural'),
('CLI-0002', '2025-06-03 08:11:28.162353', 1, '2025-06-03 08:11:28.162353', 1, 'juridico'),
('CLI-0003', '2025-06-07 22:36:35.516557', 1, '2025-06-07 22:36:35.516557', 2, 'natural'),
('CLI-0004', '2025-06-07 22:38:54.867709', 1, '2025-06-07 22:38:54.867709', 3, 'juridico'),
('CLI-0005', '2025-06-07 22:41:08.953586', 1, '2025-06-07 22:41:08.953586', 4, 'juridico'),
('CLI-0006', '2025-05-10 09:26:26.743345', 1, '2025-05-10 09:26:26.743345', 1, 'natural'),
('CLI-0007', '2025-06-09 13:28:02.513505', 1, '2025-06-09 13:28:02.513505', 1, 'natural'),
('CLI-0008', '2025-06-14 22:11:01.769572', 1, '2025-06-14 22:11:01.769572', 1, 'juridico');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente_juridico`
--

DROP TABLE IF EXISTS `cliente_juridico`;
CREATE TABLE IF NOT EXISTS `cliente_juridico` (
  `cod_cliente` varchar(8) NOT NULL,
  `id_empresa` int DEFAULT NULL,
  PRIMARY KEY (`cod_cliente`),
  UNIQUE KEY `UKbii7v5jnyga1208wyiewujurr` (`id_empresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cliente_juridico`
--

INSERT INTO `cliente_juridico` (`cod_cliente`, `id_empresa`) VALUES
('CLI-0002', 2),
('CLI-0004', 3),
('CLI-0005', 4),
('CLI-0008', 10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente_natural`
--

DROP TABLE IF EXISTS `cliente_natural`;
CREATE TABLE IF NOT EXISTS `cliente_natural` (
  `cod_cliente` varchar(8) NOT NULL,
  `id_persona` int DEFAULT NULL,
  PRIMARY KEY (`cod_cliente`),
  UNIQUE KEY `UKjhenctlemle8ffn2k12fl8k8l` (`id_persona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cliente_natural`
--

INSERT INTO `cliente_natural` (`cod_cliente`, `id_persona`) VALUES
('CLI-0001', 1),
('CLI-0003', 4),
('CLI-0006', 5),
('CLI-0007', 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_venta`
--

DROP TABLE IF EXISTS `detalle_venta`;
CREATE TABLE IF NOT EXISTS `detalle_venta` (
  `id_producto` int NOT NULL,
  `id_venta` int NOT NULL,
  `cantidad` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_producto`,`id_venta`),
  KEY `FKoknpg31rxsqnjxrsu7iy47p1o` (`id_venta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `detalle_venta`
--

INSERT INTO `detalle_venta` (`id_producto`, `id_venta`, `cantidad`, `created_at`, `id_estado`, `subtotal`, `updated_at`) VALUES
(1, 1, 1, '2025-06-02 19:07:06.319591', 0, 35.00, '2025-06-07 20:14:07.196739'),
(1, 2, 4, '2025-06-03 01:45:58.562651', 0, 140.00, '2025-06-08 11:30:09.148696'),
(1, 3, 2, '2025-06-03 01:49:04.734217', 0, 70.00, '2025-06-10 09:29:14.616799'),
(1, 4, 2, '2025-06-03 01:52:47.193062', 0, 70.00, '2025-06-07 20:23:02.954615'),
(1, 5, 2, '2025-06-03 08:12:27.928527', 1, 70.00, '2025-06-03 08:12:27.928527'),
(1, 6, 2, '2025-06-07 16:05:08.434051', 1, 70.00, '2025-06-07 16:05:08.434051'),
(1, 7, 2, '2025-06-07 18:17:03.469513', 1, 70.00, '2025-06-07 18:17:03.469513'),
(1, 10, 3, '2025-06-16 23:05:57.916515', 1, 105.00, '2025-06-16 23:05:57.916515'),
(1, 12, 2, '2025-06-17 00:59:55.895432', 1, 70.00, '2025-06-17 00:59:55.895432'),
(1, 22, 15, '2025-06-22 17:20:25.202707', 1, 525.00, '2025-06-22 17:20:25.202707'),
(2, 4, 1, '2025-06-03 01:52:47.193062', 0, 45.00, '2025-06-07 20:23:03.087139'),
(2, 5, 3, '2025-06-03 08:12:27.928527', 1, 135.00, '2025-06-03 08:12:27.928527'),
(2, 12, 1, '2025-06-17 00:59:55.895432', 1, 45.00, '2025-06-17 00:59:55.895432'),
(2, 14, 52, '2025-06-21 18:19:53.043069', 1, 2340.00, '2025-06-21 18:19:53.043069'),
(3, 12, 5, '2025-06-17 00:59:55.895432', 1, 142.50, '2025-06-17 00:59:55.895432'),
(3, 27, 6, '2025-06-24 08:08:26.734179', 1, 171.00, '2025-06-24 08:08:26.734179'),
(4, 12, 1, '2025-06-17 00:59:55.895432', 1, 215.00, '2025-06-17 00:59:55.895432'),
(4, 29, 2, '2025-06-24 08:10:56.152449', 1, 430.00, '2025-06-24 08:10:56.152449'),
(5, 12, 2, '2025-06-17 00:59:55.895432', 1, 96.00, '2025-06-17 00:59:55.895432'),
(5, 27, 4, '2025-06-24 08:08:26.734179', 1, 192.00, '2025-06-24 08:08:26.734179'),
(6, 8, 2, '2025-06-08 00:37:53.988382', 1, 62.40, '2025-06-08 00:37:53.988382'),
(6, 13, 3, '2025-06-17 02:29:11.931793', 1, 93.60, '2025-06-17 02:29:11.931793'),
(6, 33, 6, '2025-07-01 08:04:17.590297', 1, 187.20, '2025-07-01 08:04:17.590297'),
(7, 9, 20, '2025-06-08 11:28:36.698277', 1, 440.00, '2025-06-08 11:28:36.698277'),
(7, 10, 40, '2025-06-16 23:05:57.917595', 1, 880.00, '2025-06-16 23:05:57.917595'),
(7, 11, 3, '2025-06-16 23:10:41.032555', 1, 66.00, '2025-06-16 23:10:41.032555'),
(7, 13, 5, '2025-06-17 02:29:11.930795', 1, 110.00, '2025-06-17 02:29:11.930795'),
(8, 8, 4, '2025-06-08 00:37:53.988382', 1, 31.60, '2025-06-08 00:37:53.988382'),
(8, 13, 20, '2025-06-17 02:29:11.931793', 1, 158.00, '2025-06-17 02:29:11.931793'),
(8, 27, 5, '2025-06-24 08:08:26.734179', 1, 39.50, '2025-06-24 08:08:26.734179'),
(9, 10, 2, '2025-06-16 23:05:57.917595', 1, 119.00, '2025-06-16 23:05:57.917595'),
(9, 11, 2, '2025-06-16 23:10:41.032555', 1, 119.00, '2025-06-16 23:10:41.032555'),
(9, 23, 2, '2025-06-23 16:01:37.828374', 1, 119.00, '2025-06-23 16:01:37.828374'),
(9, 25, 2, '2025-06-23 16:02:18.360910', 1, 119.00, '2025-06-23 16:02:18.360910'),
(9, 32, 10, '2025-07-01 08:02:29.445192', 1, 595.00, '2025-07-01 08:02:29.445192'),
(10, 17, 54, '2025-06-22 00:10:10.444055', 1, 334.80, '2025-06-22 00:10:10.444055'),
(10, 19, 20, '2025-06-22 15:47:25.247320', 1, 124.00, '2025-06-22 15:47:25.247320'),
(10, 28, 20, '2025-06-24 08:09:29.486501', 1, 124.00, '2025-06-24 08:09:29.486501'),
(10, 31, 4, '2025-07-01 07:59:31.593509', 1, 24.80, '2025-07-01 07:59:31.593509'),
(10, 32, 15, '2025-07-01 08:02:29.445192', 1, 93.00, '2025-07-01 08:02:29.445192'),
(10, 34, 100, '2025-07-01 08:38:00.828553', 1, 620.00, '2025-07-01 08:38:00.828553'),
(11, 9, 3, '2025-06-08 11:28:36.699283', 1, 162.00, '2025-06-08 11:28:36.699283'),
(11, 11, 2, '2025-06-16 23:10:41.032555', 1, 108.00, '2025-06-16 23:10:41.032555'),
(11, 15, 45, '2025-06-21 23:55:54.035711', 1, 2430.00, '2025-06-21 23:55:54.035711'),
(11, 16, 5, '2025-06-21 23:57:12.032259', 1, 270.00, '2025-06-21 23:57:12.032259'),
(11, 18, 3, '2025-06-22 01:34:08.955455', 1, 162.00, '2025-06-22 01:34:08.955455'),
(11, 21, 3, '2025-06-22 16:28:44.250053', 1, 162.00, '2025-06-22 16:28:44.250053'),
(11, 24, 2, '2025-06-23 16:01:57.145796', 1, 108.00, '2025-06-23 16:01:57.145796'),
(11, 26, 2, '2025-06-24 00:03:39.423657', 1, 108.00, '2025-06-24 00:03:39.423657'),
(11, 27, 2, '2025-06-24 08:08:26.734179', 1, 108.00, '2025-06-24 08:08:26.734179'),
(11, 32, 4, '2025-07-01 08:02:29.445192', 1, 216.00, '2025-07-01 08:02:29.445192'),
(11, 33, 5, '2025-07-01 08:04:17.590297', 1, 270.00, '2025-07-01 08:04:17.590297'),
(12, 11, 1, '2025-06-16 23:10:41.032555', 1, 65.00, '2025-06-16 23:10:41.032555'),
(12, 20, 4, '2025-06-22 16:27:24.728562', 1, 260.00, '2025-06-22 16:27:24.728562'),
(12, 28, 4, '2025-06-24 08:09:29.486501', 1, 260.00, '2025-06-24 08:09:29.486501'),
(12, 30, 2, '2025-07-01 07:59:03.199257', 1, 130.00, '2025-07-01 07:59:03.199257'),
(12, 31, 1, '2025-07-01 07:59:31.594620', 1, 65.00, '2025-07-01 07:59:31.594620'),
(13, 10, 1, '2025-06-16 23:05:57.917595', 1, 78.00, '2025-06-16 23:05:57.917595'),
(13, 29, 3, '2025-06-24 08:10:56.152449', 1, 234.00, '2025-06-24 08:10:56.152449');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

DROP TABLE IF EXISTS `empleado`;
CREATE TABLE IF NOT EXISTS `empleado` (
  `cod_empleado` varchar(20) DEFAULT NULL,
  `id_persona` int NOT NULL,
  `id_cargo` int NOT NULL,
  `id_sucursal` int NOT NULL,
  PRIMARY KEY (`id_persona`),
  KEY `FK739vkywoel8qoad30ovv9ksgl` (`id_cargo`),
  KEY `FK5lshn105gw8o1gcw78wysko3u` (`id_sucursal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`cod_empleado`, `id_persona`, `id_cargo`, `id_sucursal`) VALUES
('EMP00001', 2, 1, 1),
('EMP00002', 3, 1, 1),
('EMP00003', 6, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

DROP TABLE IF EXISTS `empresa`;
CREATE TABLE IF NOT EXISTS `empresa` (
  `id_empresa` int NOT NULL AUTO_INCREMENT,
  `correo` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `direccion` varchar(150) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `nombre_comercial` varchar(80) DEFAULT NULL,
  `razon_social` varchar(100) NOT NULL,
  `ruc` varchar(11) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_empresa`),
  UNIQUE KEY `UKlav9hagh3bprtwk9asbhrkxdj` (`razon_social`),
  UNIQUE KEY `UKfkso2kbttplho71hoeka6px1s` (`ruc`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `empresa`
--

INSERT INTO `empresa` (`id_empresa`, `correo`, `created_at`, `direccion`, `id_estado`, `nombre_comercial`, `razon_social`, `ruc`, `telefono`, `updated_at`) VALUES
(1, 'linox@gmail.com', '2025-06-02 18:46:43.057210', 'AV.GUILLERMO DANSEY N° 980 H-13 LIMA LIMA LIMA', 1, 'LINOX', 'ACERO LINOX E.I.R.L.', '20603643454', '986890833', '2025-06-02 18:47:27.473855'),
(2, 'admin@example.com', '2025-06-02 19:01:53.858178', 'AV.GUILLERMO DANSEY N° 980 H-13 LIMA LIMA LIMA', 1, 'ArcelorMittal', 'ArcerolMittal S.A.', '20603643452', '927663928', '2025-06-03 08:11:27.679484'),
(3, 'contacto@constructoraandina.pe', '2025-06-07 22:38:54.393748', 'Av. Javier Prado Este 4567, San Borja, Lima', 1, 'Constructora Andina', 'Constructora Andina SAC', '20547896321', '014567893', '2025-06-07 22:38:54.393748'),
(4, 'ventas@metalurgicadelsur.com', '2025-06-07 22:41:08.696740', 'Jr. Industrial 241, Parque Industrial, Arequipa', 1, 'Metalúrgica del Sur', 'Metalúrgica del Sur EIRL', '20458963214', '054234567', '2025-06-07 22:41:08.696740'),
(5, 'ventas@acerosperu.com', '2025-06-07 23:46:16.045609', 'Av. Argentina 1234, Cercado de Lima, Lima', 1, 'Aceros del Perú', 'Aceros del Perú S.A.C.', '20546789012', '(01) 567-8901', '2025-06-07 23:46:16.045609'),
(6, 'contacto@metalurgicaandina.pe', '2025-06-07 23:50:23.473289', 'Jr. Los Ferroles 456, Ate, Lima', 1, 'Metalúrgica Andina', 'Metalúrgica Andina E.I.R.L.', '10458923100', '989-456-123', '2025-06-07 23:50:23.474291'),
(7, 'servicio@acerosarequipa.com', '2025-06-07 23:51:35.886526', 'Av. La Cultura 1478, Arequipa', 1, 'Aceros Arequipa', 'Aceros Arequipa S.A.', '20100123971', '(054) 234567', '2025-06-07 23:51:35.886526'),
(8, 'global@steelimport.com.pe', '2025-06-07 23:52:58.183787', 'Av. El Sol 300, San Juan de Lurigancho, Lima', 1, 'Global Steel Import', 'Global Steel Import S.A.C.', '20600111222', '981-987-123', '2025-06-07 23:52:58.183787'),
(9, 'pedidos@acerosindustriales.pe', '2025-06-07 23:54:23.032810', 'Calle Metalurgia 999, Callao', 1, 'Aceros y Suministros Industriales', 'Aceros y Suministros Industriales S.A.C.', '20345678900', '(01) 789-6543', '2025-06-07 23:54:23.032810'),
(10, '', '2025-06-14 22:11:01.286939', '', 1, '', '', ',', '', '2025-06-14 22:11:01.286939');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa_anfitrion`
--

DROP TABLE IF EXISTS `empresa_anfitrion`;
CREATE TABLE IF NOT EXISTS `empresa_anfitrion` (
  `logo_url` varchar(255) DEFAULT NULL,
  `id_empresa` int NOT NULL,
  PRIMARY KEY (`id_empresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `empresa_anfitrion`
--

INSERT INTO `empresa_anfitrion` (`logo_url`, `id_empresa`) VALUES
('/uploads/empresa/2391f982-fb1e-49a8-9777-c8f6b78927a5_logo_linox_2.jpg', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `kardex`
--

DROP TABLE IF EXISTS `kardex`;
CREATE TABLE IF NOT EXISTS `kardex` (
  `id_kardex` int NOT NULL AUTO_INCREMENT,
  `cantidad` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `documento_referencia` varchar(30) DEFAULT NULL,
  `fecha_movimiento` datetime(6) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `observaciones` varchar(100) DEFAULT NULL,
  `precio_unitario` decimal(10,2) DEFAULT NULL,
  `stock_resultante` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_producto` int DEFAULT NULL,
  `id_productoe` int DEFAULT NULL,
  `id_sucursal` int DEFAULT NULL,
  `id_tipo_movimiento` int DEFAULT NULL,
  PRIMARY KEY (`id_kardex`),
  KEY `FKcsdmhmlq8k6vaodyqfv3ybbs5` (`id_producto`),
  KEY `FK1lobecy12t0wdyo9mor6w8aak` (`id_productoe`),
  KEY `FK1jw3p60w9hx6rx54rwv7tg5yk` (`id_sucursal`),
  KEY `FKbnwd35rcw7tvy3is2em332dsn` (`id_tipo_movimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `kardex`
--

INSERT INTO `kardex` (`id_kardex`, `cantidad`, `created_at`, `documento_referencia`, `fecha_movimiento`, `id_estado`, `id_usuario`, `observaciones`, `precio_unitario`, `stock_resultante`, `updated_at`, `id_producto`, `id_productoe`, `id_sucursal`, `id_tipo_movimiento`) VALUES
(1, 2, '2025-06-07 18:17:03.365856', 'RRD01-00007', '2025-06-07 18:17:02.767789', 1, 1, 'Se realizó una venta', 35.00, 46, '2025-06-07 18:17:03.365856', 1, NULL, 1, 2),
(2, 1, '2025-06-07 20:14:07.524860', 'RRD01-00001', '2025-06-07 20:14:07.523893', 1, 1, 'Anulación de venta', 35.00, 47, '2025-06-07 20:14:07.524860', 1, NULL, 1, 1),
(3, 2, '2025-06-07 20:23:03.043017', 'RRD01-00004', '2025-06-07 20:23:03.041925', 1, 1, 'Anulación de venta', 35.00, 49, '2025-06-07 20:23:03.043017', 1, NULL, 1, 1),
(4, 1, '2025-06-07 20:23:03.279980', 'RRD01-00004', '2025-06-07 20:23:03.279210', 1, 1, 'Anulación de venta', 45.00, 101, '2025-06-07 20:23:03.279980', 2, NULL, 1, 1),
(5, 2, '2025-06-08 00:37:52.768009', 'RRD01-00008', '2025-06-08 00:37:51.869934', 1, 1, 'Se realizó una venta', 31.20, 498, '2025-06-08 00:37:52.768009', 6, NULL, 1, 2),
(6, 4, '2025-06-08 00:37:53.615826', 'RRD01-00008', '2025-06-08 00:37:51.869934', 1, 1, 'Se realizó una venta', 7.90, 1796, '2025-06-08 00:37:53.615826', 8, NULL, 2, 2),
(7, 20, '2025-06-08 11:28:35.910583', 'RRD01-00009', '2025-06-08 11:28:35.106843', 1, 1, 'Se realizó una venta', 22.00, 1430, '2025-06-08 11:28:35.910583', 7, NULL, 2, 2),
(8, 3, '2025-06-08 11:28:36.508979', 'RRD01-00009', '2025-06-08 11:28:35.106843', 1, 1, 'Se realizó una venta', 54.00, 847, '2025-06-08 11:28:36.508979', 11, NULL, 1, 2),
(9, 4, '2025-06-08 11:30:09.811043', 'RRD01-00002', '2025-06-08 11:30:09.811043', 1, 1, 'Anulación de venta', 35.00, 53, '2025-06-08 11:30:09.811043', 1, NULL, 1, 1),
(10, 2, '2025-06-10 09:29:14.813423', 'RRD01-00003', '2025-06-10 09:29:14.811422', 1, 1, 'Anulación de venta', 35.00, 55, '2025-06-10 09:29:14.813423', 1, NULL, 1, 1),
(11, 3, '2025-06-16 23:05:52.200675', 'RRD01-00010', '2025-06-16 23:05:47.269658', 1, 1, 'Se realizó una venta', 35.00, 52, '2025-06-16 23:05:52.200675', 1, NULL, 1, 2),
(12, 40, '2025-06-16 23:05:55.646898', 'RRD01-00010', '2025-06-16 23:05:47.269658', 1, 1, 'Se realizó una venta', 22.00, 1390, '2025-06-16 23:05:55.646898', 7, NULL, 2, 2),
(13, 2, '2025-06-16 23:05:56.397369', 'RRD01-00010', '2025-06-16 23:05:47.269658', 1, 1, 'Se realizó una venta', 59.50, 598, '2025-06-16 23:05:56.397369', 9, NULL, 2, 2),
(14, 1, '2025-06-16 23:05:57.161206', 'RRD01-00010', '2025-06-16 23:05:47.269658', 1, 1, 'Se realizó una venta', 78.00, 299, '2025-06-16 23:05:57.161206', 13, NULL, 1, 2),
(15, 1, '2025-06-16 23:10:38.096379', 'RRD01-00011', '2025-06-16 23:10:36.528221', 1, 1, 'Se realizó una venta', 65.00, 429, '2025-06-16 23:10:38.096379', 12, NULL, 1, 2),
(16, 2, '2025-06-16 23:10:38.562891', 'RRD01-00011', '2025-06-16 23:10:36.528221', 1, 1, 'Se realizó una venta', 54.00, 845, '2025-06-16 23:10:38.562891', 11, NULL, 1, 2),
(17, 2, '2025-06-16 23:10:38.905720', 'RRD01-00011', '2025-06-16 23:10:36.528221', 1, 1, 'Se realizó una venta', 59.50, 596, '2025-06-16 23:10:38.905720', 9, NULL, 2, 2),
(18, 3, '2025-06-16 23:10:40.872045', 'RRD01-00011', '2025-06-16 23:10:36.528221', 1, 1, 'Se realizó una venta', 22.00, 1387, '2025-06-16 23:10:40.872045', 7, NULL, 2, 2),
(19, 2, '2025-06-17 00:59:55.390325', 'RRD01-00012', '2025-06-17 00:59:54.904508', 1, 1, 'Se realizó una venta', 35.00, 50, '2025-06-17 00:59:55.390325', 1, NULL, 1, 2),
(20, 1, '2025-06-17 00:59:55.528335', 'RRD01-00012', '2025-06-17 00:59:54.904508', 1, 1, 'Se realizó una venta', 45.00, 100, '2025-06-17 00:59:55.528335', 2, NULL, 1, 2),
(21, 5, '2025-06-17 00:59:55.723424', 'RRD01-00012', '2025-06-17 00:59:54.904508', 1, 1, 'Se realizó una venta', 28.50, 1195, '2025-06-17 00:59:55.723424', 3, NULL, 1, 2),
(22, 1, '2025-06-17 00:59:55.768175', 'RRD01-00012', '2025-06-17 00:59:54.904508', 1, 1, 'Se realizó una venta', 215.00, 349, '2025-06-17 00:59:55.768175', 4, NULL, 2, 2),
(23, 2, '2025-06-17 00:59:55.868343', 'RRD01-00012', '2025-06-17 00:59:54.904508', 1, 1, 'Se realizó una venta', 48.00, 778, '2025-06-17 00:59:55.868343', 5, NULL, 1, 2),
(24, 5, '2025-06-17 02:29:11.611097', 'RRD01-00013', '2025-06-17 02:29:10.923812', 1, 1, 'Se realizó una venta', 22.00, 1382, '2025-06-17 02:29:11.611097', 7, NULL, 2, 2),
(25, 20, '2025-06-17 02:29:11.821258', 'RRD01-00013', '2025-06-17 02:29:10.923812', 1, 1, 'Se realizó una venta', 7.90, 1776, '2025-06-17 02:29:11.821258', 8, NULL, 2, 2),
(26, 3, '2025-06-17 02:29:11.888024', 'RRD01-00013', '2025-06-17 02:29:10.923812', 1, 1, 'Se realizó una venta', 31.20, 495, '2025-06-17 02:29:11.888024', 6, NULL, 1, 2),
(27, 52, '2025-06-21 18:19:52.825071', 'RRD01-00014', '2025-06-21 18:19:51.979975', 1, 1, 'Se realizó una venta', 45.00, 48, '2025-06-21 18:19:52.825071', 2, NULL, 1, 2),
(28, 45, '2025-06-21 23:55:53.915322', 'RRD01-00015', '2025-06-21 23:55:53.765572', 1, 1, 'Se realizó una venta', 54.00, 800, '2025-06-21 23:55:53.915322', 11, NULL, 1, 2),
(29, 5, '2025-06-21 23:57:11.965560', 'RRD01-00016', '2025-06-21 23:57:11.838151', 1, 1, 'Se realizó una venta', 54.00, 795, '2025-06-21 23:57:11.965560', 11, NULL, 1, 2),
(30, 54, '2025-06-22 00:10:10.310818', 'RRD01-00017', '2025-06-22 00:10:09.811212', 1, 1, 'Se realizó una venta', 6.20, 866, '2025-06-22 00:10:10.310818', 10, NULL, 1, 2),
(31, 3, '2025-06-22 01:34:08.690047', 'RRD01-00018', '2025-06-22 01:34:08.067429', 1, 1, 'Se realizó una venta', 54.00, 792, '2025-06-22 01:34:08.690047', 11, NULL, 1, 2),
(32, 20, '2025-06-22 15:47:24.416851', 'RRD01-00019', '2025-06-22 15:47:23.383835', 1, 1, 'Se realizó una venta', 6.20, 846, '2025-06-22 15:47:24.416851', 10, NULL, 1, 2),
(33, 4, '2025-06-22 16:27:24.584633', 'RRD01-00020', '2025-06-22 16:27:23.851477', 1, 1, 'Se realizó una venta', 65.00, 425, '2025-06-22 16:27:24.584633', 12, NULL, 1, 2),
(34, 3, '2025-06-22 16:28:44.164527', 'RRD01-00021', '2025-06-22 16:28:43.952362', 1, 1, 'Se realizó una venta', 54.00, 789, '2025-06-22 16:28:44.164527', 11, NULL, 1, 2),
(35, 15, '2025-06-22 17:20:24.972464', 'RRD01-00022', '2025-06-22 17:20:24.684341', 1, 1, 'Se realizó una venta', 35.00, 35, '2025-06-22 17:20:24.972464', 1, NULL, 3, 2),
(36, 2, '2025-06-23 16:01:36.607567', 'RRD01-00023', '2025-06-23 16:01:35.302883', 1, 1, 'Se realizó una venta', 59.50, 594, '2025-06-23 16:01:36.607567', 9, NULL, 2, 2),
(37, 2, '2025-06-23 16:01:57.097347', 'RRD01-00024', '2025-06-23 16:01:56.730356', 1, 1, 'Se realizó una venta', 54.00, 787, '2025-06-23 16:01:57.097347', 11, NULL, 1, 2),
(38, 2, '2025-06-23 16:02:18.170801', 'RRD01-00025', '2025-06-23 16:02:17.843318', 1, 1, 'Se realizó una venta', 59.50, 592, '2025-06-23 16:02:18.170801', 9, NULL, 2, 2),
(39, 2, '2025-06-24 00:03:38.509429', 'RRD01-00026', '2025-06-24 00:03:36.867462', 1, 1, 'Se realizó una venta', 54.00, 785, '2025-06-24 00:03:38.509429', 11, NULL, 1, 2),
(40, 2, '2025-06-24 08:08:26.345969', 'RRD01-00027', '2025-06-24 08:08:25.380101', 1, 1, 'Se realizó una venta', 54.00, 783, '2025-06-24 08:08:26.345969', 11, NULL, 1, 2),
(41, 5, '2025-06-24 08:08:26.524498', 'RRD01-00027', '2025-06-24 08:08:25.380101', 1, 1, 'Se realizó una venta', 7.90, 1771, '2025-06-24 08:08:26.524498', 8, NULL, 2, 2),
(42, 6, '2025-06-24 08:08:26.612652', 'RRD01-00027', '2025-06-24 08:08:25.380101', 1, 1, 'Se realizó una venta', 28.50, 1189, '2025-06-24 08:08:26.612652', 3, NULL, 1, 2),
(43, 4, '2025-06-24 08:08:26.664552', 'RRD01-00027', '2025-06-24 08:08:25.380101', 1, 1, 'Se realizó una venta', 48.00, 41, '2025-06-24 08:08:26.664552', 5, NULL, 3, 2),
(44, 4, '2025-06-24 08:09:28.450516', 'RRD01-00028', '2025-06-24 08:09:27.843734', 1, 1, 'Se realizó una venta', 65.00, 421, '2025-06-24 08:09:28.450516', 12, NULL, 1, 2),
(45, 20, '2025-06-24 08:09:29.412542', 'RRD01-00028', '2025-06-24 08:09:27.843734', 1, 1, 'Se realizó una venta', 6.20, 826, '2025-06-24 08:09:29.412542', 10, NULL, 1, 2),
(46, 3, '2025-06-24 08:10:50.400338', 'RRD01-00029', '2025-06-24 08:10:49.684202', 1, 1, 'Se realizó una venta', 78.00, 296, '2025-06-24 08:10:50.400338', 13, NULL, 1, 2),
(47, 2, '2025-06-24 08:10:52.218250', 'RRD01-00029', '2025-06-24 08:10:49.684202', 1, 1, 'Se realizó una venta', 215.00, 347, '2025-06-24 08:10:52.218250', 4, NULL, 4, 2),
(48, 2, '2025-07-01 07:59:03.060545', 'RRD01-00030', '2025-07-01 07:59:02.592121', 1, 1, 'Se realizó una venta', 65.00, 419, '2025-07-01 07:59:03.060545', 12, NULL, 1, 2),
(49, 4, '2025-07-01 07:59:31.085221', 'RRD01-00031', '2025-07-01 07:59:30.677761', 1, 1, 'Se realizó una venta', 6.20, 822, '2025-07-01 07:59:31.085221', 10, NULL, 1, 2),
(50, 1, '2025-07-01 07:59:31.434819', 'RRD01-00031', '2025-07-01 07:59:30.677761', 1, 1, 'Se realizó una venta', 65.00, 418, '2025-07-01 07:59:31.434819', 12, NULL, 1, 2),
(51, 15, '2025-07-01 08:02:29.347613', 'RRD01-00032', '2025-07-01 08:02:29.258679', 1, 1, 'Se realizó una venta', 6.20, 807, '2025-07-01 08:02:29.347613', 10, NULL, 1, 2),
(52, 4, '2025-07-01 08:02:29.380617', 'RRD01-00032', '2025-07-01 08:02:29.258679', 1, 1, 'Se realizó una venta', 54.00, 779, '2025-07-01 08:02:29.380617', 11, NULL, 1, 2),
(53, 10, '2025-07-01 08:02:29.427460', 'RRD01-00032', '2025-07-01 08:02:29.258679', 1, 1, 'Se realizó una venta', 59.50, 582, '2025-07-01 08:02:29.427460', 9, NULL, 2, 2),
(54, 5, '2025-07-01 08:04:17.277391', 'RRD01-00033', '2025-07-01 08:04:17.201655', 1, 1, 'Se realizó una venta', 54.00, 774, '2025-07-01 08:04:17.277391', 11, NULL, 1, 2),
(55, 6, '2025-07-01 08:04:17.509765', 'RRD01-00033', '2025-07-01 08:04:17.201655', 1, 1, 'Se realizó una venta', 31.20, 489, '2025-07-01 08:04:17.509765', 6, NULL, 1, 2),
(56, 100, '2025-07-01 08:38:00.785418', 'RRD01-00034', '2025-07-01 08:38:00.509023', 1, 1, 'Se realizó una venta', 6.20, 707, '2025-07-01 08:38:00.785418', 10, NULL, 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido`
--

DROP TABLE IF EXISTS `pedido`;
CREATE TABLE IF NOT EXISTS `pedido` (
  `id_pedido` int NOT NULL AUTO_INCREMENT,
  `archivo_diseño` varchar(255) DEFAULT NULL,
  `estado` int DEFAULT NULL,
  `fech_pedido` datetime(6) DEFAULT NULL,
  `fecha_entrega_estimada` datetime(6) DEFAULT NULL,
  `fecha_entrega_real` datetime(6) DEFAULT NULL,
  `inicial` decimal(10,2) DEFAULT NULL,
  `observaciones` text,
  `total` decimal(10,2) DEFAULT NULL,
  `cod_cliente` varchar(8) NOT NULL,
  `id_persona` int NOT NULL,
  PRIMARY KEY (`id_pedido`),
  KEY `FKrrh8kvk0683cmmesmkvrm9th4` (`cod_cliente`),
  KEY `FK8649g4ju4g98obvhxmjuoxxir` (`id_persona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permiso`
--

DROP TABLE IF EXISTS `permiso`;
CREATE TABLE IF NOT EXISTS `permiso` (
  `id_permiso` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `descripcion_permiso` varchar(255) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `nombre_permiso` varchar(30) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_permiso`),
  UNIQUE KEY `UKke2qkm3yxfjcm175hh2kib5uh` (`nombre_permiso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

DROP TABLE IF EXISTS `persona`;
CREATE TABLE IF NOT EXISTS `persona` (
  `id_persona` int NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(50) DEFAULT NULL,
  `correo` varchar(50) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `dni` varchar(8) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `nombres` varchar(40) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `ruc` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id_persona`),
  UNIQUE KEY `UKlodekqxpnd5v94t4hc6v372xp` (`ruc`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`id_persona`, `apellidos`, `correo`, `created_at`, `direccion`, `dni`, `id_estado`, `nombres`, `telefono`, `updated_at`, `ruc`) VALUES
(1, 'Ramos', 'mramosp@unitru.edu.pe', NULL, 'calle tupac amaru 367', '74278315', 1, 'Kenji', '927663928', NULL, NULL),
(2, 'Ramos', 'maykolkrp.2017@gmail.com', '2025-06-02 19:06:34.824057', 'calle tupac amaru 367', '74278315', 1, 'Maykol', '927663928', '2025-06-02 19:06:34.824057', NULL),
(3, 'Romero', 'mramosp@unitru.edu.pe', '2025-06-03 07:43:11.473056', 'AV.GUILLERMO DANSEY N° 980 H-13 LIMA LIMA LIMA', '22255555', 1, 'Rodolfo', '987654321', '2025-06-03 07:43:11.473056', NULL),
(4, 'Ramos Pérez', 'jc.ramos@gmail.com', NULL, 'Av. Faucett 1350, Callao, Lima', '46789213', 1, 'Juan Carlos', '987654321', NULL, NULL),
(5, 'Toribio', 'admin@example.com', NULL, 'AV.GUILLERMO DANSEY N° 980 H-13 LIMA LIMA LIMA', '48888888', 1, 'rodolfo', '987654321', NULL, NULL),
(6, 'juearex', 'mavenick@gmail.com', '2025-06-09 13:25:18.616298', 'calle mas alla', '74278378', 1, 'Rodolfo', '147885239', '2025-06-09 13:25:18.616298', NULL),
(7, 'Rondan', 'miler@gmail.com', NULL, 'calle mas alla', '44455555', 1, 'Miller', '987654321', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

DROP TABLE IF EXISTS `producto`;
CREATE TABLE IF NOT EXISTS `producto` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `cod_producto` varchar(50) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `descripcion_producto` varchar(255) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `nombre_producto` varchar(50) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `stock` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_categoria_p` int DEFAULT NULL,
  `id_proveedor` int DEFAULT NULL,
  `id_sucursal` int DEFAULT NULL,
  `id_unidad_medida` int DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  KEY `FKceucufwdcowgctpgxm3cd1wrr` (`id_categoria_p`),
  KEY `FKkinjnx6sxv6kf9s6i21ttfnfo` (`id_proveedor`),
  KEY `FK65ll9cs4g6a9bbn5f7nv37n05` (`id_sucursal`),
  KEY `FK1pa05vw0vyf3p32bv41541dbi` (`id_unidad_medida`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`id_producto`, `cod_producto`, `created_at`, `descripcion_producto`, `id_estado`, `nombre_producto`, `precio_unitario`, `stock`, `updated_at`, `id_categoria_p`, `id_proveedor`, `id_sucursal`, `id_unidad_medida`) VALUES
(1, 'PRD-00001', '2025-06-02 19:04:33.934516', 'Largo: 3 metros ', 1, 'Tubo de acero 3/4', 35.00, 35, '2025-06-22 17:20:24.732783', NULL, NULL, 3, 1),
(2, 'PRD-00002', '2025-06-03 01:51:37.544054', 'Largo: 3 metros ', 1, 'Tubo 1/2', 45.00, 48, '2025-06-21 18:19:52.424384', 1, 1, 1, 1),
(3, 'PRD-00003', '2025-06-08 00:08:29.700133', 'Acero corrugado para construcción, ½ pulgada', 1, 'Varilla Corrugada ½\" x 9m', 28.50, 1189, '2025-06-24 08:08:26.570198', 11, 2, 1, 1),
(4, 'PRD-00004', '2025-06-08 00:10:09.775501', 'Planchas laminadas en caliente 4x8 pies', 1, 'Planchas de Acero ASTM A36', 215.00, 347, '2025-06-24 08:10:50.616241', NULL, NULL, 4, 1),
(5, 'PRD-00005', '2025-06-08 00:11:44.779381', 'Tubo redondo galvanizado para estructuras', 1, 'Tubo de Acero 2\" Sch 40', 48.00, 41, '2025-06-24 08:08:26.647876', NULL, NULL, 3, 1),
(6, 'PRD-00006', '2025-06-08 00:12:57.099127', 'Ángulo L laminado para estructuras metálicas', 1, 'Ángulo de Acero 1.5\" x ⅛\"', 31.20, 489, '2025-07-01 08:04:17.356445', 5, 6, 1, 1),
(7, 'PRD-00007', '2025-06-08 00:14:18.270363', 'Barras de acero para concreto armado', 1, 'Fierro Estriado ⅜\"', 22.00, 1382, '2025-06-17 02:29:10.942182', 11, 1, 2, 1),
(8, 'PRD-00008', '2025-06-08 00:27:40.003978', 'Bobina de acero galvanizado para techos', 1, 'Bobina Galvanizada 0.5mm', 7.90, 1771, '2025-06-24 08:08:26.479370', 9, 2, 2, 3),
(9, 'PRD-00009', '2025-06-08 00:31:39.814725', 'Perfil estructural para techos y cerchas metálicas', 1, 'Perfil C 100x50x6m', 59.50, 582, '2025-07-01 08:02:29.395521', 5, 5, 2, 1),
(10, 'PRD-00010', '2025-06-08 00:34:05.383844', 'Rollo de alambre recocido para amarre de varillas', 1, 'Alambre de Amarre Cal. 16', 6.20, 707, '2025-07-01 08:38:00.520397', 8, 2, 1, 3),
(11, 'PRD-00011', '2025-06-08 00:35:06.224773', 'Tubería cuadrada para estructuras metálicas', 1, 'Tubería Cuadrada 2\" x 2mm', 54.00, 774, '2025-07-01 08:04:17.206856', 4, 5, 1, 1),
(12, 'PRD-00012', '2025-06-08 00:36:08.297223', 'Platina para fabricación y estructuras metálicas', 1, 'Platina de Acero 2\"x1/4\"x6m', 65.00, 418, '2025-07-01 07:59:31.276747', 2, 4, 1, 1),
(13, 'PRD-00013', '2025-06-08 10:42:25.144800', 'Malla de acero para refuerzo de concreto 2.4x6 m', 1, 'Malla Electrosoldada 4mm', 78.00, 296, '2025-06-24 08:10:49.684202', 8, 3, 1, 1),
(14, 'PRD-00014', '2025-06-09 13:31:40.936188', 'longitud: 4x8 m', 2, 'Tubo 1/8', 10.00, 10, '2025-06-09 13:32:14.459883', 4, 3, 2, 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
CREATE TABLE IF NOT EXISTS `proveedor` (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_empresa` int NOT NULL,
  PRIMARY KEY (`id_proveedor`),
  KEY `FK4te09ngay2d4ol71mjkg15gsa` (`id_empresa`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`id_proveedor`, `created_at`, `id_estado`, `updated_at`, `id_empresa`) VALUES
(1, '2025-06-02 19:01:54.034952', 1, '2025-06-02 19:01:54.034952', 2),
(2, '2025-06-07 23:46:16.396086', 1, '2025-06-07 23:46:16.396086', 5),
(3, '2025-06-07 23:50:24.007685', 1, '2025-06-07 23:50:24.007685', 6),
(4, '2025-06-07 23:51:36.502624', 1, '2025-06-07 23:51:36.502624', 7),
(5, '2025-06-07 23:53:03.536460', 1, '2025-06-07 23:53:03.536460', 8),
(6, '2025-06-07 23:54:31.486028', 1, '2025-06-07 23:54:31.486028', 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

DROP TABLE IF EXISTS `rol`;
CREATE TABLE IF NOT EXISTS `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `descripcion_rol` varchar(255) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `nombre_rol` varchar(30) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `UKl0qdsam7tunbtmxcmeeyfcifk` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id_rol`, `created_at`, `descripcion_rol`, `id_estado`, `nombre_rol`, `updated_at`) VALUES
(1, '2025-06-02 18:57:13.200463', 'todos los permisos', 1, 'ADMIN', '2025-06-02 18:57:13.200463');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_permiso`
--

DROP TABLE IF EXISTS `rol_permiso`;
CREATE TABLE IF NOT EXISTS `rol_permiso` (
  `created_at` datetime(6) NOT NULL,
  `id_estado` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_permiso` int NOT NULL,
  `id_rol` int NOT NULL,
  PRIMARY KEY (`id_permiso`,`id_rol`),
  KEY `FKsxc3d8lmtj7em6n8j0wl4jwco` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sucursal`
--

DROP TABLE IF EXISTS `sucursal`;
CREATE TABLE IF NOT EXISTS `sucursal` (
  `id_sucursal` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `nombre_sucursal` varchar(40) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_empresa` int NOT NULL,
  PRIMARY KEY (`id_sucursal`),
  KEY `FK84wdiwq1aakc7cq7klk0y7ya` (`id_empresa`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `sucursal`
--

INSERT INTO `sucursal` (`id_sucursal`, `created_at`, `direccion`, `email`, `id_estado`, `nombre_sucursal`, `telefono`, `updated_at`, `id_empresa`) VALUES
(1, '2025-06-02 18:59:44.441221', 'AV.GUILLERMO DANSEY N° 980 H-13 LIMA LIMA LIMA', 'admin@gmail.com', 1, 'Bodega1', '987654321', '2025-06-02 18:59:44.441221', 1),
(2, '2025-06-07 22:25:20.008357', 'AV.GUILLERMO DANSEY N° 980 H-15 LIMA LIMA LIMA', 'linox@gmail.com', 1, 'Bodega2', '986890833', '2025-06-21 23:33:44.695170', 1),
(3, '2025-06-09 13:21:09.271655', 'DIRECCION', 'kevin@gmail.com', 1, 'Bodega3', '920374819', '2025-06-21 23:34:00.692413', 1),
(4, '2025-06-09 13:21:39.430591', 'DIRECCION', 'kevin@gmail.com', 1, 'Bodega4', '92038495R', '2025-06-21 23:34:16.528034', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_movimiento`
--

DROP TABLE IF EXISTS `tipo_movimiento`;
CREATE TABLE IF NOT EXISTS `tipo_movimiento` (
  `id_tipo_movimiento` int NOT NULL AUTO_INCREMENT,
  `cod_tipo_movimiento` varchar(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `nombre_tipo_movimiento` varchar(30) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_tipo_movimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipo_movimiento`
--

INSERT INTO `tipo_movimiento` (`id_tipo_movimiento`, `cod_tipo_movimiento`, `created_at`, `id_estado`, `nombre_tipo_movimiento`, `updated_at`) VALUES
(1, 'ENT', '2025-06-07 16:14:51.852491', 1, 'Entrada', '2025-06-07 16:28:44.246050'),
(2, 'SAL', '2025-06-07 16:52:07.922606', 1, 'Salida', '2025-06-07 16:52:07.922606');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `unidad_medida`
--

DROP TABLE IF EXISTS `unidad_medida`;
CREATE TABLE IF NOT EXISTS `unidad_medida` (
  `id_unidad_medida` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `descripcion` varchar(30) NOT NULL,
  `id_estado` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `simbolo` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id_unidad_medida`),
  UNIQUE KEY `UKrxlj9e7xc6kgbwyxlstvwruul` (`descripcion`),
  UNIQUE KEY `UK3atxepneaj9bb9y9livtrpxao` (`simbolo`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `unidad_medida`
--

INSERT INTO `unidad_medida` (`id_unidad_medida`, `created_at`, `descripcion`, `id_estado`, `updated_at`, `simbolo`) VALUES
(1, '2025-06-02 19:03:36.555173', 'Unidad', 1, '2025-06-07 23:11:34.503710', 'und'),
(2, '2025-06-07 23:14:19.460661', 'Metro', 1, '2025-06-07 23:14:19.460661', 'm'),
(3, '2025-06-07 23:14:44.004490', 'Kilogramo', 1, '2025-06-07 23:14:44.004490', 'kg'),
(4, '2025-06-07 23:14:59.061219', 'Tonelada', 1, '2025-06-07 23:14:59.061219', 't'),
(6, '2025-06-07 23:41:31.473446', 'Metro cuadrado', 1, '2025-06-07 23:41:31.473446', 'm²'),
(7, '2025-06-07 23:41:52.264931', 'Paquete', 1, '2025-06-07 23:41:52.264931', 'paq'),
(8, '2025-06-07 23:42:27.641169', 'Rollo', 1, '2025-06-07 23:42:27.641169', 'roll'),
(9, '2025-06-07 23:42:44.030437', 'Pieza', 1, '2025-06-07 23:42:44.030437', 'pz'),
(10, '2025-06-07 23:43:02.825746', 'Juego', 1, '2025-06-07 23:43:02.825746', 'jgo'),
(11, '2025-06-07 23:43:24.550240', 'Metro lineal', 1, '2025-06-07 23:43:24.550240', 'ml');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `contraseña_enc` varchar(255) NOT NULL,
  `correo` varchar(80) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `id_estado` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `url_foto` text,
  `usuario` varchar(50) NOT NULL,
  `id_persona` int DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `UKi02kr8ui5pqddyd7pkm3v4jbt` (`usuario`),
  UNIQUE KEY `UK2mlfr087gb1ce55f2j87o74t` (`correo`),
  KEY `FKagix3q8yqktlyj3yp1sn0mcd9` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `contraseña_enc`, `correo`, `created_at`, `id_estado`, `updated_at`, `url_foto`, `usuario`, `id_persona`) VALUES
(1, '$2a$10$Z7dtybPTE0KEtFtlcaYt1.wTL3d/Y4u4aMs5dVa2022QYuhY6qgyu', 'admin@example.com', '2025-06-02 18:29:45.466687', 1, '2025-06-02 18:29:45.466687', NULL, 'admin', NULL),
(2, '$2a$10$zOc5b8zZkNMq1nSrh.4Auuw8a/a0iEpTwXZCF1nQzu/XRiBoTPNm.', 'mramosp@unitru.edu.pe', '2025-06-30 11:12:58.617716', 1, '2025-06-30 11:12:58.617716', '/uploads/usuarios/464f4590-594b-40a8-8b15-4bb00b039a02_usuario1.png', 'maykol', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_rol`
--

DROP TABLE IF EXISTS `usuario_rol`;
CREATE TABLE IF NOT EXISTS `usuario_rol` (
  `created_at` datetime(6) NOT NULL,
  `id_estado` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_rol` int NOT NULL,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id_rol`,`id_usuario`),
  KEY `FK3ftpt75ebughsiy5g03b11akt` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuario_rol`
--

INSERT INTO `usuario_rol` (`created_at`, `id_estado`, `updated_at`, `id_rol`, `id_usuario`) VALUES
('2025-06-02 18:57:36.468089', 1, '2025-06-02 18:57:36.468089', 1, 1),
('2025-06-30 11:13:41.031076', 1, '2025-06-30 11:13:41.031076', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta`
--

DROP TABLE IF EXISTS `venta`;
CREATE TABLE IF NOT EXISTS `venta` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `cod_venta` varchar(30) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `fechav` datetime(6) NOT NULL,
  `id_estado` int DEFAULT NULL,
  `total` decimal(10,2) NOT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_cliente` varchar(8) NOT NULL,
  `id_empleado` int NOT NULL,
  PRIMARY KEY (`id_venta`),
  UNIQUE KEY `UK1qvmwlwfric6y7dojnvcn23pt` (`cod_venta`),
  KEY `FK1db02qyoe55s64l1skr6ipe6x` (`cod_cliente`),
  KEY `FK748lxxe2tw5mlt50ume0tt31j` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `venta`
--

INSERT INTO `venta` (`id_venta`, `cod_venta`, `created_at`, `fechav`, `id_estado`, `total`, `update_at`, `updated_at`, `cod_cliente`, `id_empleado`) VALUES
(1, 'RRD01-00001', '2025-06-02 19:07:06.319591', '2025-06-02 19:07:04.069647', 0, 35.00, '2025-06-07 20:14:06.492260', '2025-06-07 20:14:06.492260', 'CLI-0001', 2),
(2, 'RRD01-00002', '2025-06-03 01:45:57.250698', '2025-06-03 01:45:57.150268', 0, 140.00, '2025-06-08 11:30:08.122579', '2025-06-08 11:30:08.122579', 'CLI-0001', 2),
(3, 'RRD01-00003', '2025-06-03 01:49:04.594916', '2025-06-03 01:49:04.590407', 0, 70.00, '2025-06-10 09:29:13.760800', '2025-06-10 09:29:13.760800', 'CLI-0001', 2),
(4, 'RRD01-00004', '2025-06-03 01:52:46.192813', '2025-05-22 01:52:46.178816', 0, 115.00, '2025-06-07 20:23:02.625278', '2025-06-07 20:23:02.625278', 'CLI-0001', 2),
(5, 'RRD01-00005', '2025-06-03 08:12:27.918512', '2025-04-15 08:12:27.904744', 1, 205.00, '2025-06-03 08:12:27.918512', '2025-06-03 08:12:27.918512', 'CLI-0001', 2),
(6, 'RRD01-00006', '2025-06-07 16:05:08.322944', '2025-04-24 16:05:07.429753', 1, 70.00, '2025-06-07 16:05:08.322944', '2025-06-07 16:05:08.322944', 'CLI-0002', 2),
(7, 'RRD01-00007', '2025-06-07 18:17:03.434091', '2025-05-21 18:17:02.767789', 1, 70.00, '2025-06-07 18:17:03.434091', '2025-06-07 18:17:03.434091', 'CLI-0002', 2),
(8, 'RRD01-00008', '2025-06-08 00:37:53.894479', '2025-05-12 00:37:51.869934', 1, 94.00, '2025-06-08 00:37:53.894479', '2025-06-08 00:37:53.894479', 'CLI-0003', 2),
(9, 'RRD01-00009', '2025-06-08 11:28:36.550081', '2025-05-22 11:28:35.106843', 1, 602.00, '2025-06-08 11:28:36.550081', '2025-06-08 11:28:36.550081', 'CLI-0005', 2),
(10, 'RRD01-00010', '2025-06-16 23:05:57.258254', '2025-05-13 10:05:47.269658', 1, 1182.00, '2025-06-16 23:05:57.258254', '2025-06-16 23:05:57.258254', 'CLI-0007', 2),
(11, 'RRD01-00011', '2025-06-16 23:10:41.032555', '2025-04-15 14:10:36.528221', 1, 358.00, '2025-06-16 23:10:41.032555', '2025-06-16 23:10:41.032555', 'CLI-0004', 2),
(12, 'RRD01-00012', '2025-06-17 00:59:55.876337', '2025-04-15 13:59:54.904508', 1, 568.50, '2025-06-17 00:59:55.876337', '2025-06-17 00:59:55.876337', 'CLI-0003', 2),
(13, 'RRD01-00013', '2025-06-17 02:29:11.928794', '2025-05-27 09:29:10.923812', 1, 361.60, '2025-06-17 02:29:11.928794', '2025-06-17 02:29:11.928794', 'CLI-0003', 3),
(14, 'RRD01-00014', '2025-06-21 18:19:53.010040', '2025-04-29 18:19:51.979975', 1, 2340.00, '2025-06-21 18:19:53.010040', '2025-06-21 18:19:53.010040', 'CLI-0006', 2),
(15, 'RRD01-00015', '2025-06-21 23:55:54.033710', '2025-05-20 16:55:53.765572', 1, 2430.00, '2025-06-21 23:55:54.033710', '2025-06-21 23:55:54.033710', 'CLI-0002', 2),
(16, 'RRD01-00016', '2025-06-21 23:57:11.998908', '2025-04-24 15:57:11.838151', 1, 270.00, '2025-06-21 23:57:11.998908', '2025-06-21 23:57:11.998908', 'CLI-0004', 2),
(17, 'RRD01-00017', '2025-06-22 00:10:10.433389', '2025-06-22 12:10:09.811212', 1, 334.80, '2025-06-22 00:10:10.433389', '2025-06-22 00:10:10.433389', 'CLI-0004', 2),
(18, 'RRD01-00018', '2025-06-16 10:34:08.950284', '2025-06-22 10:34:08.067429', 1, 162.00, '2025-06-22 01:34:08.950284', '2025-06-22 01:34:08.950284', 'CLI-0005', 2),
(19, 'RRD01-00019', '2025-06-22 15:47:25.182373', '2025-06-22 15:47:23.383835', 1, 124.00, '2025-06-22 15:47:25.182373', '2025-06-22 15:47:25.182373', 'CLI-0006', 2),
(20, 'RRD01-00020', '2025-06-22 16:27:24.688341', '2025-06-22 16:27:23.851477', 1, 260.00, '2025-06-22 16:27:24.688341', '2025-06-22 16:27:24.688341', 'CLI-0002', 2),
(21, 'RRD01-00021', '2025-06-22 16:28:44.221311', '2025-06-21 16:28:43.952362', 1, 162.00, '2025-06-22 16:28:44.221311', '2025-06-22 16:28:44.221311', 'CLI-0001', 2),
(22, 'RRD01-00022', '2025-06-22 17:20:25.168105', '2025-06-22 17:20:24.684341', 1, 525.00, '2025-06-22 17:20:25.168105', '2025-06-22 17:20:25.168105', 'CLI-0002', 2),
(23, 'RRD01-00023', '2025-06-23 16:01:37.603148', '2025-06-23 16:01:35.302883', 1, 119.00, '2025-06-23 16:01:37.603148', '2025-06-23 16:01:37.603148', 'CLI-0001', 2),
(24, 'RRD01-00024', '2025-06-23 16:01:57.145796', '2025-06-23 16:01:56.730356', 1, 108.00, '2025-06-23 16:01:57.145796', '2025-06-23 16:01:57.145796', 'CLI-0002', 2),
(25, 'RRD01-00025', '2025-06-23 16:02:18.360910', '2025-06-23 16:02:17.843318', 1, 119.00, '2025-06-23 16:02:18.360910', '2025-06-23 16:02:18.360910', 'CLI-0003', 2),
(26, 'RRD01-00026', '2025-06-24 00:03:39.347931', '2025-06-24 11:03:36.867462', 1, 108.00, '2025-06-24 00:03:39.347931', '2025-06-24 00:03:39.347931', 'CLI-0004', 2),
(27, 'RRD01-00027', '2025-06-24 08:08:26.697668', '2025-06-24 08:08:25.380101', 1, 510.50, '2025-06-24 08:08:26.697668', '2025-06-24 08:08:26.697668', 'CLI-0004', 2),
(28, 'RRD01-00028', '2025-06-24 08:09:29.482655', '2025-06-24 08:09:27.843734', 1, 384.00, '2025-06-24 08:09:29.482655', '2025-06-24 08:09:29.482655', 'CLI-0004', 2),
(29, 'RRD01-00029', '2025-06-24 08:10:55.202927', '2025-06-24 08:10:49.684202', 1, 664.00, '2025-06-24 08:10:55.202927', '2025-06-24 08:10:55.202927', 'CLI-0005', 2),
(30, 'RRD01-00030', '2025-07-01 07:59:03.157590', '2025-07-01 07:59:02.592121', 1, 130.00, '2025-07-01 07:59:03.157590', '2025-07-01 07:59:03.157590', 'CLI-0001', 2),
(31, 'RRD01-00031', '2025-07-01 07:59:31.579634', '2025-07-01 07:59:30.677761', 1, 89.80, '2025-07-01 07:59:31.579634', '2025-07-01 07:59:31.579634', 'CLI-0001', 2),
(32, 'RRD01-00032', '2025-07-01 08:02:29.445192', '2025-07-01 08:02:29.258679', 1, 904.00, '2025-07-01 08:02:29.445192', '2025-07-01 08:02:29.445192', 'CLI-0003', 2),
(33, 'RRD01-00033', '2025-07-01 08:04:17.565714', '2025-07-01 08:04:17.201655', 1, 457.20, '2025-07-01 08:04:17.565714', '2025-07-01 08:04:17.565714', 'CLI-0004', 2),
(34, 'RRD01-00034', '2025-07-01 08:38:00.828553', '2025-07-01 08:38:00.509023', 1, 620.00, '2025-07-01 08:38:00.828553', '2025-07-01 08:38:00.828553', 'CLI-0004', 2);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `FKdajda7wildpob82xutyb4qh9y` FOREIGN KEY (`id_categoria_c`) REFERENCES `categoria_cliente` (`id_categoria`);

--
-- Filtros para la tabla `cliente_juridico`
--
ALTER TABLE `cliente_juridico`
  ADD CONSTRAINT `FK11xpummvsr5fee2nek8pu5a3d` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`),
  ADD CONSTRAINT `FKbvedp4wbjywraknlmm2r0qwb4` FOREIGN KEY (`cod_cliente`) REFERENCES `cliente` (`cod_cliente`);

--
-- Filtros para la tabla `cliente_natural`
--
ALTER TABLE `cliente_natural`
  ADD CONSTRAINT `FKf4qpaffgsxlarptel82sw9p0f` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`),
  ADD CONSTRAINT `FKm9pc25i6ickm756phwlupef5v` FOREIGN KEY (`cod_cliente`) REFERENCES `cliente` (`cod_cliente`);

--
-- Filtros para la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD CONSTRAINT `FKoknpg31rxsqnjxrsu7iy47p1o` FOREIGN KEY (`id_venta`) REFERENCES `venta` (`id_venta`),
  ADD CONSTRAINT `FKsntaik0t9jxcky777753wytsx` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`);

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `FK3yo5m2sf91t2spkatlwxagm5x` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`),
  ADD CONSTRAINT `FK5lshn105gw8o1gcw78wysko3u` FOREIGN KEY (`id_sucursal`) REFERENCES `sucursal` (`id_sucursal`),
  ADD CONSTRAINT `FK739vkywoel8qoad30ovv9ksgl` FOREIGN KEY (`id_cargo`) REFERENCES `cargo` (`id_cargo`);

--
-- Filtros para la tabla `empresa_anfitrion`
--
ALTER TABLE `empresa_anfitrion`
  ADD CONSTRAINT `FKdl2elr18kn6ola63yw2ynj9sv` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`);

--
-- Filtros para la tabla `kardex`
--
ALTER TABLE `kardex`
  ADD CONSTRAINT `FK1jw3p60w9hx6rx54rwv7tg5yk` FOREIGN KEY (`id_sucursal`) REFERENCES `sucursal` (`id_sucursal`),
  ADD CONSTRAINT `FK1lobecy12t0wdyo9mor6w8aak` FOREIGN KEY (`id_productoe`) REFERENCES `producto` (`id_producto`),
  ADD CONSTRAINT `FKbnwd35rcw7tvy3is2em332dsn` FOREIGN KEY (`id_tipo_movimiento`) REFERENCES `tipo_movimiento` (`id_tipo_movimiento`),
  ADD CONSTRAINT `FKcsdmhmlq8k6vaodyqfv3ybbs5` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`);

--
-- Filtros para la tabla `pedido`
--
ALTER TABLE `pedido`
  ADD CONSTRAINT `FK8649g4ju4g98obvhxmjuoxxir` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`),
  ADD CONSTRAINT `FKrrh8kvk0683cmmesmkvrm9th4` FOREIGN KEY (`cod_cliente`) REFERENCES `cliente` (`cod_cliente`);

--
-- Filtros para la tabla `producto`
--
ALTER TABLE `producto`
  ADD CONSTRAINT `FK1pa05vw0vyf3p32bv41541dbi` FOREIGN KEY (`id_unidad_medida`) REFERENCES `unidad_medida` (`id_unidad_medida`),
  ADD CONSTRAINT `FK65ll9cs4g6a9bbn5f7nv37n05` FOREIGN KEY (`id_sucursal`) REFERENCES `sucursal` (`id_sucursal`),
  ADD CONSTRAINT `FKceucufwdcowgctpgxm3cd1wrr` FOREIGN KEY (`id_categoria_p`) REFERENCES `categoria_producto` (`id_categoria_p`),
  ADD CONSTRAINT `FKkinjnx6sxv6kf9s6i21ttfnfo` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedor` (`id_proveedor`);

--
-- Filtros para la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD CONSTRAINT `FK4te09ngay2d4ol71mjkg15gsa` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`);

--
-- Filtros para la tabla `rol_permiso`
--
ALTER TABLE `rol_permiso`
  ADD CONSTRAINT `FKrhxhgw05bdvokfrpppumlfh5d` FOREIGN KEY (`id_permiso`) REFERENCES `permiso` (`id_permiso`),
  ADD CONSTRAINT `FKsxc3d8lmtj7em6n8j0wl4jwco` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`);

--
-- Filtros para la tabla `sucursal`
--
ALTER TABLE `sucursal`
  ADD CONSTRAINT `FK84wdiwq1aakc7cq7klk0y7ya` FOREIGN KEY (`id_empresa`) REFERENCES `empresa_anfitrion` (`id_empresa`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `FKagix3q8yqktlyj3yp1sn0mcd9` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`);

--
-- Filtros para la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD CONSTRAINT `FK3ftpt75ebughsiy5g03b11akt` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `FKkxcv7htfnm9x1wkofnud0ewql` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`);

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `FK1db02qyoe55s64l1skr6ipe6x` FOREIGN KEY (`cod_cliente`) REFERENCES `cliente` (`cod_cliente`),
  ADD CONSTRAINT `FK748lxxe2tw5mlt50ume0tt31j` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_persona`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
