/*
 * Copyright 2018 Ki11er_wolf
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

import com.ki11erwolf.resynth.ResynthConfig;
import com.ki11erwolf.resynth.block.tileEntity.ResynthTileEntity;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.item.ResynthItems;
import com.ki11erwolf.resynth.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Mineral Enriched Soil.
 * The soil block for the mods plants.
 */
@SuppressWarnings("deprecation")
public class BlockMineralSoil extends ResynthTileEntity<TileEntityMineralSoil> {

    /**
     * The value that correlates to the blocks appearance.
     */
    public static final PropertyInteger PERCENTAGE = PropertyInteger.create("percent", 0, 4);

    /**
     * The bounding box for the block.
     */
    protected static final AxisAlignedBB MINERAL_SOIL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D,
            0.9375D, 1.0D);

    /**
     * The configuration settings for this block.
     */
    private static final ResynthConfig.MineralSoil MINERAL_SOIL_CONFIG = ResynthConfig.MINERAL_SOIL;

    /**
     * Constructor.
     */
    protected BlockMineralSoil(){
        super(Material.GROUND, "mineralSoil");
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PERCENTAGE, 0));
        this.setLightOpacity(255);
        BlockUtil.setHarvestLevel(this, BlockUtil.HarvestTools.SHOVEL, 0);
    }

    /**
     * Drops the amount of ItemMineralRock put
     * into it (within a an item or two of accuracy).
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
        TileEntityMineralSoil te = getTileEntity(worldIn, pos);
        int drops = (int)((te.getPercentage() - 1.0F) / MINERAL_SOIL_CONFIG.mineralValue);
        EntityItem items = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                new ItemStack(ResynthItems.ITEM_MINERAL_ROCK, drops));
        worldIn.spawnEntity(items);
    }

    /**
     * Increases the blocks tile entity percentage value
     * by a config specified value at the cost
     * of ItemMineralRocks.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     * @param playerIn -
     * @param hand -
     * @param facing -
     * @param hitX -
     * @param hitY -
     * @param hitZ -
     * @return -
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ){

        if(!playerIn.getHeldItemMainhand().getItem().getClass().equals(ResynthItems.ITEM_MINERAL_ROCK.getClass()))
            return false;

        if(getTileEntity(worldIn, pos).getPercentage() >= 50)
            return false;

        if(!worldIn.isRemote){
            ItemStack offer = playerIn.getHeldItemMainhand();
            TileEntityMineralSoil entity = getTileEntity(worldIn, pos);

            if(hand.equals(EnumHand.MAIN_HAND)){
                if(!playerIn.isCreative())
                    offer.shrink(1);
                entity.incrementPercentage(MINERAL_SOIL_CONFIG.mineralValue);
            }

            float percentage = entity.getPercentage();
            updateState(worldIn, pos, state);
            ((TileEntityMineralSoil) worldIn.getTileEntity(pos)).setPercentage(percentage);
            String perc = String.valueOf(entity.getPercentage());


            playerIn.sendMessage(new TextComponentString("Soil mineral content: "
                    + perc
                    .substring(0, perc.length() > 4 ? 4 : perc.length())
                    + "%"));
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param blockState {@inheritDoc}
     * @param blockAccess {@inheritDoc}
     * @param pos {@inheritDoc}
     * @param side {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
                                        EnumFacing side){
        switch (side){
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                Block block = iblockstate.getBlock();
                return !iblockstate.isOpaqueCube() && block != this && block != Blocks.GRASS_PATH;
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    /**
     * {@inheritDoc} This block.
     *
     * @param state {@inheritDoc}
     * @param rand {@inheritDoc}
     * @param fortune {@inheritDoc}
     * @return {@inheritDoc} This block.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(ResynthBlocks.BLOCK_MINERAL_SOIL);
    }

    /**
     * {@inheritDoc}
     *
     * @param meta {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(PERCENTAGE, meta);
    }

    /**
     * {@inheritDoc}
     *
     * @param state {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(PERCENTAGE);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @SuppressWarnings("RedundantArrayCreation")
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[] {PERCENTAGE});
    }

    /**
     * {@inheritDoc}
     *
     * @param worldIn {@inheritDoc}
     * @param state {@inheritDoc}
     * @param pos {@inheritDoc}
     * @param face {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    /**
     * Returns the bounding box.
     *
     * @param state -
     * @param source -
     * @param pos -
     * @return the bounding box. {@link #MINERAL_SOIL_AABB}
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return MINERAL_SOIL_AABB;
    }

    /**
     * @param state -
     * @return {@code false}.
     */
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    /**
     * @param state -
     * @return {@code false}.
     */
    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Class<TileEntityMineralSoil> getTileEntityClass() {
        return TileEntityMineralSoil.class;
    }

    /**
     * {@inheritDoc}
     * @param world the world the block is in.
     * @param state the block state of the block.
     * @return {@inheritDoc}
     */
    @Override
    public TileEntityMineralSoil createTileEntity(World world, IBlockState state) {
        return new TileEntityMineralSoil();
    }

    /**
     * Updates the look of the block to match
     * the percentage value in its tile entity.
     *
     * @param worldIn -
     * @param pos -
     * @param state -
     */
    private void updateState(World worldIn, BlockPos pos, IBlockState state){
        TileEntityMineralSoil entity = (TileEntityMineralSoil) worldIn.getTileEntity(pos);
        float percentage = entity.getPercentage();
        worldIn.setBlockState(pos, getStateFromTileEntity(worldIn, pos));
    }

    /**
     * Get's the correct block state for the percentage
     * value in the blocks tile entity.
     *
     * @param worldIn -
     * @param pos -
     * @return the block state matching the tile entities percentage value.
     */
    private static IBlockState getStateFromTileEntity(World worldIn, BlockPos pos){
        float percentage = ((TileEntityMineralSoil)worldIn.getTileEntity(pos)).getPercentage();

        if(percentage > 49.9){
            return worldIn.getBlockState(pos).withProperty(PERCENTAGE, 4/*50%*/);
        }

        if(percentage > 39.9){
            return worldIn.getBlockState(pos).withProperty(PERCENTAGE, 3/*40%*/);
        }

        if(percentage > 29.9){
            return worldIn.getBlockState(pos).withProperty(PERCENTAGE, 2/*30%*/);
        }

        if(percentage > 19.9){
            return worldIn.getBlockState(pos).withProperty(PERCENTAGE, 1/*20%*/);
        }

        return worldIn.getBlockState(pos).withProperty(PERCENTAGE, 0/*10%*/);
    }
}