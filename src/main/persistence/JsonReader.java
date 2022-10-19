package persistence;

import model.Filter;
import model.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

//represents a reader that reads image from JSON data in file
public class JsonReader {
    private String source;

    //constructs a reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads image from file and returns it. Throws IOException if an error occurs while reading
    //         the image data from file
    public Image read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseImage(jsonObject);
    }

    //EFFECTS: reads source file as string and return it
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    //EFFECTS: parses image from JSON object and returns it
    private Image parseImage(JSONObject jsonObject) {
        int width = Integer.parseInt(jsonObject.getString("width"));
        int height = Integer.parseInt(jsonObject.getString("height"));
        Image img = new Image(width, height);
        addListOfFilter(img, jsonObject);
        addDegreeOfPixelation(img, jsonObject);
        addImageResult(img, jsonObject);
        return img;
    }

    //MODIFIES: img
    //EFFECTS: parses listOfFilter from JSON object and adds them to image
    private void addListOfFilter(Image img, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("listOfFilter");
        for (Object json: jsonArray) {
            JSONObject nextFilter = (JSONObject) json;
            addFilter(img, nextFilter);
        }
    }

    //MODIFIES: img
    //EFFECTS: parses filter from JSON object and adds it to image
    private void addFilter(Image img, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Filter filter = new Filter(name);
        img.addFilter(filter);
        img.addIfUnique(filter);
    }

    //MODIFIES: img
    //EFFECTS: parses degreeOfPixelation from JSON object and adds it to image
    private void addDegreeOfPixelation(Image img, JSONObject jsonObject) {
        int degreeOfPixelation = Integer.parseInt(jsonObject.getString("degreeOfPixelation"));
        img.setDegreeOfPixelation(degreeOfPixelation);
    }

    //MODIFIES: img
    //EFFECTS: parses imageResult from JSON object and adds it to image
    private void addImageResult(Image img, JSONObject jsonObject) {
        String imageResult = jsonObject.getString("imageResult");
        img.setImageResult(imageResult);
    }
}
