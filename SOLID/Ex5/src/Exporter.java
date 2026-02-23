public abstract class Exporter {
    // implied "contract" but not enforced (smell)
    public final  ExportResult export(ExportRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("ExportRequest cannot be null");
        }
        return doExport(req);
    }
    protected abstract ExportResult doExport(ExportRequest req);
}
