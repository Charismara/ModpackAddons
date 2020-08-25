/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Gui Widget for a Scrollable List
 *
 * @author CDAGaming
 */
public class ScrollableListControl extends ExtendedList<ScrollableListControl.StringEntry> {
    /**
     * The Currently Selected Value in the List
     */
    public String currentValue;

    /**
     * The Items available to select within the List Gui
     */
    public List<String> itemList;

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param mc           The Minecraft Instance for this Control
     * @param width        The Width of this Control
     * @param height       The Height of this Control
     * @param topIn        How far from the top of the Screen the List should render at
     * @param bottomIn     How far from the bottom of the Screen the List should render at
     * @param slotHeightIn The height of each slot in the list
     * @param itemList     The List of items to allocate for the slots in the Gui
     * @param currentValue The current value, if any, to select upon initialization of the Gui
     */
    public ScrollableListControl(Minecraft mc, int width, int height, int topIn, int bottomIn, int slotHeightIn, List<String> itemList, String currentValue) {
        super(mc, width, height, topIn, bottomIn, slotHeightIn);
        this.itemList = itemList;
        this.currentValue = currentValue;

        updateEntries();
    }

    @Override
    public void setSelected(StringEntry entry) {
        super.setSelected(entry);
        if (entry != null) {
            currentValue = entry.name;
        }
    }

    /**
     * Retrieves the Amount of Items in the List
     *
     * @return The Amount of Items in the List
     */
    @Override
    protected int getItemCount() {
        return itemList.size();
    }

    /**
     * Renders the Background for this Control
     */
    @Override
    protected void renderBackground(@Nonnull MatrixStack matrixStack) {
        if (getItemCount() != getEventListeners().size()) {
            clearEntries();
            updateEntries();
        }
    }

    /**
     * Updates the Entries in this List
     */
    public void updateEntries() {
        for (String item : itemList) {
            StringEntry dataEntry = new StringEntry(item);
            this.addEntry(dataEntry);
            if (item.equals(currentValue)) {
                this.setSelected(dataEntry);
            }
        }

        if (this.getSelected() != null) {
            this.centerScrollOn(this.getSelected());
        }
    }

    /**
     * Gui Entry for a Scrollable List
     *
     * @author CDAGaming
     */
    public class StringEntry extends ExtendedList.AbstractListEntry<StringEntry> {
        /**
         * The name of this Entry
         */
        private final String name;

        /**
         * Initialization Event for this Control, assigning defined arguments
         *
         * @param name The name to assign to this Entry
         */
        public StringEntry(String name) {
            this.name = name;
        }

        /**
         * Renders this Entry to the List
         *
         * @param matrices    The Matrix Stack, used for Rendering
         * @param index       The Index of the Entry within the List
         * @param y           The Y Coordinate to render at
         * @param x           The X Coordinate to render at
         * @param entryWidth  The specified Entry's Width
         * @param entryHeight The specified Entry's Height
         * @param mouseX      The Event Mouse X Coordinate
         * @param mouseY      The Event Mouse Y Coordinate
         * @param hovered     Whether the specified entry is currently hovered over
         * @param tickDelta   The Rendering Tick Rate
         */
        @Override
        public void render(@Nonnull MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            CraftPresence.instance.fontRenderer.drawStringWithShadow(matrices, name, x, y, 0xFFFFFF);
        }

        /**
         * Event to trigger upon the mouse being clicked
         *
         * @param mouseX The Event Mouse X Coordinate
         * @param mouseY The Event Mouse Y Coordinate
         * @param button The Event Mouse Button Clicked
         * @return The Event Result
         */
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.onPressed();
                return true;
            } else {
                return false;
            }
        }

        /**
         * The Event to occur when this Entry is pressed
         */
        private void onPressed() {
            ScrollableListControl.this.setSelected(this);
        }
    }
}
