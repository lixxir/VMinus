package net.lixir.vminus.core.visions;

import net.lixir.vminus.core.VisionProperty;
import net.lixir.vminus.core.values.BasicVisionValue;

import java.util.ArrayList;

public abstract class Vision<T> implements IMergableVision<T> {
    private final ArrayList<String> entries = new ArrayList<>();

    public ArrayList<String> getEntries() {
        return entries;
    }

    public void addEntry(String entry) {
        entries.add(entry);
    }

    public void mergeEntries(ArrayList<String> entries) {
        this.entries.addAll(entries);
    }

    protected <t> void appendProperty(StringBuilder stringBuilder, VisionProperty<BasicVisionValue<t>, t> property) {
        if (property.getValue() != null) {
            stringBuilder.append("(" + property.getName() + ": " + property.getValue() + "),");
        }
    }
}
