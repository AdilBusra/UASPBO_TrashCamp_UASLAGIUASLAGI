# Package Model Frontend (`model`)

Package ini digunakan untuk menyimpan kelas-kelas Java biasa (POJO/DTO) yang berfungsi sebagai representasi atau struktur data di sisi client (JavaFX).

## Perbedaan Penting dengan Model Backend:
- **TIDAK MENGGUNAKAN** anotasi JPA seperti `@Entity`, `@Table`, `@Id`, dll.
- Model di sini murni digunakan untuk menangkap dan menampung data hasil *parsing* JSON dari REST API Backend (Spring Boot), atau sebaliknya, membungkus data dari input form JavaFX sebelum dikirim ke Backend.

## Rencana Kelas Model yang Diperlukan:

1. `Pendakian.java`
    - Menampung data kelompok pendaki (id, nama ketua, nomor HP, jumlah anggota, waktu naik/turun, status).
    - Properti di kelas ini bisa menggunakan tipe data JavaFX Property (seperti `SimpleStringProperty`, `SimpleIntegerProperty`) jika ingin memanfaatkan fitur *Data Binding* agar data di `TableView` otomatis ter-update saat ada perubahan.

2. `MasterSampah.java`
    - Menampung data jenis sampah beserta tarif dendanya untuk disajikan pada pilihan dropdown/combobox di form check-in.

3. `DetailSampah.java`
    - Menampung rincian item sampah bawaan (nama item, jumlah saat naik, jumlah saat turun, denda).

## Penerapan Pilar PBO (Encapsulation):
Setiap kelas model di package ini wajib menerapkan prinsip **Encapsulation** dengan ketat:
- Semua atribut dideklarasikan sebagai `private`.
- Akses data dikendalikan penuh menggunakan metode `public getter` dan `public setter`.