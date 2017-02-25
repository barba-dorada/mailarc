package ru.cwl.mailarc.parser.p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by vad on 20.04.2015 14:27.
 */
public class Util {
    static Logger log= LoggerFactory.getLogger(Util.class);
    static public byte[] path2bytes(Path path) {
        File file = path.toFile();
        byte[] fileBArray = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBArray);
            return fileBArray;
        } catch (IOException e) {
            log.error("path2bytes error!",e);
            e.printStackTrace();
        }
        return new byte[0];
    }

    static public Stream<Path> getPathStream(String src) {
        Path path = Paths.get(src);
        Stream<Path> s = null;
        try {
            s = Files.list(path);
        } catch (IOException e) {
            log.error("",e);
            e.printStackTrace();
            return Stream.empty();
        }
        return s.filter(p -> !p.toFile().isDirectory() && p.toString().endsWith(".msg"));
    }

    static public String reduceNoise(String src){
        String dst = src.replaceAll("\\S{50,}", "");
        //dst=dst.replaceAll("\\s+"," ");
        dst = dst.replaceAll("=[0-9A-F]{2}", "");
        dst = dst.replaceAll("<[^<>]+>", "");
        dst = dst.replaceAll("&\\w{2,};", "");
        dst = dst.replaceAll("\\n{2,}", "\n");
        dst = dst.replaceAll("\\s{2,}", " ");
        //dst = dst.replaceAll("[\\w-]+[:].+", "");
        dst = dst.replaceAll("[ =\\.,)(]{3,}", "");
        dst = dst.replaceAll("------=_NextPart.+", "");
        dst = dst.replaceAll("\\n{2,}", "\n");
        return dst;
    }
}
