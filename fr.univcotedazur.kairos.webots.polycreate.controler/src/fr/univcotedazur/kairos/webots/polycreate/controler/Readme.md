#Contrôleur de robot ménager

L’objectif de ce projet est de nous laisser structurer, déstructurer et restructurer un statechart décrivant le
code de contrôle d’un robot ménager permettant d’aspirer le sol mais aussi de faire d’autres choses comme
attraper des objets ou dessiner sur le sol. Plus précisément le but est de contrôler les différents actionneurs
du robots simulé dans un environnement virtuel en fonction des informations données par ses capteurs.
Le robot à contrôler est basé sur l’environnement cyberbotique webots (https://cyberbotics.com/).

##MVP
Pour la version minimale de votre projet, le robot doit se déplacer dans l’environnement en évitant un
maximum les obstacles et sans rester bloquer, sans tomber dans les escaliers ou les trous (s’il y en a) et
en évitant de taper dans les objets. Également, il ne devra pas franchir les murs virtuels qui délimitent des
espaces où le robot ne doit pas aller. Enfin il devra couvrir un maximum la pièce afin qu’elle soit propre.