#  Hospital Management System (HMS)

A simple Java-based hospital management system built for CPS2231 coursework. The system supports basic hospital operations such as doctor login, patient consultation records, and billing.

> Developed by Yu Yiduo  
> Course: CPS2231 
> Instructor: Prof. Wu

---

##  Features

- **Doctor Registration & Login**  
  Doctors can create their own accounts and log in with ID and password.

- **Consultation Record**  
  Doctors can input patient information and record consultation notes.

- **Billing System**  
  Patients are able to pay medical bills based on the consultation.

- **Persistent Storage**  
  All records (doctors, patients, admin, system logs) are saved to local files using binary I/O.

---

##  System Construction

- **Main Components**:
    - `Doctor` (Admin)
    - `Patient`
    - `HMS` (Main system frame)
    - `MedicalRecord`

- **Storage Design**:
    - `./tmp/admin` — Admin data (binary)
    - `./tmp/doctors` — Doctor accounts (binary)
    - `./tmp/patient` — Patient records (binary)
    - `./tmp/system.txt` — System logs
    - `./desktop/records/patients.txt` — Output of patient info for viewing

- Uses `ObjectInputStream` / `ObjectOutputStream` for reading and writing serialized objects.

---

## Technologies Used

- Java
- File I/O (Binary serialization)
- Command-line interface




