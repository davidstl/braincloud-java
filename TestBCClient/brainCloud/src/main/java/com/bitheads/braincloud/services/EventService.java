package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONException;
import org.json.JSONObject;

public class EventService {

    private enum Parameter {
        toId,
        eventType,
        eventData,
        recordLocally,
        fromId,
        eventId,
        includeIncomingEvents,
        includeSentEvents,
        evId
    }

    private BrainCloudClient _client;

    public EventService(BrainCloudClient client) {
        _client = client;
    }

    /**
     * Sends an event to the designated player id with the attached json data.
     * Any events that have been sent to a player will show up in their incoming
     * event mailbox. If the recordLocally flag is set to true, a copy of
     * this event (with the exact same event id) will be stored in the sending
     * player's "sent" event mailbox.
     *
     * Note that the list of sent and incoming events for a player is returned
     * in the "ReadPlayerState" call (in the BrainCloudPlayer module).
     *
     * Service Name - event
     * Service Operation - SEND
     *
     * @param toPlayerId The id of the player who is being sent the event
     * @param eventType The user-defined type of the event.
     * @param jsonEventData The user-defined data for this event encoded in JSON.
     * @param callback The callback.
     */
    public void sendEvent(String toPlayerId, String eventType, String jsonEventData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();

            data.put(Parameter.toId.name(), toPlayerId);
            data.put(Parameter.eventType.name(), eventType);

            JSONObject jsonData = new JSONObject(jsonEventData);
            data.put(Parameter.eventData.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.SEND, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
        }
    }

    /**
     * Updates an event in the player's incoming event mailbox.
     *
     * Service Name - event
     * Service Operation - UPDATE_EVENT_DATA
     *
     * @param evId The event id
     * @param jsonEventData The user-defined data for this event encoded in JSON.
     * @param callback The  callback.
     */
    public void updateIncomingEventData(String evId, String jsonEventData, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.evId.name(), evId);

            JSONObject jsonData = new JSONObject(jsonEventData);
            data.put(Parameter.eventData.name(), jsonData);

            ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.UPDATE_EVENT_DATA, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
        }
    }

    /**
     * Delete an event out of the player's incoming mailbox.
     *
     * Service Name - event
     * Service Operation - DELETE_INCOMING
     *
     * @param evId The event id
     * @param callback The callback.
     */
    public void deleteIncomingEvent(String evId, IServerCallback callback) {
        try {
            JSONObject data = new JSONObject();
            data.put(Parameter.evId.name(), evId);

            ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.DELETE_INCOMING, data, callback);
            _client.sendRequest(sc);

        } catch (JSONException e) {
        }
    }

    /**
     * Get the events currently queued for the player.
     *
     * Service Name - event
     * Service Operation - GET_EVENTS
     *
     * @param callback The method to be invoked when the server response is received
     */
    public void getEvents(IServerCallback callback) {
        ServerCall sc = new ServerCall(ServiceName.event, ServiceOperation.GET_EVENTS, null, callback);
        _client.sendRequest(sc);
    }
}
