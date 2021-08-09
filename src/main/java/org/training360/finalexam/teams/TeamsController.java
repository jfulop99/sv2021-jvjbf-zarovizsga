package org.training360.finalexam.teams;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.training360.finalexam.players.CreatePlayerCommand;

import javax.validation.Valid;
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
}
