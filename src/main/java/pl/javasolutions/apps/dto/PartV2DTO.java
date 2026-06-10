package pl.javasolutions.apps.dto;

import java.math.BigDecimal;

public class PartV2DTO {

    private Long id;
    private String name;
    private String manufacturer;
    private BigDecimal price;

    public PartV2DTO() {}

    public PartV2DTO(Long id, String name, String manufacturer, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

