package gr.crud.service.service;

import gr.crud.service.entity.User;
import gr.crud.service.exceptionHandling.UserNotFoundException;
import gr.crud.service.exceptionHandling.UsersEmailAlreadyInUseException;
import gr.crud.service.model.UserRequest;
import gr.crud.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "key", key = "#id")
    public User findUsersByUserId(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id".concat(id.toString())));
    }

    public ResponseEntity<Object> createUser(UserRequest userRequest) {
        existUserByEmail(userRequest.email());
        userRepository.save(User
                .builder()
                .firstname(userRequest.firstname())
                .lastname(userRequest.lastname())
                .email(userRequest.email())
                .build()
        );
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<Object> updateUser(UUID usersId, UserRequest userRequest) {
        User user = findUsersByUserId(usersId);
        if (!user.getEmail().equals(userRequest.email())) {
            existUserByEmail(userRequest.email());
        }
        user.setEmail(userRequest.email());
        user.setFirstname(userRequest.firstname());
        user.setLastname(userRequest.lastname());
        userRepository.save(user);
        return new ResponseEntity<>(OK);
    }


    public ResponseEntity<Object> deleteUser(UUID id) {
        existUserById(id);
        userRepository.deleteById(id);
        return new ResponseEntity<>(OK);
    }

    private void existUserByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UsersEmailAlreadyInUseException("The given email is already in use from another user");
        }
    }

    private void existUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("No users found with id: ".concat(id.toString()));
        }
    }
}
