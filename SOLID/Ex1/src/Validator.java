import java.util.*;

public class Validator {

    private final Set<String> allowedCourse = Set.of("CSE", "AI", "SWE");

    public List<String> validate(Map<String, String> kv) {
        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");

        List<String> errors = new ArrayList<>();
        if (name.isBlank()) errors.add("name is required");
        if (email.isBlank() || !email.contains("@")) errors.add("email is invalid");
        if (phone.isBlank() || !phone.chars().allMatch(Character::isDigit)) errors.add("phone is invalid");
        if (!allowedCourse.contains(program)) {            
            errors.add("program is invalid");
        }
        return errors;
    }    
}
