package com.example.backend.mapper;

import com.example.backend.controller.response.UserResponse;
import com.example.backend.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = InventoryMapper.class)
public interface UserMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "inventories", source = "user.inventories")
    UserResponse toResponse(AppUser user);
}