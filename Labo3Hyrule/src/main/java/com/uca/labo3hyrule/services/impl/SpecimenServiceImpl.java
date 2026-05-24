package com.uca.labo3hyrule.services.impl;

import com.uca.labo3hyrule.common.mappers.SpecimenMapper;
import com.uca.labo3hyrule.domain.dto.request.CreateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.request.UpdateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.response.PageableResponse;
import com.uca.labo3hyrule.domain.dto.response.SpecimenResponse;
import com.uca.labo3hyrule.domain.entities.Specimen;
import com.uca.labo3hyrule.exceptions.ResourceNotFoundException;
import com.uca.labo3hyrule.repositories.SpecimenRepository;
import com.uca.labo3hyrule.services.SpecimenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecimenServiceImpl implements SpecimenService {
    private final SpecimenRepository specimenRepository;
    private final SpecimenMapper specimenMapper;

    @Override
    @Transactional
    public SpecimenResponse createSpecimen(CreateSpecimenRequest request) {
        return specimenMapper.toDto(
                specimenRepository.save(specimenMapper.toEntityCreate(request))
        );
    }

    @Override
    public PageableResponse<SpecimenResponse> getAllSpecimens(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Specimen> specimenPage = specimenRepository.findAll(pageable);

        if (specimenPage.isEmpty())
            throw new ResourceNotFoundException("No specimens are registered in Hyrule");

        Page<SpecimenResponse> responseData = specimenMapper.toPageDto(specimenPage);

        return PageableResponse.<SpecimenResponse>builder()
                .data(responseData.getContent())
                .currentPage(specimenPage.getNumber())
                .pageSize(specimenPage.getSize())
                .totalElements(specimenPage.getTotalElements())
                .totalPages(specimenPage.getTotalPages())
                .hasNext(specimenPage.hasNext())
                .build();
    }

    @Override
    public SpecimenResponse getSpecimenById(UUID id) {
        return specimenMapper.toDto(specimenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specimen not found in Sheikah Slate records"))
        );
    }

    @Override
    @Transactional
    public SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request) {
        this.getSpecimenById(id);
        return specimenMapper.toDto(specimenRepository.save(specimenMapper.toEntityUpdate(request, id)));
    }

    @Override
    @Transactional
    public SpecimenResponse deleteSpecimen(UUID id) {
        SpecimenResponse existSpecimen = this.getSpecimenById(id);
        specimenRepository.deleteById(id);
        return existSpecimen;
    }
}
