import java.util.*;

public class AttendanceRule implements EligibilityRule {
    private final int minAttendance;

    public AttendanceRule(int minAttendance) {
        this.minAttendance = minAttendance;
    }

    @Override
    public Optional<String> check(StudentProfile sp) {
        if (sp.attendancePct < minAttendance) {
            return Optional.of("attendance below " + minAttendance);
        }
        return Optional.empty();
    }
  
}
