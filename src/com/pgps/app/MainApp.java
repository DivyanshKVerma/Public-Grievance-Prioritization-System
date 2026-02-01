package com.pgps.app;

import com.pgps.dao.*;
import com.pgps.enums.*;
import com.pgps.model.Grievance;
import com.pgps.service.EscalationService;
import com.pgps.service.FinalPriorityEngine;

import java.util.*;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final GrievanceDAO dao = new GrievanceDAOImpl();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== PUBLIC GRIEVANCE SYSTEM =====");
            System.out.println("1. View all grievances");
            System.out.println("2. View grievance by ID");
            System.out.println("3. Add new grievance");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> viewAll();
                case 2 -> viewById();
                case 3 -> addGrievance();
                case 4 -> {
                    System.out.println("Exiting system.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAll() 
    {

        List<Grievance> grievances = dao.findAll();
        EscalationService escalationService = new EscalationService();

        List<Grievance> newlyEscalated = new ArrayList<>();

        System.out.println("\n---- ALL GRIEVANCES ----");

        for (Grievance g : grievances) {

            GrievanceStatus oldStatus = g.getStatus();

            if (escalationService.shouldEscalate(g)) {
                escalationService.escalate(g);

                // log only if status changed NOW
                if (oldStatus != GrievanceStatus.ESCALATED) {
                    newlyEscalated.add(g);
                }
            }

            System.out.println(
                "ID: " + g.getGrievanceId() +
                " | Title: " + g.getTitle() +
                " | Status: " + g.getStatus() +
                " | Priority: " + g.getPriorityScore()
            );
        }

        // Persist + log only new escalations
        for (Grievance g : newlyEscalated) {

            dao.updateEscalation(
                g.getGrievanceId(),
                g.getStatus(),
                g.getAssignedAuthorityId()
            );

            System.out.println(
                "⚠ Grievance ID " + g.getGrievanceId() +
                " escalated due to SLA breach."
            );
        }
    }

    private static void viewById() {
        System.out.print("Enter grievance ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Grievance g = dao.findById(id);

        if (g == null) {
            System.out.println("Grievance not found.");
            return;
        }

        System.out.println("\n--- GRIEVANCE DETAILS ---");
        System.out.println("Title: " + g.getTitle());
        System.out.println("Description: " + g.getDescription());
        System.out.println("Category: " + g.getCategory());
        System.out.println("Severity: " + g.getSeverity());
        System.out.println("Status: " + g.getStatus());
        System.out.println("Priority: " + g.getPriorityScore());
    }

    private static void addGrievance() {

    Grievance g = new Grievance();

    System.out.print("Title: ");
    g.setTitle(scanner.nextLine());

    System.out.print("Description: ");
    g.setDescription(scanner.nextLine());

    // fixed values for now
    g.setCategory(GrievanceCategory.WATER);
    g.setSeverity(SeverityLevel.MEDIUM);
    g.setStatus(GrievanceStatus.SUBMITTED);
    g.setRaisedByUserId(1);
    g.setAssignedAuthorityId(101);

    // IMPORTANT: set createdAt before priority calculation
    g.setCreatedAt(java.time.LocalDateTime.now());

    FinalPriorityEngine engine = new FinalPriorityEngine();
    int priority = engine.calculateFinalPriority(g);
    g.setPriorityScore(priority);

    System.out.println("AI explanation: " + engine.explain(g));


    dao.save(g);

    System.out.println("Grievance added successfully with priority: " + priority);
    }
}
