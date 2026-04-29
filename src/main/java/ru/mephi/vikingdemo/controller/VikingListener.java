package ru.mephi.vikingdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.SwingUtilities;
import java.util.List;

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

    public void refreshGui() {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> {
                List<Viking> vikings = service.findAll();
                gui.refreshTable(vikings);
            });
        }
    }

    public void addVikingAndRefresh(Viking viking) {
        service.addSpecificViking(viking);
        refreshGui();
    }

    public void deleteVikingAndRefresh(int index) {
        service.deleteViking(index);
        refreshGui();
    }

    public void updateVikingAndRefresh(int index, Viking viking) {
        service.updateViking(index, viking);
        refreshGui();
    }

    void testAdd() {
        gui.addNewViking(service.createRandomViking());
    }
}