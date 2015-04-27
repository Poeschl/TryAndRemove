/*
 * Copyright (c) 2015 Markus Poeschl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.poeschl.apps.tryandremove.models;

/**
 * Representation of an navigation drawer item. It is generalised to provide items with different targets.
 * Created by Markus Pöschl on 20.01.15.
 */
public class MenuItem<T> {

    private T target;
    private String title;
    private int iconResource;

    /**
     * Creates a new item.
     *
     * @param title        The title of the item.
     * @param iconResource The icon resource ot the item. Can be <code>-1</code> to create a item without icon.
     */
    public MenuItem(String title, int iconResource, T target) {
        this.title = title;
        this.iconResource = iconResource;
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Receives the item icon. Can be <code>-1</code> if no icon is specified.
     *
     * @return The icon as resource int.
     */
    public int getIconResource() {
        return iconResource;
    }

    /**
     * Set the icon of the item.
     *
     * @param iconResource The resource int of the item or <code>-1</code> for a item without icon.
     */
    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }
}
