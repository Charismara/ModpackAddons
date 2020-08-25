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
package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.CommandUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.commands.CommandsGui;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;

public class MainGui extends ExtendedScreen {
    private ExtendedButtonControl biomeSet, dimensionSet, serverSet, statusSet, proceedButton, commandGUIButton;

    public MainGui(Screen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        CraftPresence.GUIS.configGUIOpened = true;

        final int calc1 = (width / 2) - 183;
        final int calc2 = (width / 2) + 3;

        // Added General Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.general"),
                        () -> CraftPresence.GUIS.openScreen(new GeneralSettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.title.general")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                minecraft.fontRenderer,
                                true
                        )
                )
        );
        biomeSet = addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.biomemessages"),
                        () -> CraftPresence.GUIS.openScreen(new BiomeSettingsGui(currentScreen)),
                        () -> {
                            if (!biomeSet.active) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showbiome"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.biomemessages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        dimensionSet = addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.dimensionmessages"),
                        () -> CraftPresence.GUIS.openScreen(new DimensionSettingsGui(currentScreen)),
                        () -> {
                            if (!dimensionSet.active) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showdimension"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.dimensionmessages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        serverSet = addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.servermessages"),
                        () -> CraftPresence.GUIS.openScreen(new ServerSettingsGui(currentScreen)),
                        () -> {
                            if (!serverSet.active) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showstate"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.servermessages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        statusSet = addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(3),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.statusmessages"),
                        () -> CraftPresence.GUIS.openScreen(new StatusMessagesGui(currentScreen)),
                        () -> {
                            if (!statusSet.active) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showstate"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.statusmessages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        minecraft.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        // Added Advanced Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(3),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.advanced"),
                        () -> CraftPresence.GUIS.openScreen(new AdvancedSettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.title.advanced")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                minecraft.fontRenderer,
                                true
                        )
                )
        );
        // Added Accessibility Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(4),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.accessibility"),
                        () -> CraftPresence.GUIS.openScreen(new AccessibilitySettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.title.accessibility")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                minecraft.fontRenderer,
                                true
                        )
                )
        );
        // Added Presence Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(4),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.presencesettings"),
                        () -> CraftPresence.GUIS.openScreen(new PresenceSettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.presencesettings")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                minecraft.fontRenderer,
                                true
                        )
                )
        );

        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (CraftPresence.CONFIG.hasChanged) {
                                CraftPresence.CONFIG.updateConfig();
                                CraftPresence.CONFIG.read(false, "UTF-8");
                                if (CraftPresence.CONFIG.hasClientPropertiesChanged) {
                                    CommandUtils.rebootRPC();
                                    CraftPresence.CONFIG.hasClientPropertiesChanged = false;
                                }
                                CommandUtils.reloadData(true);
                                CraftPresence.CONFIG.hasChanged = false;
                            }

                            CraftPresence.GUIS.configGUIOpened = false;
                            if (minecraft.player != null) {
                                minecraft.player.closeScreen();
                            } else {
                                CraftPresence.GUIS.openScreen(parentScreen);
                            }
                        }
                )
        );
        // Added About Button
        addControl(
                new ExtendedButtonControl(
                        (width - 30), 10,
                        20, 20,
                        "?",
                        () -> CraftPresence.GUIS.openScreen(new AboutGui(currentScreen))
                )
        );
        commandGUIButton = addControl(
                new ExtendedButtonControl(
                        (width - 105), (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.commands"),
                        () -> CraftPresence.GUIS.openScreen(new CommandsGui(currentScreen))
                )
        );
        // Added Reset Config Button
        addControl(
                new ExtendedButtonControl(
                        10, (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.reset"),
                        () -> {
                            CraftPresence.CONFIG.setupInitialValues();
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                        }
                )
        );

        super.initializeUi();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");

        drawString(matrixStack, minecraft.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);

        biomeSet.active = CraftPresence.CONFIG.showCurrentBiome;
        dimensionSet.active = CraftPresence.CONFIG.showCurrentDimension;
        serverSet.active = CraftPresence.CONFIG.showGameState;
        statusSet.active = CraftPresence.CONFIG.showGameState;
        commandGUIButton.active = CraftPresence.CONFIG.enableCommands;

        proceedButton.setMessage(new StringTextComponent(CraftPresence.CONFIG.hasChanged ? ModUtils.TRANSLATOR.translate("gui.config.message.button.save") : ModUtils.TRANSLATOR.translate("gui.config.message.button.back")));

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int mouseX, int mouseY) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (CraftPresence.CONFIG.hasChanged || CraftPresence.CONFIG.hasClientPropertiesChanged) {
                CraftPresence.CONFIG.setupInitialValues();
                CraftPresence.CONFIG.read(false, "UTF-8");
                CraftPresence.CONFIG.hasChanged = false;
                CraftPresence.CONFIG.hasClientPropertiesChanged = false;
            }
            CraftPresence.GUIS.configGUIOpened = false;
        }
        return super.keyPressed(keyCode, mouseX, mouseY);
    }
}
