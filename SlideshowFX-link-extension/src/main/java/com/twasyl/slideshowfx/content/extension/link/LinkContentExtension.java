package com.twasyl.slideshowfx.content.extension.link;

import com.twasyl.slideshowfx.content.extension.AbstractContentExtension;
import com.twasyl.slideshowfx.content.extension.link.controllers.LinkContentExtensionController;
import com.twasyl.slideshowfx.markup.IMarkup;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The LinkContentExtension extends the AbstractContentExtension. It allows to build a content containing links to insert
 * inside a SlideshowFX presentation.
 * This extension doesn't use other resources
 * This extension supports HTML and Textile markup languages.
 *
 * @author Thierry Wasylczenko
 * @version 1.0
 * @since SlideshowFX 1.0
 */
public class LinkContentExtension extends AbstractContentExtension {
    private static final Logger LOGGER = Logger.getLogger(LinkContentExtension.class.getName());

    private LinkContentExtensionController controller;

    public LinkContentExtension() {
        super("LINK", null,
                FontAwesomeIcon.LINK,
                "Insert a link",
                "Insert a link");
    }

    @Override
    public Pane getUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/com/twasyl/slideshowfx/content/extension/link/fxml/LinkContentExtension.fxml"));
        Pane root = null;

        try {
            loader.setClassLoader(getClass().getClassLoader());
            root = loader.load();
            this.controller = loader.getController();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can not load UI for LinkContentExtension", e);
        }

        return root;
    }

    @Override
    public String buildContentString(IMarkup markup) {
        final StringBuilder builder = new StringBuilder();

        if(markup == null || "HTML".equals(markup.getCode())) {
            builder.append(this.buildDefaultContentString());
        } else if("TEXTILE".equals(markup.getCode())) {
            builder.append("\"")
                    .append(this.controller.getText() == null || this.controller.getText().trim().isEmpty() ?
                            this.controller.getAddress() : this.controller.getText())
                    .append("\":").append(this.controller.getAddress());
        } else {
            builder.append(this.buildDefaultContentString());
        }

        return builder.toString();
    }

    @Override
    public String buildDefaultContentString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("<a href=\"").append(this.controller.getAddress()).append("\">")
                .append(this.controller.getText() == null || this.controller.getText().trim().isEmpty() ?
                                this.controller.getAddress() : this.controller.getText())
                .append("</a>");

        return builder.toString();
    }
}
