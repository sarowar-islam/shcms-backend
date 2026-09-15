package cuet.shcms.app.seeder;

import cuet.shcms.app.entity.Complaint;
import cuet.shcms.app.entity.ComplaintUpdate;
import cuet.shcms.app.entity.User;
import cuet.shcms.app.repository.ComplaintRepository;
import cuet.shcms.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataSeeder(UserRepository userRepository, ComplaintRepository complaintRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
        }
        if (complaintRepository.count() == 0) {
            seedComplaints();
        }
    }

    private void seedUsers() {
        List<User> users = Arrays.asList(
            createUser("2204107", "2204107", "student", User.Role.STUDENT, "Sarowar Islam",
                "u2204107@student.cuet.ac.bd", "A-204", null, "+91 98765 43210", LocalDate.of(2022, 8, 1)),
            createUser("2204048", "2204048", "student", User.Role.STUDENT, "Priya Sharma",
                "priya.sharma@university.edu", "B-112", null, "+91 98765 11223", LocalDate.of(2022, 8, 1)),
            createUser("2203091", "2203091", "student", User.Role.STUDENT, "Rohan Verma",
                "rohan.verma@university.edu", "C-305", null, "+91 91234 56789", LocalDate.of(2021, 8, 1)),
            createUser("2205062", "2205062", "student", User.Role.STUDENT, "Kavya Nair",
                "kavya.nair@university.edu", "A-108", null, "+91 87654 32109", LocalDate.of(2023, 8, 1)),
            createUser("2201033", "2201033", "student", User.Role.STUDENT, "Siddharth Rao",
                "siddharth.rao@university.edu", "D-210", null, "+91 99887 76654", LocalDate.of(2020, 8, 1)),
            createUser("staff1", "staff1", "staff1", User.Role.STAFF, "Ramesh Kumar",
                "ramesh.kumar@university.edu", null, "Electrical & Maintenance", "+91 94455 66778", LocalDate.of(2019, 3, 15)),
            createUser("staff2", "staff2", "staff2", User.Role.STAFF, "Sunita Pillai",
                "sunita.pillai@university.edu", null, "Plumbing & Civil", "+91 93344 55667", LocalDate.of(2020, 6, 1)),
            createUser("staff3", "staff3", "staff3", User.Role.STAFF, "Mohan Das",
                "mohan.das@university.edu", null, "Security & General", "+91 92233 44556", LocalDate.of(2018, 11, 20)),
            createUser("admin", "admin", "admin", User.Role.ADMIN, "Dr. Anita Krishnan",
                "admin@university.edu", null, "Hall Administration", "+91 98001 23456", LocalDate.of(2015, 7, 1))
        );

        userRepository.saveAll(users);
        System.out.println("Users seeded successfully!");
    }

    private User createUser(String userId, String username, String password, User.Role role, String name,
                           String email, String room, String department, String phone, LocalDate joinedDate) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setName(name);
        user.setEmail(email);
        user.setRoom(room);
        user.setDepartment(department);
        user.setPhone(phone);
        user.setJoinedDate(joinedDate);
        user.setActive(true);
        return user;
    }

    private void seedComplaints() {
        // Complaint 1: Fan switch not working
        Complaint c1 = createComplaint("CMP-001", "2204107", "Sarowar Islam", "A-204",
            "Fan switch not working",
            "The ceiling fan switch in my room has stopped working completely. The fan does not respond when the switch is toggled. This is causing severe discomfort especially during night hours.",
            Complaint.ComplaintCategory.ELECTRICAL, Complaint.UrgencyLevel.HIGH, Complaint.ComplaintStatus.IN_PROGRESS);
        c1.setAssignedTo("staff1");
        c1.setSubmittedAt(LocalDateTime.of(2026, 8, 20, 9, 15));
        c1.setUpdatedAt(LocalDateTime.of(2026, 8, 21, 11, 30));

        ComplaintUpdate u1 = createUpdate("Complaint received and assigned to Ramesh Kumar for inspection.", "admin", "Dr. Anita Krishnan");
        u1.setTimestamp(LocalDateTime.of(2026, 8, 20, 10, 0));
        c1.addUpdate(u1);

        ComplaintUpdate u2 = createUpdate("Inspected the room. Faulty switch identified. Replacement part ordered. Will be fixed by tomorrow.", "staff1", "Ramesh Kumar");
        u2.setTimestamp(LocalDateTime.of(2026, 8, 21, 11, 30));
        c1.addUpdate(u2);

        complaintRepository.save(c1);

        // Complaint 2: Water leakage from washroom tap
        Complaint c2 = createComplaint("CMP-002", "2204107", "Sarowar Islam", "A-204",
            "Water leakage from washroom tap",
            "The hot water tap in the attached washroom is leaking continuously. Water is pooling on the floor and this is creating a slipping hazard.",
            Complaint.ComplaintCategory.PLUMBING, Complaint.UrgencyLevel.CRITICAL, Complaint.ComplaintStatus.RESOLVED);
        c2.setAssignedTo("staff2");
        c2.setSubmittedAt(LocalDateTime.of(2026, 8, 10, 14, 0));
        c2.setUpdatedAt(LocalDateTime.of(2026, 8, 12, 16, 0));

        ComplaintUpdate u3 = createUpdate("Plumber visited and replaced the tap washer. Issue resolved.", "staff2", "Sunita Pillai");
        u3.setTimestamp(LocalDateTime.of(2026, 8, 12, 16, 0));
        c2.addUpdate(u3);

        complaintRepository.save(c2);

        // Complaint 3: Broken study chair
        Complaint c3 = createComplaint("CMP-003", "2204048", "Priya Sharma", "B-112",
            "Broken study chair",
            "The study chair's backrest is broken and wobbles dangerously. Using it poses a risk of injury. Requesting replacement urgently.",
            Complaint.ComplaintCategory.FURNITURE, Complaint.UrgencyLevel.MEDIUM, Complaint.ComplaintStatus.PENDING);
        c3.setSubmittedAt(LocalDateTime.of(2026, 8, 23, 8, 30));
        c3.setUpdatedAt(LocalDateTime.of(2026, 8, 23, 8, 30));

        complaintRepository.save(c3);

        // Complaint 4: Wi-Fi not working in room
        Complaint c4 = createComplaint("CMP-004", "2204048", "Priya Sharma", "B-112",
            "Wi-Fi not working in room",
            "There has been no internet connectivity in my room for the past 3 days. The Wi-Fi access point on the B-wing corridor shows connected but there is no actual internet access.",
            Complaint.ComplaintCategory.INTERNET, Complaint.UrgencyLevel.HIGH, Complaint.ComplaintStatus.IN_PROGRESS);
        c4.setAssignedTo("staff3");
        c4.setSubmittedAt(LocalDateTime.of(2026, 8, 19, 20, 0));
        c4.setUpdatedAt(LocalDateTime.of(2026, 8, 22, 10, 15));

        ComplaintUpdate u4 = createUpdate("Networking team notified. Will check the B-wing access point.", "staff3", "Mohan Das");
        u4.setTimestamp(LocalDateTime.of(2026, 8, 22, 10, 15));
        c4.addUpdate(u4);

        complaintRepository.save(c4);

        // Complaint 5: Common bathroom not cleaned
        Complaint c5 = createComplaint("CMP-005", "2203091", "Rohan Verma", "C-305",
            "Common bathroom not cleaned",
            "The common bathroom on the 3rd floor of C block has not been cleaned for over 4 days. There is a foul odour and the floor is visibly dirty.",
            Complaint.ComplaintCategory.CLEANLINESS, Complaint.UrgencyLevel.HIGH, Complaint.ComplaintStatus.RESOLVED);
        c5.setAssignedTo("staff3");
        c5.setSubmittedAt(LocalDateTime.of(2026, 8, 15, 7, 45));
        c5.setUpdatedAt(LocalDateTime.of(2026, 8, 15, 14, 30));

        ComplaintUpdate u5 = createUpdate("Housekeeping team deployed. Bathroom cleaned and sanitised.", "staff3", "Mohan Das");
        u5.setTimestamp(LocalDateTime.of(2026, 8, 15, 14, 30));
        c5.addUpdate(u5);

        complaintRepository.save(c5);

        // Complaint 6: Main gate security not present at night
        Complaint c6 = createComplaint("CMP-006", "2205062", "Kavya Nair", "A-108",
            "Main gate security not present at night",
            "On multiple occasions between 11 PM and 2 AM, the security guard at the main gate was found absent. This is a major safety concern for residents.",
            Complaint.ComplaintCategory.SECURITY, Complaint.UrgencyLevel.CRITICAL, Complaint.ComplaintStatus.IN_PROGRESS);
        c6.setAssignedTo("staff3");
        c6.setSubmittedAt(LocalDateTime.of(2026, 8, 22, 23, 10));
        c6.setUpdatedAt(LocalDateTime.of(2026, 8, 23, 9, 0));

        ComplaintUpdate u6 = createUpdate("Issue escalated to the security supervisor. Night shift roster reviewed.", "admin", "Dr. Anita Krishnan");
        u6.setTimestamp(LocalDateTime.of(2026, 8, 23, 9, 0));
        c6.addUpdate(u6);

        complaintRepository.save(c6);

        // Complaint 7: Power socket not working
        Complaint c7 = createComplaint("CMP-007", "2204107", "Sarowar Islam", "A-204",
            "Power socket not working",
            "The 5-pin power socket near the study table is dead. No power output detected. All other sockets in the room are working fine.",
            Complaint.ComplaintCategory.ELECTRICAL, Complaint.UrgencyLevel.MEDIUM, Complaint.ComplaintStatus.PENDING);
        c7.setSubmittedAt(LocalDateTime.of(2026, 8, 24, 17, 30));
        c7.setUpdatedAt(LocalDateTime.of(2026, 8, 24, 17, 30));

        complaintRepository.save(c7);

        // Complaint 8: Room door lock jammed
        Complaint c8 = createComplaint("CMP-008", "2203091", "Rohan Verma", "C-305",
            "Room door lock jammed",
            "The door lock of my room is jammed and doesn't lock properly from inside. This is a safety and privacy concern.",
            Complaint.ComplaintCategory.OTHERS, Complaint.UrgencyLevel.HIGH, Complaint.ComplaintStatus.REJECTED);
        c8.setSubmittedAt(LocalDateTime.of(2026, 8, 18, 13, 0));
        c8.setUpdatedAt(LocalDateTime.of(2026, 8, 19, 9, 0));

        ComplaintUpdate u7 = createUpdate("After inspection, the lock was found to be manually tampered. Student is advised to not tamper with fixtures. Complaint rejected. Please contact the warden directly.", "staff1", "Ramesh Kumar");
        u7.setTimestamp(LocalDateTime.of(2026, 8, 19, 9, 0));
        c8.addUpdate(u7);

        complaintRepository.save(c8);

        System.out.println("Complaints seeded successfully!");
    }

    private Complaint createComplaint(String complaintId, String studentId, String studentName, String room,
                                      String title, String description, Complaint.ComplaintCategory category,
                                      Complaint.UrgencyLevel urgency, Complaint.ComplaintStatus status) {
        Complaint complaint = new Complaint();
        complaint.setComplaintId(complaintId);
        complaint.setStudentId(studentId);
        complaint.setStudentName(studentName);
        complaint.setRoom(room);
        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setCategory(category);
        complaint.setUrgency(urgency);
        complaint.setStatus(status);
        return complaint;
    }

    private int updateCounter = 1;

    private ComplaintUpdate createUpdate(String message, String by, String byName) {
        ComplaintUpdate update = new ComplaintUpdate();
        update.setUpdateId(String.format("UPD-%03d", updateCounter++));
        update.setMessage(message);
        update.setBy(by);
        update.setByName(byName);
        return update;
    }
}