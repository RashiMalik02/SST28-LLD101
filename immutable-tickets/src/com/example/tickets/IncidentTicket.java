package com.example.tickets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * INTENTION: A ticket should be an immutable record-like object.
 *
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - mutable fields
 * - multiple constructors
 * - public setters
 * - tags list can be modified from outside
 * - validation is scattered elsewhere
 *
 * TODO (student): refactor to immutable + Builder.
 */
public class IncidentTicket {

    private String id;
    private String reporterEmail;
    private String title;

    private String description;
    private String priority;       // LOW, MEDIUM, HIGH, CRITICAL
    private List<String> tags;     // mutable leak
    private String assigneeEmail;
    private boolean customerVisible;
    private Integer slaMinutes;    // optional
    private String source;         // e.g. "CLI", "WEBHOOK", "EMAIL"


    private IncidentTicket(Builder b) {
        this.id = b.id;
        this.reporterEmail = b.reporterEmail;
        this.title = b.title;
        this.description = b.description;
        this.priority = b.priority;
        this.assigneeEmail = b.assigneeEmail;
        this.customerVisible = b.customerVisible;
        this.slaMinutes = b.slaMinutes;
        this.source = b.source;
        this.tags = Collections.unmodifiableList(new ArrayList<>(b.tags));
    }


    // Getters
    public String getId() { return id; }
    public String getReporterEmail() { return reporterEmail; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public List<String> getTags() { return tags; } // BROKEN: leaks internal list
    public String getAssigneeEmail() { return assigneeEmail; }
    public boolean isCustomerVisible() { return customerVisible; }
    public Integer getSlaMinutes() { return slaMinutes; }
    public String getSource() { return source; }

    // Setters (BROKEN: should not exist after refactor)
    public void setId(String id) { this.id = id; }
    public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setTags(List<String> tags) { this.tags = tags; } // BROKEN: retains external reference
    public void setAssigneeEmail(String assigneeEmail) { this.assigneeEmail = assigneeEmail; }
    public void setCustomerVisible(boolean customerVisible) { this.customerVisible = customerVisible; }
    public void setSlaMinutes(Integer slaMinutes) { this.slaMinutes = slaMinutes; }
    public void setSource(String source) { this.source = source; }

    public Builder toBuilder() {
        return Builder.from(this);
    }

    public static Builder builder(String id, String reporterEmail, String title) {
        return new Builder(id, reporterEmail, title);
    }

    public static final class Builder {

        private final String id;
        private final String reporterEmail;
        private final String title;

        private String description;
        private String priority = "MEDIUM";
        private List<String> tags = new ArrayList<>();
        private String assigneeEmail;
        private boolean customerVisible = false;
        private Integer slaMinutes;
        private String source = "CLI";

        private Builder(String id, String reporterEmail, String title) {
            this.id = id;
            this.reporterEmail = reporterEmail;
            this.title = title;
        }
        public static Builder from(IncidentTicket t) {
            Builder b = new Builder(t.id, t.reporterEmail, t.title);
            b.description = t.description;
            b.priority = t.priority;
            b.tags = new ArrayList<>(t.tags);
            b.assigneeEmail = t.assigneeEmail;
            b.customerVisible = t.customerVisible;
            b.slaMinutes = t.slaMinutes;
            b.source = t.source;
            return b;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }
        public Builder assigneeEmail(String assigneeEmail) {
            this.assigneeEmail = assigneeEmail;
            return this;
        }

        public Builder customerVisible(boolean customerVisible) {
            this.customerVisible = customerVisible;
            return this;
        }

        public Builder slaMinutes(Integer slaMinutes) {
            this.slaMinutes = slaMinutes;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public IncidentTicket build() {

            Validation.requireTicketId(id);
            Validation.requireEmail(reporterEmail, "reporterEmail");

            Validation.requireNonBlank(title, "title");
            Validation.requireMaxLen(title, 80, "title");

            Validation.requireOneOf(priority, "priority",
                    "LOW", "MEDIUM", "HIGH", "CRITICAL");

            if (assigneeEmail != null) {
                Validation.requireEmail(assigneeEmail, "assigneeEmail");
            }

            Validation.requireRange(slaMinutes, 5, 7200, "slaMinutes");
            return new IncidentTicket(this);
        }
    }


    @Override
    public String toString() {
        return "IncidentTicket{" +
                "id='" + id + '\'' +
                ", reporterEmail='" + reporterEmail + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", tags=" + tags +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", customerVisible=" + customerVisible +
                ", slaMinutes=" + slaMinutes +
                ", source='" + source + '\'' +
                '}';
    }
}
