import java.util.Scanner;
import java.util.ArrayList;

public class RunSimulation {

    static int arrTime;
    static int size;
    static int numJobs;
    static double meanResp;
    static double sizeProb;
    static double arrProb;
    static int processedJobs;
    static int longestWait;

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        System.out.println("Which experiment would you like to run? Enter 1 or 2:");
        if(kb.next().equals("1")){
            runSimulation1();
        }
        else{
            runSimulation2();}
    }

    public static void runSimulation1() {
        processedJobs = 0;
        sizeProb = 0.25;
        arrProb = 0.12;

        Scanner kb = new Scanner(System.in);
        System.out.println("What kind of queue would you like to test?");
        System.out.println("(1) FIFO");
        System.out.println("(2) Random");
        System.out.println("(3) Shortest first");

        String ans = kb.next();

        System.out.println("Type your desired max time:");
        int maxTime = kb.nextInt();

        ArrayList queue = new ArrayList<Job>();
        Server server;

        //populate the queue with 25 random job entries
        for (int i = 0; i < 26; i++) {
            Job newJob = new Job(generateRandom(sizeProb), 0); //size and then arrival time
            queue.add(newJob);

        }

        if (ans.equals("1")) {
            //set up for FIFO
            server = new Server("FIFO", queue);
        }
        else if (ans.equals("2")) {
            //set up for random
            server = new Server("Random", queue);
        }
        else {
            server = new Server("shortest", queue);
            // set up for shortest first
        }

        //now the server is set up!

        while (server.getTime() < maxTime){
            processDeparture(server);
            processArrival(server);
        }

        System.out.println("The server processed " + processedJobs + " jobs");

        System.out.println("The longest wait time was: " + longestWait); //NOTE: this is the longest wait time only for jobs that were processed

        System.out.println("The mean response time was: " + meanResp/processedJobs);

        System.out.println("The current queue looks like: ");

    }

    public static void runSimulation2(){
        processedJobs = 0;

        Scanner kb = new Scanner(System.in);
        System.out.println("What kind of queue would you like to test?");
        System.out.println("(1) FIFO");
        System.out.println("(2) Random");
        System.out.println("(3) Shortest first");

        String ans = kb.next();

        System.out.println("Type your desired p value:");
        sizeProb = kb.nextDouble();

        //arrProb = sizeProb/2; //NOTE: THIS IS JUST RANDOM LOL

        ArrayList queue = new ArrayList<Job>();
        Server server;

        //populate the queue with 25 random job entries
        for (int i = 0; i < 26; i++) {
            Job newJob = new Job(generateRandom(sizeProb), 0); //size and then arrival time
            queue.add(newJob);

        }

        if (ans.equals("1")) {
            //set up for FIFO
            server = new Server("FIFO", queue);
        }
        else if (ans.equals("2")) {
            //set up for random
            server = new Server("Random", queue);
        }
        else {
            server = new Server("shortest", queue);
            // set up for shortest first
        }

        //now the server is set up!

        while (server.getTime() < 1000000) {
            processDeparture(server);
            processArrival(server);
        }

        System.out.println("The server processed " + processedJobs + " jobs");

        System.out.println("The longest wait time was: " + longestWait); //NOTE: this is the longest wait time only for jobs that were processed

        System.out.println("The mean response time was: " + meanResp/processedJobs);
    }

    public static int generateRandom(double p) {
            double value = Math.random(); //generate random number between 1 and max value of an integer

            if (value < p) {
                return 1;
            }

            double CDF = p;
            double count = 1;

            while (CDF < value) {
                CDF = CDF + p * Math.pow((1 - p), count);
                count += 1;
            }
            return (int) count;
    }

    public static void processArrival(Server server){
        Job newJob = new Job(size, server.getTime());
        server.addJob(newJob); //add the job to the queue

        //arrTime = server.getTime() + generateRandom(arrProb); //generate information for next job
        size = generateRandom(sizeProb);

        numJobs -= 1;
    }

    public static void processDeparture(Server server){
        processedJobs+=1;

        if(server.getDeparture()-server.getArr()> longestWait){
            longestWait = server.getDeparture()-server.getArr();
        }

        meanResp = meanResp+ (server.getDeparture()-server.getArr());

        server.setTime(server.getDeparture()); //set current time to departure time
        if (server.queueSize() == 0){ //if the queue is empty
            server.setState(); //set the server to idle
        }
        else {server.removeJob();} //start working on the first job in the queue
    }
}