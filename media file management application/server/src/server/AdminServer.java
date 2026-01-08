/*
package server;

import contract.*;
import domainLogic.Admin;
import domainLogic.*;
import java.util.Scanner;

import java.io.*;
import java.net.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class AdminServer {
    private Admin admin;
    private String protocol;
    private int capacity;

    public AdminServer(String protocol, int capacity) {
        this.admin = new Admin(100);
        this.protocol = protocol.toLowerCase();
        this.capacity = capacity;
    }

    public void start() throws IOException {
        if (protocol.equals("tcp")) {
            startTCPServer();
        } else if (protocol.equals("udp")) {
            startUDPServer();
        } else {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
    }

    private void startTCPServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("TCP Server started on port 8080");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new TCPClientHandler(clientSocket)).start();  //each connection is handled in a separate thread
        }
    }

    private void startUDPServer() throws IOException {
        DatagramSocket socket = new DatagramSocket(8080);
        System.out.println("UDP Server started on port 8080");
        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            new Thread(new UDPClientHandler(socket, packet)).start();   // each datagram is processed in a separate thread
        }
    }

    private class TCPClientHandler implements Runnable {
        private Socket clientSocket;

        public TCPClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String command;
                while ((command = in.readLine()) != null) {
                    String response = handleCommand(command);
                    out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class UDPClientHandler implements Runnable {
        private DatagramSocket socket;
        private DatagramPacket packet;

        public UDPClientHandler(DatagramSocket socket, DatagramPacket packet) {
            this.socket = socket;
            this.packet = packet;
        }

        @Override
        public void run() {
            try {
                String command = new String(packet.getData(), 0, packet.getLength());
                String response = handleCommand(command);
                byte[] buffer = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0].toLowerCase()) {
            case "insert":
                if (parts.length < 9) return "Error: Invalid insert command";
                // General Media Data
                String mediaType = parts[0].toLowerCase();
                String uploaderName = parts[1];
                String tagsString = parts[2];
                int size = Integer.parseInt(parts[3]);
                BigDecimal cost = new BigDecimal(parts[4]);
                int duration = Integer.parseInt(parts[5]);
                String address = parts[parts.length - 2];
                String title = parts[parts.length - 1];

                Set<Tag> tags = new HashSet<>();
                if (!tagsString.equals(",")) {
                    for (String tag : tagsString.split(",")) {
                        tags.add(Tag.valueOf(tag.trim()));
                    }
                }

                Uploader uploader = admin.findUploaderByName(uploaderName);
                if (uploader == null) {
                    return "Error: Uploader '" + uploaderName + "' not found.";
                }


                switch (mediaType.toLowerCase()) {
                    case "audio":
                        if (parts.length < 10) return "Error: Insufficient data for audio.";
                        int samplingRate = Integer.parseInt(parts[9]);
                        admin.uploadMedia(new AudioImpl(uploader, tags, size, cost, duration, samplingRate, address, title),uploaderName);
                        break;
                    case "video":
                        if (parts.length < 10) return "Error: Insufficient data for video.";
                        int resolution = Integer.parseInt(parts[9]);
                        admin.uploadMedia(new VideoImpl(uploader, tags, size, cost, duration, resolution, address, title),uploaderName);
                        break;
                    case "audiovideo":
                        if (parts.length < 11) return "Error: Insufficient data for audioVideo.";
                        samplingRate = Integer.parseInt(parts[9]);
                        resolution = Integer.parseInt(parts[10]);
                        admin.uploadMedia(new AudioVideoImpl(uploader, tags, size, cost, duration, samplingRate, resolution, address, title),uploaderName);
                        break;
                    default:
                        return "Error: Unsupported media type.";
                }
                return "Insertion successful for " + title;

            case "remove":
                if (parts.length < 2) return "Error: Invalid remove command";
                String removeAddress = parts[1];
                String result = admin.deleteMediaFile(removeAddress);
                return result;  // directly returning the result as a String
            case "display":
                StringBuilder response = new StringBuilder("List of audios:\n");
                for (MediaContentImpl media : admin.getMediaFiles(mediaType)) {
                    response.append("Title: ").append(media.getTitle())
                            .append(", Address: ").append(media.getAddress())
                            .append(", Access Count: ").append(media.getAccessCount()).append("\n");
                }
                return response.toString();
            case "modify":
                if (parts.length < 2) return "Error: Invalid modify command";
                String modifyAddress = parts[1];
                AudioImpl audio = (AudioImpl) admin.findAudioByAddress(modifyAddress);
                if (audio != null) {
                    audio.incrementAccessCount();
                    return "Access count for '" + audio.getAddress() + "' incremented to: " + audio.getAccessCount();
                } else {
                    return "Audio not found with address: " + modifyAddress;
                }
            case "savejos":
                if (parts.length < 2) return "Error: Invalid saveJOS command";
                String saveJOSFilename = parts[1];
                try {
                    admin.saveStateJOS(saveJOSFilename);
                    return "State saved to " + saveJOSFilename;
                } catch (IOException e) {
                    return "Failed to save state: " + e.getMessage();
                }
            case "loadjos":
                if (parts.length < 2) return "Error: Invalid loadJOS command";
                String loadJOSFilename = parts[1];
                try {
                    admin = Admin.loadStateJOS(loadJOSFilename);
                    return "State loaded from " + loadJOSFilename;
                } catch (IOException | ClassNotFoundException e) {
                    return "Failed to load state: " + e.getMessage();
                }
            case "savejbp":
                if (parts.length < 2) return "Error: Invalid saveJBP command";
                String saveJBPFilename = parts[1];
                try {
                    admin.saveStateJBP(saveJBPFilename);
                    return "State saved to " + saveJBPFilename;
                } catch (IOException e) {
                    return "Failed to save state: " + e.getMessage();
                }
            case "loadjbp":
                if (parts.length < 2) return "Error: Invalid loadJBP command";
                String loadJBPFilename = parts[1];
                try {
                    admin = Admin.loadStateJBP(loadJBPFilename);
                    return "State loaded from " + loadJBPFilename;
                } catch (IOException e) {
                    return "Failed to load state: " + e.getMessage();
                }
            default:
                return "Invalid command";
        }
    }

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter protocol (tcp/udp): ");
        String protocol = scanner.nextLine().trim().toLowerCase();

        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();

        try {
            AdminServer server = new AdminServer(protocol, capacity);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

 */
