package ru.cwl.mailarc.domain;

import org.junit.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by vadim.tishenko
 * on 14.03.2017 0:04.
 */
public class HeaderTest {

    /*
    // Create Jsonb and serialize
Jsonb jsonb = JsonbBuilder.create();
String result = jsonb.toJson(dog);

// Deserialize back
dog = jsonb.fromJson("{name:\"Falco\",age:4,bitable:false}", Dog.class);
     */
    @Test
    public void testSerialize(){
        Header h=new Header();
        h.from="it@iph.ru";
        h.to=new ArrayList<>();
        h.to.add("f1@iph.ru");
        h.to.add("f2@iph.ru");
        h.to.add("f3@iph.ru");

        h.cc=new ArrayList<>();
        h.cc.add("ccf1@iph.ru");
        h.cc.add("ccf2@iph.ru");
        h.cc.add("ccf3@iph.ru");
        h.sent= LocalDateTime.now();
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(h);
        System.out.println(result);
    }

}