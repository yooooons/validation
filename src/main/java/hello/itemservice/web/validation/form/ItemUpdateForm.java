package hello.itemservice.web.validation.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//컨트롤러 팩키지에서 관리이유
//화면과 웹에 특화된 기술
//따라서 controller level까지만 사용
@Data
public class ItemUpdateForm {



    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000,max = 1000000)
    private Integer price;

    //수정에서는 수량을 자유롭게 변경할 수 있다
    private Integer quantity;
}
