package com.company.elevator;

import com.company.Direction;
import com.company.request.Request;
import com.company.request.RequestType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ElevatorManager {

    private int elevatorsAmount;
    private int floorsAmount;
    private Elevator[] elevators;

    public ElevatorManager() {
    }

    public void run() {

        Scanner in = new Scanner(System.in);

        System.out.print("Enter floors amount: ");
        floorsAmount = in.nextInt();

        System.out.print("Enter elevators amount: ");
        elevatorsAmount = in.nextInt();
        initElevators();

        boolean end = false;
        while (!end) {
            System.out.print("\n1. Elevators status\n2. Call elevator\n3. Do next step\n4. Elevator queue status\n5. Exit\nChoose option: ");
            int option = in.nextInt();

            switch (option) {
                case 1 -> elevatorStatus();
                case 2 -> callElevator();
                case 3 -> doStep();
                case 4 -> elevatorQueueStatus();
                default -> end = true;
            }
        }
    }

    private void elevatorStatus() {

        for(Elevator elevator : elevators) {
            System.out.println(elevator);
        }
    }

    private void callElevator() {

        Scanner in = new Scanner(System.in);

        System.out.print("\nEnter call floor(0 - " + floorsAmount + "): ");
        int floor = in.nextInt();

        System.out.print("Enter call direction(UP/DOWN): ");
        String direction = in.next();

        if (floor > floorsAmount)
            throw new InputMismatchException();

        if (direction.equalsIgnoreCase("UP"))
            callElevator(new Request(floor, Direction.UP, RequestType.OUTSIDE));
        if (direction.equalsIgnoreCase("DOWN"))
            callElevator(new Request(floor, Direction.DOWN, RequestType.OUTSIDE));
    }

    private void doStep() {

        for (Elevator elevator : elevators)
            elevator.doStep();
    }

    private void initElevators() {

        elevators = new Elevator[elevatorsAmount];
        for (int i = 0; i < elevatorsAmount; i++) {
            elevators[i] = new Elevator(i, floorsAmount);
        }
    }

    private void callElevator(Request request) {
        Arrays.stream(elevators)
                .forEach((elevator) -> elevator.updateMatchCoef(request));

        Arrays.stream(elevators)
                .max(Comparator.comparing(Elevator::getMatchCoef))
                .orElseThrow()
                .addToQueue(request);
    }

    private void elevatorQueueStatus() {

        Scanner in = new Scanner(System.in);

        System.out.print("\nEnter elevator ID: ");
        int id = in.nextInt();

        System.out.println(elevators[id].queueStatus());
    }
}
