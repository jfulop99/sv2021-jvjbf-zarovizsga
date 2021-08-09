package org.training360.finalexam.players;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
public class PlayersController {

    private PlayersService playersService;

    @GetMapping
    public List<PlayerDto> getAllPlayers(){
        return playersService.getAllPlayers();
    }

    @PostMapping
    public PlayerDto createPlayer(@Valid @RequestBody CreatePlayerCommand command){
        return playersService.createPlayer(command);
    }

    @DeleteMapping("/{id}")
    public void deletePlayerById(@PathVariable Long id){
        playersService.deletePlayerById(id);
    }

}
