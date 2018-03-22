package com.shbs.admin.roomtype;

import com.shbs.common.roomtype.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RoomTypeDashboardForm {

    @NotBlank
    private String type;

    private String description;
    private String image;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;

    public RoomTypeDashboardForm(RoomType roomType) {
        this.type = roomType.getType();
        this.description = roomType.getDescription();
        this.image = roomType.getImage();
        this.quantity = roomType.getQuantity();
        this.price = roomType.getPrice();
    }
}
