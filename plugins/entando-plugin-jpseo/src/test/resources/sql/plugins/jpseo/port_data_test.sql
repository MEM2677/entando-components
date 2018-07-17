INSERT INTO jpseo_contentextraparams(contentid, workxml, onlinexml) VALUES ('ART111', '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <descriptions>
    <property key="en">EN Content Description 1</property>
    <property key="it">Descrizione IT Content 1</property>
  </descriptions>
  <keywords>
    <property key="en" useDefaultLang="true" >keyEN1.1,keyEN1.2</property>
    <property key="it">keyIT1.1,keyIT1.2,keyIT1.3,keyIT1.4</property>
  </keywords>
  <complexParameters>
    <parameter key="key1">
        <property key="it">VALUE_1 IT ART111</property>
    </parameter>
    <parameter key="key2">
      <property key="fr">VALUE_2 FR ART111</property>
      <property key="en">VALUE_2 EN ART111</property>
      <property key="it">VALUE_2 IT ART111</property>
    </parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN ART111</property>
      <property key="it">VALUE_3 IT ART111</property>
    </parameter>
    <parameter key="key4">
        <property key="it">VALUE_4 IT ART111</property>
    </parameter>
  </complexParameters>
</config>', NULL);


INSERT INTO jpseo_contentextraparams(contentid, workxml, onlinexml) VALUES ('ART1', '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <descriptions>
    <property key="en">EN Content Description 1 ART1 work</property>
  </descriptions>
  <keywords>
    <property key="en" useDefaultLang="true" >work_ART1_keyEN1.1,work_ART1_keyEN1.2</property>
  </keywords>
  <complexParameters>
    <parameter key="key1">
        <property key="it">VALUE_1 IT ART1 work</property>
    </parameter>
    <parameter key="key2">
      <property key="en">VALUE_2 EN ART1 work</property>
      <property key="it">VALUE_2 IT ART1 work</property>
    </parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN ART1 work</property>
    </parameter>
    <parameter key="key4">
        <property key="it">VALUE_4 IT ART1 work</property>
    </parameter>
  </complexParameters>
</config>', '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <descriptions>
    <property key="en">EN Content Description 1 ART1 online</property>
  </descriptions>
  <keywords>
    <property key="en" useDefaultLang="true" >online_ART1_keyEN1.1,online_ART1_keyEN1.2</property>
  </keywords>
  <complexParameters>
    <parameter key="key1">
        <property key="it">VALUE_1 IT ART1 online</property>
    </parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN ART1 online</property>
    </parameter>
    <parameter key="key4">
        <property key="it">VALUE_4 IT ART1 online</property>
    </parameter>
  </complexParameters>
</config>');




INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('seo_page_1', 'homepage', 7, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('seo_page_2', 'homepage', 8, 'free');

INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('seo_page_1', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Seo Page 1</property>
<property key="it">Pagina Seo 1</property>
</properties>
', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
  <useextradescriptions>false</useextradescriptions>
  <descriptions>
    <property key="en">EN Description SeoPage 1</property>
    <property key="it">Descrizione IT SeoPage 1</property>
  </descriptions>
  <keywords>
    <property key="en" useDefaultLang="true" >keyEN1.1,keyEN1.2</property>
    <property key="it">keyIT1.1,keyIT1.2,keyIT1.3,keyIT1.4</property>
  </keywords>
  <complexParameters>
    <parameter key="key1">VALUE_1</parameter>
    <parameter key="key2">VALUE_2</parameter>
    <parameter key="key5">
      <property key="fr">VALUE_5 FR</property>
      <property key="en">VALUE_5 EN</property>
      <property key="it">VALUE_5 IT</property>
    </parameter>
    <parameter key="key6">VALUE_6</parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN</property>
      <property key="it">VALUE_3 IT</property>
    </parameter>
    <parameter key="key4">VALUE_4</parameter>
  </complexParameters>
</config>', '2018-06-20 16:31:31');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('seo_page_2', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Seo Page 1</property>
<property key="it">Pagina Seo 1</property>
</properties>
', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
  <useextradescriptions>false</useextradescriptions>
  <descriptions>
    <property key="en">EN Description SeoPage 2</property>
    <property key="it">Descrizione IT SeoPage 2</property>
  </descriptions>
  <keywords>
    <property key="en">keyEN2.1,keyEN2.2,keyEN2.3</property>
    <property key="it">keyIT2.1,keyIT2.2</property>
  </keywords>
  <complexParameters>
    <lang code="it">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_IT</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
    <lang code="en">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_EN</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
  </complexParameters>
</config>', '2018-06-26 17:31:31');
