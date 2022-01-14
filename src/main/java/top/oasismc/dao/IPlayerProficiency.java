package top.oasismc.dao;
//待设计
import java.util.UUID;

public interface IPlayerProficiency {

    boolean insert(UUID uuid, int swordLevel, int bowLevel, int pickaxeLevel, int axeLevel, int shovelLevel, int hoeLevel);

}
