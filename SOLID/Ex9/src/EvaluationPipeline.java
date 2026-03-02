public class EvaluationPipeline {

    private final PlagiarismService plagiarism;
    private final GradingService grader;
    private final ReportService writer;

    public EvaluationPipeline(
            PlagiarismService plagiarism,
            GradingService grader,
            ReportService writer) {

        this.plagiarism = plagiarism;
        this.grader = grader;
        this.writer = writer;
    }

    public void evaluate(Submission sub) {

        int plag = plagiarism.check(sub);
        System.out.println("PlagiarismScore=" + plag);

        int code = grader.grade(sub);
        System.out.println("CodeScore=" + code);

        String reportName = writer.write(sub, plag, code);
        System.out.println("Report written: " + reportName);

        int total = plag + code;
        String result = (total >= 90) ? "PASS" : "FAIL";
        System.out.println("FINAL: " + result + " (total=" + total + ")");
    }
}