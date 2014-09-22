/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

/**
 *
 * @author sikdar
 */
public interface Edge {

    int getOther(int v);

    int getV1();

    int getV2();
    
}
