package de.theorie.bosses.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.theorie.bosses.MoreBosses;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class RapierUpgradeStationRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack rapier;

    public  RapierUpgradeStationRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, ItemStack rapier){
    this.id = id;
    this.output = output;
    this.recipeItems = recipeItems;
    this.rapier = rapier;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return recipeItems.get(0).test(pContainer.getItem(1)) && pContainer.getItem(2).getItem() == rapier.getItem() ? true : false;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<RapierUpgradeStationRecipe> {
        private Type(){ }
        public static final Type INSTANCE = new Type();
        public static final String ID = "rapier_upgrading";
    }

    public static class Serializer implements RecipeSerializer<RapierUpgradeStationRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(MoreBosses.MODID,"rapier_upgrading");

        @Override
        public RapierUpgradeStationRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            ItemStack rapier = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "rapier"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new RapierUpgradeStationRecipe(id, output, inputs, rapier);
        }

        @Override
        public RapierUpgradeStationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }
            ItemStack output = buf.readItem();
            ItemStack rapier = buf.readItem();
            return new RapierUpgradeStationRecipe(id, output, inputs, rapier);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RapierUpgradeStationRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }

}