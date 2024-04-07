package me.kodysimpson.simpapi.region;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public class Region {

    private Location corner1;
    private Location corner2;

    public Region() {
        this.corner1 = null;
        this.corner2 = null;
    }

    public boolean isSet(){
        return corner1 != null && corner2 != null;
    }

    public World getWorld(){
        return corner1.getWorld();
    }

    public boolean isIn(final Location loc) {

        double xMin = Math.min(corner1.getX(), corner2.getX());
        double xMax = Math.max(corner1.getX(), corner2.getX());
        double yMin = Math.min(corner1.getY(), corner2.getY());
        double yMax = Math.max(corner1.getY(), corner2.getY());
        double zMin = Math.min(corner1.getZ(), corner2.getZ());
        double zMax = Math.max(corner1.getZ(), corner2.getZ());

        //if the location is within the region in the x, y, and z axis
        return loc.getBlockX() >= xMin && loc.getBlockX() <= xMax && loc
                .getBlockY() >= yMin && loc.getBlockY() <= yMax && loc
                .getBlockZ() >= zMin && loc.getBlockZ() <= zMax;
    }

    public int getTotalBlockSize() {
        return (int) (this.getHeight() * this.getXWidth() * this.getZWidth());
    }

    public double getHeight() {
        double yMin = Math.min(corner1.getY(), corner2.getY());
        double yMax = Math.max(corner1.getY(), corner2.getY());
        return yMax - yMin + 1;
    }

    public double getXWidth() {
        double xMin = Math.min(corner1.getX(), corner2.getX());
        double xMax = Math.max(corner1.getX(), corner2.getX());
        return xMax - xMin + 1;
    }

    public double getZWidth() {
        double zMin = Math.min(corner1.getZ(), corner2.getZ());
        double zMax = Math.max(corner1.getZ(), corner2.getZ());
        return zMax - zMin + 1;
    }

    public List<Block> blockList(World world) {

        double xMin = Math.min(corner1.getX(), corner2.getX());
        double xMax = Math.max(corner1.getX(), corner2.getX());
        double yMin = Math.min(corner1.getY(), corner2.getY());
        double yMax = Math.max(corner1.getY(), corner2.getY());
        double zMin = Math.min(corner1.getZ(), corner2.getZ());
        double zMax = Math.max(corner1.getZ(), corner2.getZ());

        final ArrayList<Block> bL = new ArrayList<>(this.getTotalBlockSize());
        for(double x = xMin; x <= xMax; ++x) {
            for(double y = yMin; y <= yMax; ++y) {
                for(double z = zMin; z <= zMax; ++z) {
                    final Block b = world.getBlockAt((int )x, (int) y, (int) z);
                    bL.add(b);
                }
            }
        }
        return bL;
    }

    public boolean isPlayerIn(Player player) {
        return this.isIn(player.getLocation());
    }

    public List<Entity> getEntities(){
        List<Entity> entities = new ArrayList<>();
        for (Block block : blockList(corner1.getWorld())) {
            entities.addAll(block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1));
        }
        return entities;
    }

}
