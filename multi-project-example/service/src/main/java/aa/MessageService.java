package aa;

import java.util.List;
import java.util.Random;

public class MessageService {

    public static String generateMessage() {
        final List<String> messages = List.of("Hello World", "Hallo Welt");
        return messages.get(new Random().nextInt(messages.size()));
    }
}
