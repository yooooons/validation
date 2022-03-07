package hello;

import hello.itemservice.domain.item.NonNullTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class NonNullTestTest {

    @Test
    void nonNull테스트하기() {
        NonNullTest nonNullTest = new NonNullTest();
        System.out.println("nonNullTest.getA() = " + nonNullTest.getA());
        Assertions.assertThrows(NullPointerException.class, () -> nonNullTest.setA(null));
        
        Assertions.assertThrows(NullPointerException.class, () ->new NonNullTest(null));

    }

}
