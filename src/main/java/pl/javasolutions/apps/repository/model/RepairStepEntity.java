package pl.javasolutions.apps.repository.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "repair_step")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.RepairStepEntity")
public class RepairStepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_order_id", nullable = false)
    private RepairOrderEntity repairOrder;

    public RepairStepEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public RepairOrderEntity getRepairOrder() { return repairOrder; }
    public void setRepairOrder(RepairOrderEntity repairOrder) { this.repairOrder = repairOrder; }
}


