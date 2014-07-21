/*
 * Copyright 2014 Thierry Wasylczenko
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

package com.twasyl.slideshowfx.controls.tree;

import com.twasyl.slideshowfx.controls.Dialog;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to create TreeCell for a TreeView containing files.
 *
 * @author Thierry Wasylczenko
 */
public class FileTreeCell extends TreeCell<File> {
    private static final Logger LOGGER = Logger.getLogger(FileTreeCell.class.getName());

    public FileTreeCell() {
        super();

        final ContextMenu contextMenu = new ContextMenu();

        final MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(event -> {
            try {
                final TextField textField = new TextField();
                final Label label = new Label("New name: ");
                final HBox hbox = new HBox(5);
                hbox.getChildren().addAll(label, textField);

                if(Dialog.showDialog(null, "Rename", hbox) == Dialog.Response.OK) {
                    ((TemplateTreeView) this.getTreeView()).renameContentOfTreeView(this.getTreeItem(), textField.getText());
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Can not rename item", e);
            }
        });

        final MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            try {
                ((TemplateTreeView) this.getTreeView()).deleteContentOfTreeView(this.getTreeItem());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Can not delete item", e);
            }
        });

        this.setContextMenu(contextMenu);

        this.treeItemProperty().addListener((value, oldValue, newValue) -> {
            if(newValue != null) {
                contextMenu.getItems().clear();

                final TemplateTreeView ttv = (TemplateTreeView) this.getTreeView();

                if(ttv.isItemRenamingAllowed(newValue)) contextMenu.getItems().add(renameItem);
                else contextMenu.getItems().remove(renameItem);

                if(ttv.isItemDeletionEnabled(newValue)) contextMenu.getItems().add(deleteItem);
                else contextMenu.getItems().remove(deleteItem);
            }
        });
    }

    @Override
    protected void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setText("");
        } else if(item == null) {
            setText("null");
        } else {
            /**
             * The drag is only allowed if the file is a directory
             */
            if(item.isDirectory()) {
                setOnDragOver(((TemplateTreeView) getTreeView()).getOnDragOverItem());
                setOnDragDropped(((TemplateTreeView) getTreeView()).getOnDragDroppedItem());
                setOnDragDone(((TemplateTreeView) getTreeView()).getOnDragDoneItem());
                setOnDragExited(((TemplateTreeView) getTreeView()).getOnDragExitedItem());
            }

            /**
             * If this TreeItem is the root of the TreeView, display "/" otherwise display the name of the file.
             */
            if(getTreeView().getRoot() == getTreeItem()) {
                setText("/");
            } else {
                setText(item.getName());
            }
        }
    }
}
