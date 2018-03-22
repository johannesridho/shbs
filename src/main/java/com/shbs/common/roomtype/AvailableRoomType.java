package com.shbs.common.roomtype;

import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
public class AvailableRoomType {

    @NotNull
    private Integer id;

    @NotBlank
    private String type;

    private String description;
    private String image;

    @NotNull
    private Integer availableQuantity;

    @NotNull
    private BigDecimal price;

    public AvailableRoomType(RoomType roomType, Integer availableQuantity) {
        this.id = roomType.getId();
        this.type = roomType.getType();
        this.description = roomType.getDescription();
        this.image = roomType.getImage();
        this.availableQuantity = availableQuantity;
        this.price = roomType.getPrice();
    }

    public AvailableRoomType(RoomType roomType) {
        this.id = roomType.getId();
        this.type = roomType.getType();
        this.description = roomType.getDescription();
        this.image = roomType.getImage();
        this.availableQuantity = roomType.getQuantity();
        this.price = roomType.getPrice();
    }
}
