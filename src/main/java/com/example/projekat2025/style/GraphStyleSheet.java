package com.example.projekat2025.style;

public final class GraphStyleSheet {

    private GraphStyleSheet() {}

    public static String get() {
        return """
            graph {
                fill-color: #f0f0f0;
                padding: 50px;
            }
            node {
                size: 30px;
                fill-color: #4a90d9;
                text-size: 12px;
                text-alignment: above;
                stroke-mode: plain;
                stroke-color: #2c5aa0;
                stroke-width: 2px;
            }
            node.selectedStart {
                fill-color: #27ae60;
                size: 40px;
            }
            node.selectedEnd {
                fill-color: #e74c3c;
                size: 40px;
            }
            node.onRoute {
                fill-color: #f39c12;
                size: 35px;
            }
            edge {
                fill-color: #888888;
                size: 1px;
            }
            edge.onRoute {
                fill-color: #e74c3c;
                size: 3px;
            }
        """;
    }
}