package net.lixir.vminus.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class VFlowingFluid extends FlowingFluid {
    private final Properties properties;

    protected VFlowingFluid(Properties properties) {
        this.properties = properties;
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return properties.flowingFluid.get();
    }

    @Override
    public @NotNull Fluid getSource() {
        return properties.sourceFluid.get();
    }


    @Override
    protected boolean canConvertToSource(@NotNull Level level) {
        return properties.canConvertToSource;
    }

    @Override
    protected void beforeDestroyingBlock(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {

    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader levelReader) {
        return properties.slopeFindDistance;
    }

    @Override
    protected int getDropOff(@NotNull LevelReader levelReader) {
        return properties.levelDecreasePerBlock;
    }

    @Override
    public @NotNull Item getBucket() {
        return properties.bucket;
    }

    public static class Flowing extends VFlowingFluid
    {
        public Flowing(Properties properties)
        {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(@NotNull FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }

    public static class Source extends VFlowingFluid
    {
        public Source(Properties properties)
        {
            super(properties);
        }

        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        public boolean isSource(@NotNull FluidState state) {
            return true;
        }
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState fluidState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull Fluid fluid, @NotNull Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader levelReader) {
        return properties.tickDelay;
    }

    @Override
    protected float getExplosionResistance() {
        return properties.explosionResistance;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState fluidState) {
        return null;
    }

    @Override
    public boolean isSource(@NotNull FluidState fluidState) {
        return fluidState.getType() == getSource();
    }

    @Override
    public int getAmount(@NotNull FluidState fluidState) {
        return isSource(fluidState) ? 8 : fluidState.getValue(LEVEL);
    }


    public static class Properties {
        private final int slopeFindDistance;
        private final int levelDecreasePerBlock;
        private final float explosionResistance;
        private final int tickDelay;
        private final boolean canConvertToSource;
        private final Item bucket;
        private final Supplier<Fluid> flowingFluid;
        private final Supplier<Fluid> sourceFluid;

        @Contract(pure = true)
        private Properties(@NotNull Builder builder) {
            this.slopeFindDistance = builder.slopeFindDistance;
            this.levelDecreasePerBlock = builder.levelDecreasePerBlock;
            this.explosionResistance = builder.explosionResistance;
            this.tickDelay = builder.tickDelay;
            this.canConvertToSource = builder.canConvertToSource;
            this.bucket = builder.bucket;
            this.flowingFluid = builder.flowingFluid;
            this.sourceFluid = builder.sourceFluid;
        }

        public static @NotNull Builder of(Supplier<Fluid> sourceFluid, Supplier<Fluid> flowingFluid) {
            return new Builder(sourceFluid, flowingFluid);
        }

        public static class Builder {
            private final Supplier<Fluid> sourceFluid;
            private final Supplier<Fluid> flowingFluid;
            private int slopeFindDistance = 4;
            private int levelDecreasePerBlock = 1;
            private float explosionResistance = 100.0f;
            private int tickDelay = 5;
            private boolean canConvertToSource = false;
            private Item bucket = null;

            public Builder(Supplier<Fluid> sourceFluid, Supplier<Fluid> flowingFluid) {
                this.sourceFluid = sourceFluid;
                this.flowingFluid = flowingFluid;
            }

            public Builder slopeFindDistance(int value) {
                this.slopeFindDistance = value;
                return this;
            }

            public Builder levelDecreasePerBlock(int value) {
                this.levelDecreasePerBlock = value;
                return this;
            }

            public Builder explosionResistance(float value) {
                this.explosionResistance = value;
                return this;
            }

            public Builder tickDelay(int value) {
                this.tickDelay = value;
                return this;
            }

            public Builder canConvertToSource(boolean value) {
                this.canConvertToSource = value;
                return this;
            }

            public Builder bucket(Item bucket) {
                this.bucket = bucket;
                return this;
            }

            public Properties build() {
                return new Properties(this);
            }
        }
    }

}
