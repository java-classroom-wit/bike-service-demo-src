package pl.javasolutions.apps.service.mechanic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.javasolutions.apps.repository.MechanicRepository;
import pl.javasolutions.apps.repository.model.MechanicEntity;
import java.util.List;
import java.util.Optional;

/**
 * Service for Mechanic domain operations.
 * Returns domain models (Mechanic) instead of JPA entities to maintain proper separation of concerns.
 */
@Service
@Transactional(readOnly = true)
public class MechanicService {

    private final MechanicRepository mechanicRepository;

    public MechanicService(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    /**
     * Retrieves all mechanics as domain models.
     * Uses JOIN FETCH for repairOrders (OneToMany) to avoid N+1 query problem.
     *
     * @return list of Mechanic domain models
     */
    public List<Mechanic> findAll() {
        return mechanicRepository.findAllWithRepairOrders().stream()
                .map(MechanicFactory::create)
                .toList();
    }

    /**
     * Retrieves a mechanic by ID as a domain model.
     * Uses JOIN FETCH to eagerly load repairOrders.
     *
     * @param id the mechanic ID
     * @return Optional containing the Mechanic domain model or empty if not found
     */
    public Optional<Mechanic> findById(Long id) {
        return mechanicRepository.findByIdWithRepairOrders(id)
                .map(MechanicFactory::create);
    }

    /**
     * Creates and saves a new mechanic from a command.
     * Returns the created mechanic as a domain model.
     *
     * @param command the create mechanic command
     * @return the created Mechanic domain model
     */
    @Transactional
    public Mechanic save(CreateMechanicCommand command) {
        MechanicEntity entity = new MechanicEntity();
        entity.setFirstName(command.firstName());
        entity.setLastName(command.lastName());
        entity.setSpecialization(command.specialization());
        entity.setHiredAt(command.hiredAt());
        entity.setEmail(command.email());

        return MechanicFactory.create(mechanicRepository.save(entity));
    }
}
