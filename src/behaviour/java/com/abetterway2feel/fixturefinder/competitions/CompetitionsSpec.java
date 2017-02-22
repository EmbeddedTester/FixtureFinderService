package com.abetterway2feel.fixturefinder.competitions;

import com.abetterway2feel.fixturefinder.FixtureFinder;
import com.abetterway2feel.fixturefinder.repository.CompetitionRepository;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;

@SuppressWarnings("WeakerAccess") //Spring injections means intellij can't deal with this
@ActiveProfiles("behaviour-test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FixtureFinder.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CompetitionsSpec {

    @LocalServerPort
    int port;

    @Autowired
    CompetitionRepository competitionRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }


    //TODO: what do we want to achieve from this check
    @Test
    public void theCompetitionsEndpointReturnsAllCompetitions() {
        when()
                .get("/competitions")
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(3));
    }

}