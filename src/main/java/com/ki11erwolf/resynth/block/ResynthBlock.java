/*
 * Copyright 2018-2019 Ki11er_wolf
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
package com.ki11erwolf.resynth.block;

import com.ki11erwolf.resynth.ResynthTabs;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.GeneralConfig;
import com.ki11erwolf.resynth.item.ResynthItemBlock;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Base block class for all Resynth blocks.
 *
 * Provides extra utility, registration and a linked ItemBlock.
 *
 * @param <T> the subclass to this class (e.i. the inheriting class).
 */
public class ResynthBlock<T extends ResynthBlock> extends Block {

    /**
     * Flag to prevent queuing a block
     * more than once.
     */
    private boolean isQueued = false;

    /**
     * The ItemBlock instance for this block instance.
     */
    private final ResynthItemBlock itemBlock;

    /**
     * Creates a new Resynth Block with the given name
     * and properties.
     *
     * @param properties the properties that define the block.
     * @param name the name of the block.
     */
    public ResynthBlock(Properties properties, String name){
        this(properties, new Item.Properties().group(ResynthTabs.TAB_RESYNTH), name);
    }

    /**
     * Creates a new Resynth Block with the given name, ItemBlock
     * properties and block properties.
     *
     * @param properties the properties that define the block.
     * @param itemProperties the properties that defines the blocks item.
     * @param name the name of the block.
     */
    public ResynthBlock(Properties properties, Item.Properties itemProperties, String name) {
        super(properties);
        setRegistryName(name);

        itemBlock = new ResynthItemBlock(this, itemProperties);
        itemBlock.setRegistryName(name);
    }

    /**
     * @return the {@link net.minecraft.item.BlockItem} for this block instance.
     */
    public ResynthItemBlock getItemBlock(){
        return this.itemBlock;
    }

    /**
     * {@inheritDoc}
     *
     * Sets the tooltip for this block from the lang files.
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        setDescriptiveTooltip(tooltip, this);
    }

    /**
     * Adds this block instance to ItemRegistrationQueue, which will then
     * be registered to the game from the queue. This will also register
     * the ItemBlock instance associated with this block instance.
     *
     * @return {@code this}.
     */
    @SuppressWarnings("WeakerAccess")//Lies
    protected T queueRegistration() {
        if (isQueued)
            throw new IllegalStateException(
                    String.format("Block: %s already queued for registration.",
                            this.getClass().getCanonicalName())
            );

        ResynthBlocks.INSTANCE.queueForRegistration(this);
        isQueued = true;

        itemBlock.queueRegistration();

        //noinspection unchecked //Should NOT be possible.
        return (T) this;
    }

    // *******
    // Utility
    // *******

    /**
     * Turns a given string into a {@link StringTextComponent}
     * containing the given string.
     *
     * @param text the given string text.
     * @return a new TextComponentString containing the given string.
     */
    @SuppressWarnings("WeakerAccess")
    static ITextComponent stringToTextComponent(String text){
        return new StringTextComponent(text);
    }

    /**
     * Will add a blocks tooltip (from lang file) to the given
     * tooltip array, provided the config allows it.
     *
     * @param tooltip the tooltip array object.
     * @param block the block who's tooltip we want.
     */
    @SuppressWarnings("WeakerAccess")
    static void setDescriptiveTooltip(List<ITextComponent> tooltip, ResynthBlock block){
        if(!ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).areTooltipsEnabled())
            return;

        if(block.getRegistryName() == null){
            tooltip.add(stringToTextComponent(TextFormatting.RED + "Error"));
        }

        tooltip.add(stringToTextComponent(TextFormatting.GRAY + I18n.format(
                "tooltip.block." + block.getRegistryName().toString().replace(":", ".")
        )));
    }

    /**
     * Will add a blocks tooltip (from lang file) to the given
     * tooltip array, provided the config allows it.
     *
     * @param tooltip the tooltip array object.
     * @param block the name of the block who's tooltip we want and key appended.
     */
    protected static void setDescriptiveTooltip(List<ITextComponent> tooltip,
                                                @SuppressWarnings("SameParameterValue") String block,
                                                Object... params){
        if(!ResynthConfig.GENERAL_CONFIG.getCategory(GeneralConfig.class).areTooltipsEnabled())
            return;

        if(block == null){
            tooltip.add(stringToTextComponent(TextFormatting.RED + "Error"));
        }

        tooltip.add(stringToTextComponent(TextFormatting.GRAY + I18n.format(
                "tooltip.block.resynth." + block, params
        )));
    }

    /**
     * Will add a blocks tooltip (from lang file) to the given
     * tooltip array, provided the config allows it.
     *
     * @param tooltip the tooltip array object.
     * @param block The block who's tooltip we want.
     */
    @SuppressWarnings("WeakerAccess")
    protected void setDescriptiveTooltip(List<ITextComponent> tooltip,
                                         @SuppressWarnings("SameParameterValue") T block,
                                         Object... params){
        if(block.getRegistryName() == null){
            return;
        }

        setDescriptiveTooltip(tooltip, block.getRegistryName().getPath(), params);
    }
}
