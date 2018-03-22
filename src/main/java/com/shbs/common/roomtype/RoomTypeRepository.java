package com.shbs.common.roomtype;

import com.shbs.common.jpa.Jpa8Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface RoomTypeRepository extends Jpa8Repository<RoomType, Integer> {
    @Query(value = "SELECT new com.shbs.common.roomtype.ReservedRoomType(r.roomTypeId, SUM(r.quantity)) " +
            "FROM Reservation r WHERE " +
            "r.cancelled = FALSE AND " +
            "((?1 BETWEEN r.startDate AND r.endDate) OR " +
            "(?2 BETWEEN r.startDate AND r.endDate) OR " +
            "(?1 <= r.startDate AND ?2 >= r.endDate)) " +
            "GROUP BY r.roomTypeId")
    List<ReservedRoomType> findReservedRoomTypes(ZonedDateTime start, ZonedDateTime end);

    List<RoomType> findByQuantityGreaterThan(Integer value);
}
