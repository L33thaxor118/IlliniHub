package com.example.alanrgan.illinihub.util;

import com.example.alanrgan.illinihub.Event;

import java.util.List;

/**
 * Interface for classes that wish to get the result of a database query
 * asynchronously. This function will be called by the class implementing the
 * interface when a Query Task initiated by DatabaseHelper finishes.
 * This prevents crashes due to main thread taking too long to respond.
 */
public interface DBHelperAsyncResponse {
    void processFinishGetMatches(List<Event> output);
}
