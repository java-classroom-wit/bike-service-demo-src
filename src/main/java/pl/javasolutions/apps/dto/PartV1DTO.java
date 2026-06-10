package pl.javasolutions.apps.dto;

import java.math.BigDecimal;

public class PartV1DTO {

    private Long id;
    private String name;
    private BigDecimal price;

    public PartV1DTO() {}

    public PartV1DTO(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

