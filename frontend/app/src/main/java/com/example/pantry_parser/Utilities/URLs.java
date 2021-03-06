package com.example.pantry_parser.Utilities;

public class URLs {
    /*
     * Base url
     */
    private static final String DOMAIN = "http://coms-309-032.cs.iastate.edu:8080";
    /*
     * Register route
     */
    public static final String URL_REGISTER = DOMAIN + "/users/";
    /*
     * User route
     */
    public static final String URL_USER = DOMAIN + "/user/";
    /*
     * User login route
     */
    public static final String URL_LOGIN = DOMAIN + "/login/";
    /*
     * Recipe route
     */
    public static final String URL_RECIPE = DOMAIN + "/recipe/";

    public static String paginatedQueryURLComposer(String base, String query, int pageNo, int perPage) {
        return base + "?query=" + query + "&pageNo=" + pageNo + "&perPage=" + perPage;
    }

    public static String updatePaginatedQueryUrl(String url, String propertyToUpdate, String newVal) {
        boolean changed = false;
        String[] base = url.split("\\?");
        String newUrl = base[0] + "?";
        if(base.length > 1){
            String[] chunks = base[1].split("&");

            for(int i = 0; i < chunks.length; i++) {
                if(chunks[i].contains(propertyToUpdate)) {
                    newUrl += propertyToUpdate + "=" + newVal;
                    changed = true;
                } else {
                    newUrl += chunks[i];
                }
                if(i != (chunks.length - 1)){
                    newUrl += "&";
                }
            }
        }

        if(!changed) {
            if(newUrl.charAt(newUrl.length()-1) != '?'){
                newUrl += "&";
            }
            newUrl += propertyToUpdate + "=" + newVal;
        }
        return newUrl;
    }

    public static String getNextPaginatedQueryPageUrl(String url) {
        boolean changed = false;
        String[] base = url.split("\\?");
        String newUrl = base[0] + "?";
        if(base.length > 1){
            String[] chunks = base[1].split("&");

            for(int i = 0; i < chunks.length; i++) {
                if(chunks[i].contains("pageNo")) {
                    int pageNo = Integer.parseInt(chunks[i].split("=")[1]) + 1;
                    newUrl += "pageNo=" + pageNo;
                    changed = true;
                } else {
                    newUrl += chunks[i];
                }
                if(i != (chunks.length - 1)){
                    newUrl += "&";
                }
            }
        }

        if(!changed) {
            if(newUrl.charAt(newUrl.length()-1) != '?'){
                newUrl += "&";
            }
            newUrl += "pageNo=1";
        }
        return newUrl;
    }

    public static String switchBaseUrl(String original, String newBase) {
        String[] chunks = original.split("\\?");
        return chunks.length > 1 ? newBase + "?" + chunks[1] : newBase;
    }
}
