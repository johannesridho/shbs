package com.shbs.admin.roomtype;

import com.shbs.admin.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/room-type")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public String index(Model model) {
        final Iterable<RoomType> roomTypes = roomTypeService.findAll();
        model.addAttribute("roomTypes", roomTypes);
        return "admin/room-type/index";
    }

    @GetMapping("create")
    public String getCreatePage(Model model) {
        model.addAttribute("roomTypeForm", new RoomTypeForm());
        Utility.addAttributesToFormModel(model, "Create",
                "/admin/room-type/create", "Create");
        return "admin/room-type/form";
    }

    @PostMapping("create")
    public String create(@Valid @ModelAttribute("roomTypeForm") RoomTypeForm roomTypeForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Utility.addAttributesToFormModel(model, "Create", "/admin/room-type/create",
                    "Create");
            return "admin/room-type/form";
        } else {
            final RoomType roomType = new RoomType();
            saveRoomType(roomType, roomTypeForm);
            return "redirect:/admin/room-type";
        }
    }

    @GetMapping("update/{id}")
    public String getUpdatePage(@PathVariable Integer id, Model model) {
        final RoomType roomType = roomTypeService.findById(id);
        final RoomTypeForm roomTypeForm = new RoomTypeForm(roomType);

        model.addAttribute("roomTypeForm", roomTypeForm);
        Utility.addAttributesToFormModel(model, "Update", "/admin/room-type/update/" + id,
                "Update");
        return "admin/room-type/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("roomTypeForm") RoomTypeForm roomTypeForm,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            Utility.addAttributesToFormModel(model, "Update", "/admin/room-type/update/" + id,
                    "Update");
            return "admin/room-type/form";
        } else {
            final RoomType roomType = roomTypeService.findById(id);
            saveRoomType(roomType, roomTypeForm);
            return "redirect:/admin/room-type";
        }
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Integer id) {
        roomTypeService.delete(id);

        return "redirect:/admin/room-type";
    }

    private void saveRoomType(RoomType roomType, RoomTypeForm form) {
        roomType.setType(form.getType());
        roomType.setDescription(form.getDescription());
        roomType.setImage(form.getImage());
        roomType.setQuantity(0);
        roomType.setPrice(form.getPrice());

        roomTypeService.save(roomType);
    }
}
