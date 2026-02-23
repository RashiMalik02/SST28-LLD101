import java.util.*;


public class CgrRule implements EligibilityRule {
    private final double minCGR;
    public CgrRule(double minCGR) {
         this.minCGR = minCGR; 
    }

    @Override
    public Optional<String> check(StudentProfile sp) {
        if (sp.cgr < minCGR) {
            return Optional.of("CGR below " + minCGR);
        }
        return Optional.empty();
    }
}
