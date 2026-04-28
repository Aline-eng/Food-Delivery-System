# 🍔 Food Delivery System (Java OOP Project)

## 📌 Project Overview
This project is a simple Food Delivery System developed using Java.  
It demonstrates core Object-Oriented Programming (OOP) principles by modeling a real-world food ordering and delivery process.

The system allows a customer to select food items from a restaurant menu, place an order, and simulate delivery.

---

## 🎯 Objectives
- Apply OOP concepts in a real-world scenario
- Build a clean and structured Java system
- Demonstrate relationships between objects

---

## 🧩 Features
- Display restaurant menu
- User selects multiple food items
- Automatic order total calculation
- Simulated order preparation and delivery
- Interactive user input using Scanner

---

## 🏗️ System Design

### Classes Used
1. **User (Abstract Class)**
    - Base class for all users
    - Contains common attributes (id, name, phone)

2. **Customer**
    - Inherits from User
    - Places orders

3. **DeliveryAgent**
    - Inherits from User
    - Handles order delivery

4. **Restaurant**
    - Manages menu and food preparation

5. **Order**
    - Stores ordered items and total price

---

## 🔗 OOP Concepts Applied

### 1. Encapsulation
- Private attributes in all classes
- Access through getters and methods

### 2. Abstraction
- `User` is an abstract class
- Defines common structure for users

### 3. Inheritance
- `Customer` and `DeliveryAgent` extend `User`

### 4. Polymorphism
- `displayDetails()` method overridden in subclasses

---

## ⚙️ How the System Works

1. System displays restaurant menu
2. User selects items
3. Items are added to the order
4. Customer places the order
5. Restaurant prepares the order
6. Delivery agent delivers the order
7. Order summary is displayed

---

## ▶️ How to Run the Project

1. Clone the repository:
```bash
git clone https://github.com/Aline-eng/Food-Delivery-System.git