# This is modified version of ATG by MCUmbrella!
The original version of ATG is here: "https://github.com/TTFTCUTS/ATG"
This version of ATG updated build environment, removed original ATG biomes and added LostCities support(provided by "thraaawn").

#Usage: 
  ATG only:
    Client:
      - Enter create world menu, click "More world options", click "World type" button until it switched to "ATG - Alternate", and then create it.
    Server:
      - First, stop the server, edit "/server.properties", find "level-type=" and write "atg" after it. Then delete old world folder and start server.

  ATG with LostCities:
    Client:
      - Enter create world menu, click "More world options", click "World type" button until it switched to "ATG - Lost Cities", and then create it.
    Server:
      - First, stop the server, edit "/server.properties", find "level-type=" and write "lc_atg" after it.
      - Edit "/config/lostcities/general.cfg", find "S:adaptingWorldTypes <>", write "atg" in "<>".
      - Then delete old world folder and start server.
