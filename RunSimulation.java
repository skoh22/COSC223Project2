import java.util.Random;

public class RunSimulation {

    static int arrTime;
    static int size;
    static int numJobs;
    static double meanNum;
    static double meanResp;
    static double sizeProb = 0.5;
    static double arrProb = 0.45;

    public static void main(String[] args) {
        numJobs = 100000;
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
        System.out.println("Current queue size: " + server.queueSize());
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
        if (numJobs <= 98000){ // start to calculate the mean number of jobs in the system
            if(!server.getState()){ //if the server is not idle
                meanNum = meanNum + server.queueSize()+1; //add extra one to account for job in service
            }
        }

        Job newJob = new Job(size , arrTime); //create a new job
        server.setTime(arrTime); //set the current time to the job's arrival time
        server.addJob(newJob); //add the job to the queue

        arrTime = server.getTime() + generateRandom(arrProb); //generate information for next job
        size = generateRandom(sizeProb);

        numJobs -= 1;
    }

    public static void processDeparture(Server server){
        if (numJobs <= 98000){ // start to calculate the mean response time
            meanResp = meanResp + (server.getDeparture()-server.getArr());
        }

        server.setTime(server.getDeparture()); //set current time to departure time
        if (server.queueSize() == 0){ //if the queue is empty
            server.setState(); //set the server to idle
        }
        else {server.removeJob();} //start working on the first job in the queue
    }
}