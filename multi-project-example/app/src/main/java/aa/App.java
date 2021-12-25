package aa;

public class App {
    public String getGreeting() {
        return MessageService.generateMessage();
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
