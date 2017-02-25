package ru.cwl;

import javafx.util.Pair;
import ru.cwl.mailarc.domain.MessageDigest;
import ru.cwl.mailarc.parser.IParser;
import ru.cwl.mailarc.parser.OldParser;
import ru.cwl.mailarc.util.Printer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by vadim.tishenko
 * on 23.02.2017 19:22.
 */
public class ListDirs {
    public static void main(String[] args) throws IOException, MessagingException {
        IParser par = new OldParser();
        //Printer prn=new Printer();
        String srcDir = "D:\\mailarc_200307";
        Path path = Paths.get(srcDir);
        Stream<Path> res = Files.walk(path);
        List<Path> r = res.filter(p -> p.toFile().isFile())
                .filter(p1 -> p1.toString().endsWith("msg"))
                .collect(Collectors.toList());

        Map<String, Throwable> errInMsg = new HashMap<>();
        for (Path path1 : r) {
            MimeMessage mm = getMimeMessageFromFile(path1.toFile());

            System.out.println(path1.getFileName().toString());
            try {
                MessageDigest md = par.parse(mm);
                //System.out.println( prn.print(md));
            } catch (Exception e) {
                e.printStackTrace();
                errInMsg.put(path1.toString(), e);
            }
        }
        System.out.printf("errors %d/%d %s %%\n", errInMsg.size(), r.size(), String.valueOf((errInMsg.size() * 100) / r.size()));

        Map<StackTraceElement, Integer> c = new HashMap<>();
        Map<StackTraceElement, Map<String, Integer>> cc = new HashMap<>();
        Map<Pair<StackTraceElement, String>, Integer> ccc = new HashMap<>();
        for (Map.Entry<String, Throwable> e : errInMsg.entrySet()) {
            System.out.println(e.getKey());
            Throwable rr = e.getValue();
            System.out.println(rr);
            for (StackTraceElement stackTraceElement : rr.getStackTrace()) {
                if (stackTraceElement.getClassName().startsWith("ru.cwl")) {
                    System.out.println(stackTraceElement);
                    Integer val = c.getOrDefault(stackTraceElement, 0);
                    c.put(stackTraceElement, val + 1);

                    Pair pair = new Pair(stackTraceElement, e.getValue().getClass().toString());
                    Integer val2 = ccc.getOrDefault(pair, 0);
                    ccc.put(pair,val2+1);

                    break;

                }
            }
            //e.getValue().printStackTrace(System.out);
//            System.out.println(e.getValue().getStackTrace()[0]);
//            System.out.println(e.getValue().toString());
        }
        for (Map.Entry<StackTraceElement, Integer> e : c.entrySet()) {
            System.out.printf("%s, %s\n", e.getKey(), e.getValue());
        }
        System.out.println("stat2");
        for (Pair<StackTraceElement, String> pair : ccc.keySet()) {
            System.out.printf("%s, %s, %s\n", pair.getKey(), pair.getValue(),ccc.get(pair));

        }


    }

    private static MimeMessage getMimeMessageFromFile(File file) throws MessagingException, FileNotFoundException {
        InputStream is2 = new FileInputStream(file);
        InputStream ist = new BufferedInputStream(is2);
        return new MimeMessage(null, ist);
    }
}
