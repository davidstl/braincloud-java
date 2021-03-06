package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerStateService {

    private enum Parameter {
        attributes,
        wipeExisting,
        playerName,
        summaryFriendData,
        playerPictureUrl,
        contactEmail,
        statusName,
        additionalSecs,
        details,
        durationSecs
    }

    private BrainCloudClient _client;

    public PlayerStateService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * @deprecated Use deleteUser() instead - Removal after September 1 2017
     */
    public void deletePlayer(IServerCallback callback) {

        JSONObject message = new JSONObject();

        ServerCall serverCall = new ServerCall(ServiceName.playerState,
                ServiceOperation.FULL_PLAYER_RESET, message, callback);
        _client.sendRequest(serverCall);
    }

    /**
     * Completely deletes the user record and all data fully owned by the
     * user. After calling this method, the user will need to
     * re-authenticate and create a new profile. This is mostly used for
     * debugging/qa.
     *
     * @param callback  The callback handler
     */
    public void deleteUser(IServerCallback callback) {

        JSONObject message = new JSONObject();

        ServerCall serverCall = new ServerCall(ServiceName.playerState,
                ServiceOperation.FULL_PLAYER_RESET, message, callback);
        _client.sendRequest(serverCall);
    }

    /**
     * Retrieve the user's attributes.
     *
     * @param callback The callback handler
     */
    public void getAttributes(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.playerState,
                ServiceOperation.GET_ATTRIBUTES, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Logs user out of the server.
     *
     * @param callback The callback handler
     */
    public void logout(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.playerState,
                ServiceOperation.LOGOUT, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * @deprecated Use readUserState() instead - Removal after September 1 2017
     */
    public void readPlayerState(IServerCallback callback) {

        JSONObject message = new JSONObject();

        ServerCall serverCall = new ServerCall(ServiceName.playerState,
                ServiceOperation.READ, message, callback);
        _client.sendRequest(serverCall);
    }

    /**
     * Read the state of the currently logged in user. This method returns a
     * JSON object describing most of the user's data: entities, statistics,
     * level, currency. Apps will typically call this method after
     * authenticating to get an up-to-date view of the user's data.
     *
     * @param callback The callback handler
     */
    public void readUserState(IServerCallback callback) {

        JSONObject message = new JSONObject();

        ServerCall serverCall = new ServerCall(ServiceName.playerState,
                ServiceOperation.READ, message, callback);
        _client.sendRequest(serverCall);
    }

    /**
     * Remove user's attributes.
     *
     * @param attributeNames
     *            Array of attribute names.
     * @param callback The callback handler
     */
    public void removeAttributes(String[] attributeNames,
                                 IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            JSONArray jsonAtts = new JSONArray();
            for (String att : attributeNames) {
                jsonAtts.put(att);
            }
            data.put(Parameter.attributes.name(), jsonAtts);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.REMOVE_ATTRIBUTES, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @deprecated Use resetUser() instead - Removal after September 1 2017
     */
    public void resetPlayer(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.playerState,
                ServiceOperation.GAME_DATA_RESET, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * This method will delete *most* data for the currently logged in user.
     * Data which is not deleted includes: currency, credentials, and purchase
     * transactions. ResetUser is different from DeleteUser in that the
     * user record will continue to exist after the reset (so the user does
     * not need to re-authenticate).
     *
     * @param callback The callback handler
     */
    public void resetUser(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.playerState,
                ServiceOperation.GAME_DATA_RESET, null, callback);
        _client.sendRequest(sc);
    }

    /**
     * Update user's attributes.
     *
     * @param jsonAttributes Single layer json string that is a set of key-value pairs
     * @param wipeExisting Whether to wipe existing attributes prior to update.
     */
    public void updateAttributes(String jsonAttributes, boolean wipeExisting,
                                 IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            JSONObject jsonData = new JSONObject(jsonAttributes);
            data.put(Parameter.attributes.name(), jsonData);
            data.put(Parameter.wipeExisting.name(), wipeExisting);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.UPDATE_ATTRIBUTES, data, callback);
            _client.sendRequest(serverCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @deprecated Use updateName() instead - Removal after September 1 2017
     */
    public void updatePlayerName(String name,
                                 IServerCallback callback) {
        updateName(name, callback);
    }

    /**
     * @deprecated Use updateName() instead - Removal after September 1 2017
     */
    public void updateUserName(String name,
                               IServerCallback callback) {
        updateName(name, callback);
    }

    /**
     * Sets the user's visible name
     *
     * @param name The name to be picked
     * @param callback The callback handler
     */
    public void updateName(String name,
                               IServerCallback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put(Parameter.playerName.name(), name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.playerState,
                ServiceOperation.UPDATE_NAME, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * Updates the "friend summary data" associated with the logged in user.
     * Some operations will return this summary data. For instance the social
     * leaderboards will return the player's score in the leaderboard along
     * with the friend summary data. Generally this data is used to provide
     * a quick overview of the user without requiring a separate API call
     * to read their public stats or entity data.
     *
     * @param jsonFriendSummaryData A JSON string defining the summary data.
     * For example:
     * {
     *   "xp":123,
     *   "level":12,
     *   "highScore":45123
     * }
     * @param callback The callback handler
     */
    public void updateSummaryFriendData(String jsonFriendSummaryData, IServerCallback callback) {

        JSONObject data = new JSONObject();
        try {
            JSONObject summaryFriendData = new JSONObject(jsonFriendSummaryData);
            data.put(Parameter.summaryFriendData.name(), summaryFriendData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCall sc = new ServerCall(ServiceName.playerState, ServiceOperation.UPDATE_SUMMARY, data, callback);
        _client.sendRequest(sc);
    }

    /**
     * @deprecated Use updateUserPictureUrl() instead - Removal after September 1 2017
     */
    public void updatePlayerPictureUrl(
            String pictureUrl,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playerPictureUrl.name(), pictureUrl);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.UPDATE_PICTURE_URL, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update User picture URL.
     *
     * Service Name - PlayerState
     * Service Operation - UPDATE_PICTURE_URL
     *
     * @param pictureUrl URL to apply
     * @param callback The callback handler
     */
    public void updateUserPictureUrl(
            String pictureUrl,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.playerPictureUrl.name(), pictureUrl);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.UPDATE_PICTURE_URL, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the user's contact email.
     * Note this is unrelated to email authentication.
     *
     * Service Name - PlayerState
     * Service Operation - UPDATE_CONTACT_EMAIL
     *
     * @param contactEmail Updated email
     * @param callback The method to be invoked when the server response is received
     */
    public void updateContactEmail(
            String contactEmail,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.contactEmail.name(), contactEmail);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.UPDATE_CONTACT_EMAIL, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete's the specified status
     *
     * Service Name - PlayerState
     * Service Operation - ClearUserStatus
     *
     * @param statusName 
     * @param callback The method to be invoked when the server response is received
     */
    public void clearUserStatus(
            String statusName,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.statusName.name(), statusName);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.CLEAR_USER_STATUS, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stack user's statuses
     *
     * Service Name - PlayerState
     * Service Operation - ClearUserStatus
     *
     * @param statusName
     * @param additionalSecs
     * @param details 
     * @param callback The method to be invoked when the server response is received
     */
    public void extendUserStatus(
            String statusName,
            int additionalSecs,
            String details,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.statusName.name(), statusName);
            data.put(Parameter.additionalSecs.name(), additionalSecs);
            JSONObject detailData = new JSONObject(details);
            data.put(Parameter.details.name(), detailData);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.EXTEND_USER_STATUS, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get user status
     *
     * Service Name - PlayerState
     * Service Operation - ClearUserStatus
     *
     * @param statusName
     * @param callback The method to be invoked when the server response is received
     */
    public void getUserStatus(
            String statusName,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.statusName.name(), statusName);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.GET_USER_STATUS, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set timed status for a user
     *
     * Service Name - PlayerState
     * Service Operation - ClearUserStatus
     *
     * @param statusName
     * @param callback The method to be invoked when the server response is received
     */
    public void setUserStatus(
            String statusName,
            int durationSecs,
            String details,
            IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.statusName.name(), statusName);
            data.put(Parameter.durationSecs.name(), durationSecs);
            JSONObject detailData = new JSONObject(details);
            data.put(Parameter.details.name(), detailData);

            ServerCall serverCall = new ServerCall(ServiceName.playerState,
                    ServiceOperation.GET_USER_STATUS, data, callback);
            _client.sendRequest(serverCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
