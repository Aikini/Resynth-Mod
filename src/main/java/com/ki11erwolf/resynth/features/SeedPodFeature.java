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
package com.ki11erwolf.resynth.features;


import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.SeedPodConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;

/**
 * Mystical Seed Pod Feature. Handles generating the
 * {@link com.ki11erwolf.resynth.block.BlockSeedPod}
 * in the world as a flower. The Seed Pod will generate
 * in every biome.
 */
class SeedPodFeature extends Feature<NoFeatureConfig> {

    /**
     * Configuration settings for Seed Pod generation.
     */
    private static final SeedPodConfig CONFIG = ResynthConfig.GENERAL_CONFIG.getCategory(SeedPodConfig.class);

    /**
     * Adds this feature to every biome provided
     * the config allows the plant to spawn.
     */
    SeedPodFeature(){
        super(NoFeatureConfig::deserialize);

        //Skip generation
        if(!CONFIG.generate())
            return;

        Biome.BIOMES.forEach(
                biome -> biome.addFeature(
                        GenerationStage.Decoration.VEGETAL_DECORATION,
                        Biome.createDecoratedFeature(
                                this,
                                NoFeatureConfig.NO_FEATURE_CONFIG,
                                Placement.COUNT_HEIGHTMAP_32,
                                new FrequencyConfig(CONFIG.getGenerationFrequency())
                        )
                )
        );
    }

    /**
     * {@inheritDoc}
     * Handles placing the Mystical Seed Pod in the world.
     *
     * @return
     */
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
                         BlockPos pos, NoFeatureConfig config) {
        BlockState blockstate = ResynthBlocks.BLOCK_SEED_POD.getDefaultState();
        int tries = 64;
        int i = 0;

        for(int j = 0; j < tries; ++j) {
            BlockPos blockpos = pos.add(
                    rand.nextInt(8) - rand.nextInt(8),
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8)
            );

            if (worldIn.isAirBlock(blockpos) && blockpos.getY() < 255
                    && blockstate.isValidPosition(worldIn, blockpos)) {
                worldIn.setBlockState(blockpos, blockstate, 2);
                ++i;
            }
        }

        return i > 0;
    }
}