package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.RoleDTO;
import com.issoft.ticketstoreapp.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDto(Role role);

    Role toRole(RoleDTO roleDTO);
}