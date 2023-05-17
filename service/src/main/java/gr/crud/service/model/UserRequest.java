package gr.crud.service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotNull(message = "Email must not be null") @Email(message = "Email must be well formatted!") String email, String firstname, String lastname) {}
