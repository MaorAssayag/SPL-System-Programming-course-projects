//package bgu.spl181.net.impl.Blockbuster;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class BlockbusterClient {
//    public static void main(String[] args) throws IOException {
//        boolean down = false;
//        if (args.length == 0) {
//            args = new String[]{"127.0.0.1", "hello"};
//        }
//
//        if (args.length < 2) {
//            System.out.println("you must supply two arguments: host, message");
//            System.exit(1);
//        }
//
//        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
//        try (Socket sock = new Socket(args[0], 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//            Scanner keyboard = new Scanner(System.in);
//            while (true) {
//                try {
//                    out.write(keyboard.nextLine());
//                    out.newLine();
//                    out.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    String line = in.readLine();
//                    System.out.println("message from server: " + line);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//}

package bgu.spl181.net.impl.Blockbuster;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class BlockbusterClient {
    public static void main(String[] args) throws IOException {
        boolean down =false;
        if (args.length == 0) {
            args = new String[]{"localhost", "hello"};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 7777);
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
            Thread keyborde = new Thread(()-> {
                Scanner keyboard = new Scanner(System.in);
                    while (true) {
                        try {
                            out.write(keyboard.nextLine());
                            out.newLine();
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

            }
            );
            Thread two  = new Thread(()-> {
                while (true) {
                    try {
                        String line = in.readLine();
                        System.out.println("message from server: " + line);
                        if(line.contains("ACK signout succeeded")) {
                            System.out.println("done");
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            );
            two.start();
            keyborde.start();
            two.join();
            keyborde.stop();
            System.out.println("done2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

