# Intermodal Transport Optimizer

![Java](https://img.shields.io/badge/Java-GUI-orange) ![Algorithms](https://img.shields.io/badge/Algorithms-Graph_Theory-blue) ![Data](https://img.shields.io/badge/Data-JSON_Processing-lightgrey)

## 📌 Project Overview
**Intermodal Transport Optimizer** is a Java-based desktop application designed to solve complex routing problems within a simulated country modeled as an **$N \times M$ city matrix**.

The system calculates optimal travel routes by combining multiple modes of transport (**Bus and Train**), allowing users to minimize travel time or cost. It features a graphical interface for route visualization, ticket purchasing simulation, and revenue analytics.

## 🏗 Technical Core
- **Graph Modeling:** The country is represented as a weighted directed graph where cities are nodes and transport lines (Bus/Train) are edges with specific weights (time, price).
- **Matrix Logic:** Custom implementation of an $N \times M$ grid system generated via external JSON configuration.
- **Pathfinding:** Algorithms to determine the most efficient path between coordinates $(x_1, y_1)$ and $(x_2, y_2)$ handling connection delays and transfer times.

## 🚀 Key Features
### 🗺️ Route Planning
- **Multi-Modal Transport:** Seamlessly combines bus and rail networks to find the best connection.
- **Optimization Criteria:** Users can prioritize **Fastest Route**, **Cheapest Route** or the **Least Transfers**.
- **Top 5 Alternatives:** Displays the best 5 route options with detailed breakdowns (transfers, wait times).

### 🎫 Booking & Simulation
- **Ticket Generation:** Simulates the purchase process by generating textual receipts (bills) stored locally.
- **Analytics Dashboard:** Tracks total tickets sold and cumulative revenue across application sessions.

### 💾 Data Persistence
- **JSON Integration:** Loads dynamic city and schedule data from `TransportDataGenerator`.
- **File I/O:** Automatic serialization of purchase records and logs.

## 🛠 Tech Stack
- **Language:** Java (JDK 17+)
- **UI Framework:** Java Swing / AWT
- **Data Format:** JSON
- **Concepts:** OOP, Graph Theory, Matrix Operations, Event-Driven Programming

## 📂 Project Structure
- `src/` - Core logic and GUI components
- `data/` - JSON configuration files for city matrices
- `receipts/` - Generated ticket files

---
*Developed by Boris Matić as a Computer Science project for "Programming Languages 2".*
