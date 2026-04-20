package com.petshop.service;

import com.petshop.dto.ServiceDTO;
import com.petshop.entity.GroomingService;
import com.petshop.entity.Pet;
import com.petshop.entity.Vaccination;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.GroomingServiceRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.VaccinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceManagerTest {

    @Mock GroomingServiceRepository groomingRepo;
    @Mock VaccinationRepository vaccinationRepo;
    @Mock PetRepository petRepo;
    @InjectMocks ServiceManager serviceManager;

    private GroomingService sampleGrooming;
    private Vaccination sampleVaccination;
    private Pet samplePet;

    @BeforeEach
    void setUp() {
        sampleGrooming = new GroomingService();
        sampleGrooming.setId(1);
        sampleGrooming.setName("Full Groom");
        sampleGrooming.setDescription("Complete grooming package");
        sampleGrooming.setPrice(500.0);
        sampleGrooming.setAvailable(true);

        sampleVaccination = new Vaccination();
        sampleVaccination.setId(1);
        sampleVaccination.setName("Rabies Vaccine");
        sampleVaccination.setDescription("Annual rabies vaccination");
        sampleVaccination.setPrice(300.0);
        sampleVaccination.setAvailable(true);

        samplePet = new Pet();
        samplePet.setPetId(1);
        samplePet.setName("Buddy");
        samplePet.setGroomingServices(new ArrayList<>());
        samplePet.setVaccinations(new ArrayList<>());
    }

    @Test
    @DisplayName("createGrooming saves and returns ServiceDTO")
    void createGrooming_savesAndReturnsDTO() {
        when(groomingRepo.save(any(GroomingService.class))).thenReturn(sampleGrooming);

        Map<String, Object> payload = Map.of(
                "name", "Full Groom",
                "description", "Complete grooming package",
                "price", 500.0,
                "available", true
        );

        ServiceDTO result = serviceManager.createGrooming(payload);

        assertThat(result.getName()).isEqualTo("Full Groom");
        assertThat(result.getDescription()).isEqualTo("Complete grooming package");
        assertThat(result.getPrice()).isEqualTo(500.0);
        assertThat(result.isAvailable()).isTrue();
        verify(groomingRepo).save(any(GroomingService.class));
    }

    @Test
    @DisplayName("createVaccination saves and returns ServiceDTO")
    void createVaccination_savesAndReturnsDTO() {
        when(vaccinationRepo.save(any(Vaccination.class))).thenReturn(sampleVaccination);

        Map<String, Object> payload = Map.of(
                "name", "Rabies Vaccine",
                "description", "Annual rabies vaccination",
                "price", 300.0,
                "available", true
        );

        ServiceDTO result = serviceManager.createVaccination(payload);

        assertThat(result.getName()).isEqualTo("Rabies Vaccine");
        assertThat(result.getDescription()).isEqualTo("Annual rabies vaccination");
        assertThat(result.getPrice()).isEqualTo(300.0);
        verify(vaccinationRepo).save(any(Vaccination.class));
    }

    @Test
    @DisplayName("assignGrooming adds grooming to pet and saves")
    void assignGrooming_success() {
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));
        when(groomingRepo.findById(1)).thenReturn(Optional.of(sampleGrooming));
        when(petRepo.save(any(Pet.class))).thenReturn(samplePet);

        Map<String, Object> result = serviceManager.assignGrooming(1, 1);

        assertThat(result.get("message")).isEqualTo("Grooming service assigned");
        assertThat(samplePet.getGroomingServices()).contains(sampleGrooming);
        verify(petRepo).save(samplePet);
    }

    @Test
    @DisplayName("assignGrooming throws exception when pet not found")
    void assignGrooming_petNotFound_throwsException() {
        when(petRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.assignGrooming(99, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("assignGrooming throws exception when service not found")
    void assignGrooming_serviceNotFound_throwsException() {
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));
        when(groomingRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.assignGrooming(1, 99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Grooming service not found");
    }

    @Test
    @DisplayName("assignVaccination adds vaccination to pet and saves")
    void assignVaccination_success() {
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));
        when(vaccinationRepo.findById(1)).thenReturn(Optional.of(sampleVaccination));
        when(petRepo.save(any(Pet.class))).thenReturn(samplePet);

        Map<String, Object> result = serviceManager.assignVaccination(1, 1);

        assertThat(result.get("message")).isEqualTo("Vaccination assigned");
        assertThat(samplePet.getVaccinations()).contains(sampleVaccination);
        verify(petRepo).save(samplePet);
    }

    @Test
    @DisplayName("assignVaccination throws exception when pet not found")
    void assignVaccination_petNotFound_throwsException() {
        when(petRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.assignVaccination(99, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("assignVaccination throws exception when vaccination not found")
    void assignVaccination_vaccinationNotFound_throwsException() {
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));
        when(vaccinationRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.assignVaccination(1, 99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Vaccination not found");
    }

    @Test
    @DisplayName("getAllGrooming returns list of ServiceDTOs")
    void getAllGrooming_returnsDTOList() {
        when(groomingRepo.findAll()).thenReturn(List.of(sampleGrooming));

        List<ServiceDTO> result = serviceManager.getAllGrooming();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("Full Groom");
        assertThat(result.get(0).getPrice()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("getAllVaccinations returns list of ServiceDTOs")
    void getAllVaccinations_returnsDTOList() {
        when(vaccinationRepo.findAll()).thenReturn(List.of(sampleVaccination));

        List<ServiceDTO> result = serviceManager.getAllVaccinations();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("Rabies Vaccine");
        assertThat(result.get(0).getPrice()).isEqualTo(300.0);
    }

    @Test
    @DisplayName("getPetGroomingServices returns DTOs for pet's grooming services")
    void getPetGroomingServices_returnsDTOList() {
        samplePet.getGroomingServices().add(sampleGrooming);
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));

        List<ServiceDTO> result = serviceManager.getPetGroomingServices(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Full Groom");
    }

    @Test
    @DisplayName("getPetGroomingServices throws exception when pet not found")
    void getPetGroomingServices_petNotFound_throwsException() {
        when(petRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.getPetGroomingServices(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("getPetVaccinations returns DTOs for pet's vaccinations")
    void getPetVaccinations_returnsDTOList() {
        samplePet.getVaccinations().add(sampleVaccination);
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));

        List<ServiceDTO> result = serviceManager.getPetVaccinations(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Rabies Vaccine");
    }

    @Test
    @DisplayName("getPetVaccinations throws exception when pet not found")
    void getPetVaccinations_petNotFound_throwsException() {
        when(petRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.getPetVaccinations(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("getPetGroomingServices returns empty list when pet has no grooming services")
    void getPetGroomingServices_emptyList() {
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));

        List<ServiceDTO> result = serviceManager.getPetGroomingServices(1);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getPetVaccinations returns empty list when pet has no vaccinations")
    void getPetVaccinations_emptyList() {
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));

        List<ServiceDTO> result = serviceManager.getPetVaccinations(1);

        assertThat(result).isEmpty();
    }
}