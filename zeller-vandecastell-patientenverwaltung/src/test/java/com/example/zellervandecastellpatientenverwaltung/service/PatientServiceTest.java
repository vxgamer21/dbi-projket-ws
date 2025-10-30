package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    private @Mock ArztRepository arztRepository;
    private @Mock PatientRepository patientRepository;

    private PatientService patientService;
    private Adresse adresse;
    private TelefonNummer telefonNummer;

    @BeforeEach
    void setUp(){
        assumeThat(arztRepository).isNotNull();
        assumeThat(patientRepository).isNotNull();
        adresse = new Adresse("Musterstraße", "12345", "Musterstadt","2400");
        telefonNummer = new TelefonNummer("0123456789", "0123", "4567890", TelefonNummerArt.MOBIL);


        patientService = new PatientService(patientRepository);
    }

    @Test
    void cant_create_patient_with_missing_name() {
        assertThatThrownBy(() -> patientService.createPatient(null, LocalDate.of(2000,7,12), 1234567890L, Versicherungsart.KRANKENKASSE,adresse,telefonNummer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name darf nicht null oder leer sein.");
    }

    @Test
    void cant_create_patient_with_missing_svnr() {
        assertThatThrownBy(() -> patientService.createPatient("Max Mustermann", LocalDate.of(2000,4,12), null, Versicherungsart.KRANKENKASSE, adresse, telefonNummer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SVNR darf nicht null sein.");
    }

    @Test
    void can_create_patient_with_valid_data() {
        Patient validPatient = FixturesFactory.PatientMaxMustermann();
        validPatient.setVersicherungsart(Versicherungsart.PRIVAT);
        validPatient.setPatientID(new Patient.PatientID(1L));

        when(patientRepository.save(any(Patient.class))).thenReturn(validPatient);

        Patient createdPatient = patientService.createPatient("Max Mustermann", null, 1234567890L, Versicherungsart.PRIVAT, adresse, telefonNummer);

        assertNotNull(createdPatient);
        assertEquals("Max Mustermann", createdPatient.getName());
        assertEquals(Versicherungsart.PRIVAT, createdPatient.getVersicherungsart());
    }

    @Test
    void can_get_patient_by_id() {
        Long patientId = 1L;
        Patient patient = FixturesFactory.PatientMaxMustermann();
        patient.setPatientID(new Patient.PatientID(patientId));

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));

        Optional<Patient> foundPatient = patientService.getPatient(patientId);

        assertTrue(foundPatient.isPresent());
        assertEquals("Max Mustermann", foundPatient.get().getName());
    }

    @Test
    void cant_get_patient_by_invalid_id() {
        Long invalidPatientId = 999L;
        when(patientRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Patient> foundPatient = patientService.getPatient(invalidPatientId);

        assertFalse(foundPatient.isPresent());
    }

    @Test
    void can_get_all_patients() {
        Patient patient1 = FixturesFactory.PatientMaxMustermann();
        patient1.setPatientID(new Patient.PatientID(1L));

        Patient patient2 = FixturesFactory.PatientMaxMustermann();
        patient2.setName("Erika Musterfrau");
        patient2.setPatientID(new Patient.PatientID(2L));

        when(patientRepository.findAll()).thenReturn(List.of(patient1, patient2));

        var patients = patientService.getAll();

        assertNotNull(patients);
        assertEquals(2, patients.size());
        assertEquals("Max Mustermann", patients.get(0).getName());
        assertEquals("Erika Musterfrau", patients.get(1).getName());
    }
}