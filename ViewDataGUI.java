import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.time.format.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ViewDataGUI {

    static final Color BG          = new Color(0xF5F4FB);
    static final Color CARD        = Color.WHITE;
    static final Color PURPLE      = new Color(0x534AB7);
    static final Color PURPLE_LITE = new Color(0xEEEDFE);
    static final Color BORDER      = new Color(0xDDDBF5);
    static final Color TEXT_PRI    = new Color(0x1A1A2E);
    static final Color TEXT_SEC    = new Color(0x6B6B8A);
    static final Color TEXT_HINT   = new Color(0xA8A8C0);
    static final Color ROW_SEL     = new Color(0xEEEDFE);
    static final Color DANGER_BG   = new Color(0xFCEBEB);
    static final Color DANGER_FG   = new Color(0xA32D2D);
    static final Color DANGER_BD   = new Color(0xF7C1C1);
    static final Color GREEN_BG    = new Color(0xE1F5EE);
    static final Color GREEN_FG    = new Color(0x0F6E56);

    static final Font FONT_BODY  = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD,  14);
    static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD,  17);

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel countBadge;

    public ViewDataGUI(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        frame = new JFrame("Personal Data Records");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 520);
        frame.setMinimumSize(new Dimension(640, 400));
        frame.getContentPane().setBackground(BG);
        frame.setLayout(new BorderLayout());

        frame.add(buildHeader(), BorderLayout.NORTH);
        frame.add(buildTable(),  BorderLayout.CENTER);
        frame.add(buildFooter(), BorderLayout.SOUTH);

        loadFromDatabase();
        updateBadge();

        tableModel.addTableModelListener(e -> updateBadge());

        // frame.setLocation(600, 80);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(
            new MatteBorder(0,0,1,0,BORDER),
            new EmptyBorder(14,20,12,20)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JLabel icon = new JLabel("▦");
        icon.setFont(new Font("Segoe UI Symbol",Font.PLAIN,22)); icon.setForeground(PURPLE);
        JPanel txt = new JPanel(new GridLayout(2,1,0,3)); txt.setOpaque(false);
        JLabel t = new JLabel("Data Records"); t.setFont(FONT_TITLE); t.setForeground(TEXT_PRI);
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0)); row.setOpaque(false);
        JLabel s = new JLabel("All saved entries"); s.setFont(FONT_SMALL); s.setForeground(TEXT_SEC);
        countBadge = new JLabel("0");
        countBadge.setFont(FONT_SMALL); countBadge.setForeground(PURPLE);
        countBadge.setBorder(new CompoundBorder(
            new LineBorder(BORDER,1,true), new EmptyBorder(1,8,1,8)));
        countBadge.setBackground(PURPLE_LITE); countBadge.setOpaque(true);
        row.add(s); row.add(countBadge);
        txt.add(t); txt.add(row);
        left.add(icon); left.add(txt);
        p.add(left, BorderLayout.WEST);
        return p;
    }

    // ── Table ─────────────────────────────────────────────────────────────────
    private JScrollPane buildTable() {
        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                boolean sel = isRowSelected(row);
                c.setBackground(sel ? ROW_SEL : (row%2==0 ? CARD : new Color(0xFAF9FE)));
                c.setForeground(sel ? PURPLE : TEXT_PRI);
                if (c instanceof JLabel) ((JLabel)c).setBorder(new EmptyBorder(6,14,6,14));
                return c;
            }
        };
        table.setFont(FONT_BODY);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setSelectionBackground(ROW_SEL);
        table.setSelectionForeground(PURPLE);
        table.setFocusable(false);
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setForeground(TEXT_SEC);
        table.getTableHeader().setBackground(new Color(0xF5F4FB));
        table.getTableHeader().setBorder(new MatteBorder(0,0,1,0,BORDER));
        table.getTableHeader().setPreferredSize(new Dimension(0,38));

        // Column widths
        int[] widths = {50, 140, 120, 90, 280};
        for (int i=0; i<widths.length && i<tableModel.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new MatteBorder(0,0,0,0,BORDER));
        sp.getViewport().setBackground(CARD);
        return sp;
    }

    // ── Footer ────────────────────────────────────────────────────────────────
    private JPanel buildFooter() {

        JPanel p = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 12));

        p.setBackground(CARD);
        p.setBorder(
            new MatteBorder(1,0,0,0,BORDER));

        JButton btnAdd =
            footerBtn("Add Data", true, false);

        JButton btnRefresh =
            footerBtn("Refresh", false, false);

        JButton btnUpdate =
            footerBtn("Edit Record", false, false);

        JButton btnDelete =
            footerBtn("Delete", false, true);

        btnAdd.addActionListener(e -> {
            new PersonalDataGUI(tableModel);
        });

        btnRefresh.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadFromDatabase();
        });

        btnUpdate.addActionListener(e -> editSelected());

        btnDelete.addActionListener(e -> deleteSelected());

        // Add Data button first
        p.add(btnAdd);
        p.add(btnRefresh);
        p.add(btnUpdate);
        p.add(btnDelete);

        return p;
    }

    private JButton footerBtn(String text, boolean primary, boolean danger) {
        JButton b = new JButton(text) {
            public void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                if (danger) g2.setColor(DANGER_BG);
                else g2.setColor(primary ? PURPLE : CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(danger ? DANGER_BD : (primary ? PURPLE : BORDER));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setColor(danger ? DANGER_FG : (primary ? Color.WHITE : TEXT_PRI));
                g2.setFont(FONT_BOLD);
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            public Dimension getPreferredSize(){
                return new Dimension(getFontMetrics(FONT_BOLD).stringWidth(getText())+36,38);
            }
        };
        b.setOpaque(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ── Edit dialog ───────────────────────────────────────────────────────────
    private void editSelected() {
        int row = table.getSelectedRow();
        if (row == -1) { tip("Select a row to edit."); return; }
        int id = (int) tableModel.getValueAt(row,0);

        JTextField fName = new JTextField(tableModel.getValueAt(row,1).toString());
        fName.setFont(FONT_BODY); fName.setPreferredSize(new Dimension(260,36));

        // Calendar picker in dialog
        PersonalDataGUI.CalendarPicker cal = new PersonalDataGUI.CalendarPicker();
        cal.setPreferredSize(new Dimension(260,42));

        JComboBox<String> cmbGender = new JComboBox<>(new String[]{"Male","Female"});
        cmbGender.setFont(FONT_BODY);
        cmbGender.setSelectedItem(tableModel.getValueAt(row,3).toString());

        String oldFav = tableModel.getValueAt(row,4).toString();
        JCheckBox cSports = new JCheckBox("Sports",   oldFav.contains("Sports"));
        JCheckBox cGames  = new JCheckBox("Video Games", oldFav.contains("Video Games"));
        JCheckBox cMusic  = new JCheckBox("Music",    oldFav.contains("Music"));
        JCheckBox cSudoku = new JCheckBox("Sudoku",   oldFav.contains("Sudoku"));
        for (JCheckBox cb : new JCheckBox[]{cSports,cGames,cMusic,cSudoku})
            { cb.setFont(FONT_BODY); cb.setOpaque(false); }

        JPanel favPan = new JPanel(new GridLayout(2,2,8,4)); favPan.setOpaque(false);
        favPan.add(cSports); favPan.add(cGames); favPan.add(cMusic); favPan.add(cSudoku);

        JPanel pan = new JPanel(new GridBagLayout()); pan.setBorder(new EmptyBorder(8,4,4,4));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,4,4,4); gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx=0; gc.gridy=0; gc.gridwidth=1; gc.weightx=0;
        pan.add(bold("Name"), gc);
        gc.gridx=1; gc.weightx=1; pan.add(fName, gc);

        gc.gridx=0; gc.gridy=1; gc.weightx=0;
        pan.add(bold("Date of Birth"), gc);
        gc.gridx=1; gc.weightx=1; pan.add(cal, gc);

        gc.gridx=0; gc.gridy=2; gc.weightx=0;
        pan.add(bold("Gender"), gc);
        gc.gridx=1; gc.weightx=1; pan.add(cmbGender, gc);

        gc.gridx=0; gc.gridy=3; gc.weightx=0;
        pan.add(bold("Favourites"), gc);
        gc.gridx=1; gc.weightx=1; pan.add(favPan, gc);

        int res = JOptionPane.showConfirmDialog(frame, pan, "Edit Record",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String name = fName.getText().trim();
        if (name.isEmpty()) { tip("Name cannot be empty."); return; }
        LocalDate d = cal.getSelectedDate();
        String dob = d != null ? d.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"))
                               : tableModel.getValueAt(row,2).toString();
        String gender = cmbGender.getSelectedItem().toString();
        StringBuilder sb = new StringBuilder();
        if (cSports.isSelected()) sb.append("Sports ");
        if (cGames.isSelected())  sb.append("Video Games ");
        if (cMusic.isSelected())  sb.append("Music ");
        if (cSudoku.isSelected()) sb.append("Sudoku ");
        String favs = sb.toString().trim();

        boolean ok = DatabaseManager.updateData(id, name, dob, gender, favs);
        if (ok) {
            tableModel.setRowCount(0); loadFromDatabase();
            JOptionPane.showMessageDialog(frame,"Updated successfully!","Updated",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JLabel bold(String t) {
        JLabel l=new JLabel(t); l.setFont(FONT_BOLD); l.setForeground(TEXT_PRI); return l;
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row==-1) { tip("Select a row to delete."); return; }
        int id = (int) tableModel.getValueAt(row,0);
        int c = JOptionPane.showConfirmDialog(frame,
            "Delete this record permanently?","Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c==JOptionPane.YES_OPTION && DatabaseManager.deleteData(id)) {
            tableModel.setRowCount(0); loadFromDatabase();
        }
    }

    private void tip(String m) {
        JOptionPane.showMessageDialog(frame,m,"Notice",JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadFromDatabase() {
        try (ResultSet rs = DatabaseManager.getAllData()) {
            while (rs.next())
                tableModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"),
                    rs.getString("dob"), rs.getString("gender"), rs.getString("favourites")});
        } catch (SQLException e) {
            // No DB connection — silently skip (works offline)
        }
    }

    private void updateBadge() {
        if (countBadge != null) countBadge.setText(String.valueOf(tableModel.getRowCount()));
    }
}
