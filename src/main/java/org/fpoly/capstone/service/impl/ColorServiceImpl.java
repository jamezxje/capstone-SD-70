package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl {
@Autowired
    private ColorRepository colorRepository;
public List<Color> getAllColors() {
    return colorRepository.findAll();
}
}
