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
package com.ki11erwolf.resynth.plant.block;

import com.ki11erwolf.resynth.ResynthTabProduce;
import com.ki11erwolf.resynth.block.ResynthBlock;
import com.ki11erwolf.resynth.util.BlockUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The base class for all blocks produced
 * by metallic plants.
 */
public class BlockOrganicPlantOre extends ResynthBlock {

    /**
     * Prefix for the ore block.
     */
    protected static final String ORE_PREFIX = "ore";

    /**
     * Constructs a new plant ore block instance.
     *
     * @param name the name of the block.
     */
    public BlockOrganicPlantOre(String name) {
        super(Material.GOURD, SoundType.STONE, name, ORE_PREFIX);
        this.setHardness(2.0F);
        this.setCreativeTab(ResynthTabProduce.RESYNTH_TAB_PRODUCE);
        BlockUtil.setHarvestLevel(this, BlockUtil.HarvestTools.AXE, 2);
    }

    /**
     * {@inheritDoc}
     * Adds a tooltip on how to use the block.
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
                               ITooltipFlag flagIn){
        tooltip.add("Can be blown up to for a chance to obtain more seeds.");
        tooltip.add("Can be smelted to obtain the original ore block.");
    }
}