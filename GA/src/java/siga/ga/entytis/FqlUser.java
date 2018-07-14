/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.entytis;

import com.restfb.Facebook;

/**
 *
 * @author Otros
 */
public class FqlUser {

    @Facebook
    String uid;
    @Facebook
    String name;

    @Override
    public String toString() {
        return String.format("%s (%s)", name, uid);
    }
}