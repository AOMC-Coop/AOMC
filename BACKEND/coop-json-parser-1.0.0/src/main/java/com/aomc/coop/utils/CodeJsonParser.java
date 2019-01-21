package com.aomc.coop.utils;

import com.aomc.coop.model.Response;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.jvm.Code;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;

public class CodeJsonParser {

    private static CodeJsonParser instance = getInstance();

    private CodeJsonParser() {

    }

    public static CodeJsonParser getInstance() {
        if(instance == null){
            synchronized (CodeJsonParser.class) {
                if(instance == null)
                    instance = new CodeJsonParser();
            }

        }
        return instance;

    }

    Response response = new Response();

    public ResponseType codeJsonParser(String errorNum){

        JSONParser parser = new JSONParser();
// ..\COMMON\code.json
        // users/iyunjae/LEEYUNJAE/smilegate/workspace/AOMC/COMMON/code.json
        try {
//            URL url = getClass().getResource("./");
//            System.out.println(url.getPath());
//
//            File file = new File("./testFile");
//            PrintStream printStream = new PrintStream(new FileOutputStream(file));
//            System.setOut(printStream);
//            //System.setErr(printStream);
//            // file로 출력
//            System.out.println("url = " + url.getPath());
//
//            InputStream path = getClass().getResourceAsStream("/");
//            String path2 = CodeJsonParser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//            String path3 = CodeJsonParser.class.getProtectionDomain().getCodeSource().toString();
//
//            System.out.println(path3);

            Object obj = parser.parse(new FileReader("../../COMMON/code.json"));


            JSONObject jsonObject = (JSONObject) obj;
            JSONObject resp = (JSONObject) jsonObject.get(errorNum);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String r = objectMapper.writeValueAsString(resp);
            response = objectMapper.readValue(r, Response.class);


            return  ResponseType.res(response.getStatus(), response.getMessage(), response.getDescription());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  ResponseType.res(404, "파일 없음", "File Not Found");

    }

}