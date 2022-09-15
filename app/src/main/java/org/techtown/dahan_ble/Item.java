package org.techtown.dahan_ble;

public class Item {
    String name;
    String macadd;

    //생성자
    public Item(String name, String macadd) {
        this.name = name;
        this.macadd = macadd;
    }

    //게터, 세터
    public String getName() { return name; }
    public String getMac() { return macadd; }
    public void setName(String name) { this.name = name; }
    public void setMac(String macadd) { this.macadd = macadd; }

    @Override
    public String toString() {
        return "Item{" + "name=" + name + ", mac address=" + macadd + "}";
    }
}
