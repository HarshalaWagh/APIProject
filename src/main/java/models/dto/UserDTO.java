package models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static constants.UserFieldConstants.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonProperty(ID)
    private Integer id;

    @JsonProperty(NAME)
    private String name;

    @JsonProperty(EMAIL)
    private String email;

    @JsonProperty(GENDER)
    private String gender;

    @JsonProperty(STATUS)
    private String status;
}
