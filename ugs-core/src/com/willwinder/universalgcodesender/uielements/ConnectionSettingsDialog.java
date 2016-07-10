/*
 * Simple dialog for configuring Sender settings.
 */
/*
    Copywrite 2013-2016 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.willwinder.universalgcodesender.uielements;

import com.willwinder.universalgcodesender.i18n.Localization;
import com.willwinder.universalgcodesender.utils.FirmwareUtils;
import com.willwinder.universalgcodesender.utils.GUIHelpers;
import com.willwinder.universalgcodesender.utils.Settings;
import com.willwinder.universalgcodesender.utils.SettingsFactory;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author wwinder
 */
public class ConnectionSettingsDialog extends JDialog {
    private boolean saveChanges;
    Settings settings;
    
    /**
     * Creates new form GrblSettingsDialog
     */
    public ConnectionSettingsDialog(Settings settings, Frame parent, boolean modal) {
        super(parent, modal);
        this.settings = settings;

        // Register callbacks
        restore.addActionListener(this::restoreDefaultSettings);
        closeWithSave.addActionListener(this::closeWithSaveActionPerformed);
        closeWithoutSave.addActionListener(this::closeWithoutSaveActionPerformed);
        helpButton.addActionListener(this::helpButtonActionPerformed);
        
        initComponents();
        setLocationRelativeTo(parent);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        saveChanges = false;
    }
    
    /**
     * Return status.
     */
    public boolean saveChanges() {
        return saveChanges;
    }

    final JLabel titleLabel = new JLabel(Localization.getString("sender.header"));
    final JButton restore = new JButton(Localization.getString("restore"));
    final JButton closeWithSave = new JButton(Localization.getString("save.close"));
    final JButton closeWithoutSave = new JButton(Localization.getString("close"));
    final JButton helpButton = new JButton(Localization.getString("help"));
    final JScrollPane scrollPane = new JScrollPane();
    ConnectionSettingsPanel settingsPanel;

    private void initComponents() {
        settingsPanel = new ConnectionSettingsPanel(settings, FirmwareUtils.getConfigFiles());
        scrollPane.setViewportView(settingsPanel);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        titleLabel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        setLayout(new MigLayout("fillx"));
        add(titleLabel, "wrap, span 4");
        add(scrollPane, "wrap, span 4, growx");
        add(helpButton);
        add(restore);
        add(closeWithoutSave);
        add(closeWithSave);

        pack();
    }

    private void closeWithoutSaveActionPerformed(java.awt.event.ActionEvent evt) {
        this.saveChanges = false;
        setVisible(false);
    }

    private void restoreDefaultSettings(java.awt.event.ActionEvent evt) {
        try {
            FirmwareUtils.restoreDefaults();
        } catch (Exception e) {
            GUIHelpers.displayErrorDialog("An error occurred while trying to restore defailts: " + e.getLocalizedMessage());
        }
        settingsPanel.updateComponents(new Settings());
    }

    private void closeWithSaveActionPerformed(java.awt.event.ActionEvent evt) {
        this.settingsPanel.updateSettingsObject();
        this.saveChanges = true;
        setVisible(false);
    }

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {
        StringBuilder message = new StringBuilder()
                .append(Localization.getString("sender.help.speed.override")).append("\n\n")
                .append(Localization.getString("sender.help.speed.percent")).append("\n\n")
                .append(Localization.getString("sender.help.command.length")).append("\n\n")
                .append(Localization.getString("sender.help.truncate")).append("\n\n")
                .append(Localization.getString("sender.help.singlestep")).append("\n\n")
                .append(Localization.getString("sender.help.whitespace")).append("\n\n")
                .append(Localization.getString("sender.help.status")).append("\n\n")
                .append(Localization.getString("sender.help.status.rate")).append("\n\n")
                .append(Localization.getString("sender.help.state")).append("\n\n")
                .append(Localization.getString("sender.help.arcs")).append("\n\n")
                .append(Localization.getString("sender.help.arcs.length")).append("\n\n")
                .append(Localization.getString("sender.help.autoconnect"))
                //.append(Localization.getString("sender.help.autoreconnect"))
                ;
        
        JOptionPane.showMessageDialog(new JFrame(), 
                message, 
                Localization.getString("sender.help.dialog.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }
}
