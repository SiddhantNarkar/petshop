package com.petshop.service;

import com.petshop.dto.PetDTO;
import com.petshop.dto.PetRequestDTO;
import com.petshop.entity.Pet;
import com.petshop.entity.PetCategory;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.PetCategoryRepository;
import com.petshop.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock private PetRepository petRepo;
    @Mock private PetCategoryRepository categoryRepo;

    @InjectMocks private PetService petService;

    private Pet samplePet;
    private PetCategory sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new PetCategory();
        sampleCategory.setCategoryId(1);
        sampleCategory.setName("Dogs");

        samplePet = new Pet();
        samplePet.setPetId(1);
        samplePet.setName("Buddy");
        samplePet.setBreed("Labrador");
        samplePet.setAge(3);
        samplePet.setPrice(15000.0);
        samplePet.setCategory(sampleCategory);
    }

    @Test
    @DisplayName("createPet returns DTO")
    void createPet_returnsDTO() {
        when(categoryRepo.findById(1)).thenReturn(Optional.of(sampleCategory));
        when(petRepo.save(any(Pet.class))).thenReturn(samplePet);

        // manually set fields (since no setters in DTO)
        // assuming getters only → use mock-style workaround
        PetRequestDTO req = mock(PetRequestDTO.class);
        when(req.getName()).thenReturn("Buddy");
        when(req.getBreed()).thenReturn("Labrador");
        when(req.getAge()).thenReturn(3);
        when(req.getPrice()).thenReturn(15000.0);
        when(req.getDescription()).thenReturn("desc");
        when(req.getImageUrl()).thenReturn("img");
        when(req.getCategoryId()).thenReturn(1);

        PetDTO result = petService.createPet(req);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Buddy");
    }

    @Test
    @DisplayName("getAllPets returns DTO list")
    void getAllPets_returnsDTOList() {
        when(petRepo.findAll()).thenReturn(List.of(samplePet));

        List<PetDTO> result = petService.getAllPets();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Buddy");
    }

    @Test
    @DisplayName("getPetById returns DTO")
    void getPetById_returnsDTO() {
        when(petRepo.findById(1)).thenReturn(Optional.of(samplePet));

        PetDTO result = petService.getPetById(1);

        assertThat(result).isNotNull();
        assertThat(result.getBreed()).isEqualTo("Labrador");
    }

    @Test
    @DisplayName("getPetById throws when not found")
    void getPetById_notFound() {
        when(petRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.getPetById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getPetsByCategory returns DTO list")
    void getPetsByCategory_returnsDTOList() {
        when(petRepo.findByCategory_CategoryId(1)).thenReturn(List.of(samplePet));

        List<PetDTO> result = petService.getPetsByCategory(1);

        assertThat(result).hasSize(1);
    }
}
