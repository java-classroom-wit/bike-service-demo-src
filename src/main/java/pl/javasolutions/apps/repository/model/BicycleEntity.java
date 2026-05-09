package pl.javasolutions.apps.repository.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bicycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.BicycleEntity")
public class BicycleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "frame_number", nullable = false, unique = true, length = 50)
    private String frameNumber;

    @Column(name = "owner_name", nullable = false, length = 200)
    private String ownerName;

    @Column(name = "owner_phone", length = 20)
    private String ownerPhone;

    @OneToMany(mappedBy = "bicycle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pl.javasolutions.apps.repository.model.BicycleEntity.repairOrders")
    private List<RepairOrderEntity> repairOrders = new ArrayList<>();

    public BicycleEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getFrameNumber() { return frameNumber; }
    public void setFrameNumber(String frameNumber) { this.frameNumber = frameNumber; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }

    public List<RepairOrderEntity> getRepairOrders() { return repairOrders; }
    public void setRepairOrders(List<RepairOrderEntity> repairOrders) { this.repairOrders = repairOrders; }
}


