package lotto.handler;

import camp.nextstep.edu.missionutils.Console;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static lotto.message.ErrorMessage.*;
import static lotto.message.SystemMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InputHandlerTest {
    private PrintStream standardOut;
    private OutputStream captor;

    @BeforeEach
    public void init() {
        standardOut = System.out;
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    @AfterEach
    public void printOutput() {
        System.setOut(standardOut);
        System.out.println(output());
    }

    @Test
    @DisplayName("유효하지 않은 로또 구매 가격 입력 시, 반복적으로 입력 메시지를 표시하고 유효한 입력을 반환")
    void testSetLottoPurchasePriceWithInvalidInput() {
        // given
        int retryCount = 3;
        int answerNum = 2000;
        String answer = "2000";

        // when
        run("qwe", "asd", "zxc", answer);
        int lottoPurchasePrice = InputHandler.setLottoPurchasePrice();

        // then
        String inputMessage = INPUT_PURCHASE_PRICE.getMessage();
        String InvalidCountFormatMessage = INPUT_PURCHASE_PRICE.getMessage() + "\n"
                + INVALID_COUNT_FORMAT.getMessage() + "\n";

        assertThat(output()).isEqualTo(
                InvalidCountFormatMessage.repeat(retryCount) +
                        inputMessage
        );
        assertThat(lottoPurchasePrice).isEqualTo(answerNum);
    }

    @Test
    @DisplayName("유효하지 않은 로또 구매 가격 입력 시, 반복적으로 입력 메시지를 표시하고 유효한 입력을 반환2")
    void testSetLottoPurchasePriceWithInvalidInput2() {
        // given
        int answerNum = 2000;
        String answer = "2000";

        // when
        run("qwe", "123", answer);
        int lottoPurchasePrice = InputHandler.setLottoPurchasePrice();

        // then
        String inputMessage = INPUT_PURCHASE_PRICE.getMessage();
        String InvalidCountFormatMessage = INPUT_PURCHASE_PRICE.getMessage() + "\n"
                + INVALID_COUNT_FORMAT.getMessage() + "\n";
        String InvalidPurchaseAmountMessage = INPUT_PURCHASE_PRICE.getMessage() + "\n"
                + INVALID_PURCHASE_AMOUNT_UNIT.getMessage() + "\n";

        assertThat(output()).isEqualTo(
                InvalidCountFormatMessage +
                        InvalidPurchaseAmountMessage +
                        inputMessage
        );
        assertThat(lottoPurchasePrice).isEqualTo(answerNum);
    }
    
    @Test
    @DisplayName("유효하지 않은 당첨 번호 입력 시, 반복적으로 입력 메시지를 표시하고 유효한 입력을 반환")
    void testSetWinningNumsWithInvalidInput() {
        // given
        String answer = "1,2,3,4,5,6";

        // when
        run("1,2,3,4,5,-", "0,1,2,3,4,5", "1,2,3,4,5,46", "1", "1,2,3,4,5,6,7", "1,1,2,3,4,5", answer);
        List<Integer> winningNums = InputHandler.setWinningNums();

        // then
        List<Integer> expectedAnswer = List.of(1, 2, 3, 4, 5, 6);

        String inputMessage = OUTPUT_WINNING_NUMBERS.getMessage();
        String InvalidateIsNumbers = INVALID_COUNT_FORMAT.getMessage() + "\n"
                + inputMessage + "\n";
        String InvalidateInLottoRange = INVALID_WINNING_NUMS_VALUE.getMessage() + "\n"
                + inputMessage + "\n";
        String InvalidateIsLottoSize = INVALID_WINNING_NUMS_COUNT.getMessage() + "\n"
                + inputMessage + "\n";
        String InvalidateUniqueNumbers = INVALID_UNIQUE_FORMAT.getMessage() + "\n"
                + inputMessage;

        assertThat(output()).isEqualTo(inputMessage + "\n"
                + InvalidateIsNumbers
                + InvalidateInLottoRange
                + InvalidateInLottoRange
                + InvalidateIsLottoSize
                + InvalidateIsLottoSize
                + InvalidateUniqueNumbers
        );
        assertThat(winningNums).isEqualTo(expectedAnswer);
    }

    @Test
    @DisplayName("유효하지 않은 보너스 번호 입력 시, 반복적으로 입력 메시지를 표시하고 유효한 입력을 반환2")
    void testSetBonusNumWithInvalidInput() {
        // given
        String answer = "7";
        List<Integer> winnerNums = List.of(1, 2, 3, 4, 5, 6);

        // when
        run("-", "q", "", "0", "46", "1", answer);
        int bonusNum = InputHandler.setBonusNum(winnerNums);

        // then
        int expectedAnswer = 7;

        String inputMessage = OUTPUT_BONUS_NUMBER.getMessage();
        String InvalidateIsNumber = INVALID_COUNT_FORMAT.getMessage() + "\n"
                + inputMessage + "\n";
        String InvalidateInLottoRange = INVALID_WINNING_NUMS_VALUE.getMessage() + "\n"
                + inputMessage + "\n";
        String InvalidateUniqueNumbers = INVALID_UNIQUE_FORMAT.getMessage() + "\n"
                + inputMessage;

        assertThat(output()).isEqualTo(inputMessage + "\n"
                + InvalidateIsNumber
                + InvalidateIsNumber
                + InvalidateIsNumber
                + InvalidateInLottoRange
                + InvalidateInLottoRange
                + InvalidateUniqueNumbers
        );

        assertEquals(bonusNum,expectedAnswer);
    }

    private String output() {
        return captor.toString().trim();
    }

    private void run(final String... args) {
        try {
            command(args);
        } finally {
            Console.close();
        }
    }

    private void command(final String... args) {
        final byte[] buf = String.join("\n", args).getBytes();
        System.setIn(new ByteArrayInputStream(buf));
    }

}