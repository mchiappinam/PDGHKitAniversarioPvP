package me.mchiappinam.pdghkitaniversario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener, CommandExecutor {

	List<String> usou = new ArrayList<String>();
	List<String> IPusou = new ArrayList<String>();
	boolean ativado = false;
	int task;
	
	  public void onEnable() {
			File file = new File(getDataFolder(),"config.yml");
			if(!file.exists()) {
				try {
					saveResource("config_template.yml",false);
					File file2 = new File(getDataFolder(),"config_template.yml");
					file2.renameTo(new File(getDataFolder(),"config.yml"));
				}
				catch(Exception e) {}
			}
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginCommand("aniversario").setExecutor(this);
		getServer().getPluginCommand("aniversariovip").setExecutor(this);
		prepararPremio();
		getServer().getConsoleSender().sendMessage("§3[PDGHKitAniversario] §2ativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHKitAniversario] §2Acesse: http://pdgh.net/");
	  }

	  public void onDisable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHKitAniversario] §2desativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHKitAniversario] §2Acesse: http://pdgh.net/");
	  }

	    @Override
	    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
	        final Player p = (Player) cs;
            if(cmd.getName().equalsIgnoreCase("aniversario")) {
      	    	if (!ativado) {
  					p.sendMessage("§cO prêmio não está acontecendo.");
      	    		return true;
      	    	}
  				if(p.hasPermission("pdgh.vip")) {
  					p.sendMessage("§6§lVocê é um jogador VIP e tem um kit ainda mais especial.");
  					p.sendMessage("§aUse: /aniversariovip");
  					return true;
  				}
            	if(args.length > 0) {
  					p.sendMessage("§cUse §l/aniversario");
  					return true;
            	}
      	    	if (usou.contains(p.getName().toLowerCase())) {
  					p.sendMessage("§cVocê já usou o prêmio de aniversário.");
      	    		return true;
      	    	}
      	    	if (IPusou.contains(p.getAddress().getAddress().getHostAddress().replaceAll("/", ""))) {
  					p.sendMessage("§cVocê já usou o prêmio de aniversário.");
      	    		return true;
      	    	}
            	if (p.getInventory().getContents() != null) {
          	      		usou.add(p.getName().toLowerCase());
          	      		IPusou.add(p.getAddress().getAddress().getHostAddress().replaceAll("/", ""));
          	      		kit(p);
          	    		p.sendMessage(" ");
          	  	    	getServer().broadcastMessage("§f[ⓅⒹⒼⒽ] §a§l"+p.getName()+" §6usou seu prêmio de aniversário da PDGH");
          	    		p.sendMessage("§f[ⓅⒹⒼⒽ] §6"+ChatColor.BOLD+"⋑ §aVocê usou o seu §lprêmio §ade aniversário por estar online.");
          	    		p.sendMessage(" ");
            		}else{
      					p.sendMessage("§cSeu inventário não está vazio!");
      					p.sendMessage("§cEsvazie seu inventário e digite novamente o comando §l/aniversario");
            		}
	            return true;
            }else if(cmd.getName().equalsIgnoreCase("aniversariovip")) {
      	    	if (!ativado) {
  					p.sendMessage("§cO prêmio não está acontecendo.");
      	    		return true;
      	    	}
  				if(!p.hasPermission("pdgh.vip")) {
  					p.sendMessage("§cApenas §6§lVIPs §cpodem usar esse prêmio.");
  					return true;
  				}
            	if(args.length > 0) {
  					p.sendMessage("§cUse §l/aniversariovip");
  					return true;
            	}
      	    	if (usou.contains(p.getName().toLowerCase())) {
  					p.sendMessage("§cVocê já usou o prêmio de aniversário.");
      	    		return true;
      	    	}
      	    	if (IPusou.contains(p.getAddress().getAddress().getHostAddress().replaceAll("/", ""))) {
  					p.sendMessage("§cVocê já usou o prêmio de aniversário.");
      	    		return true;
      	    	}
            	if (p.getInventory().getContents() != null) {
          	      		usou.add(p.getName().toLowerCase());
          	      		IPusou.add(p.getAddress().getAddress().getHostAddress().replaceAll("/", ""));
          	      		kitV(p);
          	    		p.sendMessage(" ");
          	  	    	getServer().broadcastMessage("§f[ⓅⒹⒼⒽ] §6§l"+p.getName()+" §6usou seu prêmio de aniversário §lVIP §6da PDGH");
          	    		p.sendMessage("§f[ⓅⒹⒼⒽ] §6"+ChatColor.BOLD+"⋑ §aVocê usou o seu §lprêmio §ade aniversário por estar online.");
          	    		p.sendMessage(" ");
            		}else{
      					p.sendMessage("§cSeu inventário não está vazio!");
      					p.sendMessage("§cEsvazie seu inventário e digite novamente o comando §l/aniversario");
            		}
	            return true;
            }
			return false;
	    }

	    public void prepararPremio() {
		    getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		      public void run() {
		    	  executarPremio();
		      }
		    }, 58*60*20L);
	    }
	    
	    public void executarPremio() {
  	    	if (ativado) {
  	    		return;
  	    	}
  	    	if (getConfig().getBoolean("jaTeve")) {
  	    		return;
  	    	}
  	    	ativado = true;
  	    	getConfig().set("jaTeve", true);
  	    	saveConfig();
  	    	reloadConfig();
  	  	  task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	    		int timer = 150;
	    		public void run() {
	      			if(timer <1) {
	      				ctask();
	    				}
	    		        if ((timer == 5) || (timer == 10) || (timer == 30) || (timer == 50) || (timer == 70) || (timer == 90) || (timer == 110) || (timer == 130) || (timer == 150)) {
	    		  	    	getServer().broadcastMessage(" ");
	    		  	    	getServer().broadcastMessage("§6§lPrêmio imperdível de §a§laniversário§6§l PDGH");
	    		  	    	getServer().broadcastMessage("§aDigite o comando §l/aniversario §apara pegar seu KIT!");
	    		  	    	getServer().broadcastMessage("§aÉ §6§lVIP§a? Digite o comando §l/aniversariovip §apara pegar seu KIT!");
	    		  	    	getServer().broadcastMessage(" ");
	    		  	    	//getServer().broadcastMessage("§bVITROLA VALENDO VIP NO TEAMSPEAK! IP: §c§lts.pdgh.com.br");
	    		  	    	getServer().broadcastMessage("§c§l"+timer+" §bsegundos restantes.");
	    		        }
	    		        if (timer == 0) {
	    		  	    	ativado = false;
	    		  	    	getServer().broadcastMessage(" ");
	    		  	    	getServer().broadcastMessage("§6§lPrêmio imperdível de §a§laniversário§6§l PDGH");
	    		  	    	getServer().broadcastMessage("§c§lFim do prêmio de aniversário!");
	    		  	    	getServer().broadcastMessage(" ");
	    		        }
	    			timer--;
	    		}
	  	  }, 0, 20*1);
	    }

		private void ctask() {
			Bukkit.getScheduler().cancelTask(task);
		}
		
		private void kit(Player p) {
		    p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5, (short) 1));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8233));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8233));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8226));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8226));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8225));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8225));
		    p.getInventory().addItem(new ItemStack(Material.ARROW, 64));
		}

		private void kitV(Player p) {
		    p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 64, (short) 1));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8233));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8233));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8233));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8226));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8226));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8226));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8225));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8225));
		    p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8225));
		    p.getInventory().addItem(new ItemStack(Material.GHAST_TEAR, 30));
		    p.getInventory().addItem(new ItemStack(Material.BLAZE_POWDER, 15));
		}
	    
	    
	    
}