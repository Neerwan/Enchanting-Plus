package net.darkhax.eplus.inventory;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class SlotEnchant extends SlotItemHandler {

    private final PredicateEnchantableItem predicate;

    public SlotEnchant (TileEntityAdvancedTable tile, int index, int xPosition, int yPosition) {

        super(tile.inventory, index, xPosition, yPosition);
        this.predicate = new PredicateEnchantableItem();
    }

    @Override
    public int getSlotStackLimit () {

        return 1;
    }

    @Override
    public boolean isItemValid (ItemStack stack) {

        return this.predicate.test(stack);
    }

}