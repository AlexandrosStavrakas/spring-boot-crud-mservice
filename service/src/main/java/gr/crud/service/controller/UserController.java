package gr.crud.service.controller;

import gr.crud.service.entity.User;
import gr.crud.service.model.UserRequest;
import gr.crud.service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User crud actions controller")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Get all the available users who are persisted in the h2 memory database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") })
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Get the user by id",
            description = "Get the user by id. In case the id is not stored in h2, the api returns Not found response")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUsersByUserId(id));
    }

    @Operation(
            summary = "Create a user",
            description = "Create a user api. If the email exists in database the create process fails.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @Operation(
            summary = "Update a user",
            description = "Update a user api. If the email of the request belongs to another user, the update process fails.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserRequest userRequest, @PathVariable UUID id) {
        return userService.updateUser(id, userRequest);
    }

    @Operation(
            summary = "Delete a user",
            description = "Delete a user api by id. In case the id is not stored in h2, the api returns Not found response.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

}
