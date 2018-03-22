package com.shbs.common.roomtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedRoomType {

    @NotNull
    private Integer id;

    @NotNull
    private Long reservedQuantity;

}
