package com.bitdubai.fermat_api.layer.all_definition.navigation_structure;

import java.io.Serializable;

/**
 * Created by Matias Furszyfer on 2015.09.1..
 */
public class TitleBar implements com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatTitleBar,Serializable {

    /**
     * class private attributes
     */
    private String label;

    private String color;

    private String backgroundImage;

    private SearchView runtimeSearchView;

    private String iconName;

    private String subTitle;

    private RuntimeFernatComboBox comboBox;
    private int labelSize=-1;
    private String titleColor;
    private byte[] toggleIcon;
    private boolean isTitleTextStatic;
    private String font;

    private FermatDrawable backgroundDrawable;

    private FermatDrawable logoDrawable;

    /**
     * This icon will be used for open the navigationDrawer, go back
     * If you put the appLink of the item "Back" the app will back when the user press the item
      */

    private MenuItem navItem;

    public void setFont(String font) {
        this.font = font;
    }

    /**
     * Class Constructors
     */
    public TitleBar() {
    }

    public TitleBar(String label, String color, String backgroundImage, SearchView runtimeSearchView) {
        this.label = label;
        this.color = color;
        this.backgroundImage = backgroundImage;
        this.runtimeSearchView = runtimeSearchView;
    }

    /**
     * Class getters
     */
    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public int getLabelSize(){
        return labelSize;
    }


    public SearchView getRuntimeSearchView() {
        if (runtimeSearchView != null) {
            return runtimeSearchView;
        }
        //TODO METODO CON RETURN NULL - OJO: solo INFORMATIVO de ayuda VISUAL para DEBUG - Eliminar si molesta
        return null;
    }

    public String getIconName() {
        return iconName;
    }

    public RuntimeFernatComboBox getComboBox() {
        return comboBox;
    }

    /**
     * Class setters
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setRuntimeSearchView(SearchView runtimeSearchView) {
        this.runtimeSearchView = runtimeSearchView;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setComboBox(RuntimeFernatComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public void setToggleIcon(byte[] toggleIcon) {
        this.toggleIcon = toggleIcon;
    }

    public byte[] getNavigationIcon() {
        return toggleIcon;
    }

    public byte[] getToggleIcon() {
        return toggleIcon;
    }

    public void setIsTitleTextStatic(boolean isTitleTextStatic) {
        this.isTitleTextStatic = isTitleTextStatic;
    }

    public boolean isTitleTextStatic(){
        return isTitleTextStatic;
    }

    public String getFont() {
        return font;
    }

    public MenuItem getNavItem() {
        return navItem;
    }

    public void setNavItem(MenuItem navItem) {
        this.navItem = navItem;
    }

    public FermatDrawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public void setBackgroundDrawable(FermatDrawable backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public FermatDrawable getLogoDrawable() {
        return logoDrawable;
    }

    public void setLogoDrawable(FermatDrawable logoDrawable) {
        this.logoDrawable = logoDrawable;
    }
}
