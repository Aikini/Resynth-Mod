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
package com.ki11erwolf.resynth.proxy;

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.ResynthFurnaceRecipes;
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.ResynthWorldGen;
import com.ki11erwolf.resynth.analytics.ConnectEvent;
import com.ki11erwolf.resynth.analytics.NewUserEvent;
import com.ki11erwolf.resynth.analytics.ResynthAnalytics;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Common(server) side initialization class.
 */
public class Common implements Proxy {

    /**
     * {@inheritDoc}
     */
    @Override
    public void construct() {
        sendConnectEvent();
        ResynthFurnaceRecipes.registerFurnaceRecipes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commonSetup(FMLCommonSetupEvent setupEvent) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void complete(FMLLoadCompleteEvent completeEvent) {

    }

    /**
     * Sends the analytics connect event and
     * new user event if enabled and appropriate.
     */
    private void sendConnectEvent(){
        ResynthAnalytics.send(new ConnectEvent());

        File resynthFile
                = new File( //TODO: get mod directory
                ResynthMod.RESYNTH_NU_FILE);

        if(!resynthFile.exists()){
            ResynthAnalytics.send(new NewUserEvent());
            try {
                boolean create = resynthFile.createNewFile();
                if(!create)
                    ResynthMod.getLogger().error("Failed to create Resynth NU file");
            } catch (IOException e) {
                ResynthMod.getLogger().error("Failed to create Resynth NU file", e);
            }
        }
    }

    /**
     * Prints every registered block and item to the console
     * if the debug setting is enabled.
     */
    private void printItemAndBlockRegisters(){
        if(true/*TODO: config*/){
            //Prints all registered blocks/items to console.
            //Helps with adding other mods ore plants
            for(Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()){
                ResynthMod.getLogger().info("<Resynth-Development-Help> | Found item: "
                        + entry.getKey().getNamespace()+ ":"
                        + entry.getKey().toString());
            }

            for(Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()){
                ResynthMod.getLogger().info("<Resynth-Development-Help> | Found block: "
                        + entry.getKey().getNamespace() + ":"
                        + entry.getKey().toString());
            }
        }
    }
}