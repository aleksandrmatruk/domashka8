package ru.skypro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> strArrList = new ArrayList<>();
        strArrList.add("Привет");
        strArrList.add("меня");
        strArrList.add("зовут");
        strArrList.add("массив");
        System.out.println(strArrList.indexOf("массив"));  // Возвращает индекс элемента списка по значению
        System.out.println(strArrList.get(0));  // Возвращает элемент списка по индексу
        Iterator<String> arrListIt = strArrList.iterator();  // Удаление элемента списка используя итератор
        while (arrListIt.hasNext()) {
            String nextEl = arrListIt.next();
            if (nextEl.contains("Привет")) {
                arrListIt.remove();
            }
        }
        System.out.println(strArrList);


        LinkedList<String> strLinkList = new LinkedList<>();
        strLinkList.add("Привет");
        strLinkList.add("меня");
        strLinkList.add("зовут");
        strLinkList.add("массив");

        System.out.println(strLinkList.indexOf("Привет"));  // Возвращает индекс элемента списка по значению
        System.out.println(strLinkList.get(1));  // Возвращает элемент списка по индексу

        strLinkList.remove("массив");  // Удаление в LinkedList
        strLinkList.remove(1);
        System.out.println(strLinkList);

        MyArrayList<String> myArrayList = new MyArrayList<>();
        myArrayList.add("1");
        myArrayList.add("2");
        System.out.println(myArrayList);


        MyLinkedList<String> myLinkedList = new MyLinkedList<>();
        myLinkedList.add("1");
        myLinkedList.add("2");
        myLinkedList.add("3");

        myLinkedList.remove(1);
        System.out.println(myLinkedList);
    }
}