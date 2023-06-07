package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public abstract class Config {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final String fileName;
    private final ConfigManager configManager;

    public Config(String fileName, ConfigManager configManager) {
        this.fileName = fileName;
        this.configManager = configManager;
        loadConfig();
    }

    public abstract void loadConfig();

    public HashMap<String, String> loadMessages(ConfigurationSection messages) {
        HashMap<String, String> messageById = new HashMap<>();
        if (messages == null) {
            return messageById;
        }
        for (String key : messages.getKeys(false)) {
            String id = key.replace("-", "_").toUpperCase();
            String message = messages.getString(key);
            MessageUtils editor = new MessageUtils(message);
            editor.color();
            messageById.put(id, editor.getMessage());
        }
        return messageById;
    }

    public String serialize(ItemStack item) {
        if (item == null) {
            return null;
        }
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();
            return new String(Base64.getEncoder().encode(io.toByteArray()));
        }catch (IOException exp) {
            return null;
        }
    }

    public ItemStack deserialize(String rawItem) {
        if (rawItem == null) {
            return null;
        }
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(rawItem));
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            return (ItemStack) is.readObject();
        }catch (IOException | ClassNotFoundException exp) {
            return null;
        }
    }

    public String serializeInventory(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public Inventory deserializeInventory(String data) {
        if (data == null) {
            return null;
        }
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException | IOException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public File saveConfig() {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, true);
        }
        return file;
    }

    public MortisKitPvp getPlugin() {
        return plugin;
    }

    public String getFileName() {
        return fileName;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
