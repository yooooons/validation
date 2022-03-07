package hello.itemservice.domain.item;


import lombok.Data;
import lombok.NonNull;

@Data
public class NonNullTest {
    @NonNull
    private Integer a;


    public NonNullTest() {
    }

    public NonNullTest(@NonNull Integer a) {
        this.a = a;
    }
}
