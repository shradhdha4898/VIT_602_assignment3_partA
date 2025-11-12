package au.vit.shams.gui;

import au.vit.shams.domain.Appointment;
import au.vit.shams.domain.AppointmentRequest;
import au.vit.shams.domain.TimeSlot;
import au.vit.shams.repository.InMemoryDoctorRepository;
import au.vit.shams.service.NotificationService;
import au.vit.shams.service.SchedulingService;
// If you use the CSV loader elsewhere, keep this import; otherwise safe to remove.
// import au.vit.shams.util.CsvDataLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShamsGui extends JFrame {

    private final SchedulingService schedulingService;
    private final NotificationService notificationService;
    private final InMemoryDoctorRepository doctorRepo;

    private JTextField txtPatient;
    private JTextField txtDoctor;
    private JTextField txtStart;
    private JTextField txtEnd;

    private JTextField txtApptId;

    private JTextArea log;

    public ShamsGui(SchedulingService s, NotificationService n, InMemoryDoctorRepository d) {
        super("SHAMS – Scheduling & Notifications (GUI)");
        this.schedulingService = s;
        this.notificationService = n;
        this.doctorRepo = d;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // Top: booking + reschedule
        JPanel form = new JPanel(new GridLayout(2, 1, 8, 8));
        form.add(buildBookingPanel());
        form.add(buildReschedulePanel());
        root.add(form, BorderLayout.NORTH);

        // Middle: action buttons (now includes Seed D1 & D2)
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        JButton btnSeedD1 = new JButton("Seed Doctor (D1 09:00–17:00)");
        btnSeedD1.addActionListener(e -> seedDoctorD1());

        JButton btnSeedD2 = new JButton("Seed Doctor (D2 10:00–18:00)");
        btnSeedD2.addActionListener(e -> seedDoctorD2());

        JButton btnListTomorrow = new JButton("List Tomorrow by Doctor");
        btnListTomorrow.addActionListener(e -> listTomorrow());

        JButton btnCancel = new JButton("Cancel by ID");
        btnCancel.addActionListener(e -> cancel());

        JButton btnDispatch = new JButton("Dispatch Reminders (Now)");
        btnDispatch.addActionListener(e -> dispatch());

        buttons.add(btnSeedD1);
        buttons.add(btnSeedD2);
        buttons.add(btnListTomorrow);
        buttons.add(btnCancel);
        buttons.add(btnDispatch);
        root.add(buttons, BorderLayout.CENTER);

        // Bottom: log
        log = new JTextArea();
        log.setEditable(false);
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(log);
        scroll.setPreferredSize(new Dimension(920, 330));
        root.add(scroll, BorderLayout.SOUTH);
    }

    // ---------- UI Panels ----------

    private JPanel buildBookingPanel() {
        JPanel p = new JPanel(new GridLayout(2, 5, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Booking"));

        txtPatient = new JTextField("P1");
        txtDoctor  = new JTextField("D1");
        txtStart   = new JTextField("2025-11-12T10:00");
        txtEnd     = new JTextField("2025-11-12T10:30");

        JButton btnBook = new JButton("Book");
        btnBook.addActionListener(e -> book());

        p.add(new JLabel("Patient ID"));
        p.add(new JLabel("Doctor ID"));
        p.add(new JLabel("Start (yyyy-MM-ddTHH:mm)"));
        p.add(new JLabel("End (yyyy-MM-ddTHH:mm)"));
        p.add(new JLabel("")); // spacer

        p.add(txtPatient);
        p.add(txtDoctor);
        p.add(txtStart);
        p.add(txtEnd);
        p.add(btnBook);

        return p;
    }

    private JPanel buildReschedulePanel() {
        JPanel p = new JPanel(new GridLayout(2, 4, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Reschedule / Cancel"));

        txtApptId = new JTextField();
        JTextField txtNewStart = new JTextField("2025-11-10T12:00");
        JTextField txtNewEnd   = new JTextField("2025-11-10T12:30");

        JButton btnReschedule = new JButton("Reschedule");
        btnReschedule.addActionListener(e -> {
            try {
                String apptId = txtApptId.getText().trim();
                LocalDateTime ns = LocalDateTime.parse(txtNewStart.getText().trim());
                LocalDateTime ne = LocalDateTime.parse(txtNewEnd.getText().trim());
                var appt = schedulingService.reschedule(apptId, ns, ne);
                log("Rescheduled: " + appt);
            } catch (Exception ex) {
                log("Error: " + ex.getMessage());
            }
        });

        p.add(new JLabel("Appointment ID"));
        p.add(new JLabel("New Start (yyyy-MM-ddTHH:mm)"));
        p.add(new JLabel("New End (yyyy-MM-ddTHH:mm)"));
        p.add(new JLabel("")); // spacer

        p.add(txtApptId);
        p.add(txtNewStart);
        p.add(txtNewEnd);
        p.add(btnReschedule);

        return p;
    }

    // ---------- Seeding helpers (D1 & D2) ----------

    private Map<DayOfWeek, List<TimeSlot>> buildWorkingHours(LocalTime start, LocalTime end) {
        Map<DayOfWeek, List<TimeSlot>> wh = new HashMap<>();
        for (DayOfWeek dow : DayOfWeek.values()) {
            wh.put(dow, List.of(new TimeSlot(start, end)));
        }
        return wh;
    }

    /** Generic doctor seeder; safe to call repeatedly (save overwrites). */
    private void seedDoctor(String id, String name, String specialty, LocalTime start, LocalTime end) {
        try {
            var wh = buildWorkingHours(start, end);
            var d = new au.vit.shams.domain.Doctor(id, name, specialty, wh);
            doctorRepo.save(d); // overwrite/insert without deleteById
            log(String.format("Seeded %s – %s (%s %s–%s).", id, name, specialty, start, end));
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
        }
    }

    private void seedDoctorD1() {
        seedDoctor("D1", "Dr Smith", "Cardiology",
                LocalTime.of(9, 0), LocalTime.of(17, 0));
    }

    private void seedDoctorD2() {
        seedDoctor("D2", "Dr Jones", "Dermatology",
                LocalTime.of(10, 0), LocalTime.of(18, 0));
    }

    // ---------- Actions ----------

    private void book() {
        try {
            String pid   = txtPatient.getText().trim();
            String did   = txtDoctor.getText().trim();
            LocalDateTime start = LocalDateTime.parse(txtStart.getText().trim());
            LocalDateTime end   = LocalDateTime.parse(txtEnd.getText().trim());

            AppointmentRequest req = new AppointmentRequest(pid, did, start, end);
            Appointment appt = schedulingService.book(req);

            log("Booked: " + appt);

            // Queue reminders
            notificationService.onAppointmentBooked(appt);
            log("Reminder queued (24h & 2h) for " + appt.getId());

            // expose id for reschedule/cancel
            txtApptId.setText(appt.getId());
        } catch (DateTimeParseException dt) {
            log("Error: invalid date/time format. Use yyyy-MM-ddTHH:mm");
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
        }
    }

    private void cancel() {
        try {
            String id = txtApptId.getText().trim();
            schedulingService.cancel(id);
            log("Cancelled: " + id);
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
        }
    }

    private void listTomorrow() {
        try {
            String did = txtDoctor.getText().trim();
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<Appointment> list = schedulingService.listByDoctorAndDate(did, tomorrow);
            if (list.isEmpty()) {
                log("No appointments for " + did + " on " + tomorrow);
            } else {
                for (Appointment a : list) {
                    log(a.toString());
                }
            }
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
        }
    }

    private void dispatch() {
        try {
            int sent = notificationService.dispatchDue();
            log("Reminders dispatched: " + sent);
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
        }
    }

    // ---------- Logging ----------

    private void log(String msg) {
         log.append(msg);
    	 log.append("\n");
         log.setCaretPosition(log.getDocument().getLength());
    }
}