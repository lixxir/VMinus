package net.lixir.vminus.datagen.util.simple;

public class DatagenObject {
    private final boolean hasTag;
    private final boolean hasModel;
    private final boolean hasLootTable;
    private final boolean hasRecipes;
    private final Type type;

    protected DatagenObject(Type type, Flags flags) {
        this.hasTag = flags.hasTag;
        this.hasModel = flags.hasModel;
        this.hasLootTable = flags.hasLootTable;
        this.hasRecipes = flags.hasRecipes;
        this.type = type;
    }

    public boolean hasTag() {
        return hasTag;
    }

    public boolean hasModel() {
        return hasModel;
    }

    public Type getType() {
        return type;
    }

    public boolean hasRecipes() {
        return hasRecipes;
    }

    public boolean hasLootTable() {
        return hasLootTable;
    }

    public static class Flags {
        private boolean hasTag = true;
        private boolean hasModel = true;
        private boolean hasLootTable = true;
        private boolean hasRecipes = true;

        public Flags hasTag(boolean value) {
            this.hasTag = value;
            return this;
        }

        public Flags hasLootTable(boolean value) {
            this.hasLootTable = value;
            return this;
        }

        public Flags hasRecipes(boolean value) {
            this.hasRecipes = value;
            return this;
        }

        public Flags hasModel(boolean value) {
            this.hasModel = value;
            return this;
        }

    }

    public enum Type {
        ORE,
        FLOWER,
        PLANT,
        LARGE_FLOWER,
        LARGE_PLANT
    }
}
