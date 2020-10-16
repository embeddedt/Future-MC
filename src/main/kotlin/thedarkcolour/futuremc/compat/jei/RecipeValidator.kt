package thedarkcolour.futuremc.compat.jei

import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.util.ErrorUtil
import net.minecraft.client.Minecraft
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.recipe.SmithingRecipe
import thedarkcolour.futuremc.registry.FRecipes

object RecipeValidator {
    fun getResults(category: IRecipeCategory<SmithingRecipe>): Results {
        val manager = Minecraft.getInstance().world!!.recipeManager
        val validator = SmithingRecipeValidator(category)
        val validSmithingRecipes = arrayListOf<SmithingRecipe>()

        for (recipe in manager.getRecipes(FRecipes.SMITHING).values) {
            if (validator.isRecipeValid(recipe as SmithingRecipe)) {
                validSmithingRecipes.add(recipe)
            }
        }

        return Results(validSmithingRecipes)
    }

    class Results(val smithingRecipes: List<SmithingRecipe>)

    // From VanillaPlugin
    private class SmithingRecipeValidator<T : IRecipe<*>>(
        private val recipeCategory: IRecipeCategory<T>,
    ) {
        fun isRecipeValid(recipe: T): Boolean {
            return if (recipe.isDynamic) {
                false
            } else {
                if (!recipe.recipeOutput.isEmpty) {
                    true
                } else {
                    val recipeInfo = getInfo(recipe)
                    FutureMC.LOGGER.error("Recipe has no output. {}", recipeInfo)
                    false
                }
            }
        }

        private fun getInfo(recipe: T): String {
            return ErrorUtil.getInfoFromRecipe(recipe, recipeCategory)
        }

        private fun getInputCount(ingredientList: List<Ingredient?>): Int {
            var inputCount = 0
            val var2: Iterator<*> = ingredientList.iterator()
            while (var2.hasNext()) {
                val ingredient = var2.next() as Ingredient
                val input = ingredient.matchingStacks ?: return -1
                ++inputCount
            }
            return inputCount
        }

        private companion object {
            private const val INVALID_COUNT = -1


        }
    }
}