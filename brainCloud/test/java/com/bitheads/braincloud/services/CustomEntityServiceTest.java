package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.ReasonCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by prestonjennings on 15-09-01.
 */
public class CustomEntityServiceTest extends TestFixtureBase {
    private final String _defaultEntityType = "athletes";

    @Test
    public void testCreateCustomEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getCustomEntityService().createEntity(
                _defaultEntityType,
                Helpers.createJsonPair("test", "testy"),
                null,
                1,
                true,
                tr);

        tr.Run();
    }

    @Test
    public void testDeleteEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultEntity(ACL.Access.None);

        _wrapper.getCustomEntityService().deleteEntity(
                _defaultEntityType,
                entityId,
                1,
                tr);

        tr.Run();
    }

    @Test
    public void testGetCount() throws Exception {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getCustomEntityService().getCount(
                _defaultEntityType,
                "{\"data.position\": \"defense\"}",
                tr);

        tr.Run();
    }

    @Test
    public void testGetEntityPage() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        _wrapper.getCustomEntityService().getEntityPage(
                _defaultEntityType, createContext(125, 1, _defaultEntityType),
                tr);
        tr.Run();
    }

    @Test
    public void testGetEntityPageOffset() throws Exception {
        TestResult tr = new TestResult(_wrapper);

         _wrapper.getCustomEntityService().getEntityPage(_defaultEntityType,
                createContext(50, 1, _defaultEntityType),
                tr);
        tr.Run();

         int page = 0;
        page = tr.m_response.getJSONObject("data").getJSONObject("results").getInt("page");

        String context = tr.m_response.getJSONObject("data").getString("context");

        _wrapper.getCustomEntityService().getEntityPageOffset(
                _defaultEntityType,
                context,
                1,
                tr);

        tr.Run();
    }
    ////////////////////////////////////////

    @Test
    public void testReadEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultEntity(ACL.Access.None);
        _wrapper.getCustomEntityService().readEntity(
                _defaultEntityType,
                entityId,
                tr);

        tr.Run();
    }

    @Test
    public void testUpdateEntity() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultEntity(ACL.Access.None);
        _wrapper.getCustomEntityService().updateEntity(
                _defaultEntityType,
                entityId,
                1,
                "{\"test\": \"Testing\"}",
                null,
                0,
                tr);

        tr.Run();
    }
    
    @Test
    public void testUpdateEntityFields() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultEntity(ACL.Access.None);
        _wrapper.getCustomEntityService().updateEntityFields(
                _defaultEntityType,
                entityId,
                1,
                "{\"goals\": \"3\"}",
                tr);
        tr.Run();
    }

    @Test
    public void testDeleteEntities() throws Exception {
        TestResult tr = new TestResult(_wrapper);
        String entityId = createDefaultEntity(ACL.Access.None);
        _wrapper.getCustomEntityService().deleteEntities(
                _defaultEntityType,
                "{\"entityId\": \"Testing\"}",
                tr);
        tr.Run();
    }

    /// <summary>
    /// Creates a default entity on the server
    /// </summary>
    /// <param name="accessLevel"> accessLevel for entity </param>
    /// <returns> The ID of the entity </returns>
    private String createDefaultEntity(ACL.Access accessLevel) {
        TestResult tr = new TestResult(_wrapper);

        ACL access = new ACL();
        access.setOther(accessLevel);
        String entityId = "";

        //Create entity
        _wrapper.getCustomEntityService().createEntity(
            _defaultEntityType,
            Helpers.createJsonPair("test", "testy"),
            null,
            1,
            true,
            tr);

        if (tr.Run()) {
            entityId = getEntityId(tr.m_response);
        }

        return entityId;
    }

    /// <summary>
    /// Returns the entityId from a raw json response
    /// </summary>
    /// <param name="json"> Json to parse for ID </param>
    /// <returns> entityId from data </returns>
    private String getEntityId(JSONObject json) {
        try {
            return json.getJSONObject("data").getString("entityId");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return "";
    }

    private String createContext(int numberOfEntitiesPerPage, int startPage, String entityType) throws Exception {
        JSONObject context = new JSONObject();

        JSONObject pagination = new JSONObject();
        pagination.put("rowsPerPage", numberOfEntitiesPerPage);
        pagination.put("pageNumber", startPage);
        context.put("pagination", pagination);

        JSONObject searchCriteria = new JSONObject();
        searchCriteria.put("entityType", entityType);

        return context.toString();
    }
}