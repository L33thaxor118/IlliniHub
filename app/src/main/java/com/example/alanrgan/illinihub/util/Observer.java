package com.example.alanrgan.illinihub.util;

public interface Observer<T> {
  void update(Observable<T> obj, T data);
}
