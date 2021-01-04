package io.github.nothtaro.maptest.core.listener;

import io.github.nothtaro.maptest.core.MapThread;
import io.github.nothtaro.maptest.core.MapGraphic;
import io.github.nothtaro.maptest.core.MapTask;
import io.github.nothtaro.maptest.core.MapThreader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CommandListener implements CommandExecutor {
    List<MapThread> threadList = new ArrayList<>();
    MapThread mapThread = new MapThreader();

    TestMapView view = new TestMapView();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(label.equalsIgnoreCase("maptest")) {
            if(args.length > 0) {
                switch (args[0]) {
                    case "start" : {
                        this.view.run(0,20,graphics -> {
                            graphics.setColor(Color.BLACK);
                            for(int x = 0; x <= 127; x++) {
                                for (int y = 0; y <= 127; y++) {
                                    graphics.fillRect(0,0,x,y);
                                }
                            }
                            graphics.setColor(Color.WHITE);
                            graphics.drawString("UNTIIII", 25,25);
                            graphics.drawString("" + System.currentTimeMillis(), 25,50);
                        });
                        break;
                    }

                    case "stop" : {
                        if(args.length > 1 && Integer.decode(args[1]) != null) {
                            Bukkit.broadcastMessage(Integer.decode(args[1]).toString());
                            Bukkit.getScheduler().cancelTask(Integer.decode(args[1]));
                        } else {
                            mapThread.stop();
                        }
                        break;
                    }

                    case "status" : {
                        Bukkit.broadcastMessage("ID: " + mapThread.getID());
                        Bukkit.broadcastMessage("IS RUNNING: " + mapThread.isRunning());
                    }

                    case "test" : {
                        ItemStack inMainHand = player.getInventory().getItemInMainHand();
                        if(inMainHand.getType().equals(Material.FILLED_MAP)) {
                            MapMeta mapMeta = (MapMeta) inMainHand.getItemMeta();
                            MapView view = mapMeta.getMapView();
                            mapMeta.getMapView().removeRenderer(mapMeta.getMapView().getRenderers().get(0));
                            mapMeta.getMapView().addRenderer(this.view);
                            player.sendMessage("マップをAWTグラフィックに置換します");
                        }
                    }

                    case "list" : {
                        for (MapThread thread: threadList) {
                            Bukkit.broadcastMessage(String.valueOf(thread.getID()));
                        }
                        break;
                    }

                    case "pause" : {
                        if(this.view.getThread().isPaused()) {
                            this.view.getThread().pause(false);
                        } else {
                            this.view.getThread().pause(true);
                        }
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    class TestMapView extends MapRenderer {
        private Image image = null;
        private final Graphics g;
        private final MapGraphic graphic = null;
        private final MapThread thread = new MapThreader();

        private TestMapView() {
            image = new BufferedImage(128,128,BufferedImage.TYPE_INT_RGB);
            this.g = image.getGraphics();
        }

        public void run(int delay, int period, MapGraphic mapGraphic) {
            MapTask task = current -> {
                if(g != null) {
                    mapGraphic.render(g);
                }
            };
            thread.start(delay, period, task);
            Bukkit.broadcastMessage("スレッド ID: " + thread.getID() + " で開始します");
        }

        public MapThread getThread() {
            return this.thread;
        }

        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            canvas.drawImage(0,0,image);
        }
    }
}
