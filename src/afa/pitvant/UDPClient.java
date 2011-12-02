package afa.pitvant;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UDPClient extends Thread {
		
	private float longitude, latitude;
	private InetAddress serverAddr;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private String coords;
	private String SERVERIP;
	private int SERVERPORT;
	
	/* Constructor */
	public UDPClient (String serverIP, int serverPORT){
		Log.d("Contructor", "Created");
		this.SERVERIP = serverIP;
		this.SERVERPORT = serverPORT;
		try {
			serverAddr = InetAddress.getByName(SERVERIP);
			socket = new DatagramSocket();
			Log.d("Contructor IP", String.valueOf(SERVERPORT));
			Log.d("Contructor Port", SERVERIP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Definition and initialization of the handler associated with this thread */
	private Handler udpHandler = new Handler () {
		
		/* Function to handle received messages from main thread */
		public void handleMessage (Message msg) {
			if (msg.what == 0){
				/* loading coordinates to local variables */
				Log.d("Handler", "Data received.");
				longitude=msg.getData().getFloat("longitude", 0);
				latitude=msg.getData().getFloat("latitude", 0);
				Log.d("Handler Coords", String.valueOf(longitude)+":"+String.valueOf(latitude));
				/* loading server ip and port to local variables */
				//SERVERIP = msg.getData().getString("serverIp");
				//SERVERPORT = msg.getData().getInt("serverPort");
				try {
					/* preparing the InetAddress and DatagramSocket to be used in sending the DatagramPacket */
					serverAddr = InetAddress.getByName(SERVERIP);
					socket = new DatagramSocket();
					Log.d("Handler IP", SERVERIP);
					Log.d("Handler Port", String.valueOf(SERVERPORT));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				run();
			}
		}
	};
	/* Function that returns the Handler associated with this thread */
	public Handler getHandler () {
		Log.d("getHandler", "Done");
		return udpHandler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		/* Conversion of coordinates to a string in desired format */
		coords = String.valueOf(longitude)+":"+String.valueOf(latitude);
		Log.d("Run Coords:", coords);
		byte[] data = coords.getBytes();
		/* preparation o DatagramPacket with formated String, server address and server port */
		packet = new DatagramPacket(data, data.length, serverAddr, SERVERPORT);
		Log.d("Run DataLen", String.valueOf(data.length));
		Log.d("Run DataIP", serverAddr.getHostAddress());
		Log.d("Run DataPort", String.valueOf(SERVERPORT));
		
		try {
			Log.d("UDP", "Sending");
			socket.send(packet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
