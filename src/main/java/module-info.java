module com.sysedit {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires transitive java.desktop;
    requires javafx.swing;

    opens com.sysedit to javafx.fxml;
    exports com.sysedit;
}
