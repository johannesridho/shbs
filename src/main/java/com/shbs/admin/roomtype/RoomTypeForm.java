package com.shbs.admin.roomtype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RoomTypeForm {

    @NotBlank
    private String type;

    private String description;
    private String image;
    private Integer quantity;
    private Integer available;

    @NotNull
    private BigDecimal price;

    public RoomTypeForm(RoomType roomType) {
        this.type = roomType.getType();
        this.description = roomType.getDescription();
        this.image = roomType.getImage();
        this.quantity = roomType.getQuantity();
        this.available = roomType.getAvailable();
        this.price = roomType.getPrice();
    }
}
