package org.betterx.bclib.api.v3.datagen;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

public interface DatapackRecipeBuilder {
    ResourceLocation getId();

    default String getNamespace() {
        return this.getId().getNamespace();
    }
    void build(RecipeOutput cc);
}
