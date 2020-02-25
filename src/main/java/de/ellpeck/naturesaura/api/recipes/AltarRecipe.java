package de.ellpeck.naturesaura.api.recipes;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class AltarRecipe {

    public final ResourceLocation name;
    public final Ingredient input;
    public final ItemStack output;
    public final IAuraType requiredType;
    public final Ingredient catalyst;
    public final int aura;
    public final int time;

    public AltarRecipe(ResourceLocation name, Ingredient input, ItemStack output, IAuraType requiredType, Ingredient catalyst, int aura, int time) {
        this.name = name;
        this.input = input;
        this.output = output;
        this.requiredType = requiredType;
        this.catalyst = catalyst;
        this.aura = aura;
        this.time = time;
    }

    public AltarRecipe register() {
        NaturesAuraAPI.ALTAR_RECIPES.put(this.name, this);
        return this;
    }
}
