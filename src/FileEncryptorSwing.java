
// Source code is decompiled from a .class file using FernFlower decompiler.
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FileEncryptorSwing extends JFrame {
    private JTextField pathField;
    private JComboBox<String> actionBox;

    public FileEncryptorSwing() {
        this.setTitle("File/Folder Encryptor");
        this.setSize(550, 300);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo((Component) null);
        this.setLayout(new FlowLayout());
        this.pathField = new JTextField(35);
        JButton browseFileButton = new JButton("Browse File");
        JButton browseFolderButton = new JButton("Browse Folder");
        String[] actions = new String[] { "Encrypt", "Decrypt" };
        this.actionBox = new JComboBox<String>(actions); // Correct
        JButton executeButton = new JButton("Execute");
        this.add(new JLabel("File/Folder Path:"));
        this.add(this.pathField);
        this.add(browseFileButton);
        this.add(browseFolderButton);
        this.add(this.actionBox);
        this.add(executeButton);
        browseFileButton.addActionListener((e) -> {
            this.browseFile();
        });
        browseFolderButton.addActionListener((e) -> {
            this.browseFolder();
        });
        executeButton.addActionListener((e) -> {
            this.processAction();
        });
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == 0) {
            this.pathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }

    }

    private void browseFolder() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(1);
        int option = folderChooser.showOpenDialog(this);
        if (option == 0) {
            this.pathField.setText(folderChooser.getSelectedFile().getAbsolutePath());
        }

    }

    private void processAction() {
        String path = this.pathField.getText();
        String action = (String) this.actionBox.getSelectedItem();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid path.", "Error", 0);
        } else {
            File file = new File(path);
            if (file.isFile()) {
                if ("Encrypt".equals(action)) {
                    this.encryptFile(file);
                } else {
                    this.decryptFile(file);
                }
            } else if (file.isDirectory()) {
                if ("Encrypt".equals(action)) {
                    this.encryptFolder(file);
                } else {
                    this.decryptFolder(file);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid path.", "Error", 0);
            }

        }
    }

    private SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    private void saveKey(SecretKey key, File keyFile) throws IOException {
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        Files.write(keyFile.toPath(), encodedKey.getBytes(), new OpenOption[0]);
    }

    private SecretKey loadKey(File keyFile) throws IOException {
        byte[] encoded = Files.readAllBytes(keyFile.toPath());
        byte[] decoded = Base64.getDecoder().decode(encoded);
        return new SecretKeySpec(decoded, 0, decoded.length, "AES");
    }

    private void encryptFile(File file) {
        try {
            SecretKey key = this.generateKey();
            File keyFile = new File(file.getAbsolutePath() + "_key.txt");
            this.saveKey(key, keyFile);
            byte[] data = Files.readAllBytes(file.toPath());
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, key);
            byte[] encrypted = cipher.doFinal(data);
            Files.write(file.toPath(), encrypted, new OpenOption[0]);
            JOptionPane.showMessageDialog(this,
                    "File encrypted successfully!\nKey saved as: " + keyFile.getAbsolutePath());
        } catch (Exception var7) {
            JOptionPane.showMessageDialog(this, "Encryption failed: " + var7.getMessage(), "Error", 0);
        }

    }

    private void decryptFile(File file) {
        try {
            File keyFile = new File(file.getAbsolutePath() + "_key.txt");
            SecretKey key = this.loadKey(keyFile);
            byte[] encrypted = Files.readAllBytes(file.toPath());
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, key);
            byte[] decrypted = cipher.doFinal(encrypted);
            Files.write(file.toPath(), decrypted, new OpenOption[0]);
            JOptionPane.showMessageDialog(this, "File decrypted successfully!");
        } catch (Exception var7) {
            JOptionPane.showMessageDialog(this, "Decryption failed: " + var7.getMessage(), "Error", 0);
        }

    }

    private void encryptFolder(File folder) {
        try {
            SecretKey key = this.generateKey();
            File keyFile = new File(folder.getAbsolutePath() + "_key.txt");
            this.saveKey(key, keyFile);
            File[] var4 = folder.listFiles();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (file.isFile()) {
                    byte[] data = Files.readAllBytes(file.toPath());
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(1, key);
                    byte[] encrypted = cipher.doFinal(data);
                    Files.write(file.toPath(), encrypted, new OpenOption[0]);
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Folder encrypted successfully!\nKey saved as: " + keyFile.getAbsolutePath());
        } catch (Exception var11) {
            JOptionPane.showMessageDialog(this, "Folder encryption failed: " + var11.getMessage(), "Error", 0);
        }

    }

    private void decryptFolder(File folder) {
        try {
            File keyFile = new File(folder.getAbsolutePath() + "_key.txt");
            SecretKey key = this.loadKey(keyFile);
            File[] var4 = folder.listFiles();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (file.isFile()) {
                    byte[] encrypted = Files.readAllBytes(file.toPath());
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(2, key);
                    byte[] decrypted = cipher.doFinal(encrypted);
                    Files.write(file.toPath(), decrypted, new OpenOption[0]);
                }
            }

            JOptionPane.showMessageDialog(this, "Folder decrypted successfully!");
        } catch (Exception var11) {
            JOptionPane.showMessageDialog(this, "Folder decryption failed: " + var11.getMessage(), "Error", 0);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            (new FileEncryptorSwing()).setVisible(true);
        });
    }
}
