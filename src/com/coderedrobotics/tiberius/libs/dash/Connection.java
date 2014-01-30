/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.tiberius.libs.dash;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public class Connection implements SimpleSock {

    private SocketConnection connection;
    private InputStreamBuffer dataInputStream;
    private OutputStreamBuffer dataOutputStream;

    public Connection() {
        connection = null;
    }

    private void reconnect() {
        boolean retry = true;
        while (retry) {
            retry = false;
            try {
                if (dataInputStream != null) {//close if open
                    dataInputStream.close();
                }
                if (dataOutputStream != null) {//close if open
                    dataOutputStream.close();
                }
                if (connection != null) {//close if open
                    connection.close();
                }
            } catch (IOException ex) {
            }
            connection = null;
            while (connection == null) {//keeps trying to connect
                System.out.println("Connecting");
                try {
                    connection = (SocketConnection) Connector.open(
                            "socket://10.27.71.55:1180", Connector.READ_WRITE);
                    System.out.println("Connected");
                } catch (IOException ex) {
                    connection = null;
                    System.out.println("No Connection");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex1) {
                    }
                }
            }
            //setup the reader and writer objects
            try {
                dataOutputStream = new OutputStreamBuffer(
                        connection.openDataOutputStream());
                dataInputStream = new InputStreamBuffer(
                        connection.openDataInputStream());
                System.out.println("dataInputStream");
            } catch (IOException ex) {
                retry = true;
            }
        }
    }

    public synchronized long readLong()
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            return dataInputStream.readLong();
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized int readInt()
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            return dataInputStream.readInt();
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized double readDouble()
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            return dataInputStream.readDouble();
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized char readChar()
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            return dataInputStream.readChar();
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }

    }

    public synchronized byte readByte()
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            return dataInputStream.readByte();
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized void writeLong(long l)
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            dataOutputStream.writeLong(l);
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized void writeInt(int i)
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            dataOutputStream.writeInt(i);
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized void writeDouble(double d)
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            dataOutputStream.writeDouble(d);
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized void writeChar(char c)
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            dataOutputStream.writeChar(c);
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized void writeByte(byte b)
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            dataOutputStream.writeByte(b);
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public synchronized String readString()
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        int size = readInt();
        String result = "";
        for (int i = 0; i < size; i++) {
            result += readChar();
        }
        return result;
    }

    public synchronized void writeString(String s)
            throws ConnectionResetException {
        writeInt(s.length());
        for (int i = 0; i < s.length(); i++) {
            writeChar(s.charAt(i));
        }
    }

    public synchronized void flush()
            throws ConnectionResetException {
        if (connection == null) {
            reconnect();
        }
        try {
            dataOutputStream.flush();
        } catch (IOException ex) {
            reconnect();
            throw new ConnectionResetException();
        }
    }

    public boolean isServer() {
        return false;
    }
}