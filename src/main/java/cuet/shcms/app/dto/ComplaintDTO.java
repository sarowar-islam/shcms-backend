package cuet.shcms.app.dto;

import cuet.shcms.app.entity.Complaint;
import cuet.shcms.app.entity.ComplaintUpdate;

import java.util.List;
import java.util.stream.Collectors;

public class ComplaintDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String room;
    private String title;
    private String description;
    private String category;
    private String urgency;
    private String status;
    private String assignedTo;
    private String submittedAt;
    private String updatedAt;
    private List<ComplaintUpdateDTO> updates;

    public ComplaintDTO() {}

    public static ComplaintDTO fromEntity(Complaint complaint) {
        ComplaintDTO dto = new ComplaintDTO();
        dto.setId(complaint.getComplaintId());
        dto.setStudentId(complaint.getStudentId());
        dto.setStudentName(complaint.getStudentName());
        dto.setRoom(complaint.getRoom());
        dto.setTitle(complaint.getTitle());
        dto.setDescription(complaint.getDescription());
        dto.setCategory(complaint.getCategory().name().toLowerCase());
        dto.setUrgency(complaint.getUrgency().name().toLowerCase());
        dto.setStatus(complaint.getStatus().name().toLowerCase());
        dto.setAssignedTo(complaint.getAssignedTo());
        dto.setSubmittedAt(complaint.getSubmittedAt().toString());
        dto.setUpdatedAt(complaint.getUpdatedAt().toString());

        if (complaint.getUpdates() != null) {
            dto.setUpdates(complaint.getUpdates().stream()
                .map(ComplaintUpdateDTO::fromEntity)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ComplaintUpdateDTO> getUpdates() {
        return updates;
    }

    public void setUpdates(List<ComplaintUpdateDTO> updates) {
        this.updates = updates;
    }

    public static class ComplaintUpdateDTO {
        private String id;
        private String message;
        private String by;
        private String byName;
        private String timestamp;

        public ComplaintUpdateDTO() {}

        public static ComplaintUpdateDTO fromEntity(ComplaintUpdate update) {
            ComplaintUpdateDTO dto = new ComplaintUpdateDTO();
            dto.setId(update.getUpdateId());
            dto.setMessage(update.getMessage());
            dto.setBy(update.getBy());
            dto.setByName(update.getByName());
            dto.setTimestamp(update.getTimestamp().toString());
            return dto;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getBy() {
            return by;
        }

        public void setBy(String by) {
            this.by = by;
        }

        public String getByName() {
            return byName;
        }

        public void setByName(String byName) {
            this.byName = byName;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}