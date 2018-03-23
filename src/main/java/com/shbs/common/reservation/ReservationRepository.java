package com.shbs.common.reservation;

import com.shbs.common.jpa.Jpa8Repository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends Jpa8Repository<Reservation, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT r FROM Reservation r " +
            "WHERE r.roomTypeId = ?1 AND " +
            "(?2 BETWEEN r.startDate AND r.endDate) OR " +
            "(?3 BETWEEN r.startDate AND r.endDate) OR " +
            "(?2 <= r.startDate AND ?3 >= r.endDate)")
    List<Reservation> find(Integer roomTypeId, ZonedDateTime start, ZonedDateTime end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT r from Reservation r WHERE r.id = ?1 AND r.cancelled = ?2")
    Optional<Reservation> findByIdAndAndCancelledForUpdate(Integer id, Boolean cancelled);

    Optional<Reservation> findByIdAndAndCancelled(Integer id, Boolean cancelled);
}
