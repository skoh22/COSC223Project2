import java.util.ArrayList;
import java.util.Comparator;

public class Server{

    ArrayList<Job> queue; //keep track of the queue
    int departure; //keep track of next departure time
    int curTime; //keep track of the current time
    Job workingJob; //job currently being worked on in the server
    boolean idle; //stores whether the server is working or not
    int arr;
    String queueType;

    public Server(String queueSelection, ArrayList<Job> new_queue){
        queueType = queueSelection;

        if(queueSelection.equals("FIFO")){
            //set up FIFO
            queue = new_queue;
        }

        else if(queueSelection.equals("Random")){
            //set up Random
            queue = new_queue;
        }

        else if(queueSelection.equals("shortest")){
            new_queue.sort(Comparator.comparing(Job::getSize)); //sort the jobs in order of job size
            queue=new_queue;
        }

        curTime = 0;
        departure = 0;

        //First thing the server does is start working on the first job
        removeJob();
        idle = false;
    }

    public void addJob(Job newJob){
        if (idle == true){ //if the server is idle, start working on the new job right away
            workingJob = newJob;
            idle = false;
            departure = curTime+workingJob.getSize();
            arr = workingJob.getArr();
        }

        else { //otherwise, add it the queue
            if(queueType.equals("FIFO") || queueType.equals("Random")) {
                //if the queue is FIFO or random, just add the job to the end of the queue
                queue.add(newJob);
            }
            else{
                queue.add(newJob);
                int i = queue.indexOf(newJob);
                int j = queue.indexOf(newJob) - 1;
                while(newJob.getSize()<queue.get(j).getSize()){
                    Job holder = queue.get(j);
                    queue.set(j, newJob);
                    queue.set(i,holder);
                    j--;
                    i--;

                    if(i == 0){
                        break;
                    }
                }
                /*for(int k = 0; k<queue.size(); k++){
                    System.out.print(queue.get(k).getSize()+" ");
                }
                System.out.println();*/
            }
        }
    }

    public void removeJob(){ //start work on the first job in the queue
        if(queueType.equals("Random")){
            //need to implement random selection of next job
        }
        else {
            workingJob = queue.remove(0);
            departure = curTime + workingJob.getSize();
            arr = workingJob.getArr();
        }
    }


    public int getTime(){
        return curTime;
    }

    public void setTime(int newTime){
        curTime = newTime;
    }

    public int getDeparture(){
        return departure;
    }

    public int queueSize(){
        return queue.size();
    }

    public void setState(){
        idle = true;
    }

    public boolean getState(){
        return idle;
    }

    public Job getWorkingJob(){
        return workingJob;
    }

    public int getArr() {return arr;}
}