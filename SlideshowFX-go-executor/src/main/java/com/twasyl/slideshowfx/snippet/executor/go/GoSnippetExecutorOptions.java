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

package com.twasyl.slideshowfx.snippet.executor.go;

import com.twasyl.slideshowfx.snippet.executor.ISnippetExecutorOptions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Options that are necessary for the Go snippet executor.
 *
 * @author Thierry Wasylczenko
 * @version 1.0.0
 * @since 1.0.0
 */
public class GoSnippetExecutorOptions implements ISnippetExecutorOptions {
    private final ObjectProperty<File> goHome = new SimpleObjectProperty<>();

    public ObjectProperty<File> goHomeProperty() { return this.goHome; }

    public File getGoHome() { return this.goHome.get(); }

    public void setGoHome(File goHome) throws FileNotFoundException {
        if(goHome == null) throw new NullPointerException("The goHome can not be null");
        if(!goHome.exists()) throw new FileNotFoundException("The goHome doesn't exist");
        if(!goHome.isDirectory()) throw new IllegalArgumentException("The goHome is not a directory");

        this.goHome.setValue(goHome);
    }
}
