package no.ntnu.stud.dominih.groupten.switcheroo;

import java.util.List;

/**
 * Simple helper interface for standardizing asynchronous responses. Classes that handle instances
 * of this interface should either call onFailure with the responsible exception, or onSuccess
 * with a List of Objects of the type T
 *
 * @param <T> The type parameter for the onSucess result list
 */
interface AsyncCallback<T> {

    void onSuccess(List<T> result);

    void onFailure(Exception cause);

}
