package com.example.backend.mapper;

import com.example.backend.controller.response.InventoryResponse;
import com.example.backend.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = BookMapper.class)  // Use BookMapper to map books
public interface InventoryMapper {

    @Mapping(target = "id", source = "inventory.id")
    @Mapping(target = "loanDate", source = "inventory.loanDate")
    @Mapping(target = "bookId", source = "inventory.book.id")
    @Mapping(target = "userId", source = "inventory.user.id")
    InventoryResponse toResponse(Inventory inventory);

    List<InventoryResponse> toResponseList(List<Inventory> inventories);
}