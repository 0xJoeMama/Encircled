package io.github.llamarama.team.encircled.common.register;

import com.google.common.collect.ImmutableMap;
import io.github.llamarama.team.encircled.common.item.GuideBookItem;
import io.github.llamarama.team.encircled.common.item.PackedBlockItem;
import io.github.llamarama.team.encircled.common.item.SpellbindingClothItem;
import io.github.llamarama.team.encircled.common.util.IdBuilder;
import io.github.llamarama.team.encircled.common.util.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ModItems {

    public static final Supplier<Item.Settings> DEFAULT = () -> new Item.Settings().group(ModItemGroup.get());
    public static final Supplier<Item.Settings> UNSTACKABLE = () -> DEFAULT.get().maxCount(1);
    public static final Supplier<Item.Settings> QUARTER_STACKABLE = () -> DEFAULT.get().maxCount(16);

    private static final Map<String, Item> REGISTRY = new ConcurrentHashMap<>();

    public static final Item GUIDE_BOOK = register("guide_book",
            new GuideBookItem(UNSTACKABLE.get()));
    public static final Item SPELLBINDING_CLOTH = register("spellbinding_cloth",
            new SpellbindingClothItem(DEFAULT.get()));
    public static final Item PACKED_BLOCK = register("packed_block",
            new PackedBlockItem(UNSTACKABLE.get()));

    private ModItems() {
    }

    @NotNull
    static Item register(String id, Item item) {
        REGISTRY.putIfAbsent(id, item);
        return item;
    }

    public static ImmutableMap<String, Item> getModItems() {
        return ImmutableMap.copyOf(REGISTRY);
    }

    public static void init() {
        REGISTRY.forEach((id, item) -> Registry.register(Registry.ITEM, IdBuilder.of(id), item));
    }

}
