package com.shbs.api.reservation;

import com.shbs.admin.roomtype.RoomType;
import com.shbs.admin.roomtype.RoomTypeRepository;
import com.shbs.api.reservation.exception.AvailableRoomsNotEnoughException;
import com.shbs.api.reservation.exception.ReservationAlreadyCancelledException;
import com.shbs.api.reservation.exception.ReservationStartDateHasPassedException;
import com.shbs.common.exception.NotFoundException;
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
        validateRoomAvailability(request);

        final Reservation reservation = new Reservation();
        reservation.setRoomTypeId(request.getRoomTypeId());
        reservation.setCustomerId(request.getCustomerId());
        reservation.setQuantity(request.getQuantity());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setCancel(Boolean.FALSE);

        return reservationRepository.save(reservation);
    }

    public Reservation get(Integer id) {
        return reservationRepository.findOne(id)
                .orElseThrow(() -> new NotFoundException(Reservation.class, id.toString()));
    }

    @Transactional
    public Reservation update(Integer id, ReservationRequest request) {
        final Reservation reservation = get(id);

        if (ZonedDateTime.now().isAfter(reservation.getStartDate())) {
            throw new ReservationStartDateHasPassedException();
        }

        final RoomType currentRoomType = getRoomType(reservation.getRoomTypeId());

        if (!request.getRoomTypeId().equals(currentRoomType)) {
            validateRoomAvailability(request);
            reservation.setRoomTypeId(request.getRoomTypeId());
        }

        reservation.setCustomerId(request.getCustomerId());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancel(Integer id) {
        final Reservation reservation = get(id);

        if (reservation.getCancel()) {
            throw new ReservationAlreadyCancelledException();
        }

        reservation.setCancel(Boolean.TRUE);

        return reservationRepository.save(reservation);
    }

    private void validateRoomAvailability(ReservationRequest request) {
        final RoomType roomType = getRoomType(request.getRoomTypeId());

        final List<Reservation> reservations = reservationRepository.find(roomType.getId(),
                request.getStartDate(), request.getEndDate());

        final Integer reservedQuantity = reservations.stream()
                .mapToInt(reservation -> reservation.getQuantity())
                .sum();

        if (request.getQuantity() > reservedQuantity) {
            throw new AvailableRoomsNotEnoughException();
        }
    }

    private RoomType getRoomType(Integer id) {
        return roomTypeRepository.findOne(id)
                .orElseThrow(() -> new NotFoundException(RoomType.class, id.toString()));
    }
}
