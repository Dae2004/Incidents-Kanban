// Java module descriptor — declares required modules for JavaFX, JDBC, SQLite, and SLF4J.
module com.helpdeskflow {
    requires javafx.controls;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;

    exports com.helpdeskflow;
}
