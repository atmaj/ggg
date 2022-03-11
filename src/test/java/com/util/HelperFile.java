package com.util;

import com.common.UIOperator;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public final class HelperFile {
    private static String featurepath = System.getProperty("user.dir") + "\\src\\test\\resources\\features";
    private static Map<String, String> hashTestData;
    private static GherkintoJSON testG = new GherkintoJSON("pretty");
    private static String nameTag;
    private static String dynamicTimeStamp;
    private static ArrayList<String> reg;


    public static String getFileContent(String fileName) {
        try {
            InputStream ir = new FileInputStream(new File(fileName));
            int ch = -1;
            StringBuffer buff = new StringBuffer();
            while ((ch = ir.read()) != -1) {
                if ((char) ch == '\r') {
                    //System.out.println("find");
                }
                buff.append(((char) ch));
            }
            ir.close();
            return buff.toString();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }

	/*public static String getdataFromFile(String fileName){

		String filePath = System.getProperty("user.dir")+"\\src\\test\\resources\\test_data\\Request" + fileName;
		try{
			return new String(Files.readAllBytes(Paths.get(filePath)))
		}catch(Exception e){
			return null;
		}
	}*/

    public static void writeToFile(String filename, String content, String ext) throws IOException {
        File f = new File(filename + "." + ext);

        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter fs = new FileWriter(f);
        fs.write(content + "\n");
        fs.close();
    }

    public static HashSet<String> createScenario(String runOn) throws Exception {
        try {
            HashSet<String> regressionTag = new HashSet<String>();
            File dynamicdir = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\features\\DynamicFeature\\");
            String fileDir = System.getProperty("user.dir") + "\\src\\test\\resources\\features\\DynamicFeature\\";
            FileUtils.deleteDirectory(new File(fileDir));
            if (!dynamicdir.exists()) {
                dynamicdir.mkdir();
                System.out.println("Dynamic feature directory created");
            }

            System.out.println("Read regression tags");
            reg = UIOperator.readRegressionTags(runOn);
            for (int x = 0; x < reg.size(); x++) {
                nameTag = reg.get(x);
                hashTestData = UIOperator.initializeExcel(nameTag);
                File f = new File(featurepath);
                File[] featureFiles = f.listFiles();
                String finalFeatureFile = "Feature: Dynamic Feature \n\n\n";
                JSONArray masterJSON = new JSONArray();
                String requiredTags = hashTestData.get("ORDERED TAGS");
                System.out.println("required tags" + requiredTags);
                if (requiredTags.isEmpty()) {
                    requiredTags = nameTag;
                }
                for (int a = 0; a < featureFiles.length; a++) {
                    if (featureFiles[a].isFile()) {
                        String jsonOfFeature = testG.gherkinTojson(featureFiles[a].getAbsolutePath());
                        JSONObject feature = (new JSONArray(jsonOfFeature)).getJSONObject(0);
                        masterJSON.put(feature);
                    }
                }

                String[] allTags = requiredTags.split(",");
                finalFeatureFile += hashTestData.get("EXECUTABLE TAGS") + "\nScenario:" + hashTestData.get("SCENARIO DESCRIPTION")
                        + "\n";

                for (int k = 0; k < allTags.length; k++) {
                    for (int y = 0; y < masterJSON.length(); y++) {
                        JSONArray elements = ((JSONObject) masterJSON.get(y)).getJSONArray("elements");
                        //System.out.println(elements.get(0).toString());
                        for (int i = 0; i < elements.length(); i++) {
                            String keyword = ((JSONObject) elements.get(i)).get("keyword").toString();
                            String scenarioName = ((JSONObject) elements.get(i)).get("name").toString();
                            JSONArray tags = ((JSONArray) (((JSONObject) elements.get(i)).get("tags")));
                            //System.out.println(tags.length());
                            for (int j = 0; j < tags.length(); j++) {
                                String tag = ((JSONObject) tags.get(j)).get("name").toString();
                                //System.out.println(allTags[k]);
                                //System.out.println(tag.trim());
                                if (allTags[k].equals(tag.trim())) {
                                    JSONArray steps = ((JSONArray) (((JSONObject) elements.get(i)).get("steps")));
                                    for (int h = 0; h < steps.length(); h++) {
                                        JSONObject step = (JSONObject) steps.get(h);
                                        finalFeatureFile += step.get("keyword").toString() + step.get("name").toString() + "\n";

                                        try {
                                            JSONArray dataTable = ((JSONArray) (((JSONObject) steps.get(h)).get("rows")));
                                            for (int b = 0; b < dataTable.length(); b++) {
                                                JSONArray dataTableRow = (JSONArray) (((JSONObject) dataTable.get(b)).get("cells"));
                                                for (int c = 0; c < dataTable.length(); c++) {
                                                    finalFeatureFile += "|" + (dataTableRow.get(c).toString());
                                                }
                                                finalFeatureFile += "|\n";
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                    try {
                                        JSONArray examples = ((JSONArray) (((JSONObject) elements.get(i)).get("examples")));
                                        finalFeatureFile += "\nExamples:\n";
                                        for (int m = 0; m < examples.length(); m++) {
                                            JSONArray lines = ((JSONArray) (((JSONObject) elements.get(m)).get("rows")));
                                            for (int b = 0; b < lines.length(); b++) {
                                                JSONArray example = ((JSONArray) (((JSONObject) elements.get(b)).get("cells")));
                                                for (int c = 0; c < example.length(); c++) {
                                                    finalFeatureFile += "|" + (example.get(c).toString());
                                                }
                                                finalFeatureFile += "|\n";
                                            }
                                        }

                                    } catch (Exception e) {

                                    }
                                }
                            }
                        }
                    }
                    regressionTag.add(nameTag.substring(1));
                }
                HelperFile.writeToFile(System.getProperty("user.dir") + "\\src\\test\\resources\\features\\DynamicFeature\\DynamicFeature_" + nameTag.substring(1), finalFeatureFile, "feature");
            }
            System.out.println("Dynamic File/s Generated for tags" + regressionTag);
            return regressionTag;
        } catch (Exception e) {
            HashSet<String> regressionTag = new HashSet<String>();
            e.getMessage();
            return regressionTag;
        }

    }
}




