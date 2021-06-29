package PO;

public enum MouseMode {
    AddNode("Dodawania"),
    PickNode("Wybierania"),
    DeleteNode("Usuwania"),
    JoinPickNode("Łączenia"),
    JoinNode("Łączenia"),
    DragMode("Wybierania"),
    WaitMode("Oczekiwania");

    public final String label;

    MouseMode(String name) {
        label="Tryb myszy: " + name;
    }
}
