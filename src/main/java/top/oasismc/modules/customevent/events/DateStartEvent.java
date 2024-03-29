package top.oasismc.modules.customevent.events;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DateStartEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private final World world;
    private boolean cancel;

    public DateStartEvent(World world, boolean cancel) {
        super(false);
        this.world = world;
        this.cancel = cancel;
    }

    public DateStartEvent(World world) {
        this(world, false);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public World getWorld() {
        return world;
    }
}
