package ru.binarysimple.ui;

/**
 * Created by voffka on 09.10.2015.
 */
public class Person {

    String name;
    String position;
    String salary;
    Integer id;
    Integer comp_id;


    Person (String _name, String _position, String _salary, Integer _id, Integer _comp_id) {
        name = _name;
        position = _position;
        salary = _salary;
        id = _id;
        comp_id = _comp_id;
    }
}
