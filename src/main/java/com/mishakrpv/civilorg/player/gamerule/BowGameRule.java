package com.mishakrpv.civilorg.player.gamerule;

import com.mishakrpv.civilorg.player.data.enums.ClassName;

@GameRule
public class BowGameRule {
    private ClassName className = ClassName.DEFAULT;

    public void setClass(ClassName className) {
        this.className = className;
    }

    public boolean doesClientMatch() {
        if (this.className == ClassName.SOLDIER) {
            return true;
        }

        return false;
    }
}
