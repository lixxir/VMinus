package net.lixir.vminus.visions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VisionExtensions {
    private static final List<Consumer<ItemVision>> ITEM_VISION_EXTENSIONS = new ArrayList<>();
    private static final List<Consumer<BlockVision>> BLOCK_VISION_EXTENSIONS = new ArrayList<>();

    public synchronized static void extendItemVision(Consumer<ItemVision> extender) {
        ITEM_VISION_EXTENSIONS.add(extender);
    }

    public synchronized static void extendBlockVision(Consumer<BlockVision> extender) {
        BLOCK_VISION_EXTENSIONS.add(extender);
    }

    static void applyExtensions(ItemVision vision) {
        for (Consumer<ItemVision> extender : ITEM_VISION_EXTENSIONS) {
            extender.accept(vision);
        }
    }

    static void applyExtensions(BlockVision vision) {
        for (Consumer<BlockVision> extender : BLOCK_VISION_EXTENSIONS) {
            extender.accept(vision);
        }
    }

}
