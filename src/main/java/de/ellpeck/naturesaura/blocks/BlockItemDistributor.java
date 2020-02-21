package de.ellpeck.naturesaura.blocks;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityItemDistributor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockItemDistributor extends BlockContainerImpl {

    public BlockItemDistributor() {
        super("item_distributor", TileEntityItemDistributor::new, ModBlocks.prop(Blocks.FURNACE));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!player.isShiftKeyDown())
            return ActionResultType.FAIL;
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof TileEntityItemDistributor))
            return ActionResultType.FAIL;
        if (!worldIn.isRemote) {
            TileEntityItemDistributor distributor = (TileEntityItemDistributor) tile;
            distributor.isRandomMode = !distributor.isRandomMode;
            distributor.sendToClients();
        }
        return ActionResultType.SUCCESS;
    }
}
