<map version="1.0.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1567001410187" ID="ID_314691154" MODIFIED="1567001417057" TEXT="Book-IT">
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
<node CREATED="1567004823436" ID="ID_308772388" MODIFIED="1567004828594" TEXT="Postgres DB"/>
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
</node>
</node>
</map>
