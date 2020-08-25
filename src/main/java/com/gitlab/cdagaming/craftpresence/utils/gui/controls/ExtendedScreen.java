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
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * An Extended and Globalized Gui Screen
 *
 * @author CDAGaming
 */
public class ExtendedScreen extends Screen {
    /**
     * The Parent or Past Screen
     */
    public final Screen parentScreen;
    /**
     * The Current Screen Instance
     */
    public final Screen currentScreen;
    /**
     * Similar to buttonList, a list of compatible controls in this Screen
     */
    private final List<AbstractGui> extendedControls = Lists.newArrayList();
    /**
     * Similar to buttonList, a list of compatible ScrollLists in this Screen
     */
    private final List<ScrollableListControl> extendedLists = Lists.newArrayList();
    /**
     * Variable Needed to ensure all buttons are initialized before rendering to prevent an NPE
     */
    private boolean initialized = false;

    /**
     * The Last Ticked Mouse X Coordinate
     */
    private int lastMouseX = 0;
    /**
     * The Last Ticked Mouse Y Coordinate
     */
    private int lastMouseY = 0;

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param parentScreen The Parent Screen for this Instance
     */
    public ExtendedScreen(Screen parentScreen) {
        super(new StringTextComponent(""));
        minecraft = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    /**
     * Pre-Initializes this Screen
     * <p>
     * Responsible for Setting preliminary data
     */
    @Override
    public void init() {
        // Clear Data before Initialization
        buttons.clear();
        extendedControls.clear();
        extendedLists.clear();

        minecraft.keyboardListener.enableRepeatEvents(true);
        initializeUi();
        super.init();
        initialized = true;
    }

    /**
     * Initializes this Screen
     * <p>
     * Responsible for setting initial Data and creating controls
     */
    public void initializeUi() {
        // N/A
    }

    /**
     * Event to trigger upon Window Resize
     *
     * @param mcIn The Minecraft Instance
     * @param w    The New Screen Width
     * @param h    The New Screen Height
     */
    @Override
    public void resize(@Nonnull Minecraft mcIn, int w, int h) {
        initialized = false;
        super.resize(mcIn, w, h);
    }

    /**
     * Adds a Compatible Button to this Screen with specified type
     *
     * @param buttonIn The Button to add to this Screen
     * @param <T>      The Button's Class Type
     * @return The added button with attached class type
     */
    @Nonnull
    @Override
    protected <T extends Widget> T addButton(@Nonnull T buttonIn) {
        return addControl(buttonIn);
    }

    /**
     * Adds a Compatible Control to this Screen with specified type
     *
     * @param buttonIn The Control to add to this Screen
     * @param <T>      The Control's Class Type
     * @return The added control with attached class type
     */
    @Nonnull
    protected <T extends AbstractGui> T addControl(@Nonnull T buttonIn) {
        if (buttonIn instanceof Button && !buttons.contains(buttonIn)) {
            buttons.add((Button) buttonIn);
        }
        if (!extendedControls.contains(buttonIn)) {
            extendedControls.add(buttonIn);
        }

        return buttonIn;
    }

    /**
     * Adds a Compatible Scroll List to this Screen with specified type
     *
     * @param buttonIn The Scroll List to add to this Screen
     * @param <T>      The Scroll List's Class Type
     * @return The added scroll list with attached class type
     */
    @Nonnull
    protected <T extends ScrollableListControl> T addList(@Nonnull T buttonIn) {
        if (!extendedLists.contains(buttonIn)) {
            extendedLists.add(buttonIn);
        }

        return buttonIn;
    }

    /**
     * Preliminary Drawing Event
     * <p>
     * Primarily used for rendering before other elements
     */
    public void preDraw() {
        if (initialized) {
            CraftPresence.GUIS.drawBackground(width, height);
        }
    }

    /**
     * Renders this Screen, including controls and post-Hover Events
     *
     * @param matrixStack  The Matrix Stack, used for Rendering
     * @param mouseX       The Event Mouse X Coordinate
     * @param mouseY       The Event Mouse Y Coordinate
     * @param partialTicks The Rendering Tick Rate
     */
    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Ensures initialization events have run first, preventing an NPE
        if (initialized) {
            for (ScrollableListControl listControl : extendedLists) {
                listControl.render(matrixStack, mouseX, mouseY, partialTicks);
            }

            for (AbstractGui extendedControl : extendedControls) {
                if (extendedControl instanceof ExtendedButtonControl) {
                    final ExtendedButtonControl button = (ExtendedButtonControl) extendedControl;
                    button.render(matrixStack, mouseX, mouseY, partialTicks);
                }
                if (extendedControl instanceof ExtendedTextControl) {
                    final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                    textField.render(matrixStack, mouseX, mouseY, partialTicks);
                }
            }

            super.render(matrixStack, mouseX, mouseY, partialTicks);

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            for (AbstractGui extendedControl : extendedControls) {
                if (extendedControl instanceof ExtendedButtonControl) {
                    final ExtendedButtonControl extendedButton = (ExtendedButtonControl) extendedControl;
                    if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, extendedButton)) {
                        extendedButton.onHover();
                    }
                }
            }
        }
    }

    /**
     * Event to trigger upon Pressing a Key
     *
     * @param keyCode The KeyCode entered, if any
     * @param mouseX  The Event Mouse X Coordinate
     * @param mouseY  The Event Mouse Y Coordinate
     * @return The Event Result
     */
    @Override
    public boolean keyPressed(int keyCode, int mouseX, int mouseY) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
        }

        for (AbstractGui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedButtonControl) {
                final ExtendedButtonControl button = (ExtendedButtonControl) extendedControl;
                button.keyPressed(keyCode, mouseX, mouseY);
            }
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.keyPressed(keyCode, mouseX, mouseY);
            }
        }
        return super.keyPressed(keyCode, mouseX, mouseY);
    }

    /**
     * Event to trigger upon Typing a Character
     *
     * @param typedChar The typed Character, if any
     * @param keyCode   The KeyCode entered, if any
     * @return The Event Result
     */
    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        for (ScrollableListControl listControl : extendedLists) {
            listControl.charTyped(typedChar, keyCode);
        }

        for (AbstractGui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedButtonControl) {
                final ExtendedButtonControl button = (ExtendedButtonControl) extendedControl;
                button.charTyped(typedChar, keyCode);
            }
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.charTyped(typedChar, keyCode);
            }
        }
        return super.charTyped(typedChar, keyCode);
    }

    /**
     * Event to trigger upon the mouse being clicked
     *
     * @param mouseX      The Event Mouse X Coordinate
     * @param mouseY      The Event Mouse Y Coordinate
     * @param mouseButton The Event Mouse Button Clicked
     * @return The Event Result
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (ScrollableListControl listControl : extendedLists) {
            listControl.mouseClicked(mouseX, mouseY, mouseButton);
        }

        for (AbstractGui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedButtonControl) {
                final ExtendedButtonControl button = (ExtendedButtonControl) extendedControl;
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Event to trigger upon scrolling the mouse
     *
     * @param mouseX       The Event Mouse X Coordinate
     * @param mouseY       The Event Mouse Y Coordinate
     * @param scrollAmount The Scroll Amount
     * @return The Event Result
     */
    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double scrollAmount) {
        for (ScrollableListControl listControl : extendedLists) {
            listControl.mouseScrolled(mouseX, mouseY, scrollAmount);
        }

        for (AbstractGui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedButtonControl) {
                final ExtendedButtonControl button = (ExtendedButtonControl) extendedControl;
                button.mouseScrolled(mouseX, mouseY, scrollAmount);
            }
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.mouseScrolled(mouseX, mouseY, scrollAmount);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollAmount);
    }

    /**
     * Event to Trigger upon Dragging the Mouse
     *
     * @param mouseX      The Event Mouse X Coordinate
     * @param mouseY      The Event Mouse Y Coordinate
     * @param mouseButton The Event Mouse Button Clicked
     * @param scrollX     The Scroll Amount for the Mouse X Coordinate
     * @param scrollY     The Scroll Amount for the Mouse Y Coordinate
     * @return The Event Result
     */
    @Override
    public boolean mouseDragged(final double mouseX, final double mouseY, final int mouseButton, final double scrollX, final double scrollY) {
        for (ScrollableListControl listControl : extendedLists) {
            listControl.mouseDragged(mouseX, mouseY, mouseButton, scrollX, scrollY);
        }

        for (AbstractGui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedButtonControl) {
                final ExtendedButtonControl button = (ExtendedButtonControl) extendedControl;
                button.mouseDragged(mouseX, mouseY, mouseButton, scrollX, scrollY);
            }
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.mouseDragged(mouseX, mouseY, mouseButton, scrollX, scrollY);
            }
        }
        return super.mouseDragged(mouseX, mouseY, mouseButton, scrollX, scrollY);
    }

    /**
     * Event to Trigger upon Releasing the Mouse
     *
     * @param mouseX      The Event Mouse X Coordinate
     * @param mouseY      The Event Mouse Y Coordinate
     * @param mouseButton The Event Mouse Button Clicked
     * @return The Event Result
     */
    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        for (ScrollableListControl listControl : extendedLists) {
            listControl.mouseReleased(mouseX, mouseY, mouseButton);
        }

        for (AbstractGui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedButtonControl) {
                final ExtendedButtonControl button = (ExtendedButtonControl) extendedControl;
                button.mouseReleased(mouseX, mouseY, mouseButton);
            }
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    /**
     * Event to trigger on each tick
     */
    @Override
    public void tick() {
        for (AbstractGui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.tick();
            }
        }
    }

    /**
     * Decide whether the Screen can close with Vanilla Methods
     *
     * @return whether the Screen can close with Vanilla Methods
     */
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    /**
     * Event to trigger upon exiting the Gui
     */
    @Override
    public void onClose() {
        initialized = false;
        CraftPresence.GUIS.resetIndex();
        minecraft.keyboardListener.enableRepeatEvents(false);
    }

    /**
     * Renders a String in the Screen, in the style of a notice
     *
     * @param matrixStack The Matrix Stack, used for Rendering
     * @param notice The List of Strings to render
     */
    public void drawNotice(final MatrixStack matrixStack, final List<String> notice) {
        drawNotice(matrixStack, notice, 2, 3);
    }

    /**
     * Renders a String in the Screen, in the style of a notice
     *
     * @param matrixStack The Matrix Stack, used for Rendering
     * @param notice      The List of Strings to render
     * @param widthScale  The Scale away from the center X to render at
     * @param heightScale The Scale away from the center Y to render at
     */
    public void drawNotice(final MatrixStack matrixStack, final List<String> notice, int widthScale, int heightScale) {
        if (notice != null && !notice.isEmpty()) {
            for (int i = 0; i < notice.size(); i++) {
                final String string = notice.get(i);
                drawString(matrixStack, minecraft.fontRenderer, string, (width / widthScale) - (StringUtils.getStringWidth(string) / 2), (height / heightScale) + (i * 10), 0xFFFFFF);
            }
        }
    }

    /**
     * Get the Current Mouse's X Coordinate Position
     *
     * @return The Mouse's X Coordinate Position
     */
    public int getMouseX() {
        return lastMouseX;
    }

    /**
     * Get the Current Mouse's Y Coordinate Position
     *
     * @return The Mouse's Y Coordinate Position
     */
    public int getMouseY() {
        return lastMouseY;
    }
}
