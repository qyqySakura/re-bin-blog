package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tag {
    private Integer id;
    private String name;        // 标签名
    private Integer postCount;  // 文章数量（用于统计显示）
}
