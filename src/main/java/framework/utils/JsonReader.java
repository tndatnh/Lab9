package framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonReader {

    /**
     * Đọc file JSON chứa danh sách UserData.
     * ObjectMapper của Jackson tự động map JSON → Java object.
     */
    public static List<UserData> readUsers(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // TypeReference<List<UserData>> nói cho Jackson biết cần parse thành List
        return mapper.readValue(new File(filePath),
                new TypeReference<List<UserData>>() {});
    }
}