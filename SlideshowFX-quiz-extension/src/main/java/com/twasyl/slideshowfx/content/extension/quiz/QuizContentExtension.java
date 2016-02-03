/*
 * Copyright 2016 Thierry Wasylczenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twasyl.slideshowfx.content.extension.quiz;

import com.twasyl.slideshowfx.beans.quiz.Quiz;
import com.twasyl.slideshowfx.content.extension.AbstractContentExtension;
import com.twasyl.slideshowfx.content.extension.ResourceType;
import com.twasyl.slideshowfx.content.extension.quiz.controllers.QuizContentExtensionController;
import com.twasyl.slideshowfx.markup.IMarkup;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link QuizContentExtension} extends the {@link AbstractContentExtension}. It allows to build a content containing
 * quiz to insert inside a SlideshowFX presentation.
 * This extension uses FontAwesome
 * This extension supports HTML markup language.
 *
 * @author Thierry Wasylczenko
 * @version 1.0
 * @since SlideshowFX 1.0.0
 */
public class QuizContentExtension extends AbstractContentExtension {
    private static final Logger LOGGER = Logger.getLogger(QuizContentExtension.class.getName());

    private QuizContentExtensionController controller;

    public QuizContentExtension() {
        super("QUIZ", QuizContentExtension.class.getResource("/com/twasyl/slideshowfx/content/extension/quiz/resources/quiz.zip"),
                FontAwesomeIcon.QUESTION,
                "Insert a quiz",
                "Insert a quiz");

        final String baseURL = "quiz/";

        // Add URL
        this.putResource(ResourceType.CSS_FILE, baseURL.concat("font-awesome-4.5.0/css/font-awesome.min.css"));
    }

    @Override
    public Pane getUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/com/twasyl/slideshowfx/content/extension/quiz/fxml/QuizContentExtension.fxml"));
        Pane root = null;

        try {
            loader.setClassLoader(getClass().getClassLoader());
            root = loader.load();
            this.controller = loader.getController();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can not load UI for QuizContentExtension", e);
        }

        return root;
    }

    @Override
    public String buildContentString(IMarkup markup) {
        return this.buildDefaultContentString();
    }

    @Override
    public String buildDefaultContentString() {
        final Quiz quiz = controller.getQuiz();

        final StringBuilder builder = new StringBuilder("<div id=\"quiz-").append(quiz.getId()).append("\" class=\"slideshowfx-quiz\" style=\"width: 100%\">\n");
        builder.append("\t<span id=\"").append(System.currentTimeMillis()).append("\" style=\"display: block; width: 100%; background-color: #ECECEC; border-radius: 10px 10px 0 0\">\n")
                .append("\t\t<span id=\"quiz-action-button-").append(quiz.getId()).append("\" onclick=\"javascript:quiz(this, '")
                .append(Base64.getEncoder().encodeToString(quiz.toJSONString().getBytes())).append("');\">")
                .append("<i class=\"fa fa-play fa-fw\" title=\"Start the quiz\"></i></span>\n\t\t&nbsp;")
                .append(quiz.getQuestion().getText()).append("\n\t</span>\n");
        builder.append("\t<ul>");

        quiz.getAnswers().forEach(answer -> {
            builder.append("\n\t\t<li>").append(answer.getText()).append("</li>");
        });

        builder.append("\n\t</ul>\n</div>");

        return builder.toString();
    }
}
