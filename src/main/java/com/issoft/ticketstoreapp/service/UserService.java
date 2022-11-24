package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.HallDTO;
import com.issoft.ticketstoreapp.dto.UserDTO;
import com.issoft.ticketstoreapp.dto.UserHallDTO;
import com.issoft.ticketstoreapp.exception.UserNotFoundException;
import com.issoft.ticketstoreapp.mapper.UserMapper;
import com.issoft.ticketstoreapp.model.User;
import com.issoft.ticketstoreapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String NO_USER_MESSAGE = "No user with name [%s] exists";
    private static final String NO_USER_FOR_CRITERIA = "No user for such criteria exists";
    private static final String NO_PERMISSION_MESSAGE = "No permission to update this user";
    private final UserRepository userRepository;
    private final HallService hallService;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserService(UserRepository userRepository,
                       HallService hallService,
                       UserMapper mapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.hallService = hallService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO findUserById(Long userId) {
        LOGGER.debug("Entering UserService.findUserById()");

        UserDTO userDTO = this.mapper.toDto(this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(NO_USER_MESSAGE, userId))));

        LOGGER.debug("Exiting UserService.findUserById()");
        return userDTO;
    }

    @Audit
    public List<UserDTO> findUsers() {
        LOGGER.debug("Entering UserService.findUsers()");

        List<UserDTO> users = this.userRepository.findAll()
                .stream()
                .map(this.mapper::toDto)
                .collect(Collectors.toList());

        LOGGER.debug("Exiting UserService.findUsers()");
        return users;
    }

    @Audit
    public UserDTO saveUser(UserDTO userDTO) {
        LOGGER.debug("Entering UserService.saveUser()");
        User user = this.mapper.toUser(userDTO);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        UserDTO savedOrUpdatedUser = this.mapper.toDto(this.userRepository.save(user));

        LOGGER.debug("Exiting UserService.saveUser()");
        return savedOrUpdatedUser;
    }

    @Audit
    @Transactional
    public UserDTO updateUser(UserDTO userDTO, String currentUserLogin) {
        LOGGER.debug("Entering UserService.updateUser()");

        if (!userDTO.getLogin().equals(currentUserLogin)) {
            LOGGER.error(NO_PERMISSION_MESSAGE);
            throw new BadCredentialsException(NO_PERMISSION_MESSAGE);
        }
        UserDTO updatedUser;
        Optional<User> existingUser = this.userRepository.findByLogin(currentUserLogin);

        if (existingUser.isPresent()) {
            this.mapper.updateUserFromDto(userDTO, existingUser.get());
            updatedUser = this.mapper.toDto(this.userRepository.save(existingUser.get()));
        } else {
            LOGGER.error(String.format(NO_USER_MESSAGE, currentUserLogin));
            throw new UserNotFoundException(String.format(NO_USER_MESSAGE, currentUserLogin));
        }

        LOGGER.debug("Exiting UserService.updateUser()");
        return updatedUser;
    }


    @Audit
    public void deleteUser(Long userId) {
        LOGGER.debug("Entering UserService.deleteUser()");

        this.userRepository.deleteById(userId);

        LOGGER.debug("Exiting UserService.deleteUser()");
    }

    @Audit
    public UserHallDTO findMostActiveUserForHall(String hallName) {
        LOGGER.debug("Entering UserService.findMostActiveUserForHall()");
        UserDTO foundUserDTO;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);

        HallDTO hallDTO = this.hallService.findHallByName(hallName);

        List<User> lastMonthMostActiveUsersForHall =
                userRepository.findLastMonthMostActiveForHall(hallName, lastMonth, now);
        if (lastMonthMostActiveUsersForHall.size() > 0) {
            foundUserDTO = this.mapper.toDto(lastMonthMostActiveUsersForHall.get(0));
        } else {
            throw new UserNotFoundException(NO_USER_FOR_CRITERIA);
        }

        UserHallDTO userHallDTO = this.mapper.toDto(foundUserDTO, hallDTO);

        LOGGER.debug("Exiting UserService.findMostActiveUserForHall()");
        return userHallDTO;
    }
}