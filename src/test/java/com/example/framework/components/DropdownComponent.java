package com.example.framework.components;

import com.microsoft.playwright.Locator;
import java.util.List;
import java.util.stream.Collectors;

public class DropdownComponent {
    private final Locator root;

    public DropdownComponent(Locator root) {
        this.root = root;
    }

    //For <select> dropdowns
    public void selectByValue(String value) {
        root.selectOption(value);
    }

    public void selectByLabel(String label) {
        root.selectOption(new String[]{label});
    }

    public String getSelectedValue() {
        return root.inputValue();
    }

    public List<String> getAllOptions() {
        return root.locator("option")
                .allInnerTexts()
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    //For custom dropdowns
    public void open() {
        root.click();
    }

    public void selectCustomOption(String optionText) {
        root.locator("text=" + optionText).click();
    }
}
