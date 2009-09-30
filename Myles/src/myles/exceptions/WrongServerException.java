/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myles.exceptions;

/**
 *
 * @author User
 */
public class WrongServerException extends Exception{
    @Override
    public String toString(){
        return "La configuracion del servidor no cumple los requisitos.";
    }
}
