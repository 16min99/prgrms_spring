package org.prgrms.kdt.voucher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FixedAmountVoucherTest { //SUT
    // 테스트 메소드는 항상 void형
    //

    @Test
    @DisplayName("기본적인 assertEqual 테스트 예")
    void testAssertEqual() {//MUT
        assertEquals(2,1+1);//기대값, 판별대상
    }

    @Test
    @DisplayName("주어진 금액만큼 할인을 해야한다")
    void testDiscount() {
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 100);
        assertEquals(900,sut.discount(1000));//테스트 확인
    }

    @Test
    @DisplayName("할인 금액은 마이너스가 될 수 없다.")
    void testWithMinus(){
        //assertThrows( );//예외가 발생할 때 예외 클래스
        var sut = new FixedAmountVoucher(UUID.randomUUID(),-100);
    }

}