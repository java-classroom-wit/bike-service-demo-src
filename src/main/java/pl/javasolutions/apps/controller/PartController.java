package pl.javasolutions.apps.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.javasolutions.apps.dto.PartDTO;
import pl.javasolutions.apps.mapper.PartMapper;
import pl.javasolutions.apps.repository.model.PartEntity;
import pl.javasolutions.apps.service.PartService;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/parts")
@Tag(name = "Parts", description = "Zarządzanie częściami rowerowymi")
public class PartController {

    private final PartService partService;
    private final PartMapper partMapper;

    public PartController(PartService partService, PartMapper partMapper) {
        this.partService = partService;
        this.partMapper = partMapper;
    }

    @GetMapping
    @Operation(summary = "Lista części", description = "Zwraca stronicowaną listę części z opcjonalnym filtrowaniem")
    public Page<PartDTO> getAll(
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return partService.findAll(manufacturer, minPrice, maxPrice, name, pageable)
                .map(partMapper::toDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobranie części", description = "Zwraca część o podanym ID")
    @ApiResponse(responseCode = "200", description = "Część znaleziona")
    @ApiResponse(responseCode = "404", description = "Część nie istnieje")
    public ResponseEntity<PartDTO> getById(@PathVariable Long id) {
        PartEntity part = partService.findById(id);
        String etag = "\"" + Integer.toHexString(part.hashCode()) + "\"";
        return ResponseEntity.ok()
                .eTag(etag)
                .body(partMapper.toDTO(part));
    }

    @PostMapping
    @Operation(summary = "Utworzenie części", description = "Tworzy nową część i zwraca 201 Created")
    @ApiResponse(responseCode = "201", description = "Część utworzona")
    @ApiResponse(responseCode = "422", description = "Błąd walidacji")
    public ResponseEntity<PartDTO> create(@Valid @RequestBody PartDTO dto) {
        PartEntity created = partService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(partMapper.toDTO(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizacja części", description = "Pełna aktualizacja części")
    @ApiResponse(responseCode = "200", description = "Część zaktualizowana")
    @ApiResponse(responseCode = "404", description = "Część nie istnieje")
    public ResponseEntity<PartDTO> update(@PathVariable Long id,
                                           @Valid @RequestBody PartDTO dto) {
        PartEntity updated = partService.update(id, dto);
        return ResponseEntity.ok(partMapper.toDTO(updated));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Częściowa aktualizacja", description = "Aktualizuje tylko przesłane pola")
    @ApiResponse(responseCode = "200", description = "Część zaktualizowana")
    @ApiResponse(responseCode = "404", description = "Część nie istnieje")
    public ResponseEntity<PartDTO> patch(@PathVariable Long id,
                                          @RequestBody PartDTO dto) {
        PartEntity patched = partService.patch(id, dto);
        return ResponseEntity.ok(partMapper.toDTO(patched));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usunięcie części", description = "Usuwa część o podanym ID")
    @ApiResponse(responseCode = "204", description = "Część usunięta")
    @ApiResponse(responseCode = "404", description = "Część nie istnieje")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

