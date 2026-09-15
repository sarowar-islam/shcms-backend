package cuet.shcms.app.service;

import cuet.shcms.app.entity.Complaint;
import cuet.shcms.app.entity.ComplaintUpdate;
import cuet.shcms.app.repository.ComplaintRepository;
import cuet.shcms.app.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final SystemConfigRepository systemConfigRepository;
    private int complaintCounter = 1;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, SystemConfigRepository systemConfigRepository) {
        this.complaintRepository = complaintRepository;
        this.systemConfigRepository = systemConfigRepository;
    }

    public List<Complaint> findAll() {
        return complaintRepository.findAllOrdered();
    }

    public Optional<Complaint> findById(Long id) {
        return complaintRepository.findById(id);
    }

    public Optional<Complaint> findByComplaintId(String complaintId) {
        return complaintRepository.findByComplaintId(complaintId);
    }

    public List<Complaint> findByStudentId(String studentId) {
        return complaintRepository.findByStudentId(studentId);
    }

    public List<Complaint> findByStatus(Complaint.ComplaintStatus status) {
        return complaintRepository.findByStatusOrdered(status);
    }

    public List<Complaint> findByAssignedTo(String assignedTo) {
        return complaintRepository.findByAssignedTo(assignedTo);
    }

    public List<Complaint> findPending() {
        return complaintRepository.findByStatusOrdered(Complaint.ComplaintStatus.PENDING);
    }

    public List<Complaint> findByCategory(Complaint.ComplaintCategory category) {
        return complaintRepository.findByCategory(category);
    }

    public Complaint createComplaint(Complaint complaint) {
        complaint.setComplaintId(String.format("CMP-%03d", complaintCounter++));
        complaint.setStatus(Complaint.ComplaintStatus.PENDING);

        if (complaint.getCategory() == null) {
            complaint.setCategory(Complaint.ComplaintCategory.OTHERS);
        }
        if (complaint.getUrgency() == null) {
            complaint.setUrgency(Complaint.UrgencyLevel.MEDIUM);
        }

        return complaintRepository.save(complaint);
    }

    public Complaint updateComplaint(Long id, Complaint updates) {
        Optional<Complaint> existingOpt = complaintRepository.findById(id);
        if (existingOpt.isPresent()) {
            Complaint existing = existingOpt.get();
            if (updates.getStatus() != null) {
                existing.setStatus(updates.getStatus());
            }
            if (updates.getAssignedTo() != null) {
                existing.setAssignedTo(updates.getAssignedTo());
            }
            return complaintRepository.save(existing);
        }
        return null;
    }

    public Complaint updateStatus(Long id, Complaint.ComplaintStatus status) {
        Optional<Complaint> existingOpt = complaintRepository.findById(id);
        if (existingOpt.isPresent()) {
            Complaint existing = existingOpt.get();
            existing.setStatus(status);
            return complaintRepository.save(existing);
        }
        return null;
    }

    public Complaint assignComplaint(Long id, String assignedTo) {
        Optional<Complaint> existingOpt = complaintRepository.findById(id);
        if (existingOpt.isPresent()) {
            Complaint existing = existingOpt.get();
            existing.setAssignedTo(assignedTo);
            existing.setStatus(Complaint.ComplaintStatus.IN_PROGRESS);
            return complaintRepository.save(existing);
        }
        return null;
    }

    public Complaint addUpdate(Long complaintId, ComplaintUpdate update) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (complaintOpt.isPresent()) {
            Complaint complaint = complaintOpt.get();
            update.setUpdateId("UPD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            update.setComplaint(complaint);
            complaint.addUpdate(update);
            return complaintRepository.save(complaint);
        }
        return null;
    }

    public long countRecentComplaints(String studentId) {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return complaintRepository.countByStudentIdAndSubmittedAtAfter(studentId, today);
    }

    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }
}