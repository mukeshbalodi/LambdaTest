package com.qa.testrailmanager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;

public class TestRailManager {

    public static final String TEST_RAIL_USERNAME = "mukeshbalodi5@gmail.com";
    public static final String TEST_RAIL_PASSWORD = "capIX5uV3SJJ0mHeDZYE-XxpVdl9ASlaKmzPs2vSU"; // API Key
    public static final String TEST_RAIL_ENGINE_URL = "https://mukeshbalodi.testrail.io/";

    public static final int TEST_CASE_PASS_STATUS = 1;
    public static final int TEST_CASE_FAIL_STATUS = 5;

    // ✅ Static Test Run ID (used for all test cases)
    public static final String TEST_RUN_ID = "10";

    public static void addResultsForTestCase(String testCaseId, int status, String error) {
        APIClient client = new APIClient(TEST_RAIL_ENGINE_URL);
        client.setUser(TEST_RAIL_USERNAME);
        client.setPassword(TEST_RAIL_PASSWORD);

        Map<String, Object> data = new HashMap<>();
        data.put("status_id", status);
        data.put("comment", "This test is automated by Mukesh. " + error);

        try {
            String caseId = testCaseId.replaceAll("[^\\d]", ""); // Extract numeric ID from "C18"
            String endpoint = "add_result_for_case/" + TEST_RUN_ID + "/" + caseId;
            client.sendPost(endpoint, data);
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL for TestRail API.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Exception while connecting to TestRail API.");
            e.printStackTrace();
        } catch (APIException e) {
            System.err.println("TestRail API Exception occurred.");
            e.printStackTrace();
        }
    }
}
