package org.ismek.beans;

import org.ismek.db.DbOperations;
import org.ismek.domain.Rehber;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.Facelet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "rehberMB")
public class RehberManagedBean {

    private int id;
    private String isim;
    private String telefon;
    private String meth;
    private List<Rehber> rehberList = new ArrayList<>();
    private List<Rehber> arananList = new ArrayList<>();

    public RehberManagedBean() {
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String rehberId = req.getParameter("rehberId");
        String meth = req.getParameter("arama");
        if (rehberId != null) {
            Rehber rehber = DbOperations.kisiGetir(Integer.valueOf(rehberId));
            id = rehber.getId();
            isim = rehber.getIsim();
            telefon = rehber.getTelefon();
        }else if (meth!=null){
           // sendRedirect("/faces/index.xhtml?arama=2");
            this.meth=meth;
            arananList = DbOperations.kisiGetir(meth);
            if(! arananList.isEmpty()){
                rehberList=arananList;
            }
        }
    }

    public void save() {
        DbOperations.rehbereEkle(isim, telefon);
        sendRedirect("/faces/index.xhtml");
    }

    public void delete(int id) {
        sendRedirect("/faces/delete.xhtml?rehberId=" + id);
    }

    public void edit(int id) {
        sendRedirect("/faces/edit.xhtml?rehberId=" + id);
    }

    private void sendRedirect(String url) {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        ExternalContext externalContext = currentInstance.getExternalContext();
        try {
            externalContext.redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        DbOperations.kisiGuncelle(id, isim, telefon);
        sendRedirect("/faces/index.xhtml");
    }

    public void delete() {
        DbOperations.kisiSil(id);
        sendRedirect("/faces/index.xhtml");
    }

    public void ara() {
        List<Rehber> rehberListesi = DbOperations.kisiGetir(isim);
        for (Rehber rehber : rehberListesi) {
            System.out.println(rehber);
        }
        if(! rehberListesi.isEmpty()){
            setRehberList(rehberListesi);
        }
        sendRedirect("/faces/index.xhtml?arama="+ isim);
    }

    public List<Rehber> getRehberList() {
        if (meth==null) {
            rehberList = DbOperations.tumRehberiGetir();
        }else {
            arananList = DbOperations.kisiGetir(meth);
            if(! arananList.isEmpty()){
                rehberList=arananList;
            }

        }
        return rehberList;
    }

    public void setRehberList(List<Rehber> rehberList) {
        this.rehberList = rehberList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}