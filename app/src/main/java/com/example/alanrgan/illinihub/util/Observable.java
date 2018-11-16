package com.example.alanrgan.illinihub.util;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
  private List<Observer<T>> observers = new ArrayList<>();

  public void notifyObservers(T data) {
    for (Observer<T> observer : observers) {
      observer.update(this, data);
    }
  }

  public void addObserver(Observer<T> observer) {
    observers.add(observer);
  }
}
