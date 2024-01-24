package dev.archtech.geomapper.controller;

import dev.archtech.geomapper.model.RequestModel;
import dev.archtech.geomapper.view.RequestView;
import javafx.fxml.FXML;

public class RequestController {
    @FXML
    private RequestView requestView;
    @FXML
    public void initialize(){
        RequestModel model = new RequestModel();
        requestView.init(model);
    }
}
