package lib;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

public class CellTransferHandler extends TransferHandler {
    // Diese Methode erstellt das Transferable-Objekt, das den Inhalt der
    // ausgewählten Zelle enthält
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JTable) {
            JTable table = (JTable) c;
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            Object cellValue = table.getValueAt(row, col);
            return new StringSelection(cellValue.toString());
        }
        return null;
    }

    // Diese Methode gibt zurück, dass nur eine Kopie des Inhalts erstellt werden
    // soll
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;
    }

    // Diese Methode überprüft, ob das importierte Objekt ein String-Objekt ist
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    // Diese Methode importiert den String-Inhalt in die ausgewählte Zelle
    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        // Überprüft, ob das importierte Objekt ein String-Objekt ist
        if (!canImport(support)) {
            return false;
        }

        // Ermittelt die Drop-Position
        JTable.DropLocation dropLocation = (JTable.DropLocation) support.getDropLocation();
        int row = dropLocation.getRow();
        int col = dropLocation.getColumn();

        try {
            // Extrahiert den importierten String-Inhalt aus dem Transferable-Objekt
            Object data = support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            // Setzt den importierten String-Inhalt in die ausgewählte Zelle ein
            JTable table = (JTable) support.getComponent();
            table.setValueAt(data, row, col);
            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    
}
