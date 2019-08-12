package com.teamacronymcoders.epos.container;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.teamacronymcoders.epos.items.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.awt.*;

public class QuiverContainer extends Container {
    @ObjectHolder("epos:quiver_container")
    public static final ContainerType<QuiverContainer> TYPE = null;

    private PosInvHandler handler;
    private PlayerInventory player;
    private boolean hasPlayerInventory;
    private int totalSlots;

    public QuiverContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(((QuiverItem) playerInventory.player.getHeldItemMainhand().getItem()).getInventory(), playerInventory);
    }

    public QuiverContainer(PosInvHandler handler, PlayerInventory player) {
        super(TYPE, 0);
        this.handler = handler;
        this.player = player;
        this.totalSlots = 0;
        int i = 0;
        for (int y = 0; y < handler.getYSize(); y++) {
            for (int x = 0; x < handler.getXSize(); x++) {
                addSlot(new SlotItemHandler(handler, i, handler.getXPos() + x * 18, handler.getYPos() + y * 18));
                i++;
            }
        }
        addPlayerChestInventory();
    }

    public void updateSlotPosition() {
        int i = 0;
        for (int y = 0; y < handler.getYSize(); ++y) {
            for (int x = 0; x < handler.getXSize(); ++x) {
                for (Slot inventorySlot : this.inventorySlots) {
                    if (!(inventorySlot instanceof SlotItemHandler)) continue;
                    if (((SlotItemHandler) inventorySlot).getItemHandler().equals(handler) && i == inventorySlot.getSlotIndex()) {
                        inventorySlot.xPos = handler.getXPos() + x * 18;
                        inventorySlot.yPos = handler.getYPos() + y * 18;
                        break;
                    }
                }
                ++i;
            }
        }
    }

    private void addPlayerChestInventory() {
        Point invPos = IAssetProvider.getAsset(IAssetProvider.DEFAULT_PROVIDER, AssetTypes.BACKGROUND).getInventoryPosition();
        if (hasPlayerInventory) return;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player, j + i * 9 + 9, invPos.x + j * 18, invPos.y + i * 18));
            }
        }
        hasPlayerInventory = true;
    }

    public void removeChestInventory() {
        this.inventorySlots.removeIf(slot -> slot.getSlotIndex() >= 9 && slot.getSlotIndex() < 9 + 3 * 9);
        hasPlayerInventory = false;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
