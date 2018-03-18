package com.shbs.admin.roomtype;

import com.shbs.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public Iterable<RoomType> findAll() {
        return roomTypeRepository.findAll();
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
