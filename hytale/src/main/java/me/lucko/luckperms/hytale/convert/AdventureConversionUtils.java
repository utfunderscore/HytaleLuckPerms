package me.lucko.luckperms.hytale.convert;

import com.hypixel.hytale.server.core.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jspecify.annotations.NonNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdventureConversionUtils {

    public static Message adapt(Component component) {
        Message message = Message.empty();

        for (StyledSegment styledSegment : parse(component)) {
            Message part = Message.raw(styledSegment.text);
            part.color(styledSegment.color);
            part.bold(styledSegment.bold);
            part.italic(styledSegment.italic);

            message = Message.join(message, part);
        }

        return message;
    }

    public record StyledSegment(
            String text,
            Color color,          // Hex color or null
            boolean bold,
            boolean italic,
            boolean underlined,
            boolean strikethrough,
            boolean obfuscated
    ) {
    }

    public static @NonNull List<StyledSegment> parse(Component component) {
        List<StyledSegment> segments = new ArrayList<>();
        flattenComponent(component, Style.empty(), segments);
        return segments;
    }

    private static void flattenComponent(@NonNull Component component, @NonNull Style inheritedStyle, @NonNull List<StyledSegment> segments) {
        // Merge inherited style with this component's style
        Style mergedStyle = inheritedStyle.merge(component.style());

        // If this is a TextComponent with content, add it as a segment
        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();
            if (!content.isEmpty()) {
                segments.add(createSegment(content, mergedStyle));
            }
        }

        // Recursively process children
        for (Component child : component.children()) {
            flattenComponent(child, mergedStyle, segments);
        }
    }

    private static StyledSegment createSegment(String text, Style style) {
        TextColor color = style.color();
        return new StyledSegment(
                text,
                color != null ? Color.decode(color.asHexString()) : Color.WHITE,
                style.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE,
                style.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE,
                style.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE,
                style.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE,
                style.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.TRUE
        );
    }

}
