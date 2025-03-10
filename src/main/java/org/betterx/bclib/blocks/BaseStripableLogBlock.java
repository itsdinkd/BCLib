package org.betterx.bclib.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourWood;
import org.betterx.bclib.interfaces.TagProvider;
import org.betterx.worlds.together.tag.v3.MineableTags;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;


public abstract class BaseStripableLogBlock extends BaseRotatedPillarBlock {
    private final Block striped;

    protected BaseStripableLogBlock(Block striped, Properties settings) {
        super(settings);
        this.striped = striped;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(
            BlockState state,
            Level world,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (TagManager.isToolWithMineableTag(player.getMainHandItem(), MineableTags.AXE)) {
            world.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(
                        pos,
                        striped.defaultBlockState()
                               .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)),
                        11
                );
                if (!player.isCreative()) {
                    player.getMainHandItem().hurt(1, world.random, (ServerPlayer) player);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public static class Wood extends BaseStripableLogBlock implements BehaviourWood, TagProvider {
        private final boolean flammable;

        public Wood(MapColor color, Block striped, boolean flammable) {
            super(
                    striped,
                    (flammable
                            ? Properties.ofFullCopy(striped).ignitedByLava()
                            : Properties.ofFullCopy(striped)).mapColor(color)
            );
            this.flammable = flammable;
        }

        @Override
        public void addTags(List<TagKey<Block>> blockTags, List<TagKey<Item>> itemTags) {
            blockTags.add(BlockTags.LOGS);
            itemTags.add(ItemTags.LOGS);

            if (flammable) {
                blockTags.add(BlockTags.LOGS_THAT_BURN);
                itemTags.add(ItemTags.LOGS_THAT_BURN);
            }
        }
    }
}
