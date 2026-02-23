import java.util.*;

public interface  EligibilityRule {
    Optional<String> check(StudentProfile sp);
}
