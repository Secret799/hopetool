package statistics;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生对象
 *
 * @author JunPzx
 * @since 2023/12/21 11:31
 */
@Data
@Accessors(chain = true)
public class Student {

    private String name;

    private String sex;

    private int age;

    private LocalDateTime birthday;

    private BigDecimal totalScore;

}
