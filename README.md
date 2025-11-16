# OOP.20251-20

# OOP Capstone Project 
- **Course:** Object-Oriented Programming (OOP.20251)
- **Class id:** 162060 - IT3100E
- **Instructor:** Nguyen Thi Thu Trang
  
# Group Information

| Member Name              | Student ID  | Email                             | Role                 |
|--------------------------|-------------|-----------------------------------|----------------------|
| Hoang Trung Dung         | 20235488    | Dung.HT235488@sis.hust.edu.vn     | Team leader          |               
| Nguyen Tat Hung          | 20235500    | Hung.NT235500@sis.hust.edu.vn     | Member               |      
| Nguyen Ngoc Tuan Anh     | 202416658   | Anh.NNT2416658@sis.hust.edu.vn    | Member               |      

# CPU Scheduling Project – File Guide

This project is a Java console program that shows how three CPU scheduling algorithms work:
- FCFS (First Come First Serve)
- SJN (Shortest Job Next / Shortest Job First)
- RR (Round Robin)

Below is a short and easy explanation of what each file does.

---

## `Process.java`

This file defines the **Process** class.

- A `Process` object stores basic information about one process:
  - `pid` – the name or ID of the process (for example: `P1`, `P2`)
  - `arrivalTime` – when the process arrives in the ready queue
  - `burstTime` – how long the process needs the CPU to finish
- Other classes use this class to work with processes in a clear way.

---

## `ProcessStats.java`

This file defines the **ProcessStats** class.

- A `ProcessStats` object stores the **result** of scheduling for one process:
  - `startTime` – when the process first starts running on the CPU
  - `completionTime` – when the process finishes
  - `waitingTime` – how long the process waited before running
  - `turnaroundTime` – total time from arrival to finish
- This class makes it easy to keep all timing values together for each process.

---

## `CPUScheduler.java`

This file defines the **CPUScheduler** abstract class.

- It is a **base class** for all scheduling algorithms.
- It has:
  - An abstract method `schedule(List<Process> processes)`  
    → Each algorithm (FCFS, SJN, RR) implements this method in its own way.
  - Methods to calculate:
    - average waiting time
    - average turnaround time
- Other scheduler classes extend this class so they share the same structure.

---

## `FCFSScheduler.java`

This file defines the **FCFSScheduler** class.

- It extends `CPUScheduler`.
- It uses the **First Come First Serve** rule:
  - Processes run in the order of their arrival time.
  - The first process that comes is the first one to get the CPU.
- It creates `ProcessStats` for each process and returns them as a list.

---

## `SJNScheduler.java`

This file defines the **SJNScheduler** class.

- It extends `CPUScheduler`.
- It uses the **Shortest Job Next** rule (also called Shortest Job First, non-preemptive):
  - At a given time, among the processes that have already arrived,
  - It chooses the one with the **smallest burst time** and runs it until it finishes.
- It creates `ProcessStats` for each process and returns them as a list.

---

## `RRScheduler.java`

This file defines the **RRScheduler** class.

- It extends `CPUScheduler`.
- It uses the **Round Robin** rule:
  - It has a `quantum` (time slice), for example 2 or 4 units.
  - Each ready process gets up to `quantum` units of CPU time.
  - If the process is not done, it goes to the back of the queue and waits for the next turn.
- It creates `ProcessStats` for each process and returns them as a list.

---

## `Main.java`

This file contains the `main` method and starts the program.

- It asks the user to enter:
  - How many processes there are.
  - For each process: PID, arrival time, and burst time.
  - The time quantum for Round Robin.
- It creates a list of `Process` objects from the input.
- It creates objects of:
  - `FCFSScheduler`
  - `SJNScheduler`
  - `RRScheduler`
- It runs each scheduler, then prints:
  - A table for each algorithm (with start time, completion time, waiting time, turnaround time).
  - The average waiting time and average turnaround time.

---
