package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Post {
    private Integer id;
    private Integer userId;        // 作者ID
    private Integer categoryId;    // 分类ID
    private String title;          // 标题
    private String content;        // 内容
    private String summary;        // 摘要
    private String cover;          // 封面图
    private Integer status;        // 状态 1-发布 0-草稿
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 关联对象（用于查询时返回）
    private User author;           // 作者信息
    private Category category;     // 分类信息
    private List<Tag> tags;        // 标签列表
}
