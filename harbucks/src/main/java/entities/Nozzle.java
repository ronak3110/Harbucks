package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Nozzle {

    @JsonProperty("count_n")
    int count;
}
