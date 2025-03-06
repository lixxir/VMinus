package net.lixir.vminus.util.setup;

import net.lixir.vminus.util.setup.block.BlockSetup;
import net.lixir.vminus.util.setup.block.BlockSetupRegistry;
import net.lixir.vminus.util.setup.item.ItemSetup;

public class SetupRegistries {
    public static void initialize() {}

    public static final BlockSetupRegistry<BlockSetup> BLOCKS = new BlockSetupRegistry<>();
    public static final BlockSetupRegistry<ItemSetup> ITEMS = new BlockSetupRegistry<>();

}
