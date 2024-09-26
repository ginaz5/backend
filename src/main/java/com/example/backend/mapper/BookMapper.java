package com.example.backend.mapper;

import com.example.backend.controller.response.BookResponse;
import com.example.backend.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "id", source = "book.id")
    @Mapping(target = "author", source = "book.author")
    @Mapping(target = "title", source = "book.title")
    @Mapping(target = "image", source = "book.image")
    BookResponse toResponse(Book book);
    List<BookResponse> toResponseList(List<Book> books);
}