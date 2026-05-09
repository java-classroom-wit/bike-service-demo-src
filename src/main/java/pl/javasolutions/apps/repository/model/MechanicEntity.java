package pl.javasolutions.apps.repository.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mechanic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.MechanicEntity")
public class MechanicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "hired_at", nullable = false)
    private LocalDate hiredAt;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.MechanicEntity.repairOrders")
    private List<RepairOrderEntity> repairOrders = new ArrayList<>();

    public MechanicEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public LocalDate getHiredAt() { return hiredAt; }
    public void setHiredAt(LocalDate hiredAt) { this.hiredAt = hiredAt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<RepairOrderEntity> getRepairOrders() { return repairOrders; }
    public void setRepairOrders(List<RepairOrderEntity> repairOrders) { this.repairOrders = repairOrders; }
}

