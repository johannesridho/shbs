package com.shbs.admin.user;

import com.shbs.admin.Utility;
import com.shbs.admin.user.model.ChangePasswordForm;
import com.shbs.admin.user.model.User;
import com.shbs.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String index(Model model) {
        Iterable<User> userList = userService.getAllUser();
        model.addAttribute("userList", userList);
        return "admin/user/index";
    }

    @GetMapping("create")
    public String getCreatePage(Model model) {
        model.addAttribute("user", new User());
        Utility.addAttributesToFormModel(model, "Create New User", "/admin/user/create", "Create");
        return "admin/user/form";
    }

    @PostMapping("create")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Utility.addAttributesToFormModel(model, "Create New User", "/admin/user/create", "Create");
            return "admin/user/form";
        } else {
            userService.saveUser(user);
            return "redirect:/admin/user";
        }
    }

    @GetMapping("delete/{name}")
    public String delete(@PathVariable String name, Principal principal) {
        final Optional<User> user = userService.getUserByUsername(name);
        if (user.isPresent() && !principal.getName().equals(name)) {
            userService.deleteUser(name);
        }

        return "redirect:/admin/user";
    }

    @GetMapping("change-password")
    public String getChangePasswordPage(Model model) {
        final ChangePasswordForm form = new ChangePasswordForm();
        model.addAttribute("changePasswordForm", form);
        return "admin/user/change-password";
    }

    @PostMapping("change-password")
    public String changePassword(
        Model model,
        Principal principal,
        @Valid @ModelAttribute("changePasswordForm") ChangePasswordForm form,
        BindingResult bindingResult) {
        final User user = userService
            .getUserByUsername(principal.getName())
            .orElseThrow(() -> new NotFoundException(User.class, principal.getName()));

        if (!userService.isPasswordMatch(form.getCurrentPassword(), user.getPassword())) {
            bindingResult.addError(new FieldError("changePasswordForm", "currentPassword", "Password is incorrect"));
        }

        if (!form.isConfirmNewPasswordMatch()) {
            bindingResult.addError(new FieldError("changePasswordForm", "confirmNewPassword", "Password not match"));
        }

        if (!bindingResult.hasErrors()) {
            userService.changeUserPassword(user, form.getNewPassword());
            return "redirect:/admin/user/change-password?success";
        }

        model.addAttribute("changePasswordForm", form);
        return "admin/user/change-password";
    }
}
