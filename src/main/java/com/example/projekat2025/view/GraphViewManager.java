package com.example.projekat2025.view;

import com.example.projekat2025.handler.NodeClickHandler;
import com.example.projekat2025.style.GraphStyleSheet;
import com.transport.model.Departure;
import com.transport.model.Station;

import javafx.scene.layout.Pane;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class GraphViewManager {

    private final Pane container;
    private final BiConsumer<String, String> onSelectionComplete;

    private Graph graph;
    private NodeClickHandler clickHandler;
    private FxViewer viewer;
    private ViewerPipe pipe;


    public GraphViewManager(Pane container, BiConsumer<String, String> onSelectionComplete) {
        this.container = container;
        this.onSelectionComplete = onSelectionComplete;
    }

    public void buildGraph(List<Station> stations, List<Departure> departures) {
        System.setProperty("org.graphstream.ui", "javafx");

        graph = new SingleGraph("Transport");
        graph.setAttribute("ui.stylesheet", GraphStyleSheet.get());

        addNodes(stations);
        addEdges(departures);

        clickHandler = new NodeClickHandler(graph, onSelectionComplete);

        embedInPane();
    }

    private void addNodes(List<Station> stations) {
        Set<String> addedCities = new HashSet<>();

        for (Station station : stations) {
            String cityId = station.getCity();
            if (!addedCities.contains(cityId)) {
                graph.addNode(cityId).setAttribute("ui.label", cityId);
                addedCities.add(cityId);
            }
        }
    }

    private void addEdges(List<Departure> departures) {
        Set<String> addedEdges = new HashSet<>();

        for (Departure dep : departures) {
            String fromCity = dep.getFrom().getCity();
            String toCity = dep.getTo().getCity();

            if (fromCity.equals(toCity)) continue;

            String edgeId = createEdgeId(fromCity, toCity);
            if (!addedEdges.contains(edgeId)) {
                graph.addEdge(edgeId, fromCity, toCity);
                addedEdges.add(edgeId);
            }
        }
    }

    private String createEdgeId(String from, String to) {
        // Sortiraj da A-B i B-A budu isti edge
        return from.compareTo(to) < 0 ? from + "-" + to : to + "-" + from;
    }


    private void embedInPane() {
        viewer = new FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        FxViewPanel viewPanel = (FxViewPanel) viewer.addDefaultView(false);
        viewPanel.prefWidthProperty().bind(container.widthProperty());
        viewPanel.prefHeightProperty().bind(container.heightProperty());

        // Listener za klikove
        pipe = viewer.newViewerPipe();
        pipe.addViewerListener(new ViewerListener() {
            @Override
            public void viewClosed(String viewName) {}

            @Override
            public void buttonPushed(String id) {
                javafx.application.Platform.runLater(() -> clickHandler.handle(id));
            }

            @Override
            public void buttonReleased(String id) {}

            @Override
            public void mouseOver(String id) {}

            @Override
            public void mouseLeft(String id) {}
        });

        // Pump events
        startEventPump();

        container.getChildren().add(viewPanel);
    }

    private void startEventPump() {
        Thread pumpThread = new Thread(() -> {
            while (true) {
                try {
                    pipe.pump();
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        pumpThread.setDaemon(true);
        pumpThread.start();
    }

    public void highlightRoute(List<Departure> route) {
        // Resetuj sve
        graph.edges().forEach(e -> e.removeAttribute("ui.class"));
        graph.nodes().forEach(n -> {
            if (!"selectedStart".equals(n.getAttribute("ui.class")) &&
                    !"selectedEnd".equals(n.getAttribute("ui.class"))) {
                n.removeAttribute("ui.class");
            }
        });

        // Highlight čvorove i ivice na ruti
        for (Departure dep : route) {
            String fromCity = dep.getFrom().getCity();
            String toCity = dep.getTo().getCity();

            graph.getNode(fromCity).setAttribute("ui.class", "onRoute");
            graph.getNode(toCity).setAttribute("ui.class", "onRoute");

            String edgeId = createEdgeId(fromCity, toCity);
            if (graph.getEdge(edgeId) != null) {
                graph.getEdge(edgeId).setAttribute("ui.class", "onRoute");
            }
        }
    }

    public void resetSelection() {
        if (clickHandler != null) {
            clickHandler.reset();
        }
        // Resetuj CSS klase na svim čvorovima
        graph.nodes().forEach(n -> n.removeAttribute("ui.class"));

        // Resetuj CSS klase na svim edgama
        graph.edges().forEach(e -> e.removeAttribute("ui.class"));
    }
}