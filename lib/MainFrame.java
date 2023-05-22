package lib;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.*;
import java.time.temporal.WeekFields;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MainFrame extends JFrame implements ActionListener {

    private LocalDate aktuellesDatum;
    private JPanel pnlNavigation;
    private JButton btnVorwoche;
    private JButton btnNaechsteWoche;
    private JPanel pnlKalender;
    private DefaultTableModel tblModel;
    private JTable tblKalender;
    private JButton btnNeuerEintrag;
    private JButton btnSchichteintrag;
    private JButton btnSpeichern;
    private JButton btnLoeschen;
    private JButton btnLaden;
    private String dateiname;
    private DefaultTableModel tblModel2;
    private JTable tblSchicht;

    public MainFrame() {
        // Fenster-Einstellungen
        setTitle("Wochenkalender");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Aktuelles Datum setzen
        aktuellesDatum = LocalDate.now();

        // GUI-Komponenten hinzufügen
        pnlNavigation = new JPanel(new FlowLayout());
        pnlNavigation.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlNavigation.setAlignmentY(Component.TOP_ALIGNMENT);

        // Erstelle die Buttons und füge ActionListener hinzu
        btnVorwoche = new JButton("<<");
        btnVorwoche.addActionListener(this);

        btnNaechsteWoche = new JButton(">>");
        btnNaechsteWoche.addActionListener(this);

        btnNeuerEintrag = new JButton("Neuer Namen Eintrag");
        btnNeuerEintrag.addActionListener(this);

        btnSchichteintrag = new JButton("neue Schichteintrag");
        btnSchichteintrag.addActionListener(this);
        btnSchichteintrag.setPreferredSize(new Dimension(50, 50));

        btnSpeichern = new JButton("Speichern");
        btnSpeichern.addActionListener(this);

        btnLoeschen = new JButton("Loeschen");
        btnLoeschen.addActionListener(this);

        btnLaden = new JButton("Laden");
        btnLaden.addActionListener(this);

        // Buttons zum Panel hinzufügen
        pnlNavigation.add(btnSchichteintrag);
        pnlNavigation.add(btnNeuerEintrag);
        pnlNavigation.add(btnVorwoche);
        pnlNavigation.add(btnNaechsteWoche);
        pnlNavigation.add(btnSpeichern);
        pnlNavigation.add(btnLoeschen);
        pnlNavigation.add(btnLaden);

        // JPanel mit BorderLayout erstellen
        pnlKalender = new JPanel(new BorderLayout());

        // Erstelle ein Array von Strings mit den Wochentagen
        String[] wochentage = { "", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag",
                "Total" };

        // Erstelle ein neues DefaultTableModel mit den Wochentagen als Spalten und
        // keine Zeilen
        tblModel = new DefaultTableModel(wochentage, 0);

        // Füge 20 leere Zeilen hinzu
        for (int i = 1; i <= 25; i++) {
            tblModel.addRow(new Object[] { "", "", "", "", "", "", "", "", "" });
        }

        // JTable mit DefaultTableModel erstellen und Eigenschaften setzen
        tblKalender = new JTable(tblModel);
        tblKalender.setDragEnabled(true);
        tblKalender.setDropMode(DropMode.USE_SELECTION);
        tblKalender.setTransferHandler(new CellTransferHandler());
        tblKalender.setRowHeight(30);

        // JScrollPane mit JTable erstellen und Eigenschaften setzen
        JScrollPane scrollPane = new JScrollPane(tblKalender);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.setPreferredSize(new Dimension(1000, 5000));
        pnlKalender.add(scrollPane, BorderLayout.CENTER);

        // Zweite Tabelle für die Schichten erstellen
        // Zwei Zeilen und gleiche Spalten wie die erste Tabelle
        tblModel2 = new DefaultTableModel(new Object[] { "", "", "", "", "", "", "", "", "" }, 2);

        tblSchicht = new JTable(tblModel2);
        tblSchicht.setDragEnabled(true);
        tblSchicht.setDropMode(DropMode.USE_SELECTION);
        tblSchicht.setTransferHandler(new CellTransferHandler());
        tblSchicht.setRowHeight(30);
        tblSchicht.setShowGrid(true); 
        

        JScrollPane scrollPane2 = new JScrollPane(tblSchicht);
        scrollPane2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPane2.setBorder(new EmptyBorder(20, 20, 20, 20));
        

        // JPanel für die Schichten erstellen und das Label hinzufügen
        JPanel pnlSchicht = new JPanel(new BorderLayout());
        pnlSchicht.add(scrollPane2, BorderLayout.CENTER);
        tblSchicht.setBackground(Color.LIGHT_GRAY);

        // Layout Manager zum Platzieren mehrerer Komponenten
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        getContentPane().add(pnlNavigation);
        getContentPane().add(pnlKalender);
        getContentPane().add(pnlSchicht); 
        getContentPane().add(btnSchichteintrag);

        // Kalender aktualisieren
        aktualisiereKalender();
        ladeWocheAusDatei();

    }

    // Main-Methode, die eine neue WochenkalenderGUI-Instanz erstellt und sichtbar
    // macht
    public static void main(String[] args) {
        MainFrame gui = new MainFrame();
        gui.setVisible(true);
    }

    // ActionPerformed-Methode, die auf Klick-Events der Buttons reagiert und
    // entsprechende Methoden aufruft
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnVorwoche) {
            // Wenn der Vorwoche-Button geklickt wurde
            speichern();
            loeschen();
            aktuellesDatum = aktuellesDatum.minusWeeks(1);
            aktualisiereKalender();
            ladeWocheAusDatei();
        } else if (event.getSource() == btnNaechsteWoche) {
            // Wenn der Nächste-Woche-Button geklickt wurde
            speichern();
            loeschen();
            aktuellesDatum = aktuellesDatum.plusWeeks(1);
            aktualisiereKalender();
            ladeWocheAusDatei();
        } else if (event.getSource() == btnNeuerEintrag) {
            // Wenn der Neuer-Eintrag-Button geklickt wurde
            neuerNamenEintrag();
        } else if (event.getSource() == btnSchichteintrag) {
            neuerSchichteintrag();
        } else if (event.getSource() == btnSpeichern) {
            // Wenn der Speichern-Button geklickt wurde
            speichern();

        } else if (event.getSource() == btnLoeschen) {
            // Wenn der Löschen-Button geklickt wurde
            loeschen();
        } else if (event.getSource() == btnLaden) {
            ladeWocheAusDatei();
            ;
        }
    }

    private void ladeWocheAusDatei() {
        LocalDate montag = aktuellesDatum.with(DayOfWeek.MONDAY);
        String wocheKey = montag.format(DateTimeFormatter.ISO_LOCAL_DATE);
        Path dataFilePath = Paths.get(wocheKey + ".csv");

        if (Files.exists(dataFilePath)) {
            try (BufferedReader reader = Files.newBufferedReader(dataFilePath)) {
                String line;
                int rowIndex = 1;

                while ((line = reader.readLine()) != null) {
                    String[] rowData = line.split(",");

                    for (int colIndex = 0; colIndex < rowData.length; colIndex++) {
                        tblModel.setValueAt(rowData[colIndex], rowIndex, colIndex);
                    }

                    rowIndex++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Andernfalls leeren Sie die Tabelle
            for (int i = 1; i < tblModel.getRowCount(); i++) {
                for (int j = 0; j < tblModel.getColumnCount(); j++) {
                    tblModel.setValueAt(null, i, j);
                }
            }
        }
    }

    private void aktualisiereKalender() {
        tblKalender.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        tblKalender.setGridColor(Color.lightGray);

        // Wochentage für die aktuelle Woche ermitteln
        LocalDate montag = aktuellesDatum.with(DayOfWeek.MONDAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd.MM.yyyy");
        tblModel.setValueAt(" Woche" + montag.get(WeekFields.ISO.weekOfWeekBasedYear()), 0, 0);
        tblModel.setValueAt(formatter.format(montag), 0, 1);
        for (int i = 1; i <= 6; i++) {
            LocalDate tag = montag.plusDays(i);
            tblModel.setValueAt(formatter.format(tag), 0, i + 1);
        }
        // Zeilen und Spalten für die Tabelle festlegen
        tblModel.setRowCount(25);
        tblModel.setColumnCount(9);

        // Setze den Dateinamen auf den Namen der aktuellen Woche
        dateiname = montag.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";

        // Tabelle aktualisieren
        tblKalender.revalidate();
        tblKalender.repaint();
    }

    private void neuerNamenEintrag() {
        // Name des neuen Eintrags abfragen
        String name = JOptionPane.showInputDialog(this, "Name des neuen Eintrags:");

        // Nächste leere Zeile in der ersten Spalte suchen
        int zeile = -1;
        for (int i = 1; i < tblModel.getRowCount(); i++) {
            if (tblModel.getValueAt(i, 0).equals("")) {
                zeile = i;
                break;
            }
        }

        if (zeile != -1) {
            // Neue Daten in Tabelle einfügen
            tblModel.setValueAt(name, zeile, 0);

            // Tabelle aktualisieren
            tblKalender.revalidate();
            tblKalender.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Keine freien Zeilen mehr verfügbar!");
        }
    }

    private void speichern() {

        // Hole den Dateinamen aus dem aktuellen Wochen-Key
        LocalDate montag = aktuellesDatum.with(DayOfWeek.MONDAY);
        String wocheKey = montag.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String dateiname = wocheKey + ".csv";

        // Prüfe, ob die Datei bereits existiert
        File file = new File(dateiname);
        if (file.exists()) {
            file.delete();
        }

        // Schreibe die Daten der Tabelle in die CSV-Datei
        try (FileWriter writer = new FileWriter(file)) {
            TableModel model = tblKalender.getModel();
            for (int i = 1; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        writer.write(value.toString());
                    }
                    writer.write(",");
                }
                writer.write("\n");
            }
            JOptionPane.showMessageDialog(this, "Tabelle erfolgreich gespeichert!", "Erfolgreich",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Tabelle: " + e.getMessage(), "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void neuerSchichteintrag() {
        // Dialogbox zum Hinzufügen einer neuen Schicht anzeigen
        JTextField nameField = new JTextField(20);
        JTextField zeitField = new JTextField(20);
        JButton colorButton = new JButton("Farbe wählen");
        Color[] color = { Color.WHITE };

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name des Mitarbeiters:"));
        panel.add(nameField);
        panel.add(new JLabel("Arbeitszeit:"));
        panel.add(zeitField);
        panel.add(new JLabel("Hintergrundfarbe:"));
        panel.add(colorButton);

        colorButton.addActionListener(e -> {
            color[0] = JColorChooser.showDialog(this, "Farbe wählen", color[0]);
        });

        int result = JOptionPane.showConfirmDialog(this, panel, "Neuer Schichteintrag", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Neue Zeile hinzufügen, wenn keine freie Zeile mehr vorhanden ist
            if (tblModel2.getRowCount() == 0 || tblModel2.getValueAt(0, 2) != null) {
                tblModel2.addRow(new Object[] { "", "", "", "", "", "", "", "", "" });
            }
            int row = 0;
            // Nächste freie Spalte finden
            int column = 0;
            while (column < 8 && tblModel2.getValueAt(row, column) != null) {
                column++;
            }
            // Schichtinformationen in die Tabelle einfügen
            String schichtInfo = nameField.getText() + "\n" + zeitField.getText();
            tblModel2.setValueAt(schichtInfo, row, column);
        }
    }

    private void loeschen() {
        TableModel model = tblKalender.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                model.setValueAt(null, i, j);
            }
        }
    }

}
