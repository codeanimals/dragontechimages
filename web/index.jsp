<%@ page import="tech.dragon.webimage.ImageDirectoryScanner" %>
<%@ page import="java.util.Map" %>
<%--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />

<!-- Core CSS file -->
<link rel="stylesheet" href="ps/photoswipe.css"> 

<!-- Skin CSS file (styling of UI - buttons, caption, etc.)
     In the folder of skin CSS file there are also:
     - .png and .svg icons sprite, 
     - preloader.gif (for browsers that do not support CSS animations) -->
<link rel="stylesheet" href="ps/default-skin/default-skin.css">

        <!-- may override Photoswipe stuff -->
 <link rel="stylesheet" href="dt/dt.css">

        <!-- Core JS file -->
<script src="ps/photoswipe-dt.js"></script>

<!-- UI JS file -->
<script src="ps/photoswipe-ui-dt.js"></script>

        <title>Welcome</title>



    </head>

    <body>
<div id='lead-text' style='color: white; background: #000000; font-family: "Helvetic Neue", Helvetica, Arial; font-size: larger; height: 100px;'>Strictly experimental. A medley of adventitious items.  Currently working on playing with Photoswipe, a wonderful JS image framework that I'm trying to make more wonderful with backend Java.</div>
<!-- Root element of PhotoSwipe. Must have class pswp. -->
<div class="pswp" tabindex="-1" role="dialog" aria-hidden="true">

    <!-- Background of PhotoSwipe. 
         It's a separate element as animating opacity is faster than rgba(). -->
    <div class="pswp__bg"></div>

    <!-- Slides wrapper with overflow:hidden. -->
    <div class="pswp__scroll-wrap">

        <!-- Container that holds slides. 
            PhotoSwipe keeps only 3 of them in the DOM to save memory.
            Don't modify these 3 pswp__item elements, data is added later on. -->
        <div class="pswp__container">
            <div class="pswp__item"></div>
            <div class="pswp__item"></div>
            <div class="pswp__item"></div>
        </div>

        <!-- Default (PhotoSwipeUI_Default) interface on top of sliding area. Can be changed. -->
        <div class="pswp__ui pswp__ui--hidden">

            <div class="pswp__top-bar">

                <!--  Controls are self-explanatory. Order can be changed. -->

                <div class="pswp__counter"></div>


                <button class="pswp__button pswp__button--share" title="Share"></button>

                <button class="pswp__button pswp__button--fs" title="Toggle fullscreen"></button>

                <button class="pswp__button pswp__button--zoom" title="Zoom in/out"></button>

                <!-- Preloader demo http://codepen.io/dimsemenov/pen/yyBWoR -->
                <!-- element will get class pswp__preloader--active when preloader is running -->
                <div class="pswp__preloader">
                    <div class="pswp__preloader__icn">
                      <div class="pswp__preloader__cut">
                        <div class="pswp__preloader__donut"></div>
                      </div>
                    </div>
                </div>
            </div>

            <div class="pswp__share-modal pswp__share-modal--hidden pswp__single-tap">
                <div class="pswp__share-tooltip"></div> 
            </div>

            <button class="pswp__button pswp__button--arrow--left" title="Previous (arrow left)">
            </button>

            <button class="pswp__button pswp__button--arrow--right" title="Next (arrow right)">
            </button>

            <div class="pswp__caption">
                <div class="pswp__caption__center"></div>
            </div>

        </div>

    </div>

</div>

<script source='text/javascript'>
var pswpElement = document.querySelectorAll('.pswp')[0];



var close = function()
{
    console.log('no closing!');

};



// build items array, this will declare var items
// todo: we could probably just leave this out and get our items with ajaz. this would mean ni jsp needed!
<%
    ImageDirectoryScanner ids = new ImageDirectoryScanner();
    String imgArrayString = ids.getItemsArray("/pics");
%>


var items =
[
<%= imgArrayString %>
];

// define options (if needed)
var options = {
    // optionName: 'option value'
    // for example:
    index: 0,
    modal: false,
    escKey: false
};


// Initializes and opens PhotoSwipe
var gallery = new PhotoSwipe( pswpElement, PhotoSwipeUI_Default, items, options);


gallery.init();



// periodically send an XHR to look for new pics
function imgReqListen ()
{
    // todo: protect clients' item array from responseText a little better, so we don't accidentally load it up with a 500 error
    try
    {
        var newItems = eval('(' + this.responseText + ')');
        if(Array.isArray(newItems))
        {
            for(var x in newItems)
            {
                items.push(newItems[x]);
            }
        }

    }
    catch(e)
    {
        console.log('exception '+e);
    }
}

var imgCheckXhr;



//check every ten seconds
var imageCheckTimer = setInterval(imageCheck, 10000);

function imageCheck()
{
    /*
     Object {src: "/pics/abort.jpg", w: 866, h: 1503, initialLayout: undefined, container: div.pswp__zoom-wrap…}
     (index):233 Object {src: "/pics/airplane_chi.jpg", w: 1000, h: 562, loading: false, loaded: true…}
     (index):233 Object {src: "/pics/avengerEngine2.jpg", w: 600, h: 337}
     (index):233 Object {src: "/pics/badtwittertranslation.png", w: 656, h: 146}
     (index):233 Object {src: "/pics/baobaoarrivesinchina.jpg", w: 1173, h: 875}
     (index):233 Object {src: "/pics/chesapeakeSUbmarine.jpg", w: 600, h: 276}
     (index):233 Object {src: "/pics/chi1.jpg", w: 600, h: 1068}
     */
    var itemsToSend = [];
    items.forEach(function(obj)
    {
        itemsToSend.push('{ src: "'+obj.src+'", w: '+obj.w+', h: '+obj.h+' }');
    });

    imgCheckXhr = new XMLHttpRequest();
    imgCheckXhr.addEventListener("load", imgReqListen);
    imgCheckXhr.open("POST", "/imagearray", true);
    imgCheckXhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    imgCheckXhr.send(itemsToSend);
}


</script>
    </body>

</html>
