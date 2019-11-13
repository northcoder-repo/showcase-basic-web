
create schema if not exists IMDB authorization sa;

set schema 'IMDB';

-- ------------------------------------------------

-- FK constraints are added as separate statements,
-- to make it easier to separate them out from the
-- rest of the create statements, if needed.

-- H2 DB automatically creates a related index for 
-- each of our FK constraints.

-- Any additional indexes which may be needed to 
-- support your usage - you have to create those!

-- (Some FKs are not implemented, due to orphan IDs.
-- We do define indexes instead for these - see below.)

-- ------------------------------------------------

-- The order in which we drop tables has to 
-- account for FK constraint dependencies.

drop table if exists title_episode;
drop table if exists title_aka_title_type;
drop table if exists title_principal;
drop table if exists title_genre;
drop table if exists title_aka;
drop table if exists talent_title;
drop table if exists talent_role;
drop table if exists talent;
drop table if exists title;
drop table if exists title_type;
drop table if exists role;
drop table if exists region;
drop table if exists language;
drop table if exists genre;
drop table if exists content_type;
drop table if exists category;

-- ------------------------------------------------

create table if not exists category (
  category_id int primary key,
  category_name varchar(100) not null) 
as select * from csvread(
  'csv_files/category.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists content_type (
  content_type_id int primary key,
  content_type_name varchar(100) not null) 
as select * from csvread(
  'csv_files/content_type.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists genre (
  genre_id int primary key,
  genre_name varchar(100) not null) 
as select * from csvread(
  'csv_files/genre.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists language (
  language_id varchar(10) primary key,
  language_name varchar(100)) 
as select * from csvread(
  'csv_files/language.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists region (
  region_id varchar(10) primary key,
  region_name varchar(100)) 
as select * from csvread(
  'csv_files/region.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists role (
  role_id int primary key,
  role_name varchar(100) not null) 
as select * from csvread(
  'csv_files/role.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists title_type (
  title_type_id int primary key,
  title_type_name varchar(100) not null) 
as select * from csvread(
  'csv_files/title_type.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists title (
  title_id varchar(20) primary key,
  content_type_id int not null,
  primary_title varchar(500) not null,
  original_title varchar(500),
  is_adult int,
  start_year int,
  end_year int,
  runtime_minutes int) 
