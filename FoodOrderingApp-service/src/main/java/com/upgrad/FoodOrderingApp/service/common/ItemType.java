package com.upgrad.FoodOrderingApp.service.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class ItemType {

    public enum NON_VEG {

            NON_VEG("NON_VEG");

            public String value;

        NON_VEG(String value) {
            this.value = value;
        }

        }
    }

