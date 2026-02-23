import java.util.*;

public class DisciplinaryRule implements EligibilityRule {
    @Override
    public Optional<String> check(StudentProfile s) {
        if (s.disciplinaryFlag != LegacyFlags.NONE) {
            return Optional.of("disciplinary flag present");
        }
        return Optional.empty();
    }
}
