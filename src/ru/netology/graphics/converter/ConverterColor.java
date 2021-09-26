package ru.netology.graphics.converter;

import ru.netology.graphics.image.TextColorSchema;

import java.util.*;

public class ConverterColor implements TextColorSchema {

    private TreeMap<Integer, Character> characterMap;

    ConverterColor(){
        this.characterMap = new TreeMap<>();
        characterMap.put(0,'▇');
        characterMap.put(30,'●');
        characterMap.put(60,'◉');
        characterMap.put(90,'◍');
        characterMap.put(120,'◎');
        characterMap.put(150,'○');
        characterMap.put(180,'☉');
        characterMap.put(210,'◌');
        characterMap.put(255,'-');
    }

    @Override
    public char convert(int color) {
        return characterMap.ceilingEntry(Integer.valueOf(color)).getValue();
    }
}
