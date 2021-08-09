package org.training360.finalexam.teams;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.training360.finalexam.players.CreatePlayerCommand;
import org.training360.finalexam.players.Player;
import org.training360.finalexam.players.PlayersRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamsService {

    private TeamsRepository teamsRepository;

    private PlayersRepository playersRepository;

    private ModelMapper modelMapper;

    public List<TeamDto> getAllTeams() {
        return teamsRepository.findAll()
                .stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    public TeamDto createTeam(CreateTeamCommand command) {
        Team team = new Team(command.getName());
        return modelMapper.map(teamsRepository.save(team), TeamDto.class);
    }

    public TeamDto addNewPlayer(Long id, CreatePlayerCommand command) {
        Team team = teamsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find team"));
        Player player = new Player(command.getName(), command.getBirthDate(), command.getPosition());
        team.addPlayer(player);
        return modelMapper.map(team, TeamDto.class);
    }

    public TeamDto addExistingPlayerToTeam(UpdateWithExistingPlayerCommand command, Long id) {
        Team team = teamsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find team"));
        Player player = playersRepository.findById(command.getPlayerId()).orElseThrow(() -> new IllegalArgumentException("Cannot find player"));
        List<Player> players = team.getPlayers();
        long count = players.stream().filter(player1 -> player1.getPosition() == player.getPosition()).count();
        if (count < 2 && player.getTeam() == null){
            team.addPlayer(player);
        }
        teamsRepository.save(team);
        return modelMapper.map(team, TeamDto.class);
    }
}
