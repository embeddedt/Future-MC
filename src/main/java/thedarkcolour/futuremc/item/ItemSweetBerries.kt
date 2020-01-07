package thedarkcolour.futuremc.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemFood
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.item.Modeled
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.block.BlockSweetBerryBush
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.init.FBlocks

class ItemSweetBerries : ItemFood(2, 0.2f, false), Modeled {
    override fun onItemUse(player: EntityPlayer, worldIn: World, position: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        var pos = position
        pos = pos.offset(facing)
        val itemstack = player.getHeldItem(hand)
        val bush: Block = FBlocks.SWEET_BERRY_BUSH
        if (!player.canPlayerEdit(pos, facing, itemstack)) {
            return EnumActionResult.FAIL
        }
        if (bush.canPlaceBlockAt(worldIn, pos)) {
            if (worldIn.isAirBlock(pos)) {
                worldIn.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0f, 0.8f)
                worldIn.setBlockState(pos, bush.defaultState.withProperty(BlockSweetBerryBush.AGE, 0))
                itemstack.shrink(1)
                return EnumActionResult.SUCCESS
            }
        }
        if (player is EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, itemstack)
        }
        return EnumActionResult.FAIL
    }

    init {
        translationKey = FutureMC.ID + ".sweet_berries"
        setRegistryName("sweet_berries")
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.FOOD else FutureMC.TAB
        addModel()
    }
}