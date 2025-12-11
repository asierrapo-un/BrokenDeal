module com.ingunal.brokendeal.beta {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;   // ← IMPORTANTE
    requires com.google.gson;

    opens com.ingunal.brokendeal.beta to javafx.fxml;
    opens com.ingunal.brokendeal.beta.controller to javafx.fxml;
    
    exports com.ingunal.brokendeal.beta;
    exports com.ingunal.brokendeal.beta.controller;
}
