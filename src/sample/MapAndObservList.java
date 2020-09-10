package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public class MapAndObservList implements Serializable {

    private ObservableList<String> langs = FXCollections.observableArrayList();
    private Map<String, KeyPair> map = new HashMap<>();

    public ObservableList<String> getlangs() {
        return langs;
    }

    public Map<String, KeyPair> getMap() {
        return map;
    }
}
