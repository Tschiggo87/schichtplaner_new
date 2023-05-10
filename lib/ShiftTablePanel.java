// package lib;

// import java.awt.BorderLayout;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import javax.swing.JButton;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.table.DefaultTableModel;

// public class ShiftTablePanel extends JPanel {
//     private JTable shiftTable;
//     private DefaultTableModel tableModel;
//     private JButton addShiftButton;

//     public ShiftTablePanel() {
//         setLayout(new BorderLayout());

//         initializeTableModel();
//         initializeTable();
//         initializeAddShiftButton();

//         JScrollPane scrollPane = new JScrollPane(shiftTable);
//         add(scrollPane, BorderLayout.CENTER);
//         add(addShiftButton, BorderLayout.NORTH);
//     }

//     private void initializeTableModel() {
//         tableModel = new DefaultTableModel(new String[] { "Schichtname", "Startzeit", "Endzeit" }, 0);
//     }

//     private void initializeTable() {
//         shiftTable = new JTable(tableModel);
//         shiftTable.setFillsViewportHeight(true);
//     }

//     private void initializeAddShiftButton() {
//         addShiftButton = new JButton("Neue Schicht hinzufügen");
//         addShiftButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 tableModel.addRow(new Object[] { "", "", "" });
//             }
//         });
//     }
// }
