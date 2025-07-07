package llf.llf.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Integer id;

    private String name;

    private String username
            ;
    private String password;

    private String email;

    private String phone;

    private String avatar;
}
