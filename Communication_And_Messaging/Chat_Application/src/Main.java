import core.Chat;
import core.ChatService;
import entities.Message;
import entities.User;

import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ChatService chatService = new ChatService();

        User alice = chatService.createUser("Alice");
        User bob = chatService.createUser("Bob");
        User charlie = chatService.createUser("Charlie");

        System.out.println("--- Users registered in the system ---");
        System.out.println();

        System.out.println("--- Starting one-on-one chat between Alice and Bob ---");
        Chat aliceBobChat = chatService.createOneToOneChat(alice.getId(), bob.getId());

        System.out.println("Alice sends a message...");
        chatService.sendMessage(alice.getId(), aliceBobChat.getId(), "Hi Bob, how are you?");

        System.out.println("\nBob sends a reply...");
        chatService.sendMessage(bob.getId(), aliceBobChat.getId(), "I'm good, Alice! Thanks for asking.");
        System.out.println();

        System.out.println("--- Starting a group chat for a 'Project Team' ---");
        List<String> projectMembers = List.of(alice.getId(), bob.getId(), charlie.getId());
        Chat projectGroup = chatService.createGroupChat("Project Team", projectMembers);

        System.out.println("Charlie sends a message to the group...");
        chatService.sendMessage(charlie.getId(), projectGroup.getId(), "Hey team, when is our deadline?");

        System.out.println("\nAlice replies to the group...");
        chatService.sendMessage(alice.getId(), projectGroup.getId(), "It's next Friday. Let's sync up tomorrow.");
        System.out.println();

        System.out.println("--- Fetching Chat Histories ---");

        System.out.println("\nHistory for chat '" + aliceBobChat.getName(alice) + "':");
        List<Message> oneToOneHistory = chatService.printChatHistory(aliceBobChat.getId());
        oneToOneHistory.forEach(message -> System.out.println(message.getContent()));

        System.out.println("\nHistory for chat '" + projectGroup.getName(charlie) + "':");
        List<Message> groupHistory = chatService.printChatHistory(projectGroup.getId());
        groupHistory.forEach(message -> System.out.println(message.getContent()));

        System.out.println("\n--- Fetching all of Alice's chats ---");
        List<Chat> aliceChats = chatService.getUserChats(alice.getId());
        aliceChats.sort(Comparator.comparing(c -> c.getName(alice)));
        for(Chat chat : aliceChats) {
            System.out.println("Chat: " + chat.getName(alice));
        }
    }
}