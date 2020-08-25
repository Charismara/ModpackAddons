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
package com.gitlab.cdagaming.craftpresence.utils.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;

import java.util.List;
import java.util.Optional;

/**
 * Dimension Utilities used to Parse Dimension Data and handle related RPC Events
 *
 * @author CDAGaming
 */
public class DimensionUtils {
    /**
     * A List of the detected Dimension Type's
     */
    private final List<ResourceLocation> DIMENSION_TYPES = Lists.newArrayList();
    /**
     * Whether this module is active and currently in use
     */
    public boolean isInUse = false;
    /**
     * Whether this module is allowed to start and enabled
     */
    public boolean enabled = false;
    /**
     * A List of the detected Dimension Names
     */
    public List<String> DIMENSION_NAMES = Lists.newArrayList();
    /**
     * The Name of the Current Dimension the Player is in
     */
    private String CURRENT_DIMENSION_NAME;
    /**
     * The alternative name for the Current Dimension the Player is in, if any
     */
    private String CURRENT_DIMENSION_IDENTIFIER;

    /**
     * Clears FULL Data from this Module
     */
    private void emptyData() {
        DIMENSION_NAMES.clear();
        DIMENSION_TYPES.clear();
        clearClientData();
    }

    /**
     * Clears Runtime Client Data from this Module (PARTIAL Clear)
     */
    public void clearClientData() {
        CURRENT_DIMENSION_NAME = null;

        isInUse = false;
        CraftPresence.CLIENT.initArgument("&DIMENSION&");
    }

    /**
     * Module Event to Occur on each tick within the Application
     */
    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.showCurrentDimension : enabled;
        final boolean needsUpdate = enabled && (
                DIMENSION_NAMES.isEmpty() || DIMENSION_TYPES.isEmpty()
        );

        if (needsUpdate) {
            getDimensions();
        }

        if (enabled) {
            if (CraftPresence.player != null) {
                isInUse = true;
                updateDimensionData();
            } else if (isInUse) {
                clearClientData();
            }
        } else {
            emptyData();
        }
    }

    /**
     * Synchronizes Data related to this module, if needed
     */
    private void updateDimensionData() {
        final ResourceLocation newDimensionType = CraftPresence.player.world.getDimensionKey().func_240901_a_();
        final String newDimensionName = StringUtils.formatDimensionName(CraftPresence.player.world.getDimensionKey().func_240901_a_().toString(), false, !CraftPresence.CONFIG.formatWords);

        final String newDimension_primaryIdentifier = StringUtils.formatDimensionName(CraftPresence.player.world.getDimensionKey().func_240901_a_().toString(), true, !CraftPresence.CONFIG.formatWords);
        final String newDimension_alternativeIdentifier = StringUtils.formatDimensionName(newDimensionType.getClass().getSimpleName(), true, !CraftPresence.CONFIG.formatWords);
        final String newDimension_Identifier = !StringUtils.isNullOrEmpty(newDimension_primaryIdentifier) ? newDimension_primaryIdentifier : newDimension_alternativeIdentifier;

        if (!newDimensionName.equals(CURRENT_DIMENSION_NAME) || !newDimension_Identifier.equals(CURRENT_DIMENSION_IDENTIFIER)) {
            CURRENT_DIMENSION_NAME = !StringUtils.isNullOrEmpty(newDimensionName) ? newDimensionName : newDimension_Identifier;
            CURRENT_DIMENSION_IDENTIFIER = newDimension_Identifier;

            if (!DIMENSION_NAMES.contains(newDimension_Identifier)) {
                DIMENSION_NAMES.add(newDimension_Identifier);
            }
            if (!DIMENSION_TYPES.contains(newDimensionType)) {
                DIMENSION_TYPES.add(newDimensionType);
            }

            updateDimensionPresence();
        }
    }

    /**
     * Updates RPC Data related to this Module
     */
    public void updateDimensionPresence() {
        // Form Dimension Argument List
        List<Tuple<String, String>> dimensionArgs = Lists.newArrayList();

        dimensionArgs.add(new Tuple<>("&DIMENSION&", CURRENT_DIMENSION_NAME));

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            dimensionArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        final String defaultDimensionMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentDimensionMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_IDENTIFIER, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
        final String currentDimensionIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_IDENTIFIER, 0, 2, CraftPresence.CONFIG.splitCharacter, CURRENT_DIMENSION_IDENTIFIER);
        final String formattedIconKey = StringUtils.formatPackIcon(currentDimensionIcon.replace(" ", "_"));

        final String CURRENT_DIMENSION_ICON = formattedIconKey.replace("&icon&", CraftPresence.CONFIG.defaultDimensionIcon);
        final String CURRENT_DIMENSION_MESSAGE = StringUtils.sequentialReplaceAnyCase(currentDimensionMSG, dimensionArgs);

        CraftPresence.CLIENT.syncArgument("&DIMENSION&", CURRENT_DIMENSION_MESSAGE, false);
        CraftPresence.CLIENT.syncArgument("&DIMENSION&", CraftPresence.CLIENT.imageOf(CURRENT_DIMENSION_ICON, CraftPresence.CONFIG.defaultDimensionIcon, true), true);
    }

    /**
     * Retrieves a List of detected Dimension Types
     *
     * @return The detected Dimension Types found
     */
    private List<ResourceLocation> getDimensionTypes() {
        List<ResourceLocation> dimensionTypes = Lists.newArrayList();
        Optional<MutableRegistry<DimensionType>> dimensionRegistry = DynamicRegistries.func_239770_b_().func_230521_a_(Registry.DIMENSION_TYPE_KEY);

        if (dimensionRegistry.isPresent()) {
            List<ResourceLocation> defaultDimensionTypes = Lists.newArrayList(dimensionRegistry.get().keySet());

            if (!defaultDimensionTypes.isEmpty()) {
                for (ResourceLocation type : defaultDimensionTypes) {
                    if (type != null) {
                        dimensionTypes.add(type);
                    }
                }
            }
        }

        return dimensionTypes;
    }

    /**
     * Updates and Initializes Module Data, based on found Information
     */
    public void getDimensions() {
        for (ResourceLocation TYPE : getDimensionTypes()) {
            if (TYPE != null) {
                if (!DIMENSION_NAMES.contains(StringUtils.formatDimensionName(TYPE.toString(), true, !CraftPresence.CONFIG.formatWords))) {
                    DIMENSION_NAMES.add(StringUtils.formatDimensionName(TYPE.toString(), true, !CraftPresence.CONFIG.formatWords));
                }
                if (!DIMENSION_TYPES.contains(TYPE)) {
                    DIMENSION_TYPES.add(TYPE);
                }
            }
        }

        for (String dimensionMessage : CraftPresence.CONFIG.dimensionMessages) {
            if (!StringUtils.isNullOrEmpty(dimensionMessage)) {
                final String[] part = dimensionMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !DIMENSION_NAMES.contains(StringUtils.formatDimensionName(part[0], true, !CraftPresence.CONFIG.formatWords))) {
                    DIMENSION_NAMES.add(StringUtils.formatDimensionName(part[0], true, !CraftPresence.CONFIG.formatWords));
                }
            }
        }
    }
}
