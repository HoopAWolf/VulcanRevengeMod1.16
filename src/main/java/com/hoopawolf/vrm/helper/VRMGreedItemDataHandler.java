package com.hoopawolf.vrm.helper;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoopawolf.vrm.data.GreedItemData;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class VRMGreedItemDataHandler
{
    public static final VRMGreedItemDataHandler INSTANCE = new VRMGreedItemDataHandler();
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();
    private static final Type ITEM_DATA_TYPE = new TypeToken<GreedItemData>()
    {
    }.getType();

    public final ArrayList<GreedItemData> data = new ArrayList<>();
    public final ArrayList<String> blackList = new ArrayList<>();

    public void findFiles(IModInfo mod, String base, Predicate<Path> rootFilter,
                          BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles)
    {
        if (mod.getModId().equals("minecraft") || mod.getModId().equals("forge"))
        {
            return;
        }

        IModFileInfo info = mod.getOwningFile();
        Path source;

        if (info instanceof ModFileInfo)
        {
            source = ((ModFileInfo) info).getFile().getFilePath();
        } else
        {
            return;
        }

        FileSystem fs = null;

        try
        {
            Path root = null;

            if (Files.isRegularFile(source))
            {
                fs = FileSystems.newFileSystem(source, null);
                root = fs.getPath("/" + base);
            } else if (Files.isDirectory(source))
            {
                root = source.resolve(base);
            }

            if (root == null || !Files.exists(root) || !rootFilter.test(root))
            {
                return;
            }

            if (processor != null)
            {
                Iterator<Path> itr = Files.walk(root).iterator();

                while (itr.hasNext())
                {
                    boolean cont = processor.apply(root, itr.next());

                    if (!visitAllFiles && !cont)
                    {
                        return;
                    }
                }
            }
        } catch (IOException ex)
        {
            throw new UncheckedIOException(ex);
        } finally
        {
            IOUtils.closeQuietly(fs);
        }
    }

    public void initJSON()
    {
        List<ModInfo> mods = ModList.get().getMods();
        Map<Pair<ModInfo, ResourceLocation>, String> foundData = new HashMap<>();

        mods.forEach(mod ->
        {
            String id = mod.getModId();
            findFiles(mod, String.format("data/%s/%s", id, Reference.VRM_DATA_LOCATION), (path) -> Files.exists(path),
                    (path, file) ->
                    {
                        if (file.toString().endsWith("greeditemblacklist.json"))
                        {
                            String fileStr = file.toString().replaceAll("\\\\", "/");
                            String relPath = fileStr
                                    .substring(fileStr.indexOf(Reference.VRM_DATA_LOCATION) + Reference.VRM_DATA_LOCATION.length() + 1);

                            String assetPath = fileStr.substring(fileStr.indexOf("/data"));
                            ResourceLocation bookId = new ResourceLocation(id, relPath);
                            foundData.put(Pair.of(mod, bookId), assetPath);
                        }

                        return true;
                    }, false);
        });

        foundData.forEach((pair, file) ->
        {
            ModInfo mod = pair.getLeft();
            Optional<? extends ModContainer> container = ModList.get().getModContainerById(mod.getModId());
            container.ifPresent(c ->
            {
                ResourceLocation res = pair.getRight();

                Class<?> ownerClass = c.getMod().getClass();
                try (InputStream stream = ownerClass.getResourceAsStream(file))
                {
                    saveData(stream);
                } catch (Exception e)
                {
                    Reference.LOGGER.error("Failed to load greeditemdata {} defined by mod {}, skipping",
                            res, c.getModInfo().getModId(), e);
                }
            });
        });

        for (GreedItemData dataList : data)
        {
            blackList.addAll(dataList.getListItems());
        }
    }

    public void saveData(InputStream stream)
    {
        Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        GreedItemData dataList = GSON.fromJson(reader, ITEM_DATA_TYPE);

        data.add(dataList);
    }
}
