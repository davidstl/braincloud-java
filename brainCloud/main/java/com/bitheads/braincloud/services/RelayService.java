package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;
import com.bitheads.braincloud.client.IRelayCallback;
import com.bitheads.braincloud.client.IRelayConnectCallback;
import com.bitheads.braincloud.client.IRelaySystemCallback;
import com.bitheads.braincloud.client.IServerCallback;
import com.bitheads.braincloud.client.ServiceName;
import com.bitheads.braincloud.client.ServiceOperation;
import com.bitheads.braincloud.comms.ServerCall;

import org.json.JSONObject;

public class RelayService {

    private BrainCloudClient _client;

    public static final int TO_ALL_PLAYERS = 131;
    public static final int CHANNEL_HIGH_PRIORITY_1 = 0;
    public static final int CHANNEL_HIGH_PRIORITY_2 = 1;
    public static final int CHANNEL_NORMAL_PRIORITY = 2;
    public static final int CHANNEL_LOW_PRIORITY = 3;

    public RelayService(BrainCloudClient client) {
        _client = client;
    }

    /**
    * Start a connection, based on connection type to 
    * brainClouds Relay Servers. Connect options come in
    * from ROOM_ASSIGNED lobby callback.
    * 
    * @param options {
    *   ssl: false,
    *   host: "168.0.1.192"
    *   port: 9000,
    *   passcode: "somePasscode",
    *   lobbyId: "55555:v5v:001"
    * }
    * @param callback Callback objects that report Success or Failure|Disconnect.
    */
    public void connect(JSONObject options, IRelayConnectCallback callback) {
        _client.getRelayComms().connect(options, callback);
    }
    
    /**
     * Disconnects from the relay server
     */
    public void disconnect() {
        _client.getRelayComms().disconnect();
    }

    /**
     * Returns whether or not we have a successful connection with
     * the relay server
     */
    public boolean isConnected() {
        return _client.getRelayComms().isConnected();
    }

    /**
     * Get the current ping for our user.
     * Note: Pings are not distributed among other members. Your game will
     * have to bundle it inside a packet and distribute to other peers.
     */
    public int getPing() {
        return _client.getRelayComms().getPing();
    }

    /**
     * Set the ping interval. Ping allows to keep the connection
     * alive, but also inform the player of his current ping.
     * The default is 1000 miliseconds interval. (1 seconds)
     */
    public void setPingInterval(int intervalMS) {
        _client.getRelayComms().setPingInterval(intervalMS);
    }

    /**
     * Get the lobby's owner profile Id.
     */
    public String getOwnerProfileId() {
        return _client.getRelayComms().getOwnerProfileId();
    }

    /**
     * Returns the profileId associated with a netId.
     */
    public String getProfileIdForNetId(int netId) {
        return _client.getRelayComms().getProfileIdForNetId(netId);
    }

    /**
     * Returns the netId associated with a profileId.
     */
    public int getNetIdForProfileId(String profileId) {
        return _client.getRelayComms().getNetIdForProfileId(profileId);
    }

    /**
     * Register callback for relay messages coming from peers.
     * 
     * @param callback Called whenever a relay message was received.
     */
    public void registerRelayCallback(IRelayCallback callback) {
        _client.getRelayComms().registerRelayCallback(callback);
    }
    public void deregisterRelayCallback() {
        _client.getRelayComms().deregisterRelayCallback();
    }

    /**
     * Register callback for RelayServer system messages.
     * 
     * @param callback Called whenever a system message was received. function(json)
     * 
     * # CONNECT
     * Received when a new member connects to the server.
     * {
     *   op: "CONNECT",
     *   profileId: "...",
     *   ownerId: "...",
     *   netId: #
     * }
     * 
     * # NET_ID
     * Receive the Net Id assossiated with a profile Id. This is
     * sent for each already connected members once you
     * successfully connected.
     * {
     *   op: "NET_ID",
     *   profileId: "...",
     *   netId: #
     * }
     * 
     * # DISCONNECT
     * Received when a member disconnects from the server.
     * {
     *   op: "DISCONNECT",
     *   profileId: "..."
     * }
     * 
     * # MIGRATE_OWNER
     * If the owner left or never connected in a timely manner,
     * the relay-server will migrate the role to the next member
     * with the best ping. If no one else is currently connected
     * yet, it will be transferred to the next member in the
     * lobby members' list. This last scenario can only occur if
     * the owner connected first, then quickly disconnected.
     * Leaving only unconnected lobby members.
     * {
     *   op: "MIGRATE_OWNER",
     *   profileId: "..."
     * }
     */
    public void registerSystemCallback(IRelaySystemCallback callback) {
        _client.getRelayComms().registerSystemCallback(callback);
    }
    public void deregisterSystemCallback() {
        _client.getRelayComms().deregisterSystemCallback();
    }

    /**
     * Send a packet to peer(s)
     * 
     * @param data Byte array for the data to send
     * @param toNetId The net id to send to, TO_ALL_PLAYERS to relay to all.
     * @param reliable Send this reliable or not.
     * @param ordered Receive this ordered or not.
     * @param channel One of: (CHANNEL_HIGH_PRIORITY_1, CHANNEL_HIGH_PRIORITY_2, CHANNEL_NORMAL_PRIORITY, CHANNEL_LOW_PRIORITY)
     */
    public void send(byte[] data, int toNetId, boolean reliable, boolean ordered, int channel) {
        _client.getRelayComms().sendRelay(data, toNetId, reliable, ordered, channel);
    }
}
