# dragontechimages
This is a simple Java 8 web app that combines the wonderful Photoswipe JavaScript framework
with a little Java in order to autoscan the image directory.  I have it set up to need an 
environment variable (namely WEB_DIR_ABSOLUTE_PATH).  Also note that this is currently 
written for Windows Server, so you may want to Linux up the path concatenation.  Of course,
you could also use the resource loader and go that route.

Note that if you deploy as an exploded .WAR, you can drop images into your pics folder without
restarting, but you'll have to hard-refresh the page.  Call it a TODO item to improve that.

This project is in early stages, so using a .JSP for dev purposes.

A working version is at http://www.dragon.tech although expect the site to be up and down during development.

