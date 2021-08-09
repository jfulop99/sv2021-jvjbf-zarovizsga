package org.training360.finalexam.players;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerCommand {

    @NotNull
    @NotBlank
    private String name;
    private LocalDate birthDate;
    private PositionType position;

    public CreatePlayerCommand(String name) {
        this.name = name;
    }
}
