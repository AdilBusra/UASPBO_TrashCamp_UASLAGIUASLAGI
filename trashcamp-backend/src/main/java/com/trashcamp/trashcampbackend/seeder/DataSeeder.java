package com.trashcamp.trashcampbackend.seeder;

import com.trashcamp.trashcampbackend.entity.MasterSampah;
import com.trashcamp.trashcampbackend.entity.Petugas;
import com.trashcamp.trashcampbackend.entity.Trail;
import com.trashcamp.trashcampbackend.entity.SystemSetting;
import com.trashcamp.trashcampbackend.repository.MasterSampahRepository;
import com.trashcamp.trashcampbackend.repository.PetugasRepository;
import com.trashcamp.trashcampbackend.repository.TrailRepository;
import com.trashcamp.trashcampbackend.repository.SystemSettingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MasterSampahRepository masterSampahRepository;
    private final PetugasRepository petugasRepository;
    private final TrailRepository trailRepository;
    private final SystemSettingRepository systemSettingRepository;

    // Dependency Injection via Constructor
    public DataSeeder(MasterSampahRepository masterSampahRepository,
                      PetugasRepository petugasRepository,
                      TrailRepository trailRepository,
                      SystemSettingRepository systemSettingRepository) {
        this.masterSampahRepository = masterSampahRepository;
        this.petugasRepository = petugasRepository;
        this.trailRepository = trailRepository;
        this.systemSettingRepository = systemSettingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedMasterSampah();
        seedPetugas();
        seedTrails();
        seedSettings();
    }

    private void seedMasterSampah() {
        if (masterSampahRepository.count() == 0) {
            masterSampahRepository.save(new MasterSampah("Botol Plastik", "Plastik", 5000.0, 15000.0, true));
            masterSampahRepository.save(new MasterSampah("Kantong Plastik", "Plastik", 3000.0, 10000.0, true));
            masterSampahRepository.save(new MasterSampah("Kaleng Minuman", "Metal", 8000.0, 20000.0, true));
            masterSampahRepository.save(new MasterSampah("Bungkus Makanan", "Plastik", 3000.0, 10000.0, true));
            masterSampahRepository.save(new MasterSampah("Baterai", "B3", 15000.0, 50000.0, true));
            masterSampahRepository.save(new MasterSampah("Botol Kaca", "Kaca", 10000.0, 25000.0, true));
            masterSampahRepository.save(new MasterSampah("Tisu / Kertas", "Organik", 2000.0, 5000.0, true));
            masterSampahRepository.save(new MasterSampah("Sisa Makanan", "Organik", 2000.0, 8000.0, true));

            System.out.println("====== DATA SEEDING: Master Sampah Berhasil Disuntikkan! ======");
        }
    }

    private void seedPetugas() {
        if (petugasRepository.count() == 0) {
            petugasRepository.save(new Petugas("adminpos1", "rahasia123", "Adil M.B (Petugas Pos 1)", "NIP-2026-001", "Pos Ranu Kumbolo"));
            petugasRepository.save(new Petugas("petugas2", "password321", "Budi Santoso (Petugas Pos 2)", "NIP-2026-002", "Pos Oro-oro Ombo"));

            System.out.println("====== DATA SEEDING: Akun Petugas Berhasil Disuntikkan! ======");
        }
    }

    private void seedTrails() {
        if (trailRepository.count() == 0) {
            trailRepository.save(new Trail("Ranu Kumbolo"));
            trailRepository.save(new Trail("Mahameru Summit"));
            trailRepository.save(new Trail("Oro-oro Ombo"));
            trailRepository.save(new Trail("Kalimati"));
            trailRepository.save(new Trail("Arcopodo"));
            trailRepository.save(new Trail("Ranupani Base Camp"));

            System.out.println("====== DATA SEEDING: Rute Trails Berhasil Disuntikkan! ======");
        }
    }

    private void seedSettings() {
        if (systemSettingRepository.count() == 0) {
            systemSettingRepository.save(new SystemSetting("ticketPrice", "15000"));
            systemSettingRepository.save(new SystemSetting("sanitationFee", "20000"));
            systemSettingRepository.save(new SystemSetting("namaStasiun", "Pos Pendakian Ranu Kumbolo"));

            System.out.println("====== DATA SEEDING: System Settings Berhasil Disuntikkan! ======");
        }
    }
}