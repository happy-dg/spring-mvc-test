package org.ksug.springcamp.testmvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TestUtil {

    private static final String CHARACTER = "a";

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );


    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static byte[] convertObjectToFormUrlEncodedBytes(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Map<String, Object> propertyValues = mapper.convertValue(object, Map.class);

        Set<String> propertyNames = propertyValues.keySet();
        Iterator<String> nameIter = propertyNames.iterator();

        StringBuilder formUrlEncoded = new StringBuilder();

        for (int index=0; index < propertyNames.size(); index++) {
            String currentKey = nameIter.next();
            Object currentValue = propertyValues.get(currentKey);

            formUrlEncoded.append(currentKey);
            formUrlEncoded.append("=");
            formUrlEncoded.append(currentValue);

            if (nameIter.hasNext()) {
                formUrlEncoded.append("&");
            }
        }

        return formUrlEncoded.toString().getBytes();
    }

    public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
}
