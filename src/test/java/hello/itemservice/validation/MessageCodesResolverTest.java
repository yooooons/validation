package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void rejectValue구조파악하기 () {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);

            //bindingResult.rejectValue("itemName", "required");
            //rejectValue에서  codesResolver를 호출
            /*반환)messageCode = required.item.itemName
            messageCode = required.itemName
            messageCode = required.java.lang.String (타입)
            messageCode = required*/
            // -> new FieldError를 만든다
            //new FieldError("item", "itemName", null, false, messageCodes, null,null);


            assertThat(messageCodes).containsExactly(
                    "required.item.itemName",
                    "required.itemName",
                    "required.java.lang.String",
                    "required"
            );

        }
    }
}
