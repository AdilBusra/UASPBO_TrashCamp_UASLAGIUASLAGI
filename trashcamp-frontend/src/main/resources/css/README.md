# Folder CSS Styling (`css`)

Folder ini digunakan untuk menyimpan seluruh file `.css` yang berfungsi mengatur tampilan visual, warna, font, dan komponen antarmuka (UI) aplikasi **TrashCamp** agar terlihat modern, bersih, dan konsisten.

## Rencana File CSS yang Diperlukan:

1. `style.css` (Global Style)
    - Mengatur tema warna utama aplikasi (karena bertema gunung/lingkungan, disarankan menggunakan dominasi warna hijau alam, putih bersih, dan abu-abu gelap).
    - Mengatur font standar aplikasi.
    - Mengatur *styling* untuk komponen global seperti `Button`, `TextField`, dan `Label`.

2. `sidebar.css` / `dashboard.css` (Custom Component Style)
    - Mengatur tampilan menu navigasi samping (*sidebar*) agar interaktif saat diarahkan kursor (*hover*).
    - Mengatur tampilan tabel (`TableView`) agar baris datanya mudah dibaca oleh petugas pos.

## Panduan Dasar Menghubungkan CSS ke FXML:
Untuk menerapkan file CSS ke dalam layout FXML, tim Frontend bisa menambahkannya langsung di dalam file `.fxml` melalui Scene Builder atau kode teks seperti ini:
```xml
<AnchorPane stylesheets="@../css/style.css" ...>