import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

//Class for testing read/write methods for ticket objects in arraylist
public class MovieDriver {
    public static void main(String[] args) {
        String newFile = "readFiles.txt";
        ArrayList<Ticket> list = new ArrayList<>();
        Movie m1 = new Movie("Interstellar", 185, 4.5, "9:00PM");
        Movie m2 = new Movie("Batman", 185, 4.7, "5:00PM");
        Movie m3 = new Movie("The Flash", 185, 2.8, "7:00PM");

        TheatreRoom room1 = new TheatreRoom(m1, 0);
        TheatreRoom room2 = new TheatreRoom(m2, 1);
        TheatreRoom room3 = new TheatreRoom(m3, 2);

        Ticket t1 = new Ticket(m1, room1, 0);
        Ticket t2 = new Ticket(m2, room2, 1);
        Ticket t3 = new Ticket(m3, room3, 2);

        list.add(t1);
        list.add(t2);
        list.add(t3);

        writeTicket(list);

        System.out.println("Tickets have been written to ticketFile.txt");

        try {
            ArrayList<Ticket> ticketsRead = readListFromFile("ticketFile.txt");
            System.out.println("tickets have been read from ticketFile.txt");
            for (Ticket t : ticketsRead) {
                System.out.println(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeTicket(ArrayList<Ticket> tickets) {
        try {
            ObjectOutputStream bw = new ObjectOutputStream(new FileOutputStream("ticketFile.txt"));
            bw.writeObject(tickets);
        } catch (Exception e) {
        }
    }

    public static ArrayList<Ticket> readListFromFile(String file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        ArrayList<Ticket> ticketList = (ArrayList<Ticket>) ois.readObject();
        ois.close();
        return ticketList;
    }

}