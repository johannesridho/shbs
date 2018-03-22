package com.shbs.api.roomtype;

import com.shbs.common.roomtype.AvailableRoomType;
import com.shbs.common.roomtype.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import static com.shbs.common.Constant.DATE_FORMAT;
import static com.shbs.common.Constant.ZONE_OFFSET;

@RestController
@RequestMapping("room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping("available")
    public List<AvailableRoomType> findAvailableRoomTypes(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDate start,
                                                          @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDate end) {
        return roomTypeService.findAvailableRoomTypes(ZonedDateTime.of(start, LocalTime.MIN, ZONE_OFFSET),
                ZonedDateTime.of(end, LocalTime.MIN, ZONE_OFFSET));
    }
}
