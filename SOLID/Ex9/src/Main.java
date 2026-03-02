public class Main {
    public static void main(String[] args) {

        System.out.println("=== Evaluation Pipeline ===");

        Submission sub =
                new Submission("23BCS1007", "public class A{}", "A.java");

        PlagiarismService plagiarism = new PlagiarismChecker();
        GradingService grader = new CodeGrader(new Rubric());
        ReportService writer = new ReportWriter();

        EvaluationPipeline pipeline =
                new EvaluationPipeline(plagiarism, grader, writer);

        pipeline.evaluate(sub);
    }
}