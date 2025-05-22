package HospitalManagementsystem;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Class.forName;

public class Hospitalmangsystem {
    private static final String url = "jdbc:mysql://localhost:3360/hospital";
    private static final String username = "root";
    private static final String password = "kiran";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        // Add patient
                        patient.addPatient();
                        System.out.println();

                    case 2:
                        //view patient
                        patient.viewpatients();
                        System.out.println();

                    case 3:
                        //view Doctors
                        doctor.viewDoctors();
                        System.out.println();

                    case 4:
                        //Book Appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();

                    case 5:
                        return;
                    default:
                        System.out.println("Enter Your choice !!");
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id : ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date(YYYY-MM-DD : ");
        String appointmentDate = scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailablility(doctorId, appointmentDate ,connection)) {
                String appointmentquery = "Insert Into appointments(patient_id, doctor_id, appointment_date) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentquery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowAffected = preparedStatement.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Appointment Booked");

                    } else {
                        System.out.println("Failed to Book Appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();

                }

            } else {
                System.out.println("Doctor not available on this date");
            }
        } else {
            System.out.println("Either doctor ot patient dosen't exit");
        }


    }

    public static boolean checkDoctorAvailablility(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*)FROM appointments WHERE doctor_id = ? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}




