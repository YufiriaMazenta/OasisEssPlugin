package top.oasismc.api.nms.actionbar;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncActionBarSendEvent extends Event implements Cancellable {

    private boolean isCancelled = false;
    private static final HandlerList handlerList = new HandlerList();
    private Player player;
    private String text;

    public AsyncActionBarSendEvent(Player player, String text) {
        super(true);
        this.player = player;
        this.text = text;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
