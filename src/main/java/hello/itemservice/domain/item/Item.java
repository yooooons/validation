package hello.itemservice.domain.item;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NonNull //set프로퍼티를 사용 할때 null값이 못들어가게 해준다
    @Range(min = 1000,max = 1000000)
    private Integer price;

    @NonNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
