package com.shbs.admin.user.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordForm {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 64;
    private static final String ALPHANUMERIC_PATTERN = "^[A-Za-z0-9]+$";
    private static final String MINIMUM_LENGTH_ERROR_MESSAGE = "Password must contain at least 6 characters";
    private static final String ALPHANUMERIC_PATTERN_ERROR_MESSAGE =
        "Password must contain only alphanumeric characters";

    @NotEmpty
    private String currentPassword;

    @NotEmpty
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = MINIMUM_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = ALPHANUMERIC_PATTERN, message = ALPHANUMERIC_PATTERN_ERROR_MESSAGE)
    private String newPassword;

    @NotEmpty
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = MINIMUM_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = ALPHANUMERIC_PATTERN, message = ALPHANUMERIC_PATTERN_ERROR_MESSAGE)
    private String confirmNewPassword;

    public boolean isConfirmNewPasswordMatch() {
        return this.confirmNewPassword.equals(this.newPassword);
    }
}
