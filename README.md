# dragontechimages
This is a simple Java 8 web app that combines the wonderful Photoswipe JavaScript framework
with a little Java in order to autoscan the image directory.  I have it set up to need an 
environment variable (namely WEB_DIR_ABSOLUTE_PATH).  Also note that this is currently 
written for Windows Server, so you may want to Linux up the path concatenation.  Of course,
you could also use the resource loader and go that route.

You need to deploy this as an exploded .WAR, so you can drop images into your pics folder without
restarting.  An AJAX call will be made from index.jsp every few seconds to see if images have
been added.  Deleted images will not be noticed (yet) but of course will not show up if the
client hard refreshes or uncaches.


A working version is at http://www.dragon.tech although expect the site to be up and down during development.

