package org.training360.finalexam.players;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayersService {

    private PlayersRepository playersRepository;
    private ModelMapper modelMapper;

    public List<PlayerDto> getAllPlayers() {
        return playersRepository.findAll().stream()
                .map(player -> modelMapper.map(player, PlayerDto.class))
                .toList();
    }

    public PlayerDto createPlayer(CreatePlayerCommand command) {

        Player player = new Player(command.getName(), command.getBirthDate(), command.getPosition());

        return modelMapper.map(playersRepository.save(player), PlayerDto.class);
    }

    public void deletePlayerById(Long id) {

        Player player = playersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot fond player"));
        playersRepository.delete(player);

    }
}
