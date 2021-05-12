import java.util.ArrayList;
import java.util.Comparator;
import java.lang.Math;

public class Server{

    ArrayList<Job> queue; //keep track of the queue
    int departure; //keep track of next departure time
    int curTime; //keep track of the current time
    Job workingJob; //job currently being worked on in the server
    boolean idle; //stores whether the server is working or not
    int arr;
    String queueType;

    public Server(String queueSelection, ArrayList<Job> new_queue){
        System.out.println("server is set up");
        queueType = queueSelection;

        if(queueSelection.equals("FIFO")){
            //set up FIFO
            queue = new_queue;
        }

        else if(queueSelection.equals("Random")){
            //set up Random
            queue = new_queue;
        }

        else if(queueSelection.equals("Kickout")){
            queue = new_queue;
        }

        else if(queueSelection.equals("shortest")){
            new_queue.sort(Comparator.comparing(Job::getSize)); //sort the jobs in order of job size
            queue=new_queue;
        }

        curTime = 0;
        departure = 0;

        //First thing the server does is start working on the first job
        if(queueSelection.equals("FIFO")||queueSelection.equals("Random")||queueSelection.equals("shortest")) {
            removeJob();
            idle = false;
        }

        else{ //we just pick the shortest job to start for kickout
            for(int i = 0; i<queue.size(); i++) {
                boolean getout = false;

                if(i == queue.size()-1){ //the smallest job is at the end of the queue
                    workingJob = queue.remove(i);
                    idle = false;
                    departure = curTime + workingJob.getSize();
                    arr = workingJob.getArr();
                    break;
                }

                for(int j = i+1; j<queue.size(); j++){
                    if(queue.get(i).getSize()>queue.get(j).getSize()){
                        break;
                    }
                    if(j ==queue.size()-1 && queue.get(i).getSize()<queue.get(j).getSize()){
                        workingJob = queue.remove(i);
                        idle = false;
                        departure = curTime + workingJob.getSize();
                        arr = workingJob.getArr();
                        getout=true;
                    }
                }

                if(getout){
                    break;
                }
            }
        }
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

            else if (queueType.equals("Kickout")) {
                /* NOTE: each arrival is immediately after a departure, so the job that
                * is currently being worked on would actually have had no work done on it yet if
                * it is kicked out of the server at this point. So, we don't need to alter the job size
                * of the current working job if it is kicked out.*/
                if (newJob.getSize() < workingJob.getSize()) { //replace working job with new job
                    //System.out.println("kicking out job");
                    Job holder = workingJob;
                    workingJob = newJob;
                    idle = false;
                    departure = curTime + workingJob.getSize();
                    arr = workingJob.getArr();
                    addJob(holder); //put the older working job back in the queue
                }
                else {
                    queue.add(newJob);
                }
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
            }
        }
    }

    public void removeJob(){ //start work on the first job in the queue
        if(queueType.equals("Random")){
            //need to implement random selection of next job
            int r = (int)(this.queue.size() * Math.random());
            this.workingJob = (Job)this.queue.remove(r);
            this.departure = this.curTime + this.workingJob.getSize();
            this.arr = this.workingJob.getArr();
        }
        else{
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

    public ArrayList<Job> getQueue() {return queue;}
}