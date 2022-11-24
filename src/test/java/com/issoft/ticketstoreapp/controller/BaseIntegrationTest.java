package com.issoft.ticketstoreapp.controller;

import com.google.gson.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
public class BaseIntegrationTest {

    protected static final String ID = "id",
            TICKETS_URL = "/api/v1/tickets/",
            CINEMAS_URL = "/api/v1/cinemas/",
            MOVIES_URL = "/api/v1/movies/",
            HALLS_URL = "/api/v1/halls/",
            ROWS_URL = "/api/v1/rows/",
            SEATS_URL = "/api/v1/seats/",
            USERS_URL = "/api/v1/users/";

    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext context;

    protected Gson jsonParser;

    @BeforeEach
    void integrationUtilSetUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        this.jsonParser = new Gson();
    }

    @AfterEach
    void integrationUtilTearDown() {
        this.jsonParser = null;
    }

    protected long getGeneratedId(MvcResult result) throws UnsupportedEncodingException {

        String responseBody = result.getResponse().getContentAsString();
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.get(ID).getAsLong();
    }

    protected List<Long> getGeneratedIds(MvcResult result) throws UnsupportedEncodingException {

        Gson jsonParser = new Gson();

        List<Long> generatedIds = new ArrayList<>();
        String responseBody = result.getResponse().getContentAsString();
        JsonElement jsonElement = jsonParser.fromJson(responseBody, JsonElement.class);
        JsonArray objects = jsonElement.getAsJsonArray();
        for (JsonElement element : objects) {
            JsonObject object = element.getAsJsonObject();
            generatedIds.add(object.get(ID).getAsLong());
        }
        return generatedIds;
    }
}