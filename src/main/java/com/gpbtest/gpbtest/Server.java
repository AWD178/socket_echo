package com.gpbtest.gpbtest;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;


public class Server {

    private static String SERVER_HOST = "localhost";
    private static Integer SERVER_PORT = 5050;
    private static Boolean SERVER_BLOCKING = false;
    private static Integer MAX_BUFFER_SIZE = 512;

    // client close connection message
    private static String CCC_MESSAGE = "Bue.";

    public static void main(String[] args) throws IOException {
        ServerSocketChannel appServer = ServerSocketChannel.open();
        Selector socketSelector = Selector.open();

        appServer.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        appServer.configureBlocking(SERVER_BLOCKING);
        appServer.register(socketSelector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started");

        // start app loop
        while (true) {
            socketSelector.select();
            Set<SelectionKey> keys = socketSelector.selectedKeys();
            // create selector keys iterator
            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isValid()) {

                    if (key.isAcceptable()) {
                        // add new connection
                        addClient(socketSelector, appServer);
                    }

                    if (key.isReadable()) {
                        socketEcho(key);
                    }
                }
            }
        }
    }

    private static void socketEcho(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);

        client.read(buffer);

        // if client send close message
        if (new String(buffer.array()).trim().equals(CCC_MESSAGE)) {
            buffer.clear();
            client.close();
            return;
        }

        // repeat client message and clear buffer
        buffer.flip();
        client.write(buffer);
        buffer.clear();
    }

    private static void addClient(Selector socketSelector, ServerSocketChannel appServer) throws IOException {
        SocketChannel client = appServer.accept();
        client.configureBlocking(false);
        client.register(socketSelector, SelectionKey.OP_READ);
    }

}
