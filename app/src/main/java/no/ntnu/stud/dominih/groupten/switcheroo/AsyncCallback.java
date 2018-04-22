package no.ntnu.stud.dominih.groupten.switcheroo;

import java.util.List;

interface AsyncCallback<T> {

    void onSuccess(List<T> result);

    void onFailure(Exception cause);

}
