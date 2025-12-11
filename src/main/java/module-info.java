module com.ingunal.brokendeal.beta {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires com.google.gson;

    // Abrir paquetes para JavaFX
    opens com.ingunal.brokendeal.beta to javafx.fxml;
    opens com.ingunal.brokendeal.beta.controller to javafx.fxml;
    
    // ← NUEVO: Abrir paquete DAO para Gson
    opens com.ingunal.brokendeal.beta.dao.dto to com.google.gson;
    
    // Exportar paquetes principales
    exports com.ingunal.brokendeal.beta;
    exports com.ingunal.brokendeal.beta.controller;
}
