-- ======================================
-- 📦 CREAR BASE DE DATOS
-- ======================================




-- ======================================
-- 🧱 CREAR TABLA DE REPORTES
-- ======================================

-- ======================================
-- 📦 CREAR TABLA DE REPORTES
-- ======================================

CREATE TABLE IF NOT EXISTS public.reporte (
    id SERIAL PRIMARY KEY,
    archivo BYTEA, -- ✅ tipo correcto para byte[]
    descripcion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT NOW(),
    latitud DOUBLE PRECISION,
    longitud DOUBLE PRECISION
);



CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    correo VARCHAR(100) UNIQUE,
    contraseña VARCHAR(255)
);

