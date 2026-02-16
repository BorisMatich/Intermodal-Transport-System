package com.example.projekat2025.handler;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.function.BiConsumer;

public class NodeClickHandler {

    private final Graph graph;
    private final BiConsumer<String, String> onSelectionComplete;

    private String selectedStartId = null;
    private String selectedEndId = null;

    public NodeClickHandler(Graph graph, BiConsumer<String, String> onSelectionComplete) {
        this.graph = graph;
        this.onSelectionComplete = onSelectionComplete;
    }

    public void handle(String nodeId) {
        if (selectedStartId == null) {
            selectStart(nodeId);
        } else if (selectedEndId == null) {
            if (nodeId.equals(selectedStartId)) {
                return; // Isti čvor, ignoriši
            }
            selectEnd(nodeId);
            onSelectionComplete.accept(selectedStartId, selectedEndId);
        } else {
            reset();
            selectStart(nodeId);
        }
    }

    private void selectStart(String nodeId) {
        selectedStartId = nodeId;
        setNodeClass(nodeId, "selectedStart");
    }

    private void selectEnd(String nodeId) {
        selectedEndId = nodeId;
        setNodeClass(nodeId, "selectedEnd");
    }

    public void reset() {
        if (selectedStartId != null) {
            setNodeClass(selectedStartId, null);
        }
        if (selectedEndId != null) {
            setNodeClass(selectedEndId, null);
        }
        selectedStartId = null;
        selectedEndId = null;
    }

    private void setNodeClass(String nodeId, String cssClass) {
        Node node = graph.getNode(nodeId);
        if (node != null) {
            if (cssClass == null) {
                node.removeAttribute("ui.class");
            } else {
                node.setAttribute("ui.class", cssClass);
            }
        }
    }

    public String getSelectedStartId() {
        return selectedStartId;
    }

    public String getSelectedEndId() {
        return selectedEndId;
    }
}