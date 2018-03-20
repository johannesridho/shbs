package com.shbs.api.reservation;

import com.shbs.common.jpa.Jpa8Repository;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReservationRepository extends Jpa8Repository<Reservation, Integer> {
    List<Reservation> find(Integer roomTypeId, ZonedDateTime start, ZonedDateTime end);
}
