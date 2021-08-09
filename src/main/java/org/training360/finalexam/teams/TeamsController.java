package org.training360.finalexam.teams;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.training360.finalexam.players.CreatePlayerCommand;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class TeamsController {

    private TeamsService teamsService;

    @GetMapping
    public List<TeamDto> getAllTeams(){
        return teamsService.getAllTeams();
    }

    @PostMapping
    public TeamDto createTeam(@Valid @RequestBody CreateTeamCommand command){
        return teamsService.createTeam(command);
    }

    @PostMapping("/{id}/players")
    public TeamDto addNewPlayer(@PathVariable Long id, @Valid @RequestBody CreatePlayerCommand command){
        return teamsService.addNewPlayer(id, command);
    }

    @PutMapping ("/{id}/players")
    TeamDto addExistingPlayerToTeam(@RequestBody UpdateWithExistingPlayerCommand command, @PathVariable Long id ){
        return teamsService.addExistingPlayerToTeam(command, id);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Problem> handleNotFound(TeamNotFoundException e) {

        Problem problem = Problem.builder()
                .withType(URI.create("teams/not-found"))
                .withTitle("Not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

}
