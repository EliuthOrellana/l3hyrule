package com.uca.labo3hyrule.services;

import com.uca.labo3hyrule.domain.dto.request.CreateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.request.UpdateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.response.PageableResponse;
import com.uca.labo3hyrule.domain.dto.response.SpecimenResponse;

import java.util.UUID;

public interface SpecimenService {
    SpecimenResponse createSpecimen(CreateSpecimenRequest request);
    PageableResponse<SpecimenResponse> getAllSpecimens(int page, int size, String sortBy, String sortOrder);
    SpecimenResponse getSpecimenById(UUID id);
    SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request);
    SpecimenResponse deleteSpecimen(UUID id);
}

