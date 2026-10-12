import core.StackOverflowService;
import entities.Answer;
import entities.Question;
import entities.Tag;
import entities.User;
import enums.VoteType;
import strategy.searching.SearchStrategy;
import strategy.searching.TagSearchStrategy;
import strategy.searching.UserSearchStrategy;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        StackOverflowService service = new StackOverflowService();

        User alice = service.createUser("Alice");
        User bob = service.createUser("Bob");
        User charlie = service.createUser("Charlie");

        System.out.println("--- Alice posts a question ---");
        Tag javaTag = new Tag("java");
        Tag designPatternsTag = new Tag("design-patterns");
        Set<Tag> tags = Set.of(javaTag, designPatternsTag);
        Question question = service.postQuestion(alice.getId(), "How to implement Observer Pattern?", "Details about Observer Pattern...", tags);
        printReputations(alice, bob, charlie);

        System.out.println("\n--- Bob and Charlie post answers ---");
        Answer bobAnswer = service.postAnswer(bob.getId(), question.getId(), "You can use the java.util.Observer interface.");
        Answer charlieAnswer = service.postAnswer(charlie.getId(), question.getId(), "A better way is to create your own Observer interface.");
        printReputations(alice, bob, charlie);

        System.out.println("\n--- Voting Occurs ---");
        service.voteOnPost(bob.getId(), question.getId(), VoteType.UPVOTE);
        service.voteOnPost(bob.getId(), charlieAnswer.getId(), VoteType.UPVOTE);
        service.voteOnPost(alice.getId(), bobAnswer.getId(), VoteType.DOWNVOTE);
        printReputations(alice, bob, charlie);

        System.out.println("\n--- Comments ---");
        service.addComment(bob.getId(), question.getId(), "Can you share what you have tried?");
        service.addComment(alice.getId(), charlieAnswer.getId(), "Glad this worked for you!");
        System.out.println("Question has " + question.getComments().size() + " comment(s)");
        System.out.println("Accepted answer has " + charlieAnswer.getComments().size() + " comment(s)");

        System.out.println("\n--- Alice accepts Charlie's answer ---");
        service.acceptAnswer(question.getId(), charlieAnswer.getId());
        printReputations(alice, bob, charlie);

        System.out.println("\n--- (C) Combined Search: Questions by 'Alice' with tag 'java' ---");
        List<SearchStrategy> filtersC = List.of(
                new UserSearchStrategy(alice),
                new TagSearchStrategy(javaTag)
        );
        List<Question> searchResults = service.searchQuestions(filtersC);
        searchResults.forEach(q -> System.out.println("  - Found: " + q.getTitle()));
    }

    private static void printReputations(User... users) {
        System.out.println("--- Current Reputations ---");
        for(User user : users) {
            System.out.printf("%s: %d\n", user.getName(), user.getReputation());
        }
    }
}