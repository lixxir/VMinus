package net.lixir.vminus.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.CapesMenuButtonMessage;
import net.lixir.vminus.procedures.*;
import net.lixir.vminus.world.inventory.CapesMenuMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class CapesMenuScreen extends AbstractContainerScreen<CapesMenuMenu> {
    private final static HashMap<String, Object> guistate = CapesMenuMenu.guistate;
    private static final ResourceLocation texture = new ResourceLocation("vminus:textures/screens/capes_menu.png");
    private final Level world;
    private final int x, y, z;
    private final Player entity;
    ImageButton imagebutton_beeper_cape;
    ImageButton imagebutton_no_cape;
    ImageButton imagebutton_no_cape_selected;
    ImageButton imagebutton_beeper_cape_selected;
    ImageButton imagebutton_locked_cape;
    ImageButton imagebutton_ghost_cape;
    ImageButton imagebutton_ghost_cape_selected;
    ImageButton imagebutton_locked_cape1;
    ImageButton imagebutton_marrow_cape;
    ImageButton imagebutton_marrow_cape_selected;
    ImageButton imagebutton_locked_cape2;
    ImageButton imagebutton_shroud_cape;
    ImageButton imagebutton_shroud_cape_selected;
    ImageButton imagebutton_locked_cape3;
    ImageButton imagebutton_troll_cape;
    ImageButton imagebutton_troll_cape_selected;
    ImageButton imagebutton_locked_cape4;

    public CapesMenuScreen(CapesMenuMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.imageWidth = 176;
        this.imageHeight = 100;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (OwnsBeeperCapeProcedure.execute(entity))
            if (mouseX > leftPos + 23 && mouseX < leftPos + 37 && mouseY > topPos + 18 && mouseY < topPos + 36)
                guiGraphics.renderTooltip(font, Component.translatable("gui.vminus.capes_menu.tooltip_patreon_only"), mouseX, mouseY);
        if (OwnsGhostCapeProcedure.execute(entity))
            if (mouseX > leftPos + 40 && mouseX < leftPos + 54 && mouseY > topPos + 18 && mouseY < topPos + 36)
                guiGraphics.renderTooltip(font, Component.translatable("gui.vminus.capes_menu.tooltip_become_a_discord_booster_to_unlo"), mouseX, mouseY);
        if (OwnsMarrowCapeProcedure.execute(entity))
            if (mouseX > leftPos + 57 && mouseX < leftPos + 71 && mouseY > topPos + 18 && mouseY < topPos + 36)
                guiGraphics.renderTooltip(font, Component.translatable("gui.vminus.capes_menu.tooltip_become_a_patreon_supporter_or_ko"), mouseX, mouseY);
        if (OwnsShroudCapeProcedure.execute(entity))
            if (mouseX > leftPos + 74 && mouseX < leftPos + 88 && mouseY > topPos + 18 && mouseY < topPos + 36)
                guiGraphics.renderTooltip(font, Component.translatable("gui.vminus.capes_menu.tooltip_become_a_patreon_supporter_or_ko1"), mouseX, mouseY);
        if (OwnsTrollCapeProcedure.execute(entity))
            if (mouseX > leftPos + 91 && mouseX < leftPos + 105 && mouseY > topPos + 18 && mouseY < topPos + 36)
                guiGraphics.renderTooltip(font, Component.translatable("gui.vminus.capes_menu.tooltip_contributors_only"), mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("gui.vminus.capes_menu.label_vminus"), 55, 5, -12829636, false);
    }

    @Override
    public void init() {
        super.init();
        imagebutton_beeper_cape = new ImageButton(this.leftPos + 18, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_beeper_cape.png"), 24, 48, e -> {
            if (NotHasBeeperCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(0, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 0, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (NotHasBeeperCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_beeper_cape", imagebutton_beeper_cape);
        this.addRenderableWidget(imagebutton_beeper_cape);
        imagebutton_no_cape = new ImageButton(this.leftPos + 1, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_no_cape.png"), 24, 48, e -> {
            if (NotHasEmptyCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(1, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 1, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (NotHasEmptyCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_no_cape", imagebutton_no_cape);
        this.addRenderableWidget(imagebutton_no_cape);
        imagebutton_no_cape_selected = new ImageButton(this.leftPos + 1, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_no_cape_selected.png"), 24, 48, e -> {
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (HasEmptyCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_no_cape_selected", imagebutton_no_cape_selected);
        this.addRenderableWidget(imagebutton_no_cape_selected);
        imagebutton_beeper_cape_selected = new ImageButton(this.leftPos + 18, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_beeper_cape_selected.png"), 24, 48, e -> {
            if (HasBeeperCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(3, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 3, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (HasBeeperCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_beeper_cape_selected", imagebutton_beeper_cape_selected);
        this.addRenderableWidget(imagebutton_beeper_cape_selected);
        imagebutton_locked_cape = new ImageButton(this.leftPos + 18, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_locked_cape.png"), 24, 48, e -> {
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (OwnsBeeperCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_locked_cape", imagebutton_locked_cape);
        this.addRenderableWidget(imagebutton_locked_cape);
        imagebutton_ghost_cape = new ImageButton(this.leftPos + 35, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_ghost_cape.png"), 24, 48, e -> {
            if (NotHasGhostCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(5, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 5, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (NotHasGhostCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_ghost_cape", imagebutton_ghost_cape);
        this.addRenderableWidget(imagebutton_ghost_cape);
        imagebutton_ghost_cape_selected = new ImageButton(this.leftPos + 35, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_ghost_cape_selected.png"), 24, 48, e -> {
            if (HasGhostCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(6, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 6, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (HasGhostCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_ghost_cape_selected", imagebutton_ghost_cape_selected);
        this.addRenderableWidget(imagebutton_ghost_cape_selected);
        imagebutton_locked_cape1 = new ImageButton(this.leftPos + 35, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_locked_cape1.png"), 24, 48, e -> {
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (OwnsGhostCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_locked_cape1", imagebutton_locked_cape1);
        this.addRenderableWidget(imagebutton_locked_cape1);
        imagebutton_marrow_cape = new ImageButton(this.leftPos + 52, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_marrow_cape.png"), 24, 48, e -> {
            if (NotHasMarrowCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(8, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 8, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (NotHasMarrowCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_marrow_cape", imagebutton_marrow_cape);
        this.addRenderableWidget(imagebutton_marrow_cape);
        imagebutton_marrow_cape_selected = new ImageButton(this.leftPos + 52, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_marrow_cape_selected.png"), 24, 48, e -> {
            if (HasMarrowCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(9, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 9, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (HasMarrowCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_marrow_cape_selected", imagebutton_marrow_cape_selected);
        this.addRenderableWidget(imagebutton_marrow_cape_selected);
        imagebutton_locked_cape2 = new ImageButton(this.leftPos + 52, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_locked_cape2.png"), 24, 48, e -> {
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (OwnsMarrowCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_locked_cape2", imagebutton_locked_cape2);
        this.addRenderableWidget(imagebutton_locked_cape2);
        imagebutton_shroud_cape = new ImageButton(this.leftPos + 69, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_shroud_cape.png"), 24, 48, e -> {
            if (NotHasShroudCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(11, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 11, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (NotHasShroudCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_shroud_cape", imagebutton_shroud_cape);
        this.addRenderableWidget(imagebutton_shroud_cape);
        imagebutton_shroud_cape_selected = new ImageButton(this.leftPos + 69, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_shroud_cape_selected.png"), 24, 48, e -> {
            if (HasShroudCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(12, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 12, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (HasShroudCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_shroud_cape_selected", imagebutton_shroud_cape_selected);
        this.addRenderableWidget(imagebutton_shroud_cape_selected);
        imagebutton_locked_cape3 = new ImageButton(this.leftPos + 69, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_locked_cape3.png"), 24, 48, e -> {
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (OwnsShroudCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_locked_cape3", imagebutton_locked_cape3);
        this.addRenderableWidget(imagebutton_locked_cape3);
        imagebutton_troll_cape = new ImageButton(this.leftPos + 86, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_troll_cape.png"), 24, 48, e -> {
            if (NotHasTrollCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(14, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 14, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (NotHasTrollCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_troll_cape", imagebutton_troll_cape);
        this.addRenderableWidget(imagebutton_troll_cape);
        imagebutton_troll_cape_selected = new ImageButton(this.leftPos + 86, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_troll_cape_selected.png"), 24, 48, e -> {
            if (HasTrollCapeProcedure.execute(entity)) {
                VMinusMod.PACKET_HANDLER.sendToServer(new CapesMenuButtonMessage(15, x, y, z));
                CapesMenuButtonMessage.handleButtonAction(entity, 15, x, y, z);
            }
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (HasTrollCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_troll_cape_selected", imagebutton_troll_cape_selected);
        this.addRenderableWidget(imagebutton_troll_cape_selected);
        imagebutton_locked_cape4 = new ImageButton(this.leftPos + 86, this.topPos + 15, 24, 24, 0, 0, 24, new ResourceLocation("vminus:textures/screens/atlas/imagebutton_locked_cape4.png"), 24, 48, e -> {
        }) {
            @Override
            public void render(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
                if (OwnsTrollCapeProcedure.execute(entity))
                    super.render(guiGraphics, gx, gy, ticks);
            }
        };
        guistate.put("button:imagebutton_locked_cape4", imagebutton_locked_cape4);
        this.addRenderableWidget(imagebutton_locked_cape4);
    }
}
