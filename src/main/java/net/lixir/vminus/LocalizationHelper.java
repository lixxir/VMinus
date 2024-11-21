package net.lixir.vminus;

import net.minecraft.client.resources.language.I18n;

public class LocalizationHelper {
    public static String getLocalizedOrDefault(String translationKey, String fallback) {
        String localizedString = I18n.get(translationKey);
        return localizedString.equals(translationKey) ? fallback : localizedString;
    }
}
