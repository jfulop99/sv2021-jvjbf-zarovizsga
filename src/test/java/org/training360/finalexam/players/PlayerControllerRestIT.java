package org.training360.finalexam.players;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from players"})
public class PlayerControllerRestIT {

    @Autowired
    TestRestTemplate template;


    @Test
    void testAddNewPlayers(){
        PlayerDto result =
                template.postForObject("/api/players",
                        new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                                PlayerDto.class);


        assertEquals("John Doe",result.getName());
        assertEquals(1991,result.getBirthDate().getYear());
        assertEquals(PositionType.CENTER_BACK,result.getPosition());
    }

    @Test
    void testGetPlayers(){
        template.postForObject("/api/players",
                new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                PlayerDto.class);

        template.postForObject("/api/players",
                new CreatePlayerCommand("Jack Doe", LocalDate.of(1992,11,10),PositionType.RIGHT_WINGER),
                PlayerDto.class);

        List<PlayerDto> result = template.exchange(
                "/api/players",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlayerDto>>() {
                }
        ).getBody();


        assertThat(result).extracting(PlayerDto::getName)
                .containsExactly("John Doe","Jack Doe");
    }

    @Test
    void deletePlayerById(){
        PlayerDto result =template.postForObject("/api/players",
                new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                PlayerDto.class);


        template.delete("/api/players/{id}", result.getId());

        List<PlayerDto> players = template.exchange(
                "/api/players",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlayerDto>>() {
                }
        ).getBody();


        assertThat(players).isEmpty();

    }

    @Test
    void testCreatePlayerWithInvalidName(){
        Problem result =
                template.postForObject("/api/players",
                        new CreatePlayerCommand(""),
                        Problem.class);

        assertEquals(Status.BAD_REQUEST,result.getStatus());
    }




}
