package com.company.elevator;

import com.company.Direction;
import com.company.request.Request;
import com.company.request.RequestType;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Elevator {

    private final int id;
    private Direction direction;
    private int currentFloor;
    private final int maxFloor;
    private double matchCoef;
    private PriorityQueue<Request> upQueue;
    private PriorityQueue<Request> downQueue;

    public Elevator(int id, int maxFloor) {
        this.id = id;
        this.maxFloor = maxFloor;
        direction = Direction.IDLE;
        currentFloor = 0;
        upQueue = new PriorityQueue<>(Comparator.comparingInt(Request::getTargetFloor));
        downQueue = new PriorityQueue<>((x,y) -> y.getTargetFloor() - x.getTargetFloor());
    }

    public void doStep() {

        if (upQueue.isEmpty() && downQueue.isEmpty())
            return;

        if (direction == Direction.UP)
            currentFloor++;
        else if (direction == Direction.DOWN)
            currentFloor--;

        checkQueue(upQueue);
        checkQueue(downQueue);

        if(direction == Direction.IDLE && !upQueue.isEmpty() && currentFloor != maxFloor)
            direction = Direction.UP;

        if(direction == Direction.IDLE && !downQueue.isEmpty() && currentFloor != 0)
            direction = Direction.DOWN;

        if (upQueue.isEmpty() && downQueue.isEmpty())
            direction = Direction.IDLE;

        if (direction == Direction.UP && upQueue.isEmpty())
            direction = Direction.DOWN;

        if (direction == Direction.DOWN && downQueue.isEmpty())
            direction = Direction.UP;
    }

    private void checkQueue(PriorityQueue<Request> queue) {

        while (!queue.isEmpty() && queue.peek().getTargetFloor() == currentFloor) {
            if (queue.peek().getType() == RequestType.OUTSIDE) {
                queue.poll();
                Request request = getInsideRequest();

                if (request.getTargetFloor() != currentFloor) {
                    if (request.getTargetFloor() > currentFloor)
                        upQueue.add(request);
                    else
                        downQueue.add(request);
                }

            } else {
                queue.poll();
            }
        }
    }

    private Request getInsideRequest() {
        Scanner in = new Scanner(System.in);

        System.out.print("\nEnter your request\nTarget floor(0 - " + maxFloor + "): ");
        int targetFloor = in.nextInt();

        if (targetFloor > maxFloor)
            throw new InputMismatchException();

        return new Request(targetFloor, Direction.IDLE, RequestType.INSIDE);
    }

    public void addToQueue(Request request) {

        if(request.getTargetFloor() > currentFloor)
            upQueue.add(request);
        else
            downQueue.add(request);
    }

    public void updateMatchCoef(Request request) {

        this.matchCoef = calculateMatchCoef(request);
    }

    private double calculateMatchCoef(Request request) {

        if(direction == Direction.IDLE)
            return 100 - (((double) (Math.abs(currentFloor-request.getTargetFloor()))/maxFloor) * 10);

        if (direction == Direction.UP && request.getTargetFloor() > currentFloor) {
            if(request.getDirection() == Direction.UP)
                return 100 - (((double) (Math.abs(currentFloor-request.getTargetFloor()))/maxFloor) * 10) - upQueue.size();
            return 100 - (((double) (Math.abs(currentFloor-request.getTargetFloor()))/maxFloor) * 10) - (upQueue.size() +  downQueue.size());
        }

        if (direction == Direction.DOWN && request.getTargetFloor() < currentFloor) {
            if(request.getDirection() == Direction.DOWN)
                return 100 - (((double) (Math.abs(currentFloor-request.getTargetFloor()))/maxFloor) * 10) - downQueue.size();
            return 100 - (((double) (Math.abs(currentFloor-request.getTargetFloor()))/maxFloor) * 10) - (upQueue.size() +  downQueue.size());
        }

        if (direction == Direction.UP && request.getTargetFloor() < currentFloor) {
            return 50 - ((downQueue.size()+upQueue.size()/(maxFloor * 1.5)) * 10);
        }

        if (direction == Direction.DOWN && request.getTargetFloor() > currentFloor) {
            return 50 - ((downQueue.size()+upQueue.size()/(maxFloor * 1.5)) * 10);
        }

        return 0;
    }

    public double getMatchCoef() {
        return matchCoef;
    }

    public String queueStatus() {

        return "ELEVATOR [" + id + "]\n" +
                "UP QUEUE: " + upQueue.toString().replace(", ", " -> ") +
                "\nDOWN QUEUE: " + downQueue.toString().replace(", ", " -> ");
    }

    @Override
    public String toString() {
        return "ELEVATOR [" + id + "] | FLOOR [" + currentFloor + "] | DIRECTION [" + direction + "]";
    }
}
