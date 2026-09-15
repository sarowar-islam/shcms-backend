package cuet.shcms.app.controller;

import cuet.shcms.app.dto.ComplaintDTO;
import cuet.shcms.app.entity.Complaint;
import cuet.shcms.app.entity.ComplaintUpdate;
import cuet.shcms.app.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints() {
        List<Complaint> complaints = complaintService.findAll();
        List<ComplaintDTO> dtos = complaints.stream()
            .map(ComplaintDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDTO> getComplaintById(@PathVariable Long id) {
        return complaintService.findById(id)
            .map(complaint -> ResponseEntity.ok(ComplaintDTO.fromEntity(complaint)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-complaint-id/{complaintId}")
    public ResponseEntity<ComplaintDTO> getComplaintByComplaintId(@PathVariable String complaintId) {
        return complaintService.findByComplaintId(complaintId)
            .map(complaint -> ResponseEntity.ok(ComplaintDTO.fromEntity(complaint)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ComplaintDTO>> getComplaintsByStudent(@PathVariable String studentId) {
        List<Complaint> complaints = complaintService.findByStudentId(studentId);
        List<ComplaintDTO> dtos = complaints.stream()
            .map(ComplaintDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ComplaintDTO>> getComplaintsByStatus(@PathVariable String status) {
        try {
            Complaint.ComplaintStatus complaintStatus = Complaint.ComplaintStatus.valueOf(status.toUpperCase());
            List<Complaint> complaints = complaintService.findByStatus(complaintStatus);
            List<ComplaintDTO> dtos = complaints.stream()
                .map(ComplaintDTO::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/assigned/{assignedTo}")
    public ResponseEntity<List<ComplaintDTO>> getComplaintsByAssignedTo(@PathVariable String assignedTo) {
        List<Complaint> complaints = complaintService.findByAssignedTo(assignedTo);
        List<ComplaintDTO> dtos = complaints.stream()
            .map(ComplaintDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ComplaintDTO>> getPendingComplaints() {
        List<Complaint> complaints = complaintService.findPending();
        List<ComplaintDTO> dtos = complaints.stream()
            .map(ComplaintDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ComplaintDTO>> getComplaintsByCategory(@PathVariable String category) {
        try {
            Complaint.ComplaintCategory complaintCategory = Complaint.ComplaintCategory.valueOf(category.toUpperCase());
            List<Complaint> complaints = complaintService.findByCategory(complaintCategory);
            List<ComplaintDTO> dtos = complaints.stream()
                .map(ComplaintDTO::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<ComplaintDTO> createComplaint(@RequestBody Complaint complaint) {
        Complaint created = complaintService.createComplaint(complaint);
        return ResponseEntity.status(HttpStatus.CREATED).body(ComplaintDTO.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintDTO> updateComplaint(@PathVariable Long id, @RequestBody Complaint updates) {
        Complaint updated = complaintService.updateComplaint(id, updates);
        if (updated != null) {
            return ResponseEntity.ok(ComplaintDTO.fromEntity(updated));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplaintDTO> updateStatus(@PathVariable Long id, @RequestBody StatusUpdate update) {
        try {
            Complaint.ComplaintStatus status = Complaint.ComplaintStatus.valueOf(update.getStatus().toUpperCase());
            Complaint updated = complaintService.updateStatus(id, status);
            if (updated != null) {
                return ResponseEntity.ok(ComplaintDTO.fromEntity(updated));
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<ComplaintDTO> assignComplaint(@PathVariable Long id, @RequestBody AssignmentUpdate update) {
        Complaint updated = complaintService.assignComplaint(id, update.getAssignedTo());
        if (updated != null) {
            return ResponseEntity.ok(ComplaintDTO.fromEntity(updated));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/updates")
    public ResponseEntity<ComplaintDTO> addUpdate(@PathVariable Long id, @RequestBody ComplaintUpdate update) {
        Complaint updated = complaintService.addUpdate(id, update);
        if (updated != null) {
            return ResponseEntity.ok(ComplaintDTO.fromEntity(updated));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }

    // Helper classes for request bodies
    public static class StatusUpdate {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class AssignmentUpdate {
        private String assignedTo;

        public String getAssignedTo() {
            return assignedTo;
        }

        public void setAssignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
        }
    }
}