import java.nio.charset.StandardCharsets;

public class PdfExporter extends Exporter {
    private static final int MAX_BODY_LENGTH = 20;

    @Override
    public ExportResult doExport(ExportRequest req) {
        // LSP fix: returns error result instead of throwing
        String body = req.body == null ? "" : req.body;
        if (body.length() > MAX_BODY_LENGTH) {
            String errorMsg = "PDF cannot handle content > 20 chars";
            return new ExportResult("application/pdf", errorMsg.getBytes(StandardCharsets.UTF_8));
        }
        String fakePdf = "PDF(" + req.title + "):" + body;
        return new ExportResult("application/pdf", fakePdf.getBytes(StandardCharsets.UTF_8));
    }
}