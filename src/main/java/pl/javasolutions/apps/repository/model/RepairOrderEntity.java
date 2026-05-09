package pl.javasolutions.apps.repository.model;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repair_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.RepairOrderEntity")
public class RepairOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bicycle_id", nullable = false)
    private BicycleEntity bicycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id")
    private MechanicEntity mechanic;

    @OneToMany(mappedBy = "repairOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.RepairOrderEntity.repairSteps")
    private List<RepairStepEntity> repairSteps = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.RepairOrderEntity.parts")
    @JoinTable(
        name = "repair_order_part",
        joinColumns = @JoinColumn(name = "repair_order_id"),
        inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private List<PartEntity> parts = new ArrayList<>();

    public RepairOrderEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }

    public BicycleEntity getBicycle() { return bicycle; }
    public void setBicycle(BicycleEntity bicycle) { this.bicycle = bicycle; }

    public MechanicEntity getMechanic() { return mechanic; }
    public void setMechanic(MechanicEntity mechanic) { this.mechanic = mechanic; }

    public List<RepairStepEntity> getRepairSteps() { return repairSteps; }
    public void setRepairSteps(List<RepairStepEntity> repairSteps) { this.repairSteps = repairSteps; }

    public List<PartEntity> getParts() { return parts; }
    public void setParts(List<PartEntity> parts) { this.parts = parts; }
}

