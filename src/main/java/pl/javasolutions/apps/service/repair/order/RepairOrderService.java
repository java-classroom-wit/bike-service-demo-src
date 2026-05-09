package pl.javasolutions.apps.service.repair.order;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.javasolutions.apps.repository.BicycleRepository;
import pl.javasolutions.apps.repository.MechanicRepository;
import pl.javasolutions.apps.repository.RepairOrderRepository;
import pl.javasolutions.apps.repository.model.BicycleEntity;
import pl.javasolutions.apps.repository.model.MechanicEntity;
import pl.javasolutions.apps.repository.model.RepairOrderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for RepairOrder domain operations.
 * Returns domain models (RepairOrder) instead of JPA entities to maintain proper separation of concerns.
 */
@Service
@Transactional(readOnly = true)
public class RepairOrderService {

    private final RepairOrderRepository repairOrderRepository;
    private final BicycleRepository bicycleRepository;
    private final MechanicRepository mechanicRepository;

    public RepairOrderService(RepairOrderRepository repairOrderRepository,
                              BicycleRepository bicycleRepository,
                              MechanicRepository mechanicRepository) {
        this.repairOrderRepository = repairOrderRepository;
        this.bicycleRepository = bicycleRepository;
        this.mechanicRepository = mechanicRepository;
    }

    /**
     * Retrieves all repair orders as domain models.
     * Uses JOIN FETCH for ManyToOne (bicycle, mechanic) and @BatchSize for collections
     * to avoid N+1 query problem.
     *
     * @return list of RepairOrder domain models
     */
    public List<RepairOrder> findAll() {
        return repairOrderRepository.findAllWithBicycleAndMechanic().stream()
                .map(RepairOrderFactory::create)
                .toList();
    }

    /**
     * Retrieves a repair order by ID as a domain model.
     * Uses JOIN FETCH for ManyToOne associations (bicycle, mechanic).
     * Collections (repairSteps, parts) are loaded via @BatchSize.
     *
     * @param id the repair order ID
     * @return Optional containing the RepairOrder domain model or empty if not found
     */
    public Optional<RepairOrder> findById(Long id) {
        return repairOrderRepository.findByIdWithBicycleAndMechanic(id)
                .map(RepairOrderFactory::create);
    }

    /**
     * Creates and saves a new repair order from a request.
     * Uses SHALLOW MAPPING strategy: entity is built manually, relationships set directly.
     *
     * @param command the create repair order request
     * @return the created RepairOrder domain model
     */
    @Transactional
    public RepairOrder save(CreateRepairOrderCommand command) {
        BicycleEntity bicycle = bicycleRepository.findById(command.bicycleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bicycle not found"));

        MechanicEntity mechanic = null;
        if (command.mechanicId() != null) {
            mechanic = mechanicRepository.findById(command.mechanicId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mechanic not found"));
        }

        RepairOrderEntity order = new RepairOrderEntity();
        order.setDescription(command.description());
        order.setStatus(command.status().name());
        order.setReceivedAt(LocalDateTime.now());
        order.setBicycle(bicycle);
        order.setMechanic(mechanic);
        order.setEstimatedCost(command.estimatedCost());

        return RepairOrderFactory.create(repairOrderRepository.save(order));
    }

    /**
     * Updates the status of a repair order.
     * Returns the updated repair order as a domain model.
     *
     * @param id     the repair order ID
     * @param status the new status
     * @return Optional containing the updated RepairOrder domain model or empty if not found
     */
    @Transactional
    public Optional<RepairOrder> updateStatus(Long id, RepairOrderStatus status) {
        Optional<RepairOrderEntity> maybeOrder = repairOrderRepository.findById(id);
        maybeOrder.ifPresent(order -> {
            order.setStatus(status.name());
            if (status == RepairOrderStatus.COMPLETED) {
                order.setCompletedAt(LocalDateTime.now());
            }
        });
        return maybeOrder.map(RepairOrderFactory::create);
    }
}
