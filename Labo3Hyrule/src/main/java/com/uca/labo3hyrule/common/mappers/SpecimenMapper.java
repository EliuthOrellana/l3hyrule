package com.uca.labo3hyrule.common.mappers;

import com.uca.labo3hyrule.domain.dto.request.CreateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.request.UpdateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.response.SpecimenResponse;
import com.uca.labo3hyrule.domain.entities.Specimen;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpecimenMapper {

    public Specimen toEntityCreate(CreateSpecimenRequest request) {
        return Specimen.builder()
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public Specimen toEntityUpdate(UpdateSpecimenRequest request, UUID id) {
        return Specimen.builder()
                .id(id)
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public SpecimenResponse toDto(Specimen specimen) {
        return SpecimenResponse.builder()
                .id(specimen.getId())
                .name(specimen.getName())
                .region(specimen.getRegion())
                .dangerLevel(specimen.getDangerLevel())
                .isFriendly(specimen.getIsFriendly())
                .build();
    }

    // TODO: El estudiante deberá agregar aquí el método para mapear un Page<Specimen> a Page<SpecimenResponse>
    // pista: utilizando .map(this::toDto)
}
