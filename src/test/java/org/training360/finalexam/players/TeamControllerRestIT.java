package org.training360.finalexam.players;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.training360.finalexam.teams.CreateTeamCommand;
import org.training360.finalexam.teams.TeamDto;
import org.training360.finalexam.teams.UpdateWithExistingPlayerCommand;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from players","delete from teams"})
public class TeamControllerRestIT {

    @Autowired
    TestRestTemplate template;


    @Test
    void testCreateNewTeam(){
        TeamDto result =
                template.postForObject("/api/teams",
                        new CreateTeamCommand("Arsenal"),
                        TeamDto.class);

        assertEquals("Arsenal",result.getName());

    }


    @Test
    void testGetTeams(){
        template.postForObject("/api/teams",
                new CreateTeamCommand("Arsenal"),
                TeamDto.class);

        template.postForObject("/api/teams",
                new CreateTeamCommand("Chelsea"),
                TeamDto.class);

        List<TeamDto> result = template.exchange(
                "/api/teams",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TeamDto>>() {
                }
        ).getBody();

        assertThat(result).extracting(TeamDto::getName)
                .containsExactly("Arsenal","Chelsea");
    }


    @Test
    void testAddNewPlayerToExistingTeam(){
        TeamDto team =
                template.postForObject("/api/teams",
                        new CreateTeamCommand("Arsenal"),
                        TeamDto.class);

        TeamDto resultWithPlayer = template.postForObject("/api/teams/{id}/players",
                new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                TeamDto.class,
                team.getId());

        assertThat(resultWithPlayer.getPlayers()).extracting(PlayerDto::getName)
                .containsExactly("John Doe");

    }

    @Test
    void testAddExistingPlayerToExistingTeam(){
        TeamDto team =
                template.postForObject("/api/teams",
                        new CreateTeamCommand("Arsenal"),
                        TeamDto.class);

        PlayerDto player =
                template.postForObject("/api/players",
                        new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                        PlayerDto.class);

        template.put("/api/teams/{id}/players", new UpdateWithExistingPlayerCommand(player.getId()), team.getId());


        List<TeamDto> result = template.exchange(
                "/api/teams",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TeamDto>>() {
                }
        ).getBody();

        assertThat(result.get(0).getPlayers()).extracting(PlayerDto::getName)
                .containsExactly("John Doe");
    }


    @Test
    void testAddExistingPlayerWithTeam(){
        TeamDto team1 =
                template.postForObject("/api/teams",
                        new CreateTeamCommand("Arsenal"),
                        TeamDto.class);

        TeamDto team2 =
                template.postForObject("/api/teams",
                        new CreateTeamCommand("Chelsea"),
                        TeamDto.class);

        PlayerDto player =
                template.postForObject("/api/players",
                        new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                        PlayerDto.class);

        template.put("/api/teams/{id}/players", new UpdateWithExistingPlayerCommand(player.getId()), team1.getId());

        template.put("/api/teams/{id}/players", new UpdateWithExistingPlayerCommand(player.getId()), team2.getId());

        List<TeamDto> result = template.exchange(
                "/api/teams",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TeamDto>>() {
                }
        ).getBody();

        TeamDto resultTeam1 = result.stream().filter(t->t.getName().equals("Arsenal")).findFirst().orElseThrow();
        TeamDto resultTeam2 = result.stream().filter(t->t.getName().equals("Chelsea")).findFirst().orElseThrow();

        assertThat(resultTeam1.getPlayers()).extracting(PlayerDto::getName)
                .containsExactly("John Doe");

        assertThat(resultTeam2.getPlayers()).isEmpty();

    }

    @Test
    void testAddPlayerWithPosition(){
        TeamDto team1 =
                template.postForObject("/api/teams",
                        new CreateTeamCommand("Arsenal"),
                        TeamDto.class);

        PlayerDto player =
                template.postForObject("/api/players",
                        new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                        PlayerDto.class);

        PlayerDto player2 =
                template.postForObject("/api/players",
                        new CreatePlayerCommand("Jack Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                        PlayerDto.class);

        PlayerDto player3 =
                template.postForObject("/api/players",
                        new CreatePlayerCommand("Jill Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                        PlayerDto.class);

        template.put("/api/teams/{id}/players", new UpdateWithExistingPlayerCommand(player.getId()), team1.getId());
        template.put("/api/teams/{id}/players", new UpdateWithExistingPlayerCommand(player2.getId()), team1.getId());
        template.put("/api/teams/{id}/players", new UpdateWithExistingPlayerCommand(player3.getId()), team1.getId());

        List<TeamDto> result = template.exchange(
                "/api/teams",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TeamDto>>() {
                }
        ).getBody();

        assertThat(result.get(0).getPlayers()).extracting(PlayerDto::getName)
                .containsOnly("John Doe","Jack Doe");

    }

    @Test
    void testAddPlayerToNotExistingTeam(){
        Long wrongId = 6666L;

       Problem result = template.postForObject("/api/teams/"+wrongId+"/players",
                new CreatePlayerCommand("John Doe", LocalDate.of(1991,11,10),PositionType.CENTER_BACK),
                Problem.class);

       assertEquals(URI.create("teams/not-found"),result.getType());
       assertEquals(Status.NOT_FOUND,result.getStatus());
    }

    @Test
    void testCreateTeamWithInvalidName(){
        Problem result =
                template.postForObject("/api/teams",
                        new CreateTeamCommand(""),
                        Problem.class);

        assertEquals(Status.BAD_REQUEST,result.getStatus());
    }
}
