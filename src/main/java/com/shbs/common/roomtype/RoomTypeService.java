package com.shbs.common.roomtype;

import com.shbs.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public Iterable<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    public List<AvailableRoomType> findAvailableRoomTypes(ZonedDateTime start, ZonedDateTime end) {
        final List<ReservedRoomType> reservedRoomTypes = roomTypeRepository.findReservedRoomTypes(start, end);
        final Map<Integer, Integer> reservedRoomTypeMap = new HashMap<>();

        reservedRoomTypes.forEach(reservedRoomType ->
                reservedRoomTypeMap.put(reservedRoomType.getId(), reservedRoomType.getReservedQuantity().intValue()));

        final List<AvailableRoomType> availableRoomTypes = roomTypeRepository
                .findByQuantityGreaterThan(0)
                .stream()
                .map(roomType -> {
                    if (reservedRoomTypeMap.containsKey(roomType.getId())) {
                        final Integer reservedQuantity = reservedRoomTypeMap.get(roomType.getId());
                        final Integer availableQuantity = roomType.getQuantity() - reservedQuantity;

                        return new AvailableRoomType(roomType, availableQuantity);
                    }

                    return new AvailableRoomType(roomType);
                })
                .filter(availableRoomType -> availableRoomType.getAvailableQuantity() > 0)
                .collect(Collectors.toList());

        return availableRoomTypes;
    }

    public RoomType findById(Integer id) {
        return roomTypeRepository.findOne(id)
                .orElseThrow(() -> new NotFoundException(RoomType.class, id.toString()));
    }

    public void save(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }

    public void delete(Integer id) {
        findById(id);
        roomTypeRepository.delete(id);
    }
}
