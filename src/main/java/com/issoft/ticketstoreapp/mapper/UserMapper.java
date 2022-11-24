package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.HallDTO;
import com.issoft.ticketstoreapp.dto.RoleDTO;
import com.issoft.ticketstoreapp.dto.UserDTO;
import com.issoft.ticketstoreapp.dto.UserHallDTO;
import com.issoft.ticketstoreapp.model.Role;
import com.issoft.ticketstoreapp.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toUser(UserDTO userDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateUserFromDto(UserDTO userDTO, @MappingTarget User existingUser);

    UserHallDTO toDto(UserDTO user, HallDTO hall);

    RoleDTO map(Role value);
}