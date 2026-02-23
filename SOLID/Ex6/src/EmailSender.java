public class EmailSender extends NotificationSender {
    public EmailSender(AuditLog audit) { super(audit); }

    @Override
    protected void doSend(Notification n) {
        System.out.println("EMAIL -> to=" + n.email + " subject=" + n.subject + " body=" + n.body);

    }

    @Override
    protected String auditEntry() {
        return "email sent";
    }
}
