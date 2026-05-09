package pl.javasolutions.apps.controller.mechanic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.javasolutions.apps.service.mechanic.MechanicService;

import java.util.List;

@RestController
@RequestMapping("/api/mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public List<MechanicResponse> getAll() {
        return mechanicService.findAll().stream().map(MechanicResponse::from).toList();
    }

    @GetMapping("/{id}")
    public MechanicResponse getById(@PathVariable Long id) {
        var mechanic = mechanicService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mechanic not found"));
        return MechanicResponse.from(mechanic);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MechanicResponse create(@RequestBody CreateMechanicRequest request) {
        var mechanic = mechanicService.save(request.toCommand());
        return MechanicResponse.from(mechanic);
    }
}

