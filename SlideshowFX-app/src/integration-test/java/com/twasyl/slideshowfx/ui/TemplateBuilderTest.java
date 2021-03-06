package com.twasyl.slideshowfx.ui;

import com.twasyl.slideshowfx.controllers.TemplateBuilderController;
import com.twasyl.slideshowfx.engine.template.TemplateEngine;
import com.twasyl.slideshowfx.utils.ResourceHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author Thierry Wasylczenko
 */
public class TemplateBuilderTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final TemplateEngine template = new TemplateEngine();
        template.loadArchive(new File("examples/templates/dark-template.sfxt"));

        final FXMLLoader loader = new FXMLLoader(ResourceHelper.getURL("/com/twasyl/slideshowfx/fxml/TemplateBuilder.fxml"));
        final Parent root = loader.load();

        final TemplateBuilderController controller = loader.getController();
        controller.setTemplateEngine(template);

        final Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Template builder");
        primaryStage.getIcons().addAll(
                new Image(ResourceHelper.getInputStream("/com/twasyl/slideshowfx/images/appicons/16.png")),
                new Image(ResourceHelper.getInputStream("/com/twasyl/slideshowfx/images/appicons/32.png")),
                new Image(ResourceHelper.getInputStream("/com/twasyl/slideshowfx/images/appicons/64.png")),
                new Image(ResourceHelper.getInputStream("/com/twasyl/slideshowfx/images/appicons/128.png")),
                new Image(ResourceHelper.getInputStream("/com/twasyl/slideshowfx/images/appicons/256.png")),
                new Image(ResourceHelper.getInputStream("/com/twasyl/slideshowfx/images/appicons/512.png")));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
