package org.training360.finalexam.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training360.finalexam.players.PlayerDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {

    private Long id;

    private String name;

    private List<PlayerDto> players;

}
