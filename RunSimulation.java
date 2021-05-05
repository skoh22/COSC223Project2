import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class RunSimulation {

    static int arrTime;
    static int size;
    static int numJobs;
    //static double meanNum;
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

        /*numJobs = 100000;
        Server server = new Server();

        //generate information for first job
        size = generateRandom(sizeProb);
        arrTime=0;
        processArrival(server);

        while(numJobs>0){
            if(server.getState()){ //the server is currently idle
                processArrival(server); //process a new arrival
            }

            else if (arrTime < server.getDeparture()) { //next arrival is before next departure
                processArrival(server); //process a new arrival
            }

            else { //next arrival is after or equal to next departure
                if(arrTime == server.getDeparture()){
                    processDeparture(server);
                    processArrival(server);
                }

                else{processDeparture(server); }
            }
        }

        System.out.println("The mean number of jobs in the system was: " + meanNum/(98000));
        System.out.println("The mean response time of the system was: " + meanResp/(98000-server.queueSize()));
        System.out.println("Current queue size: " + server.queueSize());*/
    }

    public static void runSimulation1() {
        processedJobs = 0;
        sizeProb = 0.25;
        arrProb = 0.1;

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
        //first thing to do is add a new job to the queue since the server started working on a new job already
        arrTime = generateRandom(arrProb);
        processArrival(server);

        while (server.getTime() < maxTime) {
            //System.out.println("Queue size is: " + server.queueSize());
            //System.out.println("arrTime is: " + arrTime);
            if (arrTime < server.getDeparture()) { //next arrival is before next departure
                processArrival(server); //process a new arrival
            }
            else { //this will keep the queue size constant
                processDeparture(server);
                processArrival(server);
            }

        }

        System.out.println("The server processed " + processedJobs + " jobs");

        System.out.println("The longest wait time was: " + longestWait); //NOTE: this is the longest wait time only for jobs that were processed

        System.out.println("The mean response time was: " + meanResp/processedJobs);

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

        arrProb = sizeProb/2; //NOTE: THIS IS JUST RANDOM LOL

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
        //first thing to do is add a new job to the queue since the server started working on a new job already
        arrTime = generateRandom(arrProb);
        processArrival(server);

        while (server.getTime() < 1000000) {
            //System.out.println("Queue size is: " + server.queueSize());
            //System.out.println("arrTime is: " + arrTime);
            if (arrTime < server.getDeparture()) { //next arrival is before next departure
                processArrival(server); //process a new arrival
            }
            else { //this will keep the queue size constant
                processDeparture(server);
                processArrival(server);
            }

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
        Job newJob = new Job(size , arrTime); //create a new job
        server.setTime(arrTime); //set the current time to the job's arrival time
        server.addJob(newJob); //add the job to the queue

        arrTime = server.getTime() + generateRandom(arrProb); //generate information for next job
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