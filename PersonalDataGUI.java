import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.*;
import java.time.format.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;


public class PersonalDataGUI {

    // ── Palette ──────────────────────────────────────────────────────────────
    static final Color BG          = new Color(0xF5F4FB);
    static final Color CARD        = Color.WHITE;
    static final Color PURPLE      = new Color(0x534AB7);
    static final Color PURPLE_LITE = new Color(0xEEEDFE);
    static final Color BORDER      = new Color(0xDDDBF5);
    static final Color TEXT_PRI    = new Color(0x1A1A2E);
    static final Color TEXT_SEC    = new Color(0x6B6B8A);
    static final Color TEXT_HINT   = new Color(0xA8A8C0);

    static final Font FONT_BODY  = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD,  14);
    static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD,  17);

    private JFrame frame;
    private JTextField txtName;
    private CalendarPicker calPicker;
    private ButtonGroup genderGroup;
    private JToggleButton btnMale, btnFemale;
    private JToggleButton chkSports, chkGames, chkMusic, chkSudoku;
    private DefaultTableModel tableModel;

    public PersonalDataGUI(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        frame = new JFrame("Personal Data Entry");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(560, 630);
        frame.setMinimumSize(new Dimension(480, 560));
        frame.getContentPane().setBackground(BG);
        frame.setLayout(new BorderLayout());
        frame.add(buildHeader(), BorderLayout.NORTH);
        frame.add(buildForm(),   BorderLayout.CENTER);
        frame.add(buildFooter(), BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 14));
        p.setBackground(CARD);
        p.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        JLabel icon = new JLabel("⬡");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        icon.setForeground(PURPLE);
        JPanel txt = new JPanel(new GridLayout(2,1,0,3));
        txt.setOpaque(false);
        JLabel t = new JLabel("Personal Data Entry"); t.setFont(FONT_TITLE); t.setForeground(TEXT_PRI);
        JLabel s = new JLabel("Fill in the details and save the record"); s.setFont(FONT_SMALL); s.setForeground(TEXT_SEC);
        txt.add(t); txt.add(s);
        p.add(icon); p.add(txt);
        return p;
    }

    private JScrollPane buildForm() {
    JPanel form = new JPanel();
    form.setBackground(BG);
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setBorder(new EmptyBorder(18, 22, 18, 22));

    // Full Name
    form.add(label("Full Name"));
    form.add(Box.createVerticalStrut(4));

    txtName = roundField("");
    txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    form.add(txtName);

    form.add(Box.createVerticalStrut(10));

    // Date
    form.add(label("Date of Birth"));
    form.add(Box.createVerticalStrut(4));

    calPicker = new CalendarPicker();
    calPicker.setAlignmentX(Component.LEFT_ALIGNMENT);
    calPicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

    form.add(calPicker);

    form.add(Box.createVerticalStrut(10));

    // Gender
    form.add(label("Gender"));
    form.add(Box.createVerticalStrut(3));

    JPanel gp = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    gp.setOpaque(false);
    gp.setAlignmentX(Component.LEFT_ALIGNMENT);
    gp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

    genderGroup = new ButtonGroup();

    btnMale = pill("Male");
    btnFemale = pill("Female");

    genderGroup.add(btnMale);
    genderGroup.add(btnFemale);

    gp.add(btnMale);
    gp.add(btnFemale);

    form.add(gp);

    form.add(Box.createVerticalStrut(10));

    // Favourites
    form.add(label("Favourites"));
    form.add(Box.createVerticalStrut(3));

    JPanel fp = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    fp.setOpaque(false);
    fp.setAlignmentX(Component.LEFT_ALIGNMENT);
    fp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    chkSports = pill("Sports");
    chkGames = pill("Video Games");
    chkMusic = pill("Music");
    chkSudoku = pill("Sudoku");

    fp.add(chkSports);
    fp.add(chkGames);
    fp.add(chkMusic);
    fp.add(chkSudoku);

    form.add(fp);

    JScrollPane sp = new JScrollPane(form);
    sp.setBorder(null);
    sp.getViewport().setBackground(BG);

    return sp;
}

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));

        p.setBackground(CARD);
        p.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));

        JButton bCancel = btn("Cancel", false);
        JButton bClear  = btn("Clear", false);
        JButton bSave   = btn("Save Record", true);

        // Close only PersonalDataGUI
        bCancel.addActionListener(e -> frame.dispose());

        bClear.addActionListener(e -> clearForm());

        bSave.addActionListener(e -> saveData());

        p.add(bCancel);
        p.add(bClear);
        p.add(bSave);

        return p;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(FONT_BOLD); l.setForeground(TEXT_PRI);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    private Component vgap(int n) { return Box.createVerticalStrut(n); }

    private JTextField roundField(String ph) {
        JTextField f = new JTextField() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g.setColor(TEXT_HINT); g.setFont(FONT_BODY);
                    Insets in = getInsets();
                    FontMetrics fm = g.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g.drawString(ph, in.left, y);
                }
            }
        };
        f.setFont(FONT_BODY); f.setForeground(TEXT_PRI); f.setBackground(CARD);
        f.setBorder(new CompoundBorder(new RoundBorder(8, BORDER), new EmptyBorder(6,10,6,10)));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private JToggleButton pill(String text) {
        JToggleButton b = new JToggleButton(text) {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean s = isSelected();
                g2.setColor(s ? PURPLE_LITE : CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.setColor(s ? PURPLE : BORDER);
                g2.setStroke(new BasicStroke(s ? 1.5f : 1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
                g2.setColor(s ? PURPLE : TEXT_SEC);
                g2.setFont(s ? FONT_BOLD : FONT_BODY);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            public Dimension getPreferredSize() {
                return new Dimension(getFontMetrics(FONT_BODY).stringWidth(getText())+30, 34);
            }
        };
        b.setOpaque(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> b.repaint());
        return b;
    }

    private JButton btn(String text, boolean primary) {
        JButton b = new JButton(text) {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(primary ? PURPLE : CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                if (!primary) { g2.setColor(BORDER); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8); }
                g2.setColor(primary ? Color.WHITE : TEXT_PRI);
                g2.setFont(FONT_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            public Dimension getPreferredSize() {
                return new Dimension(getFontMetrics(FONT_BOLD).stringWidth(getText())+36, 38);
            }
        };
        b.setOpaque(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void saveData() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) { msg("Please enter a name."); return; }
        LocalDate d = calPicker.getSelectedDate();
        if (d == null) { msg("Please select a date of birth."); return; }
        if (genderGroup.getSelection() == null) { msg("Please select a gender."); return; }
        String dob    = d.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"));
        String gender = btnMale.isSelected() ? "Male" : "Female";
        StringBuilder sb = new StringBuilder();
        if (chkSports.isSelected()) sb.append("Sports ");
        if (chkGames.isSelected())  sb.append("Video Games ");
        if (chkMusic.isSelected())  sb.append("Music ");
        if (chkSudoku.isSelected()) sb.append("Sudoku ");
        String favs = sb.toString().trim();
        tableModel.addRow(new Object[]{tableModel.getRowCount()+1, name, dob, gender, favs});
        DatabaseManager.insertData(name, dob, gender, favs);
        JOptionPane.showMessageDialog(frame, "Record saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void clearForm() {
        txtName.setText(""); calPicker.clear();
        genderGroup.clearSelection();
        for (JToggleButton b : new JToggleButton[]{btnMale,btnFemale,chkSports,chkGames,chkMusic,chkSudoku})
            { b.setSelected(false); b.repaint(); }
    }

    private void msg(String m) {
        JOptionPane.showMessageDialog(frame, m, "Required", JOptionPane.WARNING_MESSAGE);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  Inline Calendar Picker
    // ═════════════════════════════════════════════════════════════════════════
    static class CalendarPicker extends JPanel {
        private LocalDate selected;
        private YearMonth cur = YearMonth.now();
        private JButton trigger;
        private JWindow pop;
        private JPanel grid;
        private JLabel hdr;

        static final String[] DOW = {"Su","Mo","Tu","We","Th","Fr","Sa"};
        static final String[] MON = {"January","February","March","April","May","June",
                                      "July","August","September","October","November","December"};

        CalendarPicker() {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

            trigger = new JButton() {
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                    g2.setColor(BORDER); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                    g2.setFont(FONT_BODY);
                    String txt = selected==null ? "Select a date"
                        : selected.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                    g2.setColor(selected==null ? TEXT_HINT : TEXT_PRI);
                    g2.drawString(txt, 12, getHeight()/2+5);
                    // calendar icon (right side)
                    int ix=getWidth()-28, iy=getHeight()/2-8;
                    g2.setColor(TEXT_SEC); g2.setStroke(new BasicStroke(1.4f));
                    g2.drawRoundRect(ix,iy,16,15,3,3);
                    g2.drawLine(ix+4,iy-2,ix+4,iy+3); g2.drawLine(ix+12,iy-2,ix+12,iy+3);
                    g2.drawLine(ix,iy+5,ix+16,iy+5);
                    g2.dispose();
                }
                public Dimension getPreferredSize(){ return new Dimension(280,42); }
            };
            trigger.setOpaque(false); trigger.setContentAreaFilled(false);
            trigger.setBorderPainted(false); trigger.setFocusPainted(false);
            trigger.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            trigger.addActionListener(e -> toggle());
            add(trigger, BorderLayout.CENTER);
        }

        void toggle() {
            if (pop!=null && pop.isVisible()) { pop.dispose(); pop=null; return; }
            Window owner = SwingUtilities.getWindowAncestor(trigger);
            pop = new JWindow(owner);
            pop.setAlwaysOnTop(true);
            pop.setFocusableWindowState(true);
            JPanel outer = new JPanel(new BorderLayout()) {
                public void paintComponent(Graphics g) {
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                    g2.setColor(BORDER); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                    g2.dispose();
                }
            };
            outer.setOpaque(false); outer.setBorder(new EmptyBorder(12,12,14,12));

            // nav
            JPanel nav = new JPanel(new BorderLayout(8,0));
            nav.setOpaque(false);
            hdr = new JLabel("",JLabel.CENTER); hdr.setFont(FONT_BOLD); hdr.setForeground(TEXT_PRI);
            JButton prev = navArrow("‹"); JButton next = navArrow("›");
            prev.addActionListener(e->{ cur=cur.minusMonths(1); repopGrid(); });
            next.addActionListener(e->{ cur=cur.plusMonths(1); repopGrid(); });
            nav.add(prev,BorderLayout.WEST); nav.add(hdr,BorderLayout.CENTER); nav.add(next,BorderLayout.EAST);
            outer.add(nav,BorderLayout.NORTH);

            grid = new JPanel(new GridLayout(0,7,4,4));
            grid.setOpaque(false); grid.setBorder(new EmptyBorder(8,0,0,0));
            repopGrid();
            outer.add(grid,BorderLayout.CENTER);

            pop.add(outer); pop.pack();
            Point loc = trigger.getLocationOnScreen();
            pop.setLocation(loc.x, loc.y+trigger.getHeight()+4);
            pop.setVisible(true);

            Toolkit.getDefaultToolkit().addAWTEventListener(ev -> {
                if (ev instanceof MouseEvent me && me.getID()==MouseEvent.MOUSE_PRESSED)
                    if (pop!=null && pop.isVisible() && !pop.getBounds().contains(me.getLocationOnScreen()))
                        { pop.dispose(); pop=null; }
            }, AWTEvent.MOUSE_EVENT_MASK);
        }

        void repopGrid() {
            hdr.setText(MON[cur.getMonthValue()-1]+" "+cur.getYear());
            grid.removeAll();
            for (String d : DOW) {
                JLabel l=new JLabel(d,JLabel.CENTER); l.setFont(FONT_SMALL); l.setForeground(TEXT_HINT);
                grid.add(l);
            }
            int startDow = cur.atDay(1).getDayOfWeek().getValue() % 7;
            for (int i=0;i<startDow;i++) grid.add(new JLabel(""));
            LocalDate today = LocalDate.now();
            for (int d=1; d<=cur.lengthOfMonth(); d++) {
                LocalDate date = cur.atDay(d);
                boolean isTod = date.equals(today);
                boolean isSel = date.equals(selected);
                JButton b = new JButton(String.valueOf(d)) {
                    public void paintComponent(Graphics g) {
                        Graphics2D g2=(Graphics2D)g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                        Color textColor;
                        if (isSel) { g2.setColor(PURPLE); g2.fillOval(1,1,getWidth()-2,getHeight()-2); textColor = Color.WHITE; }
                        else if (isTod) { g2.setColor(PURPLE_LITE); g2.fillOval(1,1,getWidth()-2,getHeight()-2); textColor = PURPLE; }
                        else if (getBackground()!=null) { g2.setColor(getBackground()); g2.fillOval(1,1,getWidth()-2,getHeight()-2); textColor = TEXT_PRI; }
                        else { textColor = TEXT_PRI; }
                        g2.setColor(textColor);
                        g2.setFont(isSel||isTod ? FONT_BOLD : FONT_SMALL);
                        FontMetrics fm=g2.getFontMetrics();
                        g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                            (getHeight()+fm.getAscent()-fm.getDescent())/2);
                        g2.dispose();
                    }
                    public Dimension getPreferredSize(){ return new Dimension(32,32); }
                };
                b.setOpaque(false); b.setContentAreaFilled(false);
                b.setBorderPainted(false); b.setFocusPainted(false);
                b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                b.addMouseListener(new MouseAdapter(){
                    public void mouseEntered(MouseEvent e){ b.setBackground(PURPLE_LITE); b.repaint(); }
                    public void mouseExited(MouseEvent e) { b.setBackground(null);        b.repaint(); }
                });
                b.addActionListener(e -> {
                    selected = date; trigger.repaint();
                    if (pop!=null){ pop.dispose(); pop=null; }
                });
                grid.add(b);
            }
            grid.revalidate(); grid.repaint();
        }

        private JButton navArrow(String t) {
            JButton b = new JButton(t);
            b.setFont(new Font("Segoe UI",Font.PLAIN,20)); b.setForeground(TEXT_SEC);
            b.setOpaque(false); b.setContentAreaFilled(false);
            b.setBorderPainted(false); b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setPreferredSize(new Dimension(32,32));
            return b;
        }

        LocalDate getSelectedDate(){ return selected; }
        void clear(){ selected=null; trigger.repaint(); }
    }

    // ── Round border ──────────────────────────────────────────────────────────
    static class RoundBorder extends AbstractBorder {
        int r; Color c;
        RoundBorder(int r, Color c){ this.r=r; this.c=c; }
        public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r); g2.dispose();
        }
        public Insets getBorderInsets(Component c){ return new Insets(4,8,4,8); }
    }

    // ── Main ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored){}

        SwingUtilities.invokeLater(() -> {

            DefaultTableModel model =
                new DefaultTableModel(
                    new String[]{
                        "ID",
                        "Name",
                        "DOB",
                        "Gender",
                        "Favourites"
                    }, 0){

                    public boolean isCellEditable(
                            int r,
                            int c){

                        return false;
                    }
                };

            // Open records only
            new ViewDataGUI(model);
        });
    }
}