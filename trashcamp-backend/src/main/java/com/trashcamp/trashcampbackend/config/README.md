# Package Configuration (`config`)

Package ini digunakan untuk menyimpan seluruh kelas konfigurasi global aplikasi Spring Boot, terutama yang berkaitan dengan keamanan (*Security*), perizinan akses (*CORS*), dan manajemen database H2.

## Yang perlu dibuat di sini:

1. `SecurityConfig.java`
    - Mengonfigurasi **Spring Security** untuk mengamankan endpoint REST API (Kriteria Wajib UAS).
    - Mengatur autentikasi login menggunakan *Basic Authentication* atau *Token-based Authentication* untuk Petugas Pos.
    - Mengatur hak akses (Otorisasi), misalnya memastikan endpoint manajemen harga denda hanya bisa diakses setelah petugas berhasil login.
    - Mengizinkan akses ke **H2 Console** (`/h2-console/**`) agar kita bisa mengecek isi database lewat browser saat masa pengembangan.

2. `CorsConfig.java` (atau digabung ke SecurityConfig)
    - Mengonfigurasi *Cross-Origin Resource Sharing* (CORS) agar aplikasi frontend **JavaFX** yang berjalan secara lokal dapat menembak dan menerima data dari server backend **Spring Boot** tanpa diblokir oleh sistem keamanan browser/jaringan.

## Catatan Penting untuk Tim Backend:
- Pastikan untuk menonaktifkan fitur `csrf` khusus untuk endpoint `/h2-console/**` dan menggunakan `headers().frameOptions().disable()` agar halaman web database H2 bisa terbuka dengan lancar di browser saat di-test.