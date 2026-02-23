import java.util.*;

public class OnboardingService {
    private final StudentRepository repository;
    private final InputParser parser;
    private final Validator validator;
    private final DetailsPrinter printer;

    public OnboardingService(StudentRepository repository, InputParser parser, Validator validator, DetailsPrinter printer) {
        this.repository = repository;
        this.parser = parser;
        this.validator = validator;
        this.printer = printer;
    }

    // Intentionally violates SRP: parses + validates + creates ID + saves + prints.
    public void registerFromRawInput(String raw) {
        printer.printInput(raw);

        Map<String, String> kv = parser.parse(raw);

        List<String> errors = validator.validate(kv);
        if (!errors.isEmpty()) {
            printer.printErrors(errors);
            return;
        }

        String id = IdUtil.nextStudentId(repository.count());

        StudentRecord rec = new StudentRecord(
                id,
                kv.get("name"),
                kv.get("email"),
                kv.get("phone"),
                kv.get("program")
        );

        repository.save(rec);

        printer.printConfirmation(rec, repository.count());
    }
}
