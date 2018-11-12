package de.ellpeck.naturesaura.blocks;

import de.ellpeck.naturesaura.NaturesAura;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityFieldCreator;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFieldCreator extends BlockContainerImpl {
    public BlockFieldCreator() {
        super(Material.ROCK, "field_creator", TileEntityFieldCreator.class, "field_creator");
        this.setSoundType(SoundType.STONE);
        this.setHardness(2F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityFieldCreator) {
            if (!worldIn.isRemote) {
                String key = NaturesAura.MOD_ID + ":field_creator_pos";
                NBTTagCompound compound = playerIn.getEntityData();
                if (!playerIn.isSneaking() && compound.hasKey(key)) {
                    BlockPos stored = BlockPos.fromLong(compound.getLong(key));
                    TileEntityFieldCreator creator = (TileEntityFieldCreator) tile;
                    if (!pos.equals(stored)) {
                        if (creator.isCloseEnough(stored)) {
                            TileEntity otherTile = worldIn.getTileEntity(stored);
                            if (otherTile instanceof TileEntityFieldCreator) {
                                creator.connectionOffset = stored.subtract(pos);
                                creator.isMain = true;
                                creator.sendToClients();

                                TileEntityFieldCreator otherCreator = (TileEntityFieldCreator) otherTile;
                                otherCreator.connectionOffset = pos.subtract(stored);
                                otherCreator.sendToClients();

                                compound.removeTag(key);
                                playerIn.sendStatusMessage(new TextComponentTranslation("info." + NaturesAura.MOD_ID + ".connected"), true);
                            } else
                                playerIn.sendStatusMessage(new TextComponentTranslation("info." + NaturesAura.MOD_ID + ".stored_pos_gone"), true);
                        } else
                            playerIn.sendStatusMessage(new TextComponentTranslation("info." + NaturesAura.MOD_ID + ".too_far"), true);
                    } else
                        playerIn.sendStatusMessage(new TextComponentTranslation("info." + NaturesAura.MOD_ID + ".same_position"), true);
                } else {
                    compound.setLong(key, pos.toLong());
                    playerIn.sendStatusMessage(new TextComponentTranslation("info." + NaturesAura.MOD_ID + ".stored_pos"), true);
                }
            }
            return true;
        } else
            return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityFieldCreator) {
            TileEntityFieldCreator creator = (TileEntityFieldCreator) tile;
            if (creator.isCharged) {
                BlockPos connected = creator.getConnectedPos();
                if (connected != null)
                    NaturesAuraAPI.instance().spawnParticleStream(
                            pos.getX() + rand.nextFloat(),
                            pos.getY() + rand.nextFloat(),
                            pos.getZ() + rand.nextFloat(),
                            connected.getX() + rand.nextFloat(),
                            connected.getY() + rand.nextFloat(),
                            connected.getZ() + rand.nextFloat(),
                            0.65F, 0x4245f4, 1F
                    );
            }
        }
    }
}
