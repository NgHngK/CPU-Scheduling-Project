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

# CPU Scheduling Algorithms – Simple Java Project

This program is a small Java console app that simulates 3 CPU scheduling algorithms:

- **FCFS** – First Come First Serve  
- **SJN** – Shortest Job Next (Shortest Job First, non-preemptive)  
- **RR** – Round Robin (uses a time quantum)

You enter a list of processes (PID, arrival time, burst time), and the program shows:

- Start time  
- Completion time  
- Waiting time  
- Turnaround time  
- Average waiting time  
- Average turnaround time  

for each algorithm.

---

## What each class does (core meaning)

### `Process`
- Stores basic data of a process:
  - Process ID (PID)
  - Arrival time
  - Burst time
- Just a simple data holder with getters.

---

### `ProcessStats`
- Stores the result for one process after scheduling:
  - Start time
  - Completion time
  - Waiting time
  - Turnaround time
- Used to easily print and calculate averages.

---

### `CPUScheduler` (abstract)
- A common parent class for all schedulers.
- Has:
  - `schedule(List<Process> processes)` → must be implemented by each algorithm.
  - Methods to calculate average waiting time and average turnaround time.
- Helps keep all schedulers in the same structure.

---

### `FCFSScheduler`
- Implements **First Come First Serve**.
- Runs processes in the order they arrive.
- Calculates `ProcessStats` for each process and returns a list of them.

---

### `SJNScheduler`
- Implements **Shortest Job Next** (Shortest Job First, non-preemptive).
- At each step, picks the process (that has arrived) with the smallest burst time.
- Calculates `ProcessStats` for each process and returns a list.

---

### `RRScheduler`
- Implements **Round Robin**.
- Uses a **time quantum** (for example 2 or 4 units).
- Gives each ready process up to `quantum` units of CPU time, then moves it to the back of the queue if it is not finished.
- Calculates `ProcessStats` for each process and returns a list.

---

### `Main`
- The starting point of the program.
- Steps:
  1. Ask user for number of processes.
  2. For each process, ask for PID, arrival time, and burst time.
  3. Ask for the time quantum (for Round Robin).
  4. Create and run:
     - `FCFSScheduler`
     - `SJNScheduler`
     - `RRScheduler`
  5. Print result tables and average times for each algorithm.

---
