Azki System Reservation

Overview

The Azki System Reservation provides an API to manage a reservation system where users can view available slots, book appointments, and cancel existing bookings. This system is built using Java and Spring Boot.

Features

View Available Slots: Retrieve all open time slots for reservations.

Book Appointments: Reserve a specific time slot for a user.

Cancel Reservations: Remove a previously made booking.

API Endpoints

Public Endpoints

POST /api/reservations

Description: Book a reservation for a specific time slot.

Request Body:

{
  "userId": 1,
  "slotId": 1
}

Responses:

200 OK: Successfully booked the reservation.


GET /api/slots

Description: Retrieve all available time slots for reservations.

Responses:

200 OK: Returns a list of available slots.


DELETE /api/reservations/{id}

Description: Cancel a reservation by its ID.

Responses:

200 OK: Successfully canceled the reservation.

Responses:

201 Created: Slot successfully added.

Database Initialization

For testing and database modeling, SQL queries defined in the run.sql file are executed at runtime. This ensures the database is pre-populated with sample data and schema required for the application.

Installation

Clone the repository:

git clone https://github.com/J-shirin97/azkiSystemReservation.git

Navigate to the project directory:

cd azkiSystemReservation

Build the project:

mvn clean install

Run the application:

mvn spring-boot:run

Testing

To run unit tests, use the following command:

mvn test
