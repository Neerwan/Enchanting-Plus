package net.darkhax.eplus.block.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.common.network.packet.n.PacketTableSync;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.libs.EnchantData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityAdvancedTable extends TileEntityWithBook implements IInteractionObject {

    public ItemStackHandlerEnchant inventory;

    public TileEntityAdvancedTable () {

        this.inventory = new ItemStackHandlerEnchant(this, 1);
    }

    public List<Enchantment> enchantmentsValid = new ArrayList<>();

    public List<EnchantmentData> enchantmentsCurrent = new ArrayList<>();

    public List<EnchantmentData> enchantmentsNew = new ArrayList<>();

    public void updateItem () {

        final ItemStack stack = this.inventory.getStackInSlot(0);
        this.enchantmentsValid.clear();
        this.enchantmentsCurrent.clear();
        this.enchantmentsNew.clear();
        if (!stack.isEmpty()) {
            if (stack.isItemEnchantable() || stack.isItemEnchanted()) {
                for (final Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                    this.enchantmentsCurrent.add(new EnchantData(entry.getKey(), entry.getValue()));
                }
                this.enchantmentsValid.addAll(this.getEnchantmentsForItem(stack));

            }
        }
        if (!this.world.isRemote) {
            EnchantingPlus.NETWORK.sendToAllAround(new PacketTableSync(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 128D));
            this.markDirty();
        }
    }

    @Override
    public void markDirty () {

        super.markDirty();
    }

    private List<Enchantment> getEnchantmentsForItem (ItemStack stack) {

        final List<Enchantment> enchList = new ArrayList<>();
        for (final Enchantment enchantment : Enchantment.REGISTRY) {

            if (stack.isItemEnchanted()) {
                if (enchantment.canApply(stack)) {
                    enchList.add(enchantment);
                }
            }
            else {
                if (enchantment.type.canEnchantItem(stack.getItem())) {
                    enchList.add(enchantment);
                }
            }
        }
        return enchList;
    }

    public int getCurrentLevelForEnchant (Enchantment enchant) {

        for (final EnchantmentData data : this.enchantmentsCurrent) {
            if (data.enchantment.getRegistryName().equals(enchant.getRegistryName())) {
                return data.enchantmentLevel;
            }
        }
        return 0;
    }

    @Override
    public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {

        return new ContainerEnchantment(playerInventory, this.world, this.pos);
    }

    @Override
    public ITextComponent getDisplayName () {

        return new TextComponentString(this.getName());
    }

    @Override
    public String getGuiID () {

        return "eplus:enchanting_table";
    }

    @Override
    public String getName () {

        return "container.enchant";
    }

    @Override
    public boolean hasCustomName () {

        return false;
    }

    @Override
    public void writeNBT (NBTTagCompound dataTag) {

        dataTag.setTag("inventory", this.inventory.serializeNBT());
    }

    @Override
    public void readNBT (NBTTagCompound dataTag) {

        this.inventory.deserializeNBT((NBTTagCompound) dataTag.getTag("inventory"));
    }

    @Override
    public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {

        return this.getCapability(capability, facing) != null || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.inventory;
        }
        return super.getCapability(capability, facing);
    }
}