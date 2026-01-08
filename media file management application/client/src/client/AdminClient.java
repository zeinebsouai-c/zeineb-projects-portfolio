package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AdminClient {
    private String protocol;
    private InetAddress serverAddress;
    private int serverPort;

    public AdminClient(String protocol, String serverAddress, int serverPort) throws UnknownHostException {
        this.protocol = protocol.toLowerCase();
        this.serverAddress = InetAddress.getByName(serverAddress);
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        if (protocol.equals("tcp")) {
            startTCPClient();
        } else if (protocol.equals("udp")) {
            startUDPClient();
        } else {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
    }

    private void startTCPClient() throws IOException {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in))) {

            String userInput;
            while ((userInput = userIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Server response: " + in.readLine());
            }
        }
    }

    private void startUDPClient() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = new byte[1024];
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

        String userInput;
        while ((userInput = userIn.readLine()) != null) {
            byte[] sendData = userInput.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);
            String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server response: " + serverResponse);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter protocol (tcp/udp): ");
        String protocol = scanner.nextLine().trim().toLowerCase();

        System.out.print("Enter server address: ");
        String serverAddress = scanner.nextLine().trim();

        System.out.print("Enter server port: ");
        int serverPort = scanner.nextInt();


        try {
            AdminClient client = new AdminClient(protocol, serverAddress, serverPort);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
