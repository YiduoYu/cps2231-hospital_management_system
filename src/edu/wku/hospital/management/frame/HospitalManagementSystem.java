package edu.wku.hospital.management.frame;

import java.io.*;
import java.util.*;
import edu.wku.hospital.management.exception.NotFoundException;
import edu.wku.hospital.management.exception.TransactionFailedException;
import edu.wku.hospital.management.model.Doctor;
import edu.wku.hospital.management.model.Hospital;
import edu.wku.hospital.management.model.Patient;
import edu.wku.hospital.management.service.IOManager;

public class HospitalManagementSystem {
    private Scanner scanner;
    private Map<String, Doctor> doctors;
    private IOManager ioManager = new IOManager();
    private int stage = 0;
    private Doctor admin;

    public HospitalManagementSystem() {
        try {
            admin = (Doctor) IOManager.loadSerializable("admin", "./tmp/admin");
            doctors = (HashMap<String, Doctor>) IOManager.loadSerializable("doctors", "./tmp/doctors");
            File aFile = new File("./tmp/system.txt");
            Scanner fScanner = new Scanner(aFile); 
            int lastInt = 0;
            if (aFile.exists()){
                while (fScanner.hasNext()) {
                    if (fScanner.hasNextInt()) {
                        lastInt = fScanner.nextInt();
                    } else {
                        fScanner.next();
                    }
                }
                fScanner.close();
            }
            stage = lastInt;
        } catch (NotFoundException e) {
            doctors = new HashMap<String, Doctor>();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        scanner = new Scanner(System.in);
    }

    public void start() {

        if (stage==0){
            System.out.println("Welcome to HMS, if you want to register your admin info," +
                "Please input \"register\"  \n (otherwise input any key word to resume) ");
        if (scanner.nextLine().equals("register")) {

            System.out.println("Enter your userID");
            String userID = scanner.nextLine();

            //load doctors from file to check if the userID has been registered
            try {
                doctors = (HashMap<String, Doctor>) IOManager.loadSerializable("doctors", "./tmp/doctors");
            } catch (NotFoundException e) {
                //if the file is not found, create a new hashmap
                doctors = new HashMap<>();
                System.out.println(e.getMessage());
            }
            //check if the userID has been registered
            if (!(doctors==null)) {
                for(Doctor x: doctors.values()){
                    if (x.getUserID().equals(userID)){
                        //if the userID has been registered, print a message and restart the system
                        System.out.println("your userID has been registered, please register again");
                        start();
                        return;
                    }

                }
            }

            //if the userID has not been registered, continue to register
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            System.out.println("Enter your department:");
            String department = scanner.nextLine();
            System.out.println("Enter your phone number:");
            String phone = scanner.nextLine();
            System.out.println("Enter your password:");
            String password = scanner.nextLine();

            Doctor doctor = new Doctor(name, department, phone, password, userID);
            doctors.put(doctor.getUserID(), doctor);
        }
        //save the doctor to the file
        this.stage = 1;
        save((Serializable) doctors, "./tmp/doctors","1");
        }


        //if the stage is 1, the system is in the login stage
        if(stage==1){
        
        System.out.println("Log in: please enter your userID");
        String userID = scanner.nextLine();
        Doctor doctor = doctors.get(userID);

        while (doctor==null){
            System.out.println("Please enter a valid id or enter quit to quit");
            userID = scanner.nextLine();
            doctor = doctors.get(userID);
            if (userID.equals("quit"))
                return;
        }

        System.out.println("Hi, doctor " + doctors.get(userID).getName());
        save(doctor, "./tmp/admin",null);
        admin = doctor;

        System.out.println("Please enter your password");
        String password = scanner.nextLine();
            while(!doctor.checkPassword(password)){
                System.out.println("Your password is incorrect, please enter again, or enter quit to quit");
                password = scanner.nextLine();
                if (password.equals("quit"))
                    return;
            }

            System.out.println("Welcome to HMS !!!");
            //save the stage to the file
            this.stage = 2;
            save(null, "./tmp/doctors","2");
        }

        if (stage == 2) {
            if (!(resume("admin", "./tmp/admin") == null)) {
                admin = (Doctor) resume("admin", "./tmp/admin");
            }
            System.out.println("Welcome back, doctor " + admin.getName());
            System.out.println("enter search(s) to search a patient\n " +
                    "enter rg to register new patient \n enter quit(q) to quit");
            String command = scanner.nextLine();

            if (command.equals("s")) {
                ArrayList<Patient> patients = admin.getPatients();
                for (Patient x : patients) {
                    System.out.print(x.getName() + "\t");
                    System.out.println(x.getId());
                }
                System.out.println("Please enter the patient's id by given name above ");
                String id = scanner.nextLine();
                boolean found = false;

                for (Patient x : patients) {
                    if (x.getId().equals(id)) {
                        System.out.println("Searched");
                        System.out.println("Patient's name: " + x.getName());
                        System.out.println("Patient's age: " + x.getAge());
                        System.out.println("Patient's address: " + x.getAddress());
                        System.out.println("Patient's phone: " + x.getPhone());
                        System.out.println("Patient's balance " + x.getBalance());
                        System.out.println("Patient's symptoms " + x.getRecords("symptoms"));
                        System.out.println("Patient's diagnosis " + x.getRecords("diagnosis"));
                        System.out.println("Patient's medicine " + x.getRecords("medicine"));

                        this.stage = 1;
                        save(admin, "./tmp/admin", "1");
                        found = true;
                        break; // 找到后退出循环
                    }
                }

                if (!found) {
                    System.out.println("Patient not found");
                    this.stage = 2;
                    save(admin, "./tmp/admin", "2");
                    start();
                }
            } else if (command.equals("rg")) {
                Patient patient = new Patient("test", "null", "null", 0, Patient.Gender.FE);
                if (!(resume("admin", "./tmp/admin") == null)) {
                    admin = (Doctor) resume("admin", "./tmp/admin");
                }
                System.out.println("Please enter the patient's name.");
                String name = scanner.nextLine();

                try {
                    doctors = (HashMap) IOManager.loadSerializable("doctors", "./tmp/doctors");
                } catch (NotFoundException e) {
                    System.out.println(e.getMessage());
                }

                ArrayList<Patient> patientList = admin.getPatients();
                boolean alreadyRegistered = false;
                for (Patient p : patientList) {
                    if (p.getName().equals(name)) {
                        System.out.println("The patient is already registered with ID:");
                        System.out.println(p.getId());
                        System.out.println("Make sure that the patient is not registered again, enter back to go back, quit to quit，" +
                                "any input to continue");
                        String quit = scanner.nextLine();
                        if (quit.equals("back")) {
                            start();
                            return;
                        }
                        if (quit.equals("quit")) {
                            return;
                        }
                        System.out.println("OK, continue to register the patient.");
                        alreadyRegistered = true;
                        break;
                    }
                }

                if (!alreadyRegistered) {
                    System.out.println("Please enter the patient's address.");
                    String address = scanner.nextLine();
                    System.out.println("Please enter the patient's phone.");
                    String phone = scanner.nextLine();
                    System.out.println("Please enter the patient's age.");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.println("Please enter the patient's gender f or m.");
                    String gender = scanner.nextLine();
                    if (gender.equals("f")) {
                        patient = new Patient(name, address, phone, age, Patient.Gender.FE);
                    } else {
                        patient = new Patient(name, address, phone, age, Patient.Gender.MA);
                    }
                    patientList.add(patient);
                    admin.setPatients(patientList);
                    this.stage = 3;
                    save(patient, "./tmp/patient", "3");
                    save(admin, "./tmp/admin", "3");
                    doctors.put(admin.getUserID(), admin);
                    save((Serializable) doctors, "./tmp/doctors", "3");
                }
            }
        }


        if (stage==3) {
            if (!(resume("admin", "./tmp/admin") == null)) {
                admin = (Doctor) resume("admin", "./tmp/admin");
            }
            Patient patient = (Patient) resume("patient", "./tmp/patient");
            System.out.println("Patient's name: " + patient.getName());
            System.out.println("Patient's address: " + patient.getAddress());
            System.out.println("Patient's phone: " + patient.getPhone());
            System.out.println("Patient's age: " + patient.getAge());
            System.out.println("Checking the patient");
            System.out.println("Please enter the patient's symptoms");
            String symptoms = scanner.nextLine();
            patient.addRecords("symptoms",symptoms);

            System.out.println("Please enter the patient's diagnosis");
            String diagnosis = scanner.nextLine();
//          System.out.println("Please enter the patient's treatment");
//          String treatment = scanner.nextLine();
            patient.addRecords("diagnosis",diagnosis);
            System.out.println("Please enter the patient's medicine");
            String medicine = scanner.nextLine();
            patient.addRecords("medicine",medicine);

            ArrayList<Patient> patientList  = admin.getPatients();
            ListIterator<Patient> iterator = patientList.listIterator();
            while (iterator.hasNext()) {
                Patient currentPatient = iterator.next();
                if (currentPatient.getId().equals(patient.getId())) {
                    iterator.set(patient);
                }
            }
            admin.setPatients(patientList);
            save(admin,"./tmp/admin","4");
            doctors.put(admin.getUserID(), admin);
            save((Serializable) doctors, "./tmp/doctors", "3");
            this.stage = 4;
            save(patient, "./tmp/patient", "4");
            System.out.println("The record will be saved to your desktop's hms_records folder or enter change" +
            "to change the directory, or enter permanent to permanently change the default directory ");
            String command = scanner.nextLine();

            if (command.equals("change")) {
                File directory = IOManager.chooseDirectory();
                if(directory==null){
                    System.out.println("You have canceled the operation, the record will be saved to your desktop's hms_records folder");
                }else{
                    directory = new File(directory, "records");
                    if (!directory.exists() || !directory.isDirectory()) {
                        directory.mkdir();
                        System.out.println("The 'records' subfolder is located at." + directory.getAbsolutePath());
                    }

                    File aFile = new File(directory, patient.getName() + patient.getId()+ ".txt");
                    
                    try {
                        FileWriter fileWriter = new FileWriter(aFile);
                        fileWriter.write("Patient's name: " + patient.getName() + "\n");
                        fileWriter.write("Patient's address: " + patient.getAddress() + "\n");
                        fileWriter.write("Patient's phone: " + patient.getPhone() + "\n");
                        fileWriter.write("Patient's age: " + patient.getAge() + "\n");
                        fileWriter.write("Patient's symptoms: " + symptoms + "\n");
                        fileWriter.write("Patient's diagnosis: " + diagnosis + "\n");
//                        fileWriter.write("Patient's treatment: " + treatment + "\n");
                        fileWriter.write("Patient's medicine: " + medicine + "\n");
                        fileWriter.write("Doctor's id: " + admin.getUserID() + "\n");
                        fileWriter.write("Date: " + new Date() + "\n");
                        fileWriter.close();
                        System.out.println("The record has been saved to " + aFile.getAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
            IOManager ioManager =  (IOManager)resume("ioManager", "./tmp/ioManager");
            if (ioManager==null){
                ioManager = this.ioManager;
            }
            
            File directory = ioManager.getCurrentDirectory();
            directory = new File(directory, "records");
                    if (!directory.exists() || !directory.isDirectory()) {
                        directory.mkdir();
                        System.out.println("The 'records' subfolder is located at." + directory.getAbsolutePath());
                    }
                    
            File aFile  = new File(directory, patient.getName() + patient.getId()+ ".txt");
                    
            try (FileWriter fileWriter = new FileWriter(aFile)) {
                fileWriter.write("Patient's name: " + patient.getName() + "\n");
                fileWriter.write("Patient's address: " + patient.getAddress() + "\n");
                fileWriter.write("Patient's phone: " + patient.getPhone() + "\n");
                fileWriter.write("Patient's age: " + patient.getAge() + "\n");
                fileWriter.write("Patient's symptoms: " + symptoms + "\n");
                fileWriter.write("Patient's diagnosis: " + diagnosis + "\n");
//                fileWriter.write("Patient's treatment: " + treatment + "\n");
                fileWriter.write("Patient's medicine: " + medicine + "\n");
                fileWriter.write("Doctor's id: " + admin.getUserID() + "\n");
                fileWriter.write("Date: " + new Date() + "\n");
                fileWriter.close();
                System.out.println("The record has been saved to " + aFile.getAbsolutePath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (stage==4) {
            if (!(resume("admin", "./tmp/admin") == null)) {
                admin = (Doctor) resume("admin", "./tmp/admin");
            }
            Hospital hospital = new Hospital();
            Patient patient = new Patient("null","null","null",0, Patient.Gender.FE);
            try {
                patient = (Patient) IOManager.loadSerializable("patient","./tmp/patient");
            } catch (NotFoundException e) {
                System.out.println(e.toString());
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the bill.");
            double amount = scanner.nextDouble();
            try {
                hospital.charge(patient,amount);
                System.out.println("patient's balance: " + patient.getBalance());
            } catch (TransactionFailedException e) {
                System.out.println("Patient's balance is not enough");
            }
            this.stage = 1;
            ArrayList<Patient> patientList  = admin.getPatients();
            ListIterator<Patient> iterator = patientList.listIterator();
            while (iterator.hasNext()) {
                Patient currentPatient = iterator.next();
                if (currentPatient.getId().equals(patient.getId())) {
                    iterator.set(patient);
                }
            }
            admin.setPatients(patientList);
            save(admin,"./tmp/admin","1");
            save(patient, "./tmp/patient", "1");
            doctors.put(admin.getUserID(), admin);
            save((Serializable) doctors, "./tmp/doctors", "3");
            start();
            return;
        }
    }


     /*   do {
            System.out.println(stateMachine.getCurrentState().name());
            System.out.println("Please enter a command: \n" +
                    " initial (i) go to the initial page \n" +
                    " register (rg) register a patient  \n" +
                    " check (ck) to check the patient \n" +
                    " bill (bl) to charge the patient\n" +
                    " record (rc) for case report   \n" +
                    " quit (q) to quit the system  \n" +
                    " expire (logout) to log out the system, need you to enter the passwords"
            );

            command = stateMachine.getCommand(scanner.nextLine());
            Serializable o = command.execute();

            stateMachine.manageState(command, o);
        } while (!command.equals(stateMachine.getCommand("quit")));
    }*/

    public Serializable resume(String resource, String file){
        Serializable o = null;
        try {
            o = IOManager.loadSerializable(resource, file);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
            return o;
    }

    public void save(Serializable o , String directory,String x){
        if (!(o==null)) {
            IOManager.saveSerializable(o,directory);
        }
        File aFile = new File("./tmp/system.txt");
        if (!aFile.exists()){
            try {
                aFile.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (x!=null) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(aFile);
                fileWriter.write(x);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new HospitalManagementSystem().start();
    }
}