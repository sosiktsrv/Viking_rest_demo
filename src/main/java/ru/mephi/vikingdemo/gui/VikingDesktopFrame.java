package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.stream.Collectors;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private JTable vikingTable;

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

        vikingTable = new JTable(tableModel);
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

        JButton lambdaButton = new JButton("Lambda Operations");
        lambdaButton.addActionListener(e -> showLambdaDialog());

        JButton generateButton = new JButton("Generate N Vikings");
        generateButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter number of vikings to generate:");
            if (input != null) {
                try {
                    int count = Integer.parseInt(input);
                    for (int i = 0; i < count; i++) {
                        Viking viking = vikingService.createRandomViking();
                        tableModel.addViking(viking);
                    }
                    JOptionPane.showMessageDialog(this, "Generated " + count + " vikings!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number!");
                }
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(lambdaButton);
        bottomPanel.add(generateButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }

    public void deleteViking(int index) {
        tableModel.deleteViking(index);
    }

    public void updateViking(int index, Viking viking) {
        tableModel.updateViking(index, viking);
    }

    private void showLambdaDialog() {
        VikingLambdaService lambda = new VikingLambdaService(vikingService,
                new ru.mephi.vikingdemo.service.VikingFactory());

        String message = "1) Оценка объема выборки:\n" +
                "Возраст больше 30: " + lambda.countByAgeGreaterThan(30) + "\n" +
                "Возраст меньше 20: " + lambda.countByAgeLessThan(20) + "\n" +
                "Возраст от 20 до 40: " + lambda.countByAgeBetween(20, 40) + "\n" +
                "Возраст вне 20-40: " + lambda.countByAgeOutside(20, 40) + "\n" +
                "Борода LONG и волосы Red: " + lambda.countByBeardAndHair(BeardStyle.LONG, HairColor.Red) + "\n" +
                "Имеют 1 или 2 топора: " + lambda.countByAxesOneOrTwo() + "\n\n" +

                "2) Информация для вывода:\n" +
                "Случайный викинг ростом выше 180: " +
                (lambda.getRandomVikingHeightAbove180() != null ?
                        lambda.getRandomVikingHeightAbove180().name() : "нет") + "\n" +
                "Викинги с легендарным снаряжением: " +
                lambda.getVikingsWithLegendaryEquipment().size() + "\n" +
                "Рыжебородые по возрасту: " +
                lambda.getRedBeardedVikingsSortedByAge().stream()
                        .map(v -> v.name() + "(" + v.age() + ")")
                        .collect(Collectors.joining(", ")) + "\n\n" +

                "3) Операции с ID:\n" +
                "Максимальный ID: " + lambda.getMaxId() + "\n" +
                "Четные ID: " + lambda.getEvenIds() + "\n";

        JOptionPane.showMessageDialog(this, message, "Результаты",
                JOptionPane.INFORMATION_MESSAGE);
    }
}