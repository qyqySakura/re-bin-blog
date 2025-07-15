package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Category {
    private Integer id;
    private String name;        // 分类名称
    private String description; // 分类描述
}
