package top.oasismc.api.message;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BaseComponentGetter {

    public static BaseComponent getBaseComponent(String text, HoverEvent hoverEvent, ClickEvent clickEvent) {
        BaseComponent textComponent = new TextComponent(text);
        textComponent.setHoverEvent(hoverEvent);
        textComponent.setClickEvent(clickEvent);
        return textComponent;
    }

}
