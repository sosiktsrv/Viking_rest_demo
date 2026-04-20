package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int row = vikingTable.getSelectedRow();
            if (row >= 0) {
                vikingService.deleteViking(row);
                tableModel.deleteViking(row);
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            int row = vikingTable.getSelectedRow();
            if (row >= 0) {
                Viking old = vikingService.findAll().get(row);

                String newName = JOptionPane.showInputDialog("New name:", old.name());
                if (newName == null) return;

                String newAge = JOptionPane.showInputDialog("New age:", old.age());
                if (newAge == null) return;

                String newHeight = JOptionPane.showInputDialog("New height (cm):", old.heightCm());
                if (newHeight == null) return;

                Viking updated = new Viking(
                        newName,
                        Integer.parseInt(newAge),
                        Integer.parseInt(newHeight),
                        old.hairColor(),
                        old.beardStyle(),
                        old.equipment()
                );

                vikingService.updateViking(row, updated);
                tableModel.updateViking(row, updated);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(createButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(updateButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }
    
    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }
}
