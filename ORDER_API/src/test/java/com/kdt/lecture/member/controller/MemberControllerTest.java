package com.kdt.lecture.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt.lecture.domain.order.MemberRepository;
import com.kdt.lecture.member.dto.MemberDto;
import com.kdt.lecture.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    void saveMember() throws Exception {
        // Given
        MemberDto request = MemberDto.builder()
                .name("kanghonggu")
                .nickName("guppy.kang")
                .age(33)
                .address("seoul")
                .description("--")
                .build();
        String requestString = objectMapper.writeValueAsString(request);

        // When // Then
        this.mockMvc.perform(post("/members")
                .content(requestString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-save",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("??????")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("????????????")
                        )
                ));
    }

    @Test
    void updateMember() throws Exception {
        // Given
        Long memberId = memberService.saveMember(
                MemberDto.builder()
                        .name("kanghonggu")
                        .nickName("guppy.kang")
                        .age(33)
                        .address("seoul")
                        .description("--")
                        .build()
        );

        MemberDto request = MemberDto.builder()
                .name("kanghonggu2")
                .nickName("guppy.kang2")
                .age(43)
                .address("seoul2")
                .description("--2")
                .build();
        String requestString = objectMapper.writeValueAsString(request);

        // When // Then
        this.mockMvc.perform(post("/members/{id}", memberId)
                .content(requestString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-update",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("??????")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("????????????")
                        )
                ));
    }

    @Test
    void getMember() throws Exception {
        // Given
        Long memberId = memberService.saveMember(
                MemberDto.builder()
                        .name("kanghonggu")
                        .nickName("guppy.kang")
                        .age(33)
                        .address("seoul")
                        .description("--")
                        .build()
        );

        // When // Then
        this.mockMvc.perform(get("/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-get",
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????????"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.age").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("??????")
                        )
                ));
    }

}