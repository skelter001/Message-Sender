package com.xaghoul.demo.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.service.impl.MessageTemplateModelAssembler;
import com.xaghoul.demo.service.impl.ScheduledMessageModelAssembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageTemplateControllerTest {

    @Autowired
    MessageTemplateModelAssembler templateModelAssembler;
    @Autowired
    ScheduledMessageModelAssembler messageModelAssembler;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getByIdTest() throws Exception {
        MessageTemplate template1 = new MessageTemplate(
                UUID.randomUUID(), "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));

        this.mockMvc.perform(post("/template/add_template").param(template1.toString()))
                .andDo(print())
                .andExpect(status().isCreated());

        this.mockMvc.perform(get("/template/" + template1.getId() ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(template1.toString())));
    }

    @Test
    void getAllTemplatesTest() throws Exception {
        MessageTemplate template1 = new MessageTemplate(
                UUID.randomUUID(), "template1", "Template $test$ message",
                Arrays.asList(new URL("https://httpbin.org/post"),
                        new URL("https://postman-echo.com/post")));
        MessageTemplate template2 = new MessageTemplate(
                UUID.randomUUID(), "$$$", "Today is $year$ year",
                Collections.singletonList(new URL("https://httpbin.org/post")));

        mockMvc.perform(post("/template/add_template")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(template1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/template/add_template")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(template2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/template/"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getAllMessages() {
    }

    @Test
    void addTemplateTest() {
    }

    @Test
    void sendMessageTest() {
    }

    @Test
    void cancelSendingMessageTest() {
    }

    @Test
    void updateTemplateTest() {
    }

    @Test
    void deleteTemplateTest() {
    }
}