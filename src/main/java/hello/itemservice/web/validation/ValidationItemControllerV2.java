package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    //@RequiredArgsConstructor 롬복으로 자동 생성자 생성 생성자1개일때 autowired생략돼도 가능 즉,@RequiredArgsConstructor하나로 자동으로 생성자의존주입
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    //요청이올때 맵핑 되기전에 실행된다
    //요청이올때 마다 WebDataBinder는 새로 만들어진다
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 오류 결과를 보관
        //검증로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item","itemName","상품이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item","price","가격은 1,000원~1,000,000까지 허용합니다."));

        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item","quantity", "수량은 최대 9,999개까지 허용합니다."));

        }
        //특정 필드가 아닌 보합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격*수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));

            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}",bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 오류 결과를 보관
        //검증로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,null,null,"가격은 1,000원~1,000,000까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,null,null, "수량은 최대 9,999개까지 허용합니다."));

        }
        //특정 필드가 아닌 보합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", null, null, "가격*수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));

            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}",bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}",bindingResult.getObjectName());
        log.info("target={} (Item클래스의 롬복 @Data가 toString을 오버라이딩 해줘서 해당 결과값이 나온다.)",bindingResult.getTarget());//



        //검증 오류 결과를 보관
        //검증로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null,null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));

        }
        //특정 필드가 아닌 보합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000,resultPrice},null));

            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}",bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}",bindingResult.getObjectName());
        log.info("target={} (Item클래스의 롬복 @Data가 toString을 오버라이딩 해줘서 해당 결과값이 나온다.)",bindingResult.getTarget());//

//        if (bindingResult.hasErrors()) {
//
//            return "validation/v2/addForm";
//        }



        //검증 오류 결과를 보관
        //검증로직
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName","required");
        if (!StringUtils.hasText(item.getItemName())) {
            log.info(item.getItemName());
            //https://velog.io/@jisk0228/JAVAisBlank-isEmpty-hasText-%EB%B9%84%EA%B5%90%ED%95%98%EA%B8%B0


//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null,null));
            bindingResult.rejectValue("itemName", "required");
        }





        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) { //item.getPrice() == null없으면 nullPointException 뜬다
            //StringUtils.hasText(item.getItemName(Integer.parseInt(item.getPrice))-> nullPointException이 뜨는데 parseInt의 매개변수인 item.getPrice가 애초에 null값이라서 문자변환 자체가 안된다.

            //bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));

            if (item.getPrice() == null) {
                bindingResult.rejectValue("price", "required");
                return "validation/v2/addForm";
            }
            if (item.getPrice() < 1000 || item.getPrice() > 1000000)
                bindingResult.rejectValue("price","range",new Object[]{1000,1000000},null);

        }




        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
            bindingResult.rejectValue("quantity","max",new Object[]{9999}, null);

        }

        //특정 필드가 아닌 보합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000,resultPrice},null));

                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);

            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}",bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    
    
//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //***
        if (itemValidator.supports(item.getClass())) {
            itemValidator.validate(item,bindingResult);
        }
        //***

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}",bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    /*
    @Validated 는 검증기를 실행하라는 애노테이션이다.
    이 애노테이션이 붙으면 앞서 WebDataBinder 에 등록한 검증기(addValidators함수를 사용해서 등록한 validator ex)ItemValidator)를 찾아서(이때 supports() 가 사용된다.) 실행한다.(validate())
    그런데 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 이때 supports() 가 사용된다.

    ex) if (itemValidator.supports(item.getClass())) {
            itemValidator.validate(item,bindingResult);
        }
    자동화 해준다

    */
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}",bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

