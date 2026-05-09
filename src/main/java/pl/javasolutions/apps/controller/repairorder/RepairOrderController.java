package pl.javasolutions.apps.controller.repairorder;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.javasolutions.apps.service.repair.order.RepairOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/repair-orders")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;

    public RepairOrderController(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    @GetMapping
    public List<RepairOrderResponse> getAll() {
        return repairOrderService.findAll().stream().map(RepairOrderResponse::from).toList();
    }

    @GetMapping("/{id}")
    public RepairOrderResponse getById(@PathVariable Long id) {
        var order = repairOrderService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repair order not found"));
        return RepairOrderResponse.from(order);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RepairOrderResponse create(@RequestBody CreateRepairOrderRequest request) {
        var order = repairOrderService.save(request.createRepairOrderCommand());
        return RepairOrderResponse.from(order);
    }

    @PutMapping("/{id}/status")
    public RepairOrderResponse updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        StatusRequest statusRequest = new StatusRequest(newStatus);
        var order = repairOrderService.updateStatus(id, statusRequest.status())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repair order not found"));
        return RepairOrderResponse.from(order);
    }
}


