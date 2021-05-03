public class Job{

    int jobSize;
    int arrival;

    public Job(int size, int arrTime){
        jobSize = size;
        arrival = arrTime;
    }

    public int getSize(){
        return jobSize;
    }

    public int getArr() {
        return arrival;
    }
}