package no.ntnu.stud.dominih.groupten.switcheroo;

import java.util.List;

public interface AsyncCallback<T> {

    void onSuccess(List<T> result);

    void onFailure(Exception cause);

}
