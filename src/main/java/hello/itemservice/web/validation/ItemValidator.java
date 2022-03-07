package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //item==clazz
        //item==subItem(자식클래스)
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;



//bindingResult 는 errors의 자식 인터페이스

        //검증 오류 결과를 보관
        //검증로직
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName","required");
        if (!StringUtils.hasText(item.getItemName())) {
            log.info(item.getItemName());
            //https://velog.io/@jisk0228/JAVAisBlank-isEmpty-hasText-%EB%B9%84%EA%B5%90%ED%95%98%EA%B8%B0


//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null,null));
            errors.rejectValue("itemName", "required2");
        }





        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) { //item.getPrice() == null없으면 nullPointException 뜬다
            //StringUtils.hasText(item.getItemName(Integer.parseInt(item.getPrice))-> nullPointException이 뜨는데 parseInt의 매개변수인 item.getPrice가 애초에 null값이라서 문자변환 자체가 안된다.

            //bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));

                errors.rejectValue("price", "required2");
                errors.rejectValue("price","range2",new Object[]{1000,1000000},null);

        }





        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
            errors.rejectValue("quantity","max2",new Object[]{9999}, null);

        }
        //특정 필드가 아닌 보합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000,resultPrice},null));

                errors.reject("totalPriceMin",new Object[]{10000,resultPrice},null);

            }
        }



    }
}
