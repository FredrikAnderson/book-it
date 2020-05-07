<map version="1.0.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1567001410187" ID="ID_314691154" MODIFIED="1579198949145" TEXT="Book-IT">
<node CREATED="1567001418939" ID="ID_745854692" MODIFIED="1567001420958" POSITION="right" TEXT="Req">
<node CREATED="1567001468216" ID="ID_1927282992" MODIFIED="1567001489045" TEXT="It shall be possible to book items"/>
<node CREATED="1567001490650" ID="ID_29613507" MODIFIED="1567001498478" TEXT="It shall be a webbased system"/>
<node CREATED="1567001501017" ID="ID_1735616207" MODIFIED="1567001520557" TEXT="It shall be possible to view resources to book"/>
<node CREATED="1567001523104" ID="ID_530589380" MODIFIED="1567001533027" TEXT="It shall be possible to view my bookings"/>
</node>
<node CREATED="1567001423353" ID="ID_1090008522" MODIFIED="1567001426181" POSITION="right" TEXT="Solution">
<node CREATED="1567001546060" ID="ID_87931245" MODIFIED="1567001549067" TEXT="DataModel">
<node CREATED="1567001597552" ID="ID_85977911" MODIFIED="1567001646176" TEXT="Resource">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      
    </p>
    <p>
      Attributes
    </p>
    <ul>
      <li>
        Name
      </li>
      <li>
        Location (?)
      </li>
    </ul>
  </body>
</html></richcontent>
</node>
<node CREATED="1567001654289" ID="ID_133258670" MODIFIED="1567001679481" TEXT="Booking">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      
    </p>
    <p>
      Attributes:
    </p>
    <ul>
      <li>
        Relation to one Resource
      </li>
    </ul>
  </body>
</html></richcontent>
</node>
</node>
<node CREATED="1567004816422" ID="ID_1070861586" MODIFIED="1567004822177" TEXT="Infrastructure">
<node CREATED="1567004839579" ID="ID_311720670" MODIFIED="1567004844914" TEXT="Database">
<node CREATED="1567004823436" ID="ID_308772388" MODIFIED="1588678093758" TEXT="Postgres DB">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      
    </p>
    <p>
      jdbc:postgresql://127.0.0.1:5432/postgres
    </p>
    <p>
      
    </p>
    <p>
      create user bookit with password 'bookit4ever!'
    </p>
    <p>
      create user bookit_test with password 'bookit4ever!'
    </p>
    <p>
      
    </p>
    <p>
      Create database
    </p>
    <p>
      ==============
    </p>
    <p>
      create schema bookit authorization bookit
    </p>
    <p>
      create schema bookit_test authorization bookit_test
    </p>
    <p>
      
    </p>
    <p>
      alter user bookit set search_path to 'bookit'
    </p>
    <p>
      alter user bookit_test set search_path to 'bookit_test'
    </p>
    <p>
      
    </p>
    <p>
      Drop database
    </p>
    <p>
      ============
    </p>
    <p>
      drop schema bookit cascade
    </p>
  </body>
</html>
</richcontent>
</node>
<node CREATED="1567004829886" ID="ID_374533942" MODIFIED="1567004837162" TEXT="H2 In-memory for testing"/>
</node>
<node CREATED="1567004859070" ID="ID_828887291" MODIFIED="1567004876897" TEXT="Docker running Java"/>
<node CREATED="1567004882421" ID="ID_1974686706" MODIFIED="1567004892122" TEXT="Spring Data JPA"/>
</node>
</node>
<node CREATED="1567001430416" ID="ID_1367538582" MODIFIED="1567001432670" POSITION="right" TEXT="Issues">
<node CREATED="1567005972175" ID="ID_847818449" MODIFIED="1567005982993" TEXT="Define REST services using openAPI"/>
<node CREATED="1567005985155" ID="ID_1853591363" MODIFIED="1567005996304" TEXT="Create Angular skeleton"/>
<node CREATED="1567005998047" ID="ID_1767621569" MODIFIED="1567006014264" TEXT="Make webclient deploy into Spring Boot webarea"/>
<node CREATED="1568549649870" ID="ID_1903599218" MODIFIED="1568549657643" TEXT="In Progress">
<node CREATED="1568549658843" ID="ID_1389628654" MODIFIED="1568549675147" TEXT="Add generated OpenAPI skeleton target to build path"/>
</node>
<node CREATED="1588413190732" ID="ID_466316914" MODIFIED="1588413281402" TEXT="Jackson cyclic bi-driectional issue">
<richcontent TYPE="NOTE"><html>
  <head>
    
  </head>
  <body>
    <p>
      <a href="https://stackoverflow.com/questions/10065002/jackson-serialization-of-entities-with-birectional-relationships-avoiding-cyc">https://stackoverflow.com/questions/10065002/jackson-serialization-of-entities-with-birectional-relationships-avoiding-cyc</a>
    </p>
    <p>
      <a href="https://www.logicbig.com/tutorials/misc/jackson/json-identity-info-annotation.html">https://www.logicbig.com/tutorials/misc/jackson/json-identity-info-annotation.html</a>
    </p>
    <p>
      
    </p>
    <p>
      
    </p>
    <p>
      <font face="SansSerif">Add</font>
    </p>
    <pre class="lang-java prettyprint prettyprinted" style="margin-top: 0px; margin-right: 0px; margin-bottom: 0; margin-left: 0px; padding-top: 12px; padding-bottom: 12px; padding-right: 8px; padding-left: 8px; border-top-style: none; border-top-width: 0px; border-right-style: none; border-right-width: 0px; border-bottom-style: none; border-bottom-width: 0px; border-left-style: none; border-left-width: 0px; font-style: normal; font-weight: 400; line-height: inherit; font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, sans-serif; font-size: 13px; vertical-align: baseline; display: block; color: rgb(36, 39, 41); letter-spacing: normal; text-align: left; text-indent: 0px; text-transform: none; word-spacing: 0px"><code style="margin-top: 0px; margin-right: 0px; margin-bottom: 0px; margin-left: 0px; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px; border-top-style: none; border-top-width: 0px; border-right-style: none; border-right-width: 0px; border-bottom-style: none; border-bottom-width: 0px; border-left-style: none; border-left-width: 0px; font-style: inherit; font-variant: inherit; font-weight: normal; line-height: inherit; font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, sans-serif; font-size: 13px; vertical-align: baseline; white-space: inherit"><font face="SansSerif" size="13px" color="black">@JsonIdentityInfo(generator=</font><font face="SansSerif" size="13px" color="rgb(43, 145, 175)">ObjectIdGenerators</font><font face="SansSerif" size="13px" color="black">.</font><font face="SansSerif" size="13px" color="rgb(43, 145, 175)">PropertyGenerator</font><font face="SansSerif" size="13px" color="black">.class, property=&quot;id&quot;)</font></code></pre>
    <font face="SansSerif">to ProjectDTO and ItemDTO and then it works.<br class="Apple-interchange-newline" face="SansSerif" /></font>
  </body>
</html>
</richcontent>
</node>
</node>
</node>
</map>
