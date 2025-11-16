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

This is a simple Java console program that simulates three CPU scheduling algorithms:

- **FCFS** – First Come First Serve  
- **SJN** – Shortest Job Next (also called Shortest Job First, non-preemptive)  
- **RR** – Round Robin (uses a time quantum)

The code is written using basic Object-Oriented Programming (OOP) ideas:
- We have classes for processes and schedulers.
- We use inheritance (a parent scheduler class and child classes for each algorithm).

---

## What each class does

### 1. `Process`

**What it represents:**

A single process in the system.

**What it stores:**

- `pid` – the name/ID of the process (for example: `P1`, `P2`, `P3`)
- `arrivalTime` – the time when the process arrives in the ready queue
- `burstTime` – how long (how many time units) the process needs the CPU

**Why it is written this way:**

- The fields are `private`.
- There are `public` getter methods to read the values.
- This is called *encapsulation*: the data is inside the class, and other classes use methods to access it.

---

### 2. `ProcessStats`

**What it represents:**

The result for one process after we run a scheduling algorithm.

**What it stores:**

- `startTime` – the time when the process first starts running on the CPU
- `completionTime` – the time when the process finishes
- `waitingTime` – how long the process waited in the ready queue
- `turnaroundTime` – total time from arrival to completion

**Why we use it:**

- It is easier to keep all these values together for each process.
- We can pass a list of `ProcessStats` around and print them in a table.

---

### 3. `CPUScheduler` (abstract class)

**What it represents:**

A general “CPU scheduler”.

You do not use this class directly.  
Other classes extend it and provide the real algorithm.

**What it contains:**

- An abstract method:
  - `schedule(List<Process> processes)`  
    This method must be implemented by each algorithm class.  
    It returns a `List<ProcessStats>` for all processes.
- Helper methods to compute averages:
  - `computeAverageWaitingTime(...)`
  - `computeAverageTurnaroundTime(...)`

**Why it is useful:**

- It gives a common structure for all schedulers.
- All scheduler classes share the same method name `schedule(...)`.
- This shows *abstraction* and *inheritance*: a general idea (CPUScheduler) and specific versions (FCFS, SJN, RR).

---

### 4. `FCFSScheduler`

**What it represents:**

The **First Come First Serve** scheduling algorithm.

**How it works:**

- Processes are sorted by arrival time.
- The process that arrives first is executed first.
- When one process finishes, the next one in order runs.

**What it does:**

- Extends `CPUScheduler`.
- For each process, it calculates:
  - start time
  - completion time
  - waiting time
  - turnaround time
- Returns a `List<ProcessStats>` with these values.

---

### 5. `SJNScheduler`

**What it represents:**

The **Shortest Job Next** algorithm (also called **Shortest Job First**, non-preemptive).

**How it works (simple idea):**

- At the current time, look at all processes that have already arrived.
- Among those, pick the process with the **smallest burst time**.
- Run that process until it finishes.
- Repeat until all processes are done.

**What it does:**

- Extends `CPUScheduler`.
- Chooses the shortest job among the processes that are ready.
- Calculates start time, completion time, waiting time, and turnaround time.
- Returns them as a `List<ProcessStats>`.

---

### 6. `RRScheduler`

**What it represents:**

The **Round Robin** scheduling algorithm.

**Key idea:**

- Each process gets a small time slice called a **time quantum** (for example 2 or 4 time units).
- Processes are placed in a queue.
- The first process runs for at most `quantum` time units.
  - If it is finished, it leaves the queue.
  - If it is not finished, it goes to the back of the queue.
- This repeats in a round, so every process gets CPU time fairly.

**What it does:**

- Extends `CPUScheduler`.
- Has a `quantum` value (given in the constructor).
- Simulates Round Robin using a simple ready queue.
- Calculates start time (first time it gets CPU), completion time, waiting time, and turnaround time for each process.
- Returns the results as a `List<ProcessStats>`.

---

### 7. `Main`

**What it represents:**

The main class with the `public static void main(String[] args)` method.

**What it does:**

1. Asks the user to input:
   - number of processes
   - for each process:
     - PID
     - arrival time
     - burst time
   - time quantum for Round Robin

2. Creates a list of `Process` objects from the user input.

3. Creates scheduler objects:
   - `FCFSScheduler`
   - `SJNScheduler`
   - `RRScheduler`

4. Calls `schedule(...)` for each scheduler and prints:
   - A table with:
     - ProcessID  
     - ArrivalTime  
     - BurstTime  
     - StartTime  
     - CompletionTime  
     - WaitingTime  
     - TurnaroundTime  
   - The average waiting time
   - The average turnaround time


