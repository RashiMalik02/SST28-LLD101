import java.util.*;


public class CreditsRule implements EligibilityRule {
    private final int minCredits;

    public CreditsRule(int minCredits) {
        this.minCredits = minCredits;
    }

    @Override
    public Optional<String> check(StudentProfile sp) {
        if (sp.earnedCredits < minCredits) {
            return Optional.of("credits below " + minCredits);
        }
        return Optional.empty();
    }
    
}
