# TODO

## Docker
docker compose pour démarrer l'app avec
* images
  * mongodb
  * rabbitmq
  * webapp springboot
  * cache redis
  * graphql
  * storybook
  * front
* version
  * desktop pour commencer
  * wsdl après?
  * podman ?

## Dev containers

Mise en place de dev containers pour faciliter la mise en place de l'environnement de dev
* Avec docker dans un premier temps
* Avec podman dans un second temps

## Multi module gradle
	
* core
  *	clients
    * lister
	* ajouter
	* supprimer
  *	adresses
	* lister par client
	* trouver par id
	* ajouter
	* supprimer
* persistence
	* event store pour mutations (dans mongodb)
	* envoie un event et le stock pour la rejouabilité
	* mongodb  
	  ecoute les events pour construire les vues
	* faire procédure pour extraire le store et le rejouer pour remonter une bd
	* tester avec db en memoire
* command line
	* utilise core en ligne de command
* web
   * rest
     * faire un core starter ?
     * utiliser openapi pour generer code à partir du contrat
     * clients
       * lister
       * trouver par id
       * ajouter
       * supprimer
     * adresses
       * trouver par id
       * ajouter
       * supprimer
* web cache
  * entete
    * gérer etag avec springboot sur les GET
  * redis
    * metter en cache lister / trouver  
      invalider cache sur ajouter / supprimer  
      -> faisable depuis l'appelant mais si on voulait le mettre en reverse proxy ce n'est pas l'outil
  * reverse proxy
    * trouver un reverse proxy qui permettrait d'invalider des caches sur les POST/PUT/DELETE
  * memory
    * via les resolver graphql voir si on peut optimiser quand on recupère une liste pour avoir  
      tous les entites principales d'abord  
      puis tous les sous objets en map pour ensuite les affecter au bon parent  
      etc...
* graphql
  * modele
    * client
      * nom
      * prenom
      * adresse
  * lister clients + adresse
  * ajouter clients (avec adresses et adresses existantes)
  * supprimer clients (avec son adresse)
  * doit passer par le web cache
* front-library
  * storybook
* front
  * angular
  * utilise la front library
  * utilise graphql
