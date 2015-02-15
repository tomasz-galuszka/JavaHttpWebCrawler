import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileSelector extends JFrame {

    public class FileSelectorException extends Exception {
        public FileSelectorException() {
        }

        public FileSelectorException(String message) {
            super(message);
        }

        public FileSelectorException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static final long serialVersionUID = 1L;

    public File selectFileDat() throws FileSelectorException {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select existing file or type filename (only dat extension)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only dat files", "dat");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(this);
        File result = chooser.getSelectedFile();

        if (!result.getName().contains(".dat")) {
            throw new FileSelectorException("File should have .dat extension ,example: mojedane.dat");
        }

        return result;
    }

    public File selectPathToSaveXls() throws FileSelectorException {
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(this);
        if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".xls")) {
            throw new FileSelectorException("File should have xls extension ,example: dane_pobrane.xls");
        }
        return chooser.getSelectedFile();
    }
}