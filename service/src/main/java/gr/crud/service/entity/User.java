package gr.crud.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "User_table")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;
    @NotNull
    @Email
    private String email;
    private String firstname;
    private String lastname;
}
