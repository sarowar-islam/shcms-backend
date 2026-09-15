package cuet.shcms.app.repository;

import cuet.shcms.app.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByComplaintId(String complaintId);
    List<Complaint> findByStudentId(String studentId);
    List<Complaint> findByStatus(Complaint.ComplaintStatus status);
    List<Complaint> findByAssignedTo(String assignedTo);
    List<Complaint> findByCategory(Complaint.ComplaintCategory category);

    @Query("SELECT c FROM Complaint c WHERE c.status = :status ORDER BY c.submittedAt DESC")
    List<Complaint> findByStatusOrdered(@Param("status") Complaint.ComplaintStatus status);

    @Query("SELECT c FROM Complaint c ORDER BY c.submittedAt DESC")
    List<Complaint> findAllOrdered();

    @Query("SELECT c FROM Complaint c WHERE c.urgency = :urgency AND c.status != 'RESOLVED' ORDER BY c.submittedAt DESC")
    List<Complaint> findPendingByUrgency(@Param("urgency") Complaint.UrgencyLevel urgency);

    long countByStudentIdAndSubmittedAtAfter(String studentId, java.time.LocalDateTime date);
}