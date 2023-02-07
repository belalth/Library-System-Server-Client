module com.librarysytsem {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.librarysytsem to javafx.fxml;
    exports com.librarysytsem;

    exports  com.librarysytsem.DataBase ;


}
