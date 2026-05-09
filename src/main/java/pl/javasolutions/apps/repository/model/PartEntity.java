package pl.javasolutions.apps.repository.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "part")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "pl.javasolutions.apps.repository.model.PartEntity")
public class PartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "manufacturer", length = 150)
    private String manufacturer;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToMany(mappedBy = "parts")
    private List<RepairOrderEntity> repairOrders = new ArrayList<>();

    public PartEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public List<RepairOrderEntity> getRepairOrders() { return repairOrders; }
    public void setRepairOrders(List<RepairOrderEntity> repairOrders) { this.repairOrders = repairOrders; }
}


