package com.trashcamp.trashcampbackend.seeder;

import com.trashcamp.trashcampbackend.entity.MasterSampah;
import com.trashcamp.trashcampbackend.entity.Petugas;
import com.trashcamp.trashcampbackend.entity.Trail;
import com.trashcamp.trashcampbackend.entity.SystemSetting;
import com.trashcamp.trashcampbackend.entity.Pendakian;
import com.trashcamp.trashcampbackend.entity.DetailSampah;
import com.trashcamp.trashcampbackend.repository.MasterSampahRepository;
import com.trashcamp.trashcampbackend.repository.PetugasRepository;
import com.trashcamp.trashcampbackend.repository.TrailRepository;
import com.trashcamp.trashcampbackend.repository.PendakianRepository;
import com.trashcamp.trashcampbackend.repository.DetailSampahRepository;
import com.trashcamp.trashcampbackend.repository.SystemSettingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MasterSampahRepository masterSampahRepository;
    private final PetugasRepository petugasRepository;
    private final TrailRepository trailRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final PendakianRepository pendakianRepository;
    private final DetailSampahRepository detailSampahRepository;

    // Dependency Injection via Constructor
    public DataSeeder(MasterSampahRepository masterSampahRepository,
                      PetugasRepository petugasRepository,
                      TrailRepository trailRepository,
                      SystemSettingRepository systemSettingRepository,
                      PendakianRepository pendakianRepository,
                      DetailSampahRepository detailSampahRepository) {
        this.masterSampahRepository = masterSampahRepository;
        this.petugasRepository = petugasRepository;
        this.trailRepository = trailRepository;
        this.systemSettingRepository = systemSettingRepository;
        this.pendakianRepository = pendakianRepository;
        this.detailSampahRepository = detailSampahRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedMasterSampah();
        seedPetugas();
        seedTrails();
        seedSettings();
        seedPendakian();
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

    private void seedPendakian() {
        if (pendakianRepository.count() == 0) {
            List<MasterSampah> masterList = masterSampahRepository.findAll();
            if (masterList.isEmpty()) return;

            // Kelompok 1: Frito Radestya
            Pendakian p1 = new Pendakian(null, "Frito Radestya", "08123456789", 4,
                    LocalDateTime.now().minusDays(2), null, "AKTIF", 60000.0, 0.0, "Ranu Kumbolo");
            p1 = pendakianRepository.save(p1);
            detailSampahRepository.save(createDetail(p1, masterList.get(0), 5)); 
            detailSampahRepository.save(createDetail(p1, masterList.get(1), 3)); 
            detailSampahRepository.save(createDetail(p1, masterList.get(3), 8)); 

            // Kelompok 2: Adil Busra
            Pendakian p2 = new Pendakian(null, "Adil Busra", "08234567890", 6,
                    LocalDateTime.now().minusDays(3), null, "AKTIF", 100000.0, 0.0, "Mahameru Summit");
            p2 = pendakianRepository.save(p2);
            detailSampahRepository.save(createDetail(p2, masterList.get(0), 10)); 
            detailSampahRepository.save(createDetail(p2, masterList.get(2), 6));  
            detailSampahRepository.save(createDetail(p2, masterList.get(4), 2));  

            // Kelompok 3: Budi Santoso
            Pendakian p3 = new Pendakian(null, "Budi Santoso", "08345678901", 3,
                    LocalDateTime.now().minusDays(1), null, "AKTIF", 50000.0, 0.0, "Oro-oro Ombo");
            p3 = pendakianRepository.save(p3);
            detailSampahRepository.save(createDetail(p3, masterList.get(0), 4));
            detailSampahRepository.save(createDetail(p3, masterList.get(1), 4));

            // Kelompok 4: Siti Aminah
            Pendakian p4 = new Pendakian(null, "Siti Aminah", "08456789012", 5,
                    LocalDateTime.now().minusHours(18), null, "AKTIF", 80000.0, 0.0, "Kalimati");
            p4 = pendakianRepository.save(p4);
            detailSampahRepository.save(createDetail(p4, masterList.get(0), 8));
            detailSampahRepository.save(createDetail(p4, masterList.get(3), 10));
            detailSampahRepository.save(createDetail(p4, masterList.get(5), 2));

            // Kelompok 5: Eko Prasetyo
            Pendakian p5 = new Pendakian(null, "Eko Prasetyo", "08567890123", 8,
                    LocalDateTime.now().minusDays(4), null, "AKTIF", 120000.0, 0.0, "Ranupani Base Camp");
            p5 = pendakianRepository.save(p5);
            detailSampahRepository.save(createDetail(p5, masterList.get(0), 12));
            detailSampahRepository.save(createDetail(p5, masterList.get(2), 8));
            detailSampahRepository.save(createDetail(p5, masterList.get(7), 4));

            // Kelompok 6: Rina Wijaya
            Pendakian p6 = new Pendakian(null, "Rina Wijaya", "08678901234", 2,
                    LocalDateTime.now().minusHours(6), null, "AKTIF", 30000.0, 0.0, "Arcopodo");
            p6 = pendakianRepository.save(p6);
            detailSampahRepository.save(createDetail(p6, masterList.get(0), 3));
            detailSampahRepository.save(createDetail(p6, masterList.get(1), 2));

            // Kelompok 7: Guntur Prabowo
            Pendakian p7 = new Pendakian(null, "Guntur Prabowo", "08789012345", 10,
                    LocalDateTime.now().minusDays(5), null, "AKTIF", 150000.0, 0.0, "Mahameru Summit");
            p7 = pendakianRepository.save(p7);
            detailSampahRepository.save(createDetail(p7, masterList.get(0), 20));
            detailSampahRepository.save(createDetail(p7, masterList.get(2), 15));
            detailSampahRepository.save(createDetail(p7, masterList.get(4), 5));

            System.out.println("====== DATA SEEDING: Kelompok Pendakian Aktif Berhasil Disuntikkan! ======");
        }
    }

    private DetailSampah createDetail(Pendakian p, MasterSampah ms, int qty) {
        DetailSampah d = new DetailSampah();
        d.setPendakian(p);
        d.setMasterSampah(ms);
        d.setJumlahBawa(qty);
        d.setJumlahKembali(0);
        d.setSubtotalDenda(0.0);
        return d;
    }
}