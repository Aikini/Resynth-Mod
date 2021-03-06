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

import com.ki11erwolf.resynth.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

/**
 * A standard ore block - handles experiences, tool and
 * harvest level.
 *
 * Used for Calvinite, Sylvanite and as a base class
 * for Mineral Stone.
 *
 * @param <T> the class inheriting from this class.
 */
public class BlockOre<T extends BlockOre> extends ResynthBlock {

    /**
     * The min and max number of experience orbs to drop.
     */
    private final int minXP, maxXP;

    /**
     * Creates a standard ore block that
     * drops 3 to 7 experience orbs.
     *
     * @param oreName the name of the ore.
     */
    BlockOre(String oreName){
        this(oreName, 3, 7);
    }

    /**
     * Creates a standard ore block that drops
     * the given amount of experience orbs.
     *
     * @param oreName the name of the ore block.
     * @param minXP the minimum amount of experience orbs to drop.
     * @param maxXP the maximum amount of experience orbs to drop.
     */
    BlockOre(String oreName, int minXP, int maxXP) {
        super(Properties.create(Material.ROCK)
                        .hardnessAndResistance(3.0F, 3.0F)
                        .lightValue(1),
                oreName
        );

        if(minXP > maxXP)
            minXP = maxXP;

        this.minXP = minXP;
        this.maxXP = maxXP;
    }

    /**
     * {@inheritDoc}
     *
     * @return the amount of experience points to give the player
     * when they break the block. Determined by config.
     */
    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune,
                          int silktouch) {
        return MathUtil.getRandomIntegerInRange(minXP, maxXP);
    }

    /**
     * {@inheritDoc}
     *
     * @return The tool needed to break this block: Pickaxe.
     */
    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    /**
     * {@inheritDoc}
     *
     * @return the required tool level to break this block: 2 (iron).
     */
    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }
}
