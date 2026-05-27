package com.trashcamp.trashcampbackend.seeder;

import com.trashcamp.trashcampbackend.entity.MasterSampah;
import com.trashcamp.trashcampbackend.entity.Petugas;
import com.trashcamp.trashcampbackend.repository.MasterSampahRepository;
import com.trashcamp.trashcampbackend.repository.PetugasRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MasterSampahRepository masterSampahRepository;
    private final PetugasRepository petugasRepository;

    // Dependency Injection via Constructor
    public DataSeeder(MasterSampahRepository masterSampahRepository, PetugasRepository petugasRepository) {
        this.masterSampahRepository = masterSampahRepository;
        this.petugasRepository = petugasRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedMasterSampah();
        seedPetugas();
    }

    private void seedMasterSampah() {
        // Hanya isi data jika tabel master_sampah masih kosong
        if (masterSampahRepository.count() == 0) {
            masterSampahRepository.save(new MasterSampah("Botol Plastik", 10000.0));
            masterSampahRepository.save(new MasterSampah("Kaleng Logam / Penyegar", 15000.0));
            masterSampahRepository.save(new MasterSampah("Gas Kaleng / Gembos", 25000.0));
            masterSampahRepository.save(new MasterSampah("Kantong Plastik / Trash Bag", 5000.0));

            System.out.println("====== DATA SEEDING: Master Sampah Berhasil Disuntikkan! ======");
        }
    }

    private void seedPetugas() {
        // Hanya isi data jika belum ada petugas sama sekali
        if (petugasRepository.count() == 0) {
            // Catatan: Sementara password masih plain text.
            // Nanti saat fitur Bcrypt diaktifkan, kita akan ubah jadi password terenkripsi.
            petugasRepository.save(new Petugas("adminpos1", "rahasia123", "Adil M.B (Petugas Pos 1)"));
            petugasRepository.save(new Petugas("petugas2", "password321", "Budi Santoso (Petugas Pos 2)"));

            System.out.println("====== DATA SEEDING: Akun Petugas Berhasil Disuntikkan! ======");
        }
    }
}