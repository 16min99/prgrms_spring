package com.kdt.lecture.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt.lecture.item.dto.ItemDto;
import com.kdt.lecture.item.dto.ItemType;
import com.kdt.lecture.item.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemService itemService;

    @Test
    void saveItem () throws Exception {
        ItemDto item = ItemDto.builder()
                .type(ItemType.CAR)
                .price(100000)
                .stockQuantity(10)
                .power(100)
                .build();


        this.mockMvc.perform(post("/items")
                .content(objectMapper.writeValueAsString(item))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("item-save",
                        requestFields(
                            fieldWithPath("price").type(JsonFieldType.NUMBER).description("??????"),
                            fieldWithPath("stockQuantity").type(JsonFieldType.NUMBER).description("????????????"),
                            fieldWithPath("type").type(JsonFieldType.STRING).description("??????"),
                            fieldWithPath("chef").type(JsonFieldType.STRING).description("??????-??????").optional(),
                            fieldWithPath("power").type(JsonFieldType.NUMBER).description("?????????-??????").optional(),
                            fieldWithPath("width").type(JsonFieldType.NUMBER).description("??????-??????").optional(),
                            fieldWithPath("height").type(JsonFieldType.NUMBER).description("??????-??????").optional()
                        ),
                        responseFields(
                            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("????????????"),
                            fieldWithPath("data").type(JsonFieldType.NUMBER).description("?????????"),
                            fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("????????????")
                        )
                ));
    }

    @Test
    void getItem () throws Exception {
        // Given
        ItemDto item = ItemDto.builder()
                .type(ItemType.CAR)
                .price(100000)
                .stockQuantity(10)
                .power(100)
                .build();
        Long id = itemService.save(item);

        // When
        this.mockMvc.perform(get("/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("item-get",
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????????"),

                                fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.stockQuantity").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("data.type").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("data.chef").type(JsonFieldType.STRING).description("??????-??????").optional(),
                                fieldWithPath("data.power").type(JsonFieldType.NUMBER).description("?????????-??????").optional(),
                                fieldWithPath("data.width").type(JsonFieldType.NUMBER).description("??????-??????").optional(),
                                fieldWithPath("data.height").type(JsonFieldType.NUMBER).description("??????-??????").optional()
                        )
                ));

    }

}