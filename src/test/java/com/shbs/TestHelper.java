package com.shbs;

import com.shbs.common.customer.Customer;
import com.shbs.common.customer.CustomerRepository;
import com.shbs.common.reservation.Reservation;
import com.shbs.common.reservation.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TestHelper {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

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

        final Reservation newReservation = reservationRepository.save(reservation);
        return newReservation.getId();
    }

    public Integer createCustomer(String username) {
        final Customer customer = new Customer();
        customer.setName("name");
        customer.setPassword("pass");
        customer.setUsername(username);

        final Customer newCustomer = customerRepository.save(customer);
        return newCustomer.getId();
    }
}