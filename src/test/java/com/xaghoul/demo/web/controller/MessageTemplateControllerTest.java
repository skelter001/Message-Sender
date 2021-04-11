package com.xaghoul.demo.web.controller;

import com.xaghoul.demo.model.MessageTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageTemplateController controller;

    @Test
    void getById() throws Exception {
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
    void getAllTemplates() {
    }

    @Test
    void getAllMessages() {
    }

    @Test
    void addTemplate() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void cancelSendingMessage() {
    }

    @Test
    void updateTemplate() {
    }

    @Test
    void deleteTemplate() {
    }
}