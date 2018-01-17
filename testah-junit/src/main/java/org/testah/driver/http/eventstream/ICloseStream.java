package org.testah.driver.http.eventstream;

public interface ICloseStream<T> {

    public boolean shouldCloseStream(T data);

}
