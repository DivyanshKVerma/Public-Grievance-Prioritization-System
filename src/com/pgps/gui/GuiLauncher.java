package com.pgps.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;

import com.pgps.dao.AuditLogDAO;
import com.pgps.dao.AuditLogDAOImpl;
import com.pgps.dao.EscalationDAO;
import com.pgps.dao.EscalationDAOImpl;
import com.pgps.dao.GrievanceDAO;
import com.pgps.dao.GrievanceDAOImpl;

import com.pgps.enums.*;
import com.pgps.model.AuditLog;
import com.pgps.model.EscalationRecord;
import com.pgps.model.Grievance;
import com.pgps.service.FinalPriorityEngine;
import com.pgps.service.SLAEscalationService;

public class GuiLauncher extends Application {

    private Stage stage;
    private Scene dashboardScene;

    // === DAOs ===
    private final GrievanceDAO grievanceDao = new GrievanceDAOImpl();
    private final EscalationDAO escalationDao = new EscalationDAOImpl();
    private final AuditLogDAO auditDao = new AuditLogDAOImpl();

    // === Authority Context ===
    private int currentAuthorityId = 101;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // SLA background job
        new Thread(() -> {
            try {
                new SLAEscalationService().checkAndEscalate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        dashboardScene = createDashboardScene();
        stage.setTitle("Public Grievance System");
        stage.setScene(dashboardScene);
        stage.show();
    }

    // ================= DASHBOARD =================

    private Scene createDashboardScene() {

        Label title = new Label("Public Grievance Prioritization System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button viewBtn = new Button("View All Grievances");
        Button addBtn = new Button("Add New Grievance");
        Button exitBtn = new Button("Exit");

        viewBtn.setPrefWidth(250);
        addBtn.setPrefWidth(250);
        exitBtn.setPrefWidth(250);

        viewBtn.setOnAction(e -> stage.setScene(createViewScene()));
        addBtn.setOnAction(e -> stage.setScene(createAddScene()));
        exitBtn.setOnAction(e -> stage.close());

        VBox root = new VBox(40, title, viewBtn, addBtn, exitBtn);
        root.setAlignment(Pos.CENTER);

        return new Scene(root, 800, 500);
    }

    // ================= VIEW GRIEVANCES (B2 + C + D) =================

    private Scene createViewScene() {

        ComboBox<Integer> authorityFilter = new ComboBox<>();
        authorityFilter.getItems().addAll(101, 102, 103, 104);
        authorityFilter.setValue(currentAuthorityId);

        Label legend = new Label("🔴 Escalated    🟠 High Priority    🟢 Resolved");

        TableView<Grievance> table = new TableView<>();

        TableColumn<Grievance, Integer> idCol =
                new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("grievanceId"));

        TableColumn<Grievance, String> titleCol =
                new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Grievance, GrievanceCategory> categoryCol =
                new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Grievance, SeverityLevel> severityCol =
                new TableColumn<>("Severity");
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));

        TableColumn<Grievance, GrievanceStatus> statusCol =
                new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Grievance, Integer> priorityCol =
                new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priorityScore"));

        table.getColumns().addAll(
                idCol, titleCol, categoryCol,
                severityCol, statusCol, priorityCol
        );

        refresh(table);

        authorityFilter.setOnAction(e -> {
            currentAuthorityId = authorityFilter.getValue();
            refresh(table);
        });

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Grievance g, boolean empty) {
                super.updateItem(g, empty);
                if (g == null || empty) setStyle("");
                else if (g.getStatus() == GrievanceStatus.ESCALATED)
                    setStyle("-fx-background-color:#ffcccc;");
                else if (g.getStatus() == GrievanceStatus.RESOLVED)
                    setStyle("-fx-background-color:#ccffcc;");
                else if (g.getPriorityScore() >= 70)
                    setStyle("-fx-background-color:#ffe0b3;");
                else setStyle("");
            }
        });

        // ---------- DETAILS ----------
        Label dTitle = new Label();
        Label dDesc = new Label();
        Label dStatus = new Label();
        Label dPriority = new Label();
        Label dAuthority = new Label();

        VBox detailsBox = new VBox(
                8,
                new Label("Details"),
                dTitle, dDesc, dStatus, dPriority, dAuthority
        );
        detailsBox.setPadding(new Insets(10));
        detailsBox.setPrefWidth(300);

        // ---------- ESCALATION HISTORY ----------
        ListView<String> escalationHistory = new ListView<>();
        escalationHistory.setPrefHeight(150);

        VBox historyBox = new VBox(
                6,
                new Label("Escalation History"),
                escalationHistory
        );
        historyBox.setPadding(new Insets(10));
        historyBox.setPrefWidth(300);

        table.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, g) -> {

                    escalationHistory.getItems().clear();

                    if (g == null) return;

                    dTitle.setText("Title: " + g.getTitle());
                    dDesc.setText("Description: " + g.getDescription());
                    dPriority.setText("Priority: " + g.getPriorityScore());
                    dAuthority.setText("Assigned Authority: " + g.getAssignedAuthorityId());

                    dStatus.setText("Status: " + g.getStatus());
                    dStatus.setStyle(
                            g.getStatus() == GrievanceStatus.ESCALATED
                                    ? "-fx-text-fill:red; -fx-font-weight:bold;"
                                    : g.getStatus() == GrievanceStatus.RESOLVED
                                        ? "-fx-text-fill:green; -fx-font-weight:bold;"
                                        : "-fx-text-fill:orange; -fx-font-weight:bold;"
                    );

                    escalationDao.findByGrievanceId(g.getGrievanceId())
                            .forEach(er -> escalationHistory.getItems().add(
                                    "From " + er.getFromAuthorityId()
                                            + " → " + er.getToAuthorityId()
                                            + " | " + er.getReason()
                                            + " | " + er.getEscalatedAt()
                            ));
                });

        // ---------- ACTIONS ----------
        Button escalateBtn = new Button("Escalate");
        Button resolveBtn = new Button("Resolve");
        Button refreshBtn = new Button("Refresh");
        Button backBtn = new Button("Back");

        escalateBtn.setStyle("-fx-opacity:0.85;");
        resolveBtn.setStyle("-fx-opacity:0.85;");

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, g) -> {
            if (g == null) {
                escalateBtn.setDisable(true);
                resolveBtn.setDisable(true);
                return;
            }

            boolean sameAuthority =
                    g.getAssignedAuthorityId() == currentAuthorityId;

            escalateBtn.setDisable(!sameAuthority || currentAuthorityId >= 104);
            resolveBtn.setDisable(!sameAuthority || currentAuthorityId < 104);
        });

        escalateBtn.setOnAction(e -> {
            Grievance g = table.getSelectionModel().getSelectedItem();
            if (g == null) return;

            int oldAuth = g.getAssignedAuthorityId();
            int newAuth = oldAuth + 1;

            grievanceDao.updateEscalation(
                    g.getGrievanceId(),
                    GrievanceStatus.ESCALATED,
                    newAuth
            );

            escalationDao.save(new EscalationRecord(
                    0,
                    g.getGrievanceId(),
                    oldAuth,
                    newAuth,
                    "SLA breach escalation",
                    java.time.LocalDateTime.now()
            ));

            auditDao.save(new AuditLog(
                    0,
                    g.getGrievanceId(),
                    "GRIEVANCE_ESCALATED",
                    "SYSTEM",
                    java.time.LocalDateTime.now()
            ));

            refresh(table);
        });

        resolveBtn.setOnAction(e -> {
            Grievance g = table.getSelectionModel().getSelectedItem();
            if (g == null) return;

            grievanceDao.updateStatus(
                    g.getGrievanceId(),
                    GrievanceStatus.RESOLVED.name()
            );

            auditDao.save(new AuditLog(
                    0,
                    g.getGrievanceId(),
                    "GRIEVANCE_RESOLVED",
                    "SYSTEM",
                    java.time.LocalDateTime.now()
            ));

            refresh(table);
        });

        refreshBtn.setOnAction(e -> refresh(table));
        backBtn.setOnAction(e -> stage.setScene(dashboardScene));

        HBox actions = new HBox(10, escalateBtn, resolveBtn, refreshBtn, backBtn);
        actions.setAlignment(Pos.CENTER);

        VBox rightPanel = new VBox(15, detailsBox, historyBox, actions);

        HBox topBar = new HBox(15, legend, authorityFilter);
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(
                10,
                topBar,
                new HBox(15, table, rightPanel)
        );
        root.setPadding(new Insets(15));

        return new Scene(root, 1200, 600);
    }

    // ================= ADD GRIEVANCE =================

    private Scene createAddScene() {

        TextField title = new TextField();
        TextArea desc = new TextArea();

        ComboBox<SeverityLevel> severity = new ComboBox<>();
        severity.getItems().addAll(SeverityLevel.values());
        severity.setValue(SeverityLevel.MEDIUM);

        Button submit = new Button("Submit");
        Button back = new Button("Back");

        submit.setOnAction(e -> {

            if (title.getText().isBlank() || desc.getText().isBlank()) {
                showAlert("Validation Error", "Title and Description are required.");
                return;
            }

            Grievance g = new Grievance();
            g.setTitle(title.getText());
            g.setDescription(desc.getText());
            g.setCategory(GrievanceCategory.WATER);
            g.setSeverity(severity.getValue());
            g.setStatus(GrievanceStatus.SUBMITTED);
            g.setRaisedByUserId(1);
            g.setAssignedAuthorityId(101);
            g.setCreatedAt(java.time.LocalDateTime.now());

            g.setPriorityScore(
                    new FinalPriorityEngine().calculateFinalPriority(g)
            );

            grievanceDao.save(g);

            auditDao.save(new AuditLog(
                    0,
                    g.getGrievanceId(),
                    "GRIEVANCE_CREATED",
                    "SYSTEM",
                    java.time.LocalDateTime.now()
            ));

            stage.setScene(dashboardScene);
        });

        back.setOnAction(e -> stage.setScene(dashboardScene));

        VBox root = new VBox(
                10,
                new Label("Title"), title,
                new Label("Description"), desc,
                new Label("Severity"), severity,
                submit, back
        );
        root.setPadding(new Insets(20));

        return new Scene(root, 500, 500);
    }

    // ================= HELPERS =================

    private void refresh(TableView<Grievance> table) {

        var data = grievanceDao.findByAuthority(currentAuthorityId);

        table.setItems(FXCollections.observableArrayList(data));

        if (data.isEmpty()) {
            showAlert(
                    "No Records",
                    "No grievances assigned to Authority " + currentAuthorityId
            );
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}