package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FriendLink {
    private Integer id;
    private String name;        // 网站名
    private String url;         // 链接
    private String description; // 描述
}
