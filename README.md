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

# CPU Scheduling Algorithms (FCFS, SJN, RR)

This project is a Java console program that simulates three classic CPU scheduling algorithms:

- **FCFS** – First-Come-First-Serve  
- **SJN** – Shortest Job Next (non-preemptive SJF)  
- **RR** – Round Robin (with time quantum)

It uses basic Object-Oriented Programming (OOP) concepts such as **encapsulation, inheritance, polymorphism, and abstraction**.

---

## Class Overview

### 1. `Process`
Represents a single process in the system.

**Purpose:**

- Stores basic information of a process:
  - `pid` – process ID (e.g. `P1`, `P2`)
  - `arrivalTime` – when the process arrives in the ready queue
  - `burstTime` – how long the process needs the CPU
- Uses **encapsulation**:
  - Fields are declared `private`
  - Values are accessed through public getter methods

---

### 2. `ProcessStats`
Represents the result of scheduling for one process.

**Purpose:**

- Stores all important timing results after scheduling:
  - `startTime` – time when the process first starts running on the CPU
  - `completionTime` – time when the process finishes
  - `waitingTime` – total time spent waiting in the ready queue
  - `turnaroundTime` – total time from arrival to completion
- Makes it easy to pass around and print per-process results in a table.

---

### 3. `CPUScheduler` (abstract)
Abstract base class for all scheduling algorithms.

**Purpose:**

- Defines the **common interface** for all CPU schedulers:
  - Abstract method:  
    - `schedule(List<Process> processes)` → returns a list of `ProcessStats`
- Provides shared helper methods:
  - `computeAverageWaitingTime(...)`
  - `computeAverageTurnaroundTime(...)`
- Shows **abstraction and inheritance**:
  - You cannot create `CPUScheduler` directly.
  - Concrete algorithms extend this class and implement their own scheduling logic.

---

### 4. `FCFSScheduler`
Implements the **First-Come-First-Serve (FCFS)** scheduling algorithm.

**Purpose:**

- Extends `CPUScheduler`.
- Runs processes in the order of their **arrival time**.
- For each process, calculates:
  - Start time
  - Completion time
  - Waiting time
  - Turnaround time
- Simple “queue-like” behavior: the process that arrives first runs first.

---

### 5. `SJNScheduler`
Implements the **Shortest Job Next (SJN / non-preemptive SJF)** algorithm.

**Purpose:**

- Extends `CPUScheduler`.
- At any moment, chooses the process (from those that have already arrived) with the **smallest burst time**.
- Non-preemptive:
  - Once a process starts running, it runs until it finishes.
- Produces `ProcessStats` for each process, similar to FCFS.

---

### 6. `RRScheduler`
Implements the **Round Robin (RR)** scheduling algorithm.

**Purpose:**

- Extends `CPUScheduler`.
- Uses a **time quantum** (fixed time slice).
- Cycles through processes:
  - Each ready process gets up to `quantum` units of CPU time.
  - If a process is not finished after its time slice, it goes back to the end of the ready q

                                      


