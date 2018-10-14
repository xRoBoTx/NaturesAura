package de.ellpeck.naturesaura.blocks.tiles;

import de.ellpeck.naturesaura.aura.FiniteAuraContainer;
import de.ellpeck.naturesaura.aura.IAuraContainer;
import de.ellpeck.naturesaura.aura.IAuraContainerProvider;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAncientLeaves extends TileEntityImpl implements IAuraContainerProvider {

    private final FiniteAuraContainer container = new FiniteAuraContainer(20) {
        @Override
        public int getAuraColor() {
            return 0xc46df9;
        }

        @Override
        public int drainAura(int amountToDrain, boolean simulate) {
            int amount = super.drainAura(amountToDrain, simulate);
            if (amount > 0 && !simulate) {
                TileEntityAncientLeaves.this.sendToClients();
            }
            return amount;
        }
    };

    @Override
    public IAuraContainer container() {
        return this.container;
    }

    @Override
    public void writeNBT(NBTTagCompound compound, boolean syncing) {
        super.writeNBT(compound, syncing);
        this.container.writeNBT(compound);
    }

    @Override
    public void readNBT(NBTTagCompound compound, boolean syncing) {
        super.readNBT(compound, syncing);
        this.container.readNBT(compound);
    }
}