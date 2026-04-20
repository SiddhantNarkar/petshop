package com.petshop.service;

import com.petshop.dto.PetCategoryDTO;
import com.petshop.dto.PetCategoryRequestDTO;
import com.petshop.entity.PetCategory;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.PetCategoryRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetCategoryServiceTest {

    @Mock private PetCategoryRepository categoryRepo;

    @InjectMocks private PetCategoryService petCategoryService;

    private PetCategory dog;

    @BeforeEach
    void setUp() {
        dog = new PetCategory();
        dog.setCategoryId(1);
        dog.setName("Dogs");
    }

    @Test
    @DisplayName("createCategory returns DTO")
    void createCategory_returnsDTO() {
        PetCategoryRequestDTO req = mock(PetCategoryRequestDTO.class);
        when(req.getName()).thenReturn("Dogs");

        when(categoryRepo.save(any())).thenReturn(dog);

        PetCategoryDTO result = petCategoryService.createCategory(req);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Dogs");
    }

    @Test
    @DisplayName("getAllCategories returns DTO list")
    void getAllCategories_returnsDTOList() {
        when(categoryRepo.findAll()).thenReturn(List.of(dog));

        List<PetCategoryDTO> result = petCategoryService.getAllCategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Dogs");
    }

    @Test
    @DisplayName("getCategoryById returns DTO")
    void getCategoryById_returnsDTO() {
        when(categoryRepo.findById(1)).thenReturn(Optional.of(dog));

        PetCategoryDTO result = petCategoryService.getCategoryById(1);

        assertThat(result.getCategoryId()).isEqualTo(1);
    }

    @Test
    @DisplayName("getCategoryById throws when not found")
    void getCategoryById_notFound() {
        when(categoryRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petCategoryService.getCategoryById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("updateCategory updates and returns DTO")
    void updateCategory_updatesAndReturnsDTO() {
        PetCategoryRequestDTO req = mock(PetCategoryRequestDTO.class);
        when(req.getName()).thenReturn("Updated Dogs");

        when(categoryRepo.findById(1)).thenReturn(Optional.of(dog));
        when(categoryRepo.save(any())).thenReturn(dog);

        PetCategoryDTO result = petCategoryService.updateCategory(1, req);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Dogs");

        verify(categoryRepo).save(any(PetCategory.class));
    }
}