import java.util.*;


public interface  InvoiceRepository {
    void save(String id, String content);
    int countLines(String id);
}
