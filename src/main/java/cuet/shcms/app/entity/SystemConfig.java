package cuet.shcms.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "system_config")
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hall_name")
    private String hallName;

    @Column(name = "total_rooms")
    private Integer totalRooms;

    @Column(name = "warden_name")
    private String wardenName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled = true;

    @Column(name = "auto_assign")
    private Boolean autoAssign = false;

    @Column(name = "max_complaints_per_day")
    private Integer maxComplaintsPerDay = 5;

    // Constructors
    public SystemConfig() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public Integer getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }

    public String getWardenName() {
        return wardenName;
    }

    public void setWardenName(String wardenName) {
        this.wardenName = wardenName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public Boolean getAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(Boolean autoAssign) {
        this.autoAssign = autoAssign;
    }

    public Integer getMaxComplaintsPerDay() {
        return maxComplaintsPerDay;
    }

    public void setMaxComplaintsPerDay(Integer maxComplaintsPerDay) {
        this.maxComplaintsPerDay = maxComplaintsPerDay;
    }
}
