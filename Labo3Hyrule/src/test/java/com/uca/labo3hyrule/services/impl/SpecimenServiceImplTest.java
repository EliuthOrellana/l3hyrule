package com.uca.labo3hyrule.services.impl;

import com.uca.labo3hyrule.common.mappers.SpecimenMapper;
import com.uca.labo3hyrule.domain.dto.request.CreateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.request.UpdateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.response.PageableResponse;
import com.uca.labo3hyrule.domain.dto.response.SpecimenResponse;
import com.uca.labo3hyrule.domain.entities.Specimen;
import com.uca.labo3hyrule.exceptions.ResourceNotFoundException;
import com.uca.labo3hyrule.repositories.SpecimenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceImplTest {

    @Mock
    private SpecimenRepository specimenRepository;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private SpecimenServiceImpl specimenService;

    @Test
    void createSpecimen_shouldSaveSpecimenAndReturnResponse() {
        UUID specimenId = UUID.randomUUID();

        CreateSpecimenRequest request = CreateSpecimenRequest.builder()
                .name("Guardian")
                .region("Central Hyrule")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        Specimen specimenToSave = Specimen.builder()
                .name("Guardian")
                .region("Central Hyrule")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        Specimen savedSpecimen = Specimen.builder()
                .id(specimenId)
                .name("Guardian")
                .region("Central Hyrule")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        SpecimenResponse expectedResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Guardian")
                .region("Central Hyrule")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        when(specimenMapper.toEntityCreate(request)).thenReturn(specimenToSave);
        when(specimenRepository.save(specimenToSave)).thenReturn(savedSpecimen);
        when(specimenMapper.toDto(savedSpecimen)).thenReturn(expectedResponse);

        SpecimenResponse result = specimenService.createSpecimen(request);

        assertThat(result).isEqualTo(expectedResponse);

        verify(specimenMapper).toEntityCreate(request);
        verify(specimenRepository).save(specimenToSave);
        verify(specimenMapper).toDto(savedSpecimen);
    }

    @Test
    void getSpecimenById_shouldReturnSpecimen_whenSpecimenExists() {
        UUID specimenId = UUID.randomUUID();

        Specimen specimen = Specimen.builder()
                .id(specimenId)
                .name("Lynel")
                .region("Hebra")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        SpecimenResponse expectedResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Lynel")
                .region("Hebra")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimen));
        when(specimenMapper.toDto(specimen)).thenReturn(expectedResponse);

        SpecimenResponse result = specimenService.getSpecimenById(specimenId);

        assertThat(result).isEqualTo(expectedResponse);

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper).toDto(specimen);
    }

    @Test
    void getSpecimenById_shouldThrowResourceNotFoundException_whenSpecimenDoesNotExist() {
        UUID specimenId = UUID.randomUUID();

        when(specimenRepository.findById(specimenId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> specimenService.getSpecimenById(specimenId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Specimen not found in Sheikah Slate records");

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper, never()).toDto(any(Specimen.class));
    }

    @Test
    void getAllSpecimens_shouldReturnPageableResponse_whenSpecimensExist() {
        UUID specimenId = UUID.randomUUID();

        Specimen specimen = Specimen.builder()
                .id(specimenId)
                .name("Bokoblin")
                .region("Great Plateau")
                .dangerLevel(3)
                .isFriendly(false)
                .build();

        SpecimenResponse specimenResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Bokoblin")
                .region("Great Plateau")
                .dangerLevel(3)
                .isFriendly(false)
                .build();

        Page<Specimen> specimenPage = new PageImpl<>(List.of(specimen));
        Page<SpecimenResponse> responsePage = new PageImpl<>(List.of(specimenResponse));

        when(specimenRepository.findAll(any(Pageable.class))).thenReturn(specimenPage);
        when(specimenMapper.toPageDto(specimenPage)).thenReturn(responsePage);

        PageableResponse<SpecimenResponse> result =
                specimenService.getAllSpecimens(0, 10, "name", "asc");

        assertThat(result.getData()).containsExactly(specimenResponse);
        assertThat(result.getCurrentPage()).isZero();
        assertThat(result.getPageSize()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.isHasNext()).isFalse();

        verify(specimenRepository).findAll(any(Pageable.class));
        verify(specimenMapper).toPageDto(specimenPage);
    }

    @Test
    void getAllSpecimens_shouldThrowResourceNotFoundException_whenNoSpecimensExist() {
        Page<Specimen> emptyPage = new PageImpl<>(List.of());

        when(specimenRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThatThrownBy(() -> specimenService.getAllSpecimens(0, 10, "name", "asc"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No specimens are registered in Hyrule");

        verify(specimenRepository).findAll(any(Pageable.class));
        verify(specimenMapper, never()).toPageDto(any());
    }

    @Test
    void updateSpecimen_shouldUpdateSpecimen_whenSpecimenExists() {
        UUID specimenId = UUID.randomUUID();

        UpdateSpecimenRequest request = UpdateSpecimenRequest.builder()
                .name("Silver Lynel")
                .region("Gerudo Highlands")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        Specimen existingSpecimen = Specimen.builder()
                .id(specimenId)
                .name("Lynel")
                .region("Hebra")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        SpecimenResponse existingResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Lynel")
                .region("Hebra")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        Specimen updatedSpecimen = Specimen.builder()
                .id(specimenId)
                .name("Silver Lynel")
                .region("Gerudo Highlands")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        SpecimenResponse expectedResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Silver Lynel")
                .region("Gerudo Highlands")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(existingSpecimen));
        when(specimenMapper.toDto(existingSpecimen)).thenReturn(existingResponse);
        when(specimenMapper.toEntityUpdate(request, specimenId)).thenReturn(updatedSpecimen);
        when(specimenRepository.save(updatedSpecimen)).thenReturn(updatedSpecimen);
        when(specimenMapper.toDto(updatedSpecimen)).thenReturn(expectedResponse);

        SpecimenResponse result = specimenService.updateSpecimen(specimenId, request);

        assertThat(result).isEqualTo(expectedResponse);

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper).toEntityUpdate(request, specimenId);
        verify(specimenRepository).save(updatedSpecimen);
        verify(specimenMapper).toDto(updatedSpecimen);
    }

    @Test
    void deleteSpecimen_shouldDeleteSpecimenAndReturnDeletedData_whenSpecimenExists() {
        UUID specimenId = UUID.randomUUID();

        Specimen specimen = Specimen.builder()
                .id(specimenId)
                .name("Keese")
                .region("Hyrule Field")
                .dangerLevel(1)
                .isFriendly(false)
                .build();

        SpecimenResponse expectedResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Keese")
                .region("Hyrule Field")
                .dangerLevel(1)
                .isFriendly(false)
                .build();

        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimen));
        when(specimenMapper.toDto(specimen)).thenReturn(expectedResponse);

        SpecimenResponse result = specimenService.deleteSpecimen(specimenId);

        assertThat(result).isEqualTo(expectedResponse);

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper).toDto(specimen);
        verify(specimenRepository).deleteById(specimenId);
    }
}