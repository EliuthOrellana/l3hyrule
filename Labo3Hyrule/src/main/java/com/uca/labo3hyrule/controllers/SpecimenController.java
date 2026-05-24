package com.uca.labo3hyrule.controllers;

import com.uca.labo3hyrule.domain.dto.request.CreateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.request.UpdateSpecimenRequest;
import com.uca.labo3hyrule.domain.dto.response.GeneralResponse;
import com.uca.labo3hyrule.domain.dto.response.PageableResponse;
import com.uca.labo3hyrule.domain.dto.response.SpecimenResponse;
import com.uca.labo3hyrule.services.SpecimenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/specimens")
@RequiredArgsConstructor
public class SpecimenController {
    private final SpecimenService specimenService;

    @PostMapping
    public ResponseEntity<GeneralResponse<SpecimenResponse>> createSpecimen(
            @Valid @RequestBody CreateSpecimenRequest request) {
        SpecimenResponse data = specimenService.createSpecimen(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse("Especimen creado", HttpStatus.CREATED.value(), data, null));
    }

    @GetMapping
    public ResponseEntity<PageableResponse<SpecimenResponse>> getAllSpecimens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        PageableResponse<SpecimenResponse> response = specimenService.getAllSpecimens(page, size, sortBy, sortOrder);
        response.setMessage("Especimenes recuperados con exito");
        response.setStatus(HttpStatus.OK.value());
        response.setTimestamp(LocalDateTime.now());
        response.setPath("/api/specimens");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> getSpecimenById(@PathVariable UUID id) {
        SpecimenResponse data = specimenService.getSpecimenById(id);
        return ResponseEntity.ok(buildResponse("especimen recuperado con exito", HttpStatus.OK.value(), data, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> updateSpecimen(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSpecimenRequest request) {
        SpecimenResponse data = specimenService.updateSpecimen(id, request);
        return ResponseEntity.ok(buildResponse("especimen actualizado", HttpStatus.OK.value(), data, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> deleteSpecimen(@PathVariable UUID id) {
        SpecimenResponse data = specimenService.deleteSpecimen(id);
        return ResponseEntity.ok(buildResponse("especimen eliminado", HttpStatus.OK.value(), data, null));
    }

    private <T> GeneralResponse<T> buildResponse(String message, int status, T data, String path) {
        return GeneralResponse.<T>builder()
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .path(path != null ? path : "/api/specimens")
                .data(data)
                .build();
    }
}

