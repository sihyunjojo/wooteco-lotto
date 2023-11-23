package lotto.view;

import java.util.List;
import lotto.message.MessageProvider;

public class OutputView {
    private OutputView() {
    }

    public static void printMessage(MessageProvider message) {
        System.out.println(message.getMessage());
    }
    public static <T> void print(T message){
        System.out.println(message);
    }
    public static <T> void printWithBlankLine(T message) {
        System.out.println(message);
        printMessageBlankLine();
    }
    public static <T> void printMessagesWithBlankLine(List<T> messages) {
        messages.forEach(System.out::println);
        printMessageBlankLine();
    }
    public static void printMessageBlankLine() {
        System.out.println();
    }
}
