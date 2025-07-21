package llf.llf.pojo;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private String bio;            // 个人简介
    private String website;        // 个人网站
    private String location;       // 所在地
    private Integer status;        // 状态 1-正常 0-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
