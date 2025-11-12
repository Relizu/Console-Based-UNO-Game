import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedReader inkey;
    private static String ip;
    private static String name;
    private int currentplayer = -1;
    private List<Player> Players = new ArrayList<>();
    private card grounCard;
    private int iterator;
    private List<List<String>> ShowPlayersList ;

    public Client(String ip ,int port){
        try{
            System.out.println("Waiting...");
            Socket s = new Socket(ip,port); 
            System.out.println("Found a server");
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            inkey = new BufferedReader(new InputStreamReader(System.in));

            out.writeUTF(name);
            out.flush();
            new Thread(()->{
                while (true) {
                    try{
                        String m ="";
                        m = in.readUTF();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        if(m.equals("max_players")){
                            System.out.println("Max players! Try again later.");
                            break;
                        }
                        if(m.equals("updateList")){
                            try{
                                Players = (List<Player>) in.readObject();
                                grounCard = (card) in.readObject();
                                iterator = in.readInt();
                                if(currentplayer== -1){currentplayer =Players.size()-1;}
                            }catch(ClassNotFoundException e){
                                System.out.println(e);
                            }
                        }
                        if(m.equals("pickColor")){
                            System.out.println("Pick a color");
                            System.out.println("1 > Red");
                            System.out.println("2 > Blue");
                            System.out.println("3 > green");
                            System.out.println("4 > yellow");
                            String serverinput = inkey.readLine();
                            out.writeUTF(serverinput);
                            out.flush();
                            
                        }
                        if(m.equals("won")){
                            String name;
                            name = in.readUTF();
                            System.out.println(name+" won!");
                            break;
                            
                        }
                        if(grounCard != null){
                            ShowPlayersList = new ArrayList<>();
                            for(Player p: Players){
                                if(Players.indexOf(p) ==iterator){
                                    ShowPlayersList.add(Arrays.asList(p.ShowPlayer(1).split("\n")));
                                }else{
                                    ShowPlayersList.add(Arrays.asList(p.ShowPlayer(0).split("\n")));
                                }
                            }
                            for(int x=0; x<ShowPlayersList.get(0).size();x++){
                                for(int y =0; y<ShowPlayersList.size();y++){
                                    System.out.print(ShowPlayersList.get(y).get(x));
                                }
                                System.out.print("\n");
                            }

                            System.out.println(grounCard);
                            Players.get(currentplayer).ShowCards();
                        }
                        
                        
                    }catch(IOException e){
                        System.out.println(e);
                    }
                }
            }).start();


            new Thread(()->{
                while (true) {
                    try{
                        String serverinput = inkey.readLine();
                        out.writeUTF(serverinput);
                        out.flush();
                    }catch(IOException e){
                        System.out.println(e);
                    }
                }
            }).start();
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Name > ");
        name = sc.nextLine();
        System.out.print("Host ip > ");
        ip = sc.nextLine();
        Client s = new Client(ip,5000);
    }
}
