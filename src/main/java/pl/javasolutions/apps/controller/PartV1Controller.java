package pl.javasolutions.apps.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.javasolutions.apps.dto.PartV1DTO;
import pl.javasolutions.apps.mapper.PartMapper;
import pl.javasolutions.apps.repository.model.PartEntity;
import pl.javasolutions.apps.service.PartService;

@RestController
@RequestMapping("/api/v1/parts")
public class PartV1Controller {

    private final PartService partService;
    private final PartMapper partMapper;

    public PartV1Controller(PartService partService, PartMapper partMapper) {
        this.partService = partService;
        this.partMapper = partMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartV1DTO> getById(@PathVariable Long id) {
        PartEntity part = partService.findById(id);
        return ResponseEntity.ok(partMapper.toV1DTO(part));
    }
}