as select * from csvread(
  'csv_files/title.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

alter table title
add foreign key (content_type_id) 
references content_type(content_type_id)
;

-- ------------------------------------------------

create table if not exists talent (
  talent_id varchar(20) primary key,
  talent_name varchar(500) not null,
  birth_year int,
  death_year int) 
as select * from csvread(
  'csv_files/talent.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

-- ------------------------------------------------

create table if not exists talent_role (
  talent_id varchar(20),
  role_id int,
  ord int not null,
  primary key (talent_id, role_id)) 
as select * from csvread(
  'csv_files/talent_role.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

alter table talent_role
add foreign key (talent_id) 
references talent(talent_id)
;

alter table talent_role
add foreign key (role_id) 
references role(role_id)
;

-- ------------------------------------------------

create table if not exists talent_title (
  talent_id varchar(20),
  title_id varchar(20),
  primary key (talent_id, title_id)) 
as select * from csvread(
  'csv_files/talent_title.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

alter table talent_title
add foreign key (talent_id) 
references talent(talent_id)
;

create index tal_ttl_title_id_idx on talent_title(title_id)
;

-- ------------------------------------------------

create table if not exists title_aka (
  title_id varchar(20),
  ord int not null,
  aka_title varchar(500) not null,
  region_id varchar(10),
  language_id varchar(10),
  additional_attrs varchar(500),
  is_original_title int,
  primary key (title_id, ord)) 
as select * from csvread(
  'csv_files/title_aka.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

alter table title_aka
add foreign key (region_id) 
references region(region_id)
;

alter table title_aka
add foreign key (language_id) 
references language(language_id)
;

-- ------------------------------------------------

create table if not exists title_genre (
  title_id varchar(20),
  genre_id int,
  ord int not null,
  primary key (title_id, genre_id)) 
as select * from csvread(
  'csv_files/title_genre.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

alter table title_genre
add foreign key (title_id) 
references title(title_id)
;

alter table title_genre
add foreign key (genre_id) 
references genre(genre_id)
;

-- ------------------------------------------------

create table if not exists title_principal (
  title_id varchar(20),
  talent_id varchar(20),
  ord int not null,
  category_id int not null,
  job varchar(1000),
  role_names varchar(1000),
  primary key (title_id, talent_id, ord)) 
as select * from csvread(
  'csv_files/title_principal.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

create index ttl_prin_tal_id_idx on title_principal(talent_id)
;

-- ------------------------------------------------

create table if not exists title_aka_title_type (
  title_id varchar(20),
  title_type_id int,
  ord int not null,
  primary key (title_id, title_type_id, ord))
as select * from csvread(
  'csv_files/title_aka_title_type.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

alter table title_aka_title_type
add foreign key (title_type_id) 
references title_type(title_type_id)
;

-- ------------------------------------------------

create table if not exists title_episode (
  title_id varchar(20),
  parent_title_id varchar(20),
  season_number int,
  episode_number int,
  primary key (title_id))
as select * from csvread(
  'csv_files/title_episode.csv', 
  null, 'charset=UTF-8 nullString=\\N')
;

create index ttl_epi_par_idx on title_episode(parent_title_id)
;

-- ------------------------------------------------

-- fill in some gaps not provided in the source data. These
-- are rough mappings and may not be 100% accurate. Suitable
-- only for demos and testing, NOT for production use.

update imdb.language set language_name = 'Basque' where language_id = 'eu';
update imdb.language set language_name = 'Kurdish' where language_id = 'ku';
update imdb.language set language_name = 'Sindhi' where language_id = 'sd';
update imdb.language set language_name = 'Erzya' where language_id = 'myv';
update imdb.language set language_name = 'private usage' where language_id = 'qbp';
update imdb.language set language_name = 'Zulu' where language_id = 'zu';
update imdb.language set language_name = 'Punjabi' where language_id = 'pa';
update imdb.language set language_name = 'Latin' where language_id = 'la';
update imdb.language set language_name = 'Italian' where language_id = 'it';
update imdb.language set language_name = 'Yiddish' where language_id = 'yi';
update imdb.language set language_name = 'Armenian' where language_id = 'hy';
update imdb.language set language_name = 'Mongolian' where language_id = 'mn';
update imdb.language set language_name = 'Malay' where language_id = 'ms';
update imdb.language set language_name = 'Estonian' where language_id = 'et';
update imdb.language set language_name = 'Welsh' where language_id = 'cy';
update imdb.language set language_name = 'Kannada' where language_id = 'kn';
update imdb.language set language_name = 'Chinese' where language_id = 'zh';
update imdb.language set language_name = 'Cree' where language_id = 'cr';
update imdb.language set language_name = 'Hawaiian' where language_id = 'haw';
update imdb.language set language_name = 'Croatian' where language_id = 'hr';
update imdb.language set language_name = 'Azerbaijani' where language_id = 'az';
update imdb.language set language_name = 'Inuktitut' where language_id = 'iu';
update imdb.language set language_name = 'Tamil' where language_id = 'ta';
update imdb.language set language_name = 'Catalan' where language_id = 'ca';
update imdb.language set language_name = 'Macedonian' where language_id = 'mk';
update imdb.language set language_name = 'Sesotho' where language_id = 'st';
update imdb.language set language_name = 'Burmese' where language_id = 'my';
update imdb.language set language_name = 'Albanian' where language_id = 'sq';
update imdb.language set language_name = 'Hungarian' where language_id = 'hu';
update imdb.language set language_name = 'Wolof' where language_id = 'wo';
update imdb.language set language_name = 'Danish' where language_id = 'da';
update imdb.language set language_name = 'Gujarati' where language_id = 'gu';
update imdb.language set language_name = 'Swedish' where language_id = 'sv';
update imdb.language set language_name = 'Georgian' where language_id = 'ka';
update imdb.language set language_name = 'Icelandic' where language_id = 'is';
update imdb.language set language_name = 'Byelorussian' where language_id = 'be';
update imdb.language set language_name = 'Turkish' where language_id = 'tr';
update imdb.language set language_name = 'Russian' where language_id = 'ru';
update imdb.language set language_name = 'Mandarin Chinese' where language_id = 'cmn';
update imdb.language set language_name = 'French' where language_id = 'fr';
update imdb.language set language_name = 'Malayalam' where language_id = 'ml';
update imdb.language set language_name = 'Thai' where language_id = 'th';
update imdb.language set language_name = 'Tajik' where language_id = 'tg';
update imdb.language set language_name = 'Norwegian' where language_id = 'no';
update imdb.language set language_name = 'Latvian, Lettish' where language_id = 'lv';
update imdb.language set language_name = 'Spanish' where language_id = 'es';
update imdb.language set language_name = 'Korean' where language_id = 'ko';
update imdb.language set language_name = 'Lithuanian' where language_id = 'lt';
update imdb.language set language_name = 'Serbian' where language_id = 'sr';
update imdb.language set language_name = 'Rhaeto-Romance' where language_id = 'rm';
update imdb.language set language_name = 'Galician' where language_id = 'gl';
update imdb.language set language_name = 'Dutch' where language_id = 'nl';
update imdb.language set language_name = 'Czech' where language_id = 'cs';
update imdb.language set language_name = 'Laothian' where language_id = 'lo';
update imdb.language set language_name = 'Kazakh' where language_id = 'kk';
update imdb.language set language_name = 'Japanese' where language_id = 'ja';
update imdb.language set language_name = 'Persian' where language_id = 'fa';
update imdb.language set language_name = 'Pashto, Pushto' where language_id = 'ps';
update imdb.language set language_name = 'Swiss German' where language_id = 'gsw';
update imdb.language set language_name = 'Tegulu' where language_id = 'te';
update imdb.language set language_name = 'Xhosa' where language_id = 'xh';
update imdb.language set language_name = 'Bengali, Bangla' where language_id = 'bn';
update imdb.language set language_name = 'Gaelic' where language_id = 'gd';
update imdb.language set language_name = 'Dari' where language_id = 'prs';
update imdb.language set language_name = 'Urdu' where language_id = 'ur';
update imdb.language set language_name = 'Greek' where language_id = 'el';
update imdb.language set language_name = 'Afrikaans' where language_id = 'af';
update imdb.language set language_name = 'Finnish' where language_id = 'fi';
update imdb.language set language_name = 'private usage' where language_id = 'qac';
update imdb.language set language_name = 'Kirghiz' where language_id = 'ky';
update imdb.language set language_name = 'Slovenian' where language_id = 'sl';
update imdb.language set language_name = 'Hindi' where language_id = 'hi';
update imdb.language set language_name = 'N''Ko' where language_id = 'nqo';
update imdb.language set language_name = 'Uzbek' where language_id = 'uz';
update imdb.language set language_name = 'German' where language_id = 'de';
update imdb.language set language_name = 'Hebrew' where language_id = 'he';
update imdb.language set language_name = 'Marathi' where language_id = 'mr';
update imdb.language set language_name = 'Polish' where language_id = 'pl';
update imdb.language set language_name = 'Arabic' where language_id = 'ar';
update imdb.language set language_name = 'Setswana' where language_id = 'tn';
update imdb.language set language_name = 'Cantonese' where language_id = 'yue';
update imdb.language set language_name = 'Bosnian' where language_id = 'bs';
update imdb.language set language_name = 'Irish' where language_id = 'ga';
update imdb.language set language_name = 'Maori' where language_id = 'mi';
update imdb.language set language_name = 'Amharic' where language_id = 'am';
update imdb.language set language_name = 'Portuguese' where language_id = 'pt';
update imdb.language set language_name = 'Ukrainian' where language_id = 'uk';
update imdb.language set language_name = 'Kirundi' where language_id = 'rn';
update imdb.language set language_name = 'Tagalog' where language_id = 'tl';
update imdb.language set language_name = 'English' where language_id = 'en';
update imdb.language set language_name = 'Indonesian' where language_id = 'id';
update imdb.language set language_name = 'private usage' where language_id = 'qal';
update imdb.language set language_name = 'Old French' where language_id = 'fro';
update imdb.language set language_name = 'Old High German' where language_id = 'goh';
update imdb.language set language_name = 'Bulgarian' where language_id = 'bg';
update imdb.language set language_name = 'private usage' where language_id = 'qbn';
update imdb.language set language_name = 'private usage' where language_id = 'qbo';
update imdb.language set language_name = 'Romanian' where language_id = 'ro';
update imdb.language set language_name = 'Vietnamese' where language_id = 'vi';
update imdb.language set language_name = 'Nepali' where language_id = 'ne';
update imdb.language set language_name = 'Slovak' where language_id = 'sk';

-- ------------------------------------------------

update imdb.region set region_name = 'United Kingdom' where region_id = 'GB';
update imdb.region set region_name = 'Bhutan' where region_id = 'BT';
update imdb.region set region_name = 'Iran' where region_id = 'IR';
update imdb.region set region_name = 'Cyprus' where region_id = 'CY';
update imdb.region set region_name = 'Benin' where region_id = 'BJ';
update imdb.region set region_name = 'Malta' where region_id = 'MT';
update imdb.region set region_name = 'Bermuda' where region_id = 'BM';
update imdb.region set region_name = 'Pakistan' where region_id = 'PK';
update imdb.region set region_name = 'Gambia' where region_id = 'GM';
update imdb.region set region_name = 'Belize' where region_id = 'BZ';
update imdb.region set region_name = 'United Arab Emirates' where region_id = 'AE';
update imdb.region set region_name = 'Madagascar' where region_id = 'MG';
update imdb.region set region_name = 'Peru' where region_id = 'PE';
update imdb.region set region_name = 'Moldova' where region_id = 'MD';
update imdb.region set region_name = 'Slovenia' where region_id = 'SI';
update imdb.region set region_name = 'unknonw' where region_id = 'XSA';
update imdb.region set region_name = 'Côte d''Ivoire' where region_id = 'CI';
update imdb.region set region_name = 'Rwanda' where region_id = 'RW';
update imdb.region set region_name = 'Canada' where region_id = 'CA';
update imdb.region set region_name = 'Congo' where region_id = 'CG';
update imdb.region set region_name = 'Bolivia' where region_id = 'BO';
update imdb.region set region_name = 'Kuwait' where region_id = 'KW';
update imdb.region set region_name = 'unknonw' where region_id = 'XWW';
update imdb.region set region_name = 'Syrian Arab Republic' where region_id = 'SY';
update imdb.region set region_name = 'Northern Mariana Islands' where region_id = 'MP';
update imdb.region set region_name = 'Equatorial Guinea' where region_id = 'GQ';
update imdb.region set region_name = 'Iceland' where region_id = 'IS';
update imdb.region set region_name = 'Switzerland' where region_id = 'CH';
update imdb.region set region_name = 'Republic of North Macedonia' where region_id = 'MK';
update imdb.region set region_name = 'Honduras' where region_id = 'HN';
update imdb.region set region_name = 'Guam' where region_id = 'GU';
update imdb.region set region_name = 'China' where region_id = 'CN';
update imdb.region set region_name = 'Eswatini' where region_id = 'SZ';
update imdb.region set region_name = 'Comoros' where region_id = 'KM';
update imdb.region set region_name = 'Palau' where region_id = 'PW';
update imdb.region set region_name = 'Uruguay' where region_id = 'UY';
update imdb.region set region_name = 'Morocco' where region_id = 'MA';
update imdb.region set region_name = 'Senegal' where region_id = 'SN';
update imdb.region set region_name = 'Croatia' where region_id = 'HR';
update imdb.region set region_name = 'Argentina' where region_id = 'AR';
update imdb.region set region_name = 'Niger' where region_id = 'NE';
update imdb.region set region_name = 'Niue' where region_id = 'NU';
update imdb.region set region_name = 'Zimbabwe' where region_id = 'ZW';
update imdb.region set region_name = 'Montserrat' where region_id = 'MS';
update imdb.region set region_name = 'United States of America' where region_id = 'US';
update imdb.region set region_name = 'unknown' where region_id = 'XKV';
update imdb.region set region_name = 'Vanuatu' where region_id = 'VU';
update imdb.region set region_name = 'Zambia' where region_id = 'ZM';
update imdb.region set region_name = 'Cayman Islands' where region_id = 'KY';
update imdb.region set region_name = 'Macao' where region_id = 'MO';
update imdb.region set region_name = 'Eritrea' where region_id = 'ER';
update imdb.region set region_name = 'Marshall Islands' where region_id = 'MH';
update imdb.region set region_name = 'Thailand' where region_id = 'TH';
update imdb.region set region_name = 'Sao Tome and Principe' where region_id = 'ST';
update imdb.region set region_name = 'Gabon' where region_id = 'GA';
update imdb.region set region_name = 'Austria' where region_id = 'AT';
update imdb.region set region_name = 'Djibouti' where region_id = 'DJ';
update imdb.region set region_name = 'Germany' where region_id = 'DE';
update imdb.region set region_name = 'Palestine' where region_id = 'PS';
update imdb.region set region_name = 'Venezuela' where region_id = 'VE';
update imdb.region set region_name = 'Finland' where region_id = 'FI';
update imdb.region set region_name = 'Japan' where region_id = 'JP';
update imdb.region set region_name = 'unknown' where region_id = 'SUHH';
update imdb.region set region_name = 'Hong Kong' where region_id = 'HK';
update imdb.region set region_name = 'Greenland' where region_id = 'GL';
update imdb.region set region_name = 'Seychelles' where region_id = 'SC';
update imdb.region set region_name = 'Nepal' where region_id = 'NP';
update imdb.region set region_name = 'American Samoa' where region_id = 'AS';
update imdb.region set region_name = 'Virgin Islands (U.K.)' where region_id = 'VG';
update imdb.region set region_name = 'unknown' where region_id = 'XAU';
update imdb.region set region_name = 'Tuvalu' where region_id = 'TV';
update imdb.region set region_name = 'Guatemala' where region_id = 'GT';
update imdb.region set region_name = 'unknown' where region_id = 'XSI';
update imdb.region set region_name = 'Myanmar' where region_id = 'MM';
update imdb.region set region_name = 'Solomon Islands' where region_id = 'SB';
update imdb.region set region_name = 'Guinea' where region_id = 'GN';
update imdb.region set region_name = 'Russian Federation' where region_id = 'RU';
update imdb.region set region_name = 'Tajikistan' where region_id = 'TJ';
update imdb.region set region_name = 'Algeria' where region_id = 'DZ';
update imdb.region set region_name = 'Cook Islands' where region_id = 'CK';
update imdb.region set region_name = 'Kiribati' where region_id = 'KI';
update imdb.region set region_name = 'Indonesia' where region_id = 'ID';
update imdb.region set region_name = 'North Korea' where region_id = 'KP';
update imdb.region set region_name = 'Grenada' where region_id = 'GD';
update imdb.region set region_name = 'Gibraltar' where region_id = 'GI';
update imdb.region set region_name = 'Bosnia and Herzegovina' where region_id = 'BA';
update imdb.region set region_name = 'Cuba' where region_id = 'CU';
update imdb.region set region_name = 'Sri Lanka' where region_id = 'LK';
update imdb.region set region_name = 'Wallis and Futuna' where region_id = 'WF';
update imdb.region set region_name = 'Bahamas' where region_id = 'BS';
update imdb.region set region_name = 'unknown' where region_id = 'XAS';
update imdb.region set region_name = 'Liechtenstein' where region_id = 'LI';
update imdb.region set region_name = 'Cabo Verde' where region_id = 'CV';
update imdb.region set region_name = 'Timor-Leste' where region_id = 'TL';
update imdb.region set region_name = 'unknown' where region_id = 'XWG';
update imdb.region set region_name = 'Kyrgyzstan' where region_id = 'KG';
update imdb.region set region_name = 'Trinidad and Tobago' where region_id = 'TT';
update imdb.region set region_name = 'Mozambique' where region_id = 'MZ';
update imdb.region set region_name = 'Saint Vincent and the Grenadines' where region_id = 'VC';
update imdb.region set region_name = 'Ethiopia' where region_id = 'ET';
update imdb.region set region_name = 'Bulgaria' where region_id = 'BG';
update imdb.region set region_name = 'Australia' where region_id = 'AU';
update imdb.region set region_name = 'Haiti' where region_id = 'HT';
update imdb.region set region_name = 'Papua New Guinea' where region_id = 'PG';
update imdb.region set region_name = 'Botswana' where region_id = 'BW';
update imdb.region set region_name = 'Ecuador' where region_id = 'EC';
update imdb.region set region_name = 'Monaco' where region_id = 'MC';
update imdb.region set region_name = 'Guinea-Bissau' where region_id = 'GW';
update imdb.region set region_name = 'Mali' where region_id = 'ML';
update imdb.region set region_name = 'South Korea' where region_id = 'KR';
update imdb.region set region_name = 'unknown' where region_id = 'VDVN';
update imdb.region set region_name = 'Oman' where region_id = 'OM';
update imdb.region set region_name = 'unknown' where region_id = 'ZRCD';
update imdb.region set region_name = 'Aruba' where region_id = 'AW';
update imdb.region set region_name = 'New Caledonia' where region_id = 'NC';
update imdb.region set region_name = 'Italy' where region_id = 'IT';
update imdb.region set region_name = 'unknown' where region_id = 'CSHH';
update imdb.region set region_name = 'Hungary' where region_id = 'HU';
update imdb.region set region_name = 'Spain' where region_id = 'ES';
update imdb.region set region_name = 'Israel' where region_id = 'IL';
update imdb.region set region_name = 'France' where region_id = 'FR';
update imdb.region set region_name = 'Namibia' where region_id = 'NA';
update imdb.region set region_name = 'unknown' where region_id = 'XNA';
update imdb.region set region_name = 'Somalia' where region_id = 'SO';
update imdb.region set region_name = 'Chile' where region_id = 'CL';
update imdb.region set region_name = 'Andorra' where region_id = 'AD';
update imdb.region set region_name = 'French Polynesia' where region_id = 'PF';
update imdb.region set region_name = 'Nicaragua' where region_id = 'NI';
update imdb.region set region_name = 'Sudan' where region_id = 'SD';
update imdb.region set region_name = 'Chad' where region_id = 'TD';
update imdb.region set region_name = 'Barbados' where region_id = 'BB';
update imdb.region set region_name = 'Portugal' where region_id = 'PT';
update imdb.region set region_name = 'unknown' where region_id = 'BUMM';
update imdb.region set region_name = 'Luxembourg' where region_id = 'LU';
update imdb.region set region_name = 'Singapore' where region_id = 'SG';
update imdb.region set region_name = 'Mauritius' where region_id = 'MU';
update imdb.region set region_name = 'Yemen' where region_id = 'YE';
update imdb.region set region_name = 'Dominica' where region_id = 'DM';
update imdb.region set region_name = 'Jersey' where region_id = 'JE';
update imdb.region set region_name = 'Colombia' where region_id = 'CO';
update imdb.region set region_name = 'Ghana' where region_id = 'GH';
update imdb.region set region_name = 'unknown' where region_id = 'XYU';
update imdb.region set region_name = 'Sierra Leone' where region_id = 'SL';
update imdb.region set region_name = 'Mexico' where region_id = 'MX';
update imdb.region set region_name = 'unknown' where region_id = 'XKO';
update imdb.region set region_name = 'Jamaica' where region_id = 'JM';
update imdb.region set region_name = 'Denmark' where region_id = 'DK';
update imdb.region set region_name = 'Costa Rica' where region_id = 'CR';
update imdb.region set region_name = 'Czechia' where region_id = 'CZ';
update imdb.region set region_name = 'Montenegro' where region_id = 'ME';
update imdb.region set region_name = 'Azerbaijan' where region_id = 'AZ';
update imdb.region set region_name = 'Cambodia' where region_id = 'KH';
update imdb.region set region_name = 'Armenia' where region_id = 'AM';
update imdb.region set region_name = 'Martinique' where region_id = 'MQ';
update imdb.region set region_name = 'Réunion' where region_id = 'RE';
update imdb.region set region_name = 'Norway' where region_id = 'NO';
update imdb.region set region_name = 'Qatar' where region_id = 'QA';
update imdb.region set region_name = 'Belgium' where region_id = 'BE';
update imdb.region set region_name = 'Angola' where region_id = 'AO';
update imdb.region set region_name = 'Egypt' where region_id = 'EG';
update imdb.region set region_name = 'Saint Kitts and Nevis' where region_id = 'KN';
update imdb.region set region_name = 'Nauru' where region_id = 'NR';
update imdb.region set region_name = 'Liberia' where region_id = 'LR';
update imdb.region set region_name = 'Romania' where region_id = 'RO';
update imdb.region set region_name = 'Saudi Arabia' where region_id = 'SA';
update imdb.region set region_name = 'Antarctica' where region_id = 'AQ';
update imdb.region set region_name = 'Afghanistan' where region_id = 'AF';
update imdb.region set region_name = 'Turkey' where region_id = 'TR';
update imdb.region set region_name = 'Mongolia' where region_id = 'MN';
update imdb.region set region_name = 'Jordan' where region_id = 'JO';
update imdb.region set region_name = 'Saint Lucia' where region_id = 'LC';
update imdb.region set region_name = 'Saint Helena' where region_id = 'SH';
update imdb.region set region_name = 'Georgia' where region_id = 'GE';
update imdb.region set region_name = 'Malaysia' where region_id = 'MY';
update imdb.region set region_name = 'Kazakhstan' where region_id = 'KZ';
update imdb.region set region_name = 'Brunei Darussalam' where region_id = 'BN';
update imdb.region set region_name = 'Taiwan' where region_id = 'TW';
update imdb.region set region_name = 'Samoa' where region_id = 'WS';
update imdb.region set region_name = 'Belarus' where region_id = 'BY';
update imdb.region set region_name = 'Western Sahara' where region_id = 'EH';
update imdb.region set region_name = 'Burkina Faso' where region_id = 'BF';
update imdb.region set region_name = 'Nigeria' where region_id = 'NG';
update imdb.region set region_name = 'El Salvador' where region_id = 'SV';
update imdb.region set region_name = 'Uganda' where region_id = 'UG';
update imdb.region set region_name = 'Burundi' where region_id = 'BI';
update imdb.region set region_name = 'Vatican' where region_id = 'VA';
update imdb.region set region_name = 'Suriname' where region_id = 'SR';
update imdb.region set region_name = 'Bahrain' where region_id = 'BH';
update imdb.region set region_name = 'Lithuania' where region_id = 'LT';
update imdb.region set region_name = 'Panama' where region_id = 'PA';
update imdb.region set region_name = 'Laos' where region_id = 'LA';
update imdb.region set region_name = 'Fiji' where region_id = 'FJ';
update imdb.region set region_name = 'Uzbekistan' where region_id = 'UZ';
update imdb.region set region_name = 'Serbia' where region_id = 'RS';
update imdb.region set region_name = 'unknown' where region_id = 'DDDE';
update imdb.region set region_name = 'Tunisia' where region_id = 'TN';
update imdb.region set region_name = 'Lebanon' where region_id = 'LB';
update imdb.region set region_name = 'Guyana' where region_id = 'GY';
update imdb.region set region_name = 'Philippines' where region_id = 'PH';
update imdb.region set region_name = 'Kenya' where region_id = 'KE';
update imdb.region set region_name = 'Guadeloupe' where region_id = 'GP';
update imdb.region set region_name = 'Congo Democratic Republic' where region_id = 'CD';
update imdb.region set region_name = 'Antigua and Barbuda' where region_id = 'AG';
update imdb.region set region_name = 'Malawi' where region_id = 'MW';
update imdb.region set region_name = 'French Guiana' where region_id = 'GF';
update imdb.region set region_name = 'Puerto Rico' where region_id = 'PR';
update imdb.region set region_name = 'Greece' where region_id = 'GR';
update imdb.region set region_name = 'Brazil' where region_id = 'BR';
update imdb.region set region_name = 'New Zealand' where region_id = 'NZ';
update imdb.region set region_name = 'Cameroon' where region_id = 'CM';
update imdb.region set region_name = 'Togo' where region_id = 'TG';
update imdb.region set region_name = 'Central African Republic' where region_id = 'CF';
update imdb.region set region_name = 'Anguilla' where region_id = 'AI';
update imdb.region set region_name = 'San Marino' where region_id = 'SM';
update imdb.region set region_name = 'unknown' where region_id = 'YUCS';
update imdb.region set region_name = 'unknown' where region_id = 'CSXX';
update imdb.region set region_name = 'Turkmenistan' where region_id = 'TM';
update imdb.region set region_name = 'India' where region_id = 'IN';
update imdb.region set region_name = 'Paraguay' where region_id = 'PY';
update imdb.region set region_name = 'Tanzania' where region_id = 'TZ';
update imdb.region set region_name = 'Libya' where region_id = 'LY';
update imdb.region set region_name = 'Dominican Republic' where region_id = 'DO';
update imdb.region set region_name = 'Poland' where region_id = 'PL';
update imdb.region set region_name = 'Faroe Islands' where region_id = 'FO';
update imdb.region set region_name = 'Albania' where region_id = 'AL';
update imdb.region set region_name = 'Iraq' where region_id = 'IQ';
update imdb.region set region_name = 'unknown' where region_id = 'XEU';
update imdb.region set region_name = 'Slovakia' where region_id = 'SK';
update imdb.region set region_name = 'Latvia' where region_id = 'LV';
update imdb.region set region_name = 'Virgin Islands (U.S.)' where region_id = 'VI';
update imdb.region set region_name = 'unknown' where region_id = 'XPI';
update imdb.region set region_name = 'Sweden' where region_id = 'SE';
update imdb.region set region_name = 'Tonga' where region_id = 'TO';
update imdb.region set region_name = 'Ukraine' where region_id = 'UA';
update imdb.region set region_name = 'Bangladesh' where region_id = 'BD';
update imdb.region set region_name = 'unknown' where region_id = 'AN';
update imdb.region set region_name = 'Netherlands' where region_id = 'NL';
update imdb.region set region_name = 'Viet Nam' where region_id = 'VN';
update imdb.region set region_name = 'Mauritania' where region_id = 'MR';
update imdb.region set region_name = 'South Africa' where region_id = 'ZA';
update imdb.region set region_name = 'Lesotho' where region_id = 'LS';
update imdb.region set region_name = 'Isle of Man' where region_id = 'IM';
update imdb.region set region_name = 'Ireland' where region_id = 'IE';
update imdb.region set region_name = 'Maldives' where region_id = 'MV';
update imdb.region set region_name = 'Estonia' where region_id = 'EE';

-- -------------------------------------------------

set schema 'IMDB';

select 'title_aka_title_type' as tbl, count(*) from title_aka_title_type
union
select 'title_principal' as tbl, count(*) from title_principal
union
select 'title_genre' as tbl, count(*) from title_genre
union
select 'title_aka' as tbl, count(*) from title_aka
union
select 'talent_title' as tbl, count(*) from talent_title
union
select 'talent_role' as tbl, count(*) from talent_role
union
select 'talent' as tbl, count(*) from talent
union
select 'title' as tbl, count(*) from title
union
select 'title_type' as tbl, count(*) from title_type
union
select 'role' as tbl, count(*) from role
union
select 'region' as tbl, count(*) from region
union
select 'language' as tbl, count(*) from language
union
select 'genre' as tbl, count(*) from genre
union
select 'content_type' as tbl, count(*) from content_type
union
select 'category' as tbl, count(*) from category
order by 1
;
