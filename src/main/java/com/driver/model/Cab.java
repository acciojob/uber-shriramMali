package com.driver.model;

import javax.persistence.*;

@Entity
public class Cab{

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 private int perKmRate;

 private boolean avilable;

 @OneToOne
 @JoinColumn
 private Driver driver;

 public Cab(){}

 public Cab(int id, int perKmRate, boolean avilable, Driver driver) {
  this.id = id;
  this.perKmRate = perKmRate;
  this.avilable = avilable;
  this.driver = driver;
 }

 public Cab(int perKmRate, boolean avilable) {
  this.perKmRate = perKmRate;
  this.avilable = avilable;
 }

 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }

 public int getPerKmRate() {
  return perKmRate;
 }

 public void setPerKmRate(int perKmRate) {
  this.perKmRate = perKmRate;
 }

 public boolean getAvilable() {
  return avilable;
 }

 public void setAvilable(boolean avilable) {
  this.avilable = avilable;
 }

 public Driver getDriver() {
  return driver;
 }

 public void setDriver(Driver driver) {
  this.driver = driver;
 }
}