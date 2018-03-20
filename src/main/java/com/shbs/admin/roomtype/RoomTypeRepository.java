package com.shbs.admin.roomtype;

import com.shbs.common.jpa.Jpa8Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface RoomTypeRepository extends Jpa8Repository<RoomType, Integer> {
    @Query(value = "SELECT r " +
            "FROM room_type r " +
            "JOIN reservation r2 ON (r.id = r2.room_type_id) " +
            "WHERE r.id = ?1 " +
            "AND ?2 NOT BETWEEN r2.start_date AND r2.end_date " +
            "AND ?3 NOT BETWEEN r2.start_date AND r2.end_date",
            nativeQuery = true)
    List<RoomType> findAvailableRoomTypes(Integer typeId, ZonedDateTime startDate, ZonedDateTime endDate);
}
