module org.example.my.task.generatenumberfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.my.task.generatenumberfx to javafx.fxml;
    exports org.example.my.task.generatenumberfx;
}