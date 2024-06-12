module com.sysedit {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires transitive java.desktop;

    opens com.sysedit to javafx.fxml;
    exports com.sysedit;
}
