package com.example.elytrafirework;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class ElytraFireworkMod implements ClientModInitializer {

    private static KeyBinding fireworkKey;

    @Override
    public void onInitializeClient() {
        // Register keybinding (default R key, but changeable in controls menu)
        fireworkKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.elytrafirework.use", // translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R, // default key
                "category.elytrafirework.main" // category in controls menu
        ));

        // Tick event to check key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (fireworkKey.wasPressed() && client.player != null) {
                PlayerEntity player = client.player;

                // Only activate if flying with elytra
                if (player.isFallFlying()) {
                    int slot = findRocketSlot(player);

                    if (slot != -1) {
                        // Use the firework from inventory
                        ItemStack rocketStack = player.getInventory().getStack(slot);
                        if (!rocketStack.isEmpty() && rocketStack.getItem() == Items.FIREWORK_ROCKET) {
                            client.interactionManager.interactItem(player, Hand.MAIN_HAND);
                        }
                    }
                }
            }
        });
    }

    private int findRocketSlot(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() == Items.FIREWORK_ROCKET) {
                return i;
            }
        }
        return -1; // No rockets found
    }
}
