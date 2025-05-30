package net.lixir.vminus.mixins.client;

import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(RecipeBookPage.class)
public interface RecipeBookPageAccessor {
    @Accessor void setRecipeCollections(List<RecipeCollection> collections);
    @Accessor void setTotalPages(int totalPages);
    @Accessor
    int getTotalPages();
    @Accessor int getCurrentPage();
    @Accessor void setCurrentPage(int page);
    @Invoker
    void invokeUpdateButtonsForPage();
}
