package pl.javasolutions.apps.dto;

import jakarta.validation.constraints.*;
import pl.javasolutions.apps.validation.ValidManufacturer;

import java.math.BigDecimal;

public class PartDTO {

    @NotBlank(message = "Nazwa części jest wymagana")
    @Size(max = 200, message = "Nazwa nie może przekraczać 200 znaków")
    private String name;

    @ValidManufacturer
    @Size(max = 150, message = "Nazwa producenta nie może przekraczać 150 znaków")
    private String manufacturer;

    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.01", message = "Cena musi być większa od 0")
    private BigDecimal price;

    public PartDTO() {}

    public PartDTO(String name, String manufacturer, BigDecimal price) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

