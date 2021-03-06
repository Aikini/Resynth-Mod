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
package com.ki11erwolf.resynth.config.categories;

import com.ki11erwolf.resynth.config.BooleanConfigValue;
import com.ki11erwolf.resynth.config.ConfigCategory;
import com.ki11erwolf.resynth.config.DoubleConfigValue;
import com.ki11erwolf.resynth.config.IntegerConfigValue;
import com.ki11erwolf.resynth.plant.set.BiochemicalSetProperties;
import com.ki11erwolf.resynth.plant.set.IBiochemicalSetProperties;

/**
 * Defines the configuration settings used by Biochemical plant sets.
 * Each Biochemical plant set contains a reference their own instance
 * of {@link BiochemicalPlantSetConfig}.
 * <p/>
 * Unlike normal configuration classes, this one simply lays out the
 * configuration settings used by Biochemical plant sets, it does
 * not specify any default values and can be instantiated multiple
 * times under different names.
 * <p/>
 * Default values are provided to the constructor using a
 * {@link BiochemicalSetProperties} instance, rather than
 * defined in the class.
 */
public class BiochemicalPlantSetConfig extends ConfigCategory implements IBiochemicalSetProperties {

    /**
     * The prefix to the name of the config setting group.
     */
    private static final String PREFIX = "plant-set-";

    /**
     * Config value that specifies the percentage
     * growth chance of the plant type.
     */
    private final DoubleConfigValue chanceToGrow;

    /**
     * Config value that specifies whether or not the plant
     * type can be grown with bonemeal.
     */
    private final BooleanConfigValue canUseBonemeal;

    /**
     * Config value that specifies the number of produce
     * items the plant type drops when broken/harvested.
     */
    private final IntegerConfigValue numberOfProduceDrops;

    /**
     * Config value that specifies the percentage chance that
     * seeds will spawn from the resources mob when killed.
     */
    private final DoubleConfigValue seedSpawnChanceFromMob;

    /**
     * Config value that specifies the percentage chance that
     * seeds will spawn from the plant produce when smashed
     */
    private final DoubleConfigValue seedSpawnChanceFromBulb;

    /**
     * @param plantSetName the name of the plant set this
     *                     instance if for (e.g. ender pearl)
     * @param defaultProperties default config values.
     */
    public BiochemicalPlantSetConfig(String plantSetName, BiochemicalSetProperties defaultProperties){
        super(PREFIX + plantSetName);

        this.chanceToGrow = new DoubleConfigValue(
                "chance-to-grow",
                "The chance (percentage) this plant type will grow on a random tick." +
                        "\nIncrease this number to increase the growth rate of the plant," +
                        "\ndecrease it to decrease the growth rate of the plant.",
                defaultProperties.chanceToGrow(),
                0.0, 100.0,
                this
        );

        this.canUseBonemeal = new BooleanConfigValue(
                "enable-bonemeal",
                "Set this to true to allow using bonemeal on this specific plant type.",
                defaultProperties.canBonemeal(),
                this
        );

        this.numberOfProduceDrops = new IntegerConfigValue(
                "number-of-produce-drops",
                "The number of produce item drops this plant type will drop when harvested/fully grown" +
                        "and broken.",
                defaultProperties.numberOfProduceDrops(),
                1, 64,
                this
        );

        this.seedSpawnChanceFromMob = new DoubleConfigValue(
                "seed-spawn-chance-from-mob",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen the mob that normally drops the final resource is killed." +
                        "\nSet this to 0 to prevent the seeds from spawning when the mob is killed.",
                defaultProperties.seedSpawnChanceFromMob(),
                0.0, 100.0,
                this
        );

        this.seedSpawnChanceFromBulb = new DoubleConfigValue(
                "seed-spawn-chance-from-bulb",
                "The chance (percentage) that this plant types seed will spawn" +
                        "\nwhen the plants produce (bulb) is smashed." +
                        "\nSet this to 0 to prevent the seeds from spawning when the bulb is smashed.",
                defaultProperties.seedSpawnChanceFromBulb(),
                0.0, 100.0,
                this
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int numberOfProduceDrops() {
        return numberOfProduceDrops.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromMob() {
        return (float) seedSpawnChanceFromMob.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float seedSpawnChanceFromBulb() {
        return (float) seedSpawnChanceFromBulb.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBonemeal() {
        return canUseBonemeal.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float chanceToGrow() {
        return (float) this.chanceToGrow.getValue();
    }
}
