package com.shbs.common.reservation;

import com.shbs.common.jpa.Jpa8Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReservationRepository extends Jpa8Repository<Reservation, Integer> {
    @Query(value = "SELECT r FROM Reservation r " +
            "WHERE r.id = ?1 AND " +
            "(?2 BETWEEN r.startDate AND r.endDate " +
            "OR ?3 BETWEEN r.startDate AND r.endDate)")
    List<Reservation> find(Integer roomTypeId, ZonedDateTime start, ZonedDateTime end);
}
