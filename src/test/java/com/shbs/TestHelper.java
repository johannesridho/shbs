package com.shbs;

import com.shbs.common.reservation.Reservation;
import com.shbs.common.reservation.ReservationRepository;
import com.shbs.common.roomtype.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TestHelper {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public void cleanUp() {
    }

    public Integer createReservation(Integer roomTypeId, Integer customerId, Integer quantity,
                                  ZonedDateTime startDate, ZonedDateTime endDate, Boolean cancelled) {
        final Reservation reservation = new Reservation();
        reservation.setRoomTypeId(roomTypeId);
        reservation.setCustomerId(customerId);
        reservation.setQuantity(quantity);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setCancelled(cancelled);

        final Reservation createdReservation = reservationRepository.save(reservation);
        return createdReservation.getId();
    }
}