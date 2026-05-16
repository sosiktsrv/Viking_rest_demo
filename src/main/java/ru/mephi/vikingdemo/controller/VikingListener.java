package ru.mephi.vikingdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.SwingUtilities;

@Component
public class VikingListener {
    private final VikingService service;
    private VikingDesktopFrame gui;

    @Autowired
    public VikingListener(VikingService service) {
        this.service = service;
    }

    public void setGui(VikingDesktopFrame gui){
        this.gui = gui;
    }

    public void addVikingAndRefresh(Viking viking) {
        service.addSpecificViking(viking);
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.addNewViking(viking));
        }
    }

    public void deleteVikingAndRefresh(int index) {
        service.deleteViking(index);
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.deleteViking(index));
        }
    }

    public void updateVikingAndRefresh(int index, Viking viking) {
        service.updateViking(index, viking);
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.updateViking(index, viking));
        }
    }

    public void generateAndRefresh(int count) {
        for (int i = 0; i < count; i++) {
            Viking viking = service.createRandomViking();
            if (gui != null) {
                SwingUtilities.invokeLater(() -> gui.addNewViking(viking));
            }
        }
    }

    void testAdd() {
        gui.addNewViking(service.createRandomViking());
    }
}