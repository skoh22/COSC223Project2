import java.util.ArrayList;

public class Server{

    ArrayList<Job> queue; //keep track of the queue
    int departure; //keep track of next departure time
    int curTime; //keep track of the current time
    Job workingJob; //job currently being worked on in the server
    boolean idle; //stores whether the server is working or not
    int arr;

    public Server(){
        queue = new ArrayList<Job>();
        curTime = 0;
        departure = 0;
        idle = true;
    }

    public void addJob(Job newJob){
        if (idle == true){ //if the server is idle, start working on the new job right away
            workingJob = newJob;
            idle = false;
            departure = curTime+workingJob.getSize();
            arr = workingJob.getArr();
        }

        else { //otherwise, add it to the end of the queue
            queue.add(newJob);
        }
    }

    public void removeJob(){ //start work on the first job in the queue
        Job workingJob = queue.remove(0);
        departure = curTime+workingJob.getSize();
        arr = workingJob.getArr();
    }

    public int getTime(){
        return curTime;
    }

    public void setTime(int time){
        curTime = time;
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