package io.github.nothtaro.maptest.core.listener;

import io.github.nothtaro.maptest.core.IMapThread;
import io.github.nothtaro.maptest.core.MapTask;
import io.github.nothtaro.maptest.core.MapThread;
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
    List<IMapThread> threadList = new ArrayList<>();
    IMapThread mapThread = new MapThread();



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(label.equalsIgnoreCase("maptest")) {
            if(args.length > 0) {
                switch (args[0]) {
                    case "start" : {
                        //threadList.add(new MapThread().start(0,20,null));
                        mapThread.start(0,current -> {

                        });
                        break;
                    }

                    case "stop" : {
                        if(args.length > 1 && Integer.decode(args[1]) != null) {
                            Bukkit.broadcastMessage(Integer.decode(args[1]).toString());
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
                            mapMeta.getMapView().addRenderer(new TestMapView());
                            player.sendMap(view);
                            player.sendMessage("マップをAWTグラフィックに置換します" + mapMeta);
                        }
                    }

                    case "list" : {
                        for (IMapThread thread: threadList) {
                            Bukkit.broadcastMessage(String.valueOf(thread.getID()));
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
        int ballX = 15;
        int ballY = 40;

        int ballXMove = 2;
        int ballYMove = 2;

        //グラフィックけい
        private final Image image;
        private Graphics graphics = null;
        //マップタイミングけい
        private IMapThread mapThread = new MapThread();
        private MapTask mapTask = current -> {
            Bukkit.broadcastMessage("RENDERING");
            if(graphics != null) {
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0,0,128,128);

                graphics.setColor(Color.GREEN);

                if(ballX + 10 > 128 || ballX < 0) {
                    ballXMove *= -1;
                }
                if (ballY + 10 > 128 || ballY < 0) {
                    ballYMove *= -1;
                }

                ballX += ballXMove;
                ballY += ballYMove;

                graphics.fillRect(ballX,ballY,10,10);
            }
        };

        private TestMapView() {
            image = new BufferedImage(128,128,BufferedImage.TYPE_INT_RGB);
            this.graphics = image.getGraphics();
            mapThread.start(0,mapTask);
            Bukkit.broadcastMessage("スレッド ID: " + mapThread.getID());
        }

        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            /*
            for(int x = 0; x <= 128-1; x++) {
                for (int y = 0; y <= 128-1; y++) {
                    canvas.setPixel(x,y, MapPalette.BLUE);
                }
            }*/
            canvas.drawImage(0,0,image);
            //canvas.drawText(0,0, MinecraftFont.Font,String.valueOf(System.currentTimeMillis()));
        }
    }
}
