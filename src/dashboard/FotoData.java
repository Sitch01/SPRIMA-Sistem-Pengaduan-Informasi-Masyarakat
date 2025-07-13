/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dashboard;

/**
 *
 * @author saktiforce
 */
public class FotoData {
    private String path;
    private javax.swing.ImageIcon thumbnail;

    public FotoData(String path, javax.swing.ImageIcon thumbnail) {
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public String getPath() {
        return path;
    }

    public javax.swing.ImageIcon getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        return path; // untuk keperluan toString di JTable
    }
}
