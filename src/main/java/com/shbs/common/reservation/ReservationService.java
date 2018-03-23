package com.shbs.common.reservation;

import com.shbs.api.reservation.ReservationRequest;
import com.shbs.common.exception.NotFoundException;
import com.shbs.common.reservation.exception.AvailableRoomsNotEnoughException;
import com.shbs.common.reservation.exception.ReservationStartDateHasPassedException;
import com.shbs.common.roomtype.RoomType;
import com.shbs.common.roomtype.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Transactional
    public Reservation create(ReservationRequest request) {
        ReservationValidationHelper.validateReservationTime(request.getStartDate(), request.getEndDate());

        validateRoomAvailability(request);

        final Reservation reservation = new Reservation();
        reservation.setRoomTypeId(request.getRoomTypeId());
        reservation.setCustomerId(request.getCustomerId());
        reservation.setQuantity(request.getQuantity());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setCancelled(Boolean.FALSE);

        return reservationRepository.save(reservation);
    }

    public Iterable<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation find(Integer id) {
        return reservationRepository.findByIdAndAndCancelled(id, Boolean.FALSE)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Reservation with id %d is not found or it has been cancelled", id)));
    }

    public Reservation findForUpdate(Integer id) {
        return reservationRepository.findByIdAndAndCancelledForUpdate(id, Boolean.FALSE)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Reservation with id %d is not found or it has been cancelled", id)));
    }

    @Transactional
    public Reservation update(Integer id, ReservationRequest request) {
        ReservationValidationHelper.validateReservationTime(request.getStartDate(), request.getEndDate());

        final Reservation reservation = findForUpdate(id);

        if (ZonedDateTime.now().isAfter(reservation.getStartDate())) {
            throw new ReservationStartDateHasPassedException();
        }

        final RoomType currentRoomType = getRoomType(reservation.getRoomTypeId());

        if (!request.getRoomTypeId().equals(currentRoomType)) {
            validateRoomAvailability(request);
            reservation.setRoomTypeId(request.getRoomTypeId());
        }

        reservation.setCustomerId(request.getCustomerId());
        reservation.setQuantity(request.getQuantity());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancel(Integer id) {
        final Reservation reservation = findForUpdate(id);

        reservation.setCancelled(Boolean.TRUE);

        return reservationRepository.save(reservation);
    }

    private void validateRoomAvailability(ReservationRequest request) {
        final RoomType roomType = getRoomType(request.getRoomTypeId());

        final List<Reservation> reservations = reservationRepository.find(roomType.getId(),
                request.getStartDate(), request.getEndDate());

        final Integer reservedQuantity = reservations.stream()
                .mapToInt(reservation -> reservation.getQuantity())
                .sum();

        if (request.getQuantity() > (roomType.getQuantity() - reservedQuantity)) {
            throw new AvailableRoomsNotEnoughException();
        }
    }

    private RoomType getRoomType(Integer id) {
        return roomTypeRepository.findOne(id)
                .orElseThrow(() -> new NotFoundException(RoomType.class, id.toString()));
    }
}
