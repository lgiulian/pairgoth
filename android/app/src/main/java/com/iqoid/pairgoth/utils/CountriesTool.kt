package com.iqoid.pairgoth.utils

class CountriesTool {

    public fun getCountries() = countries.entries.sortedBy { it.value }
    public fun getCountry(code: String) = countries[code]

    companion object {
        public val countries = mapOf(
            "ad" to "Andorra",
            "ae" to "United Arab Emirates",
            "af" to "Afghanistan",
            "ai" to "Anguilla",
            "al" to "Albania",
            "am" to "Armenia",
            "ao" to "Angola",
            "ar" to "Argentina",
            "at" to "Austria",
            "au" to "Australia",
            "aw" to "Aruba",
            "az" to "Azerbaijan",
            "ba" to "Bosnia and Herzegovina",
            "bb" to "Barbados",
            "bd" to "Bangladesh",
            "be" to "Belgium",
            "bf" to "Burkina Faso",
            "bg" to "Bulgaria",
            "bh" to "Bahrain",
            "bi" to "Burundi",
            "bj" to "Benin",
            "bl" to "Saint Barthélemy",
            "bm" to "Bermuda",
            "bn" to "Brunei",
            "bo" to "Bolivia",
            "br" to "Brazil",
            "bs" to "Bahamas",
            "bt" to "Bhutan",
            "bw" to "Botswana",
            "by" to "Belarus",
            "bz" to "Belize",
            "ca" to "Canada",
            "cd" to "Dem. Rep. of the Congo",
            "cf" to "Central African Republic",
            "cg" to "Congo",
            "ch" to "Switzerland",
            "ci" to "Ivory Coast",
            "cl" to "Chile",
            "cm" to "Cameroon",
            "cn" to "China",
            "co" to "Colombia",
            "cr" to "Costa Rica",
            "cu" to "Cuba",
            "cv" to "Cabo Verde",
            "cw" to "Curaçao",
            "cy" to "Cyprus",
            "cz" to "Czechia",
            "de" to "Germany",
            "dj" to "Djibouti",
            "dk" to "Denmark",
            "dm" to "Dominica",
            "do" to "Dominican Republic",
            "dz" to "Algeria",
            "ec" to "Ecuador",
            "ee" to "Estonia",
            "eg" to "Egypt",
            "eh" to "Western Sahara*",
            "er" to "Eritrea",
            "es" to "Spain",
            "et" to "Ethiopia",
            "fi" to "Finland",
            "fj" to "Fiji",
            "fr" to "France",
            "ga" to "Gabon",
            "gb" to "United Kingdom",
            "gd" to "Grenada",
            "ge" to "Georgia",
            "gg" to "Guernsey",
            "gh" to "Ghana",
            "gi" to "Gibraltar",
            "gl" to "Greenland",
            "gm" to "Gambia",
            "gn" to "Guinea",
            "gq" to "Equatorial Guinea",
            "gr" to "Greece",
            "gt" to "Guatemala",
            "gu" to "Guam",
            "gw" to "Guinea-Bissau",
            "gy" to "Guyana",
            "hk" to "Hong Kong",
            "hn" to "Honduras",
            "hr" to "Croatia",
            "ht" to "Haiti",
            "hu" to "Hungary",
            "id" to "Indonesia",
            "ie" to "Ireland",
            "il" to "Israel",
            "im" to "Isle of Man",
            "in" to "India",
            "iq" to "Iraq",
            "ir" to "Iran",
            "is" to "Iceland",
            "it" to "Italy",
            "je" to "Jersey",
            "jm" to "Jamaica",
            "jo" to "Jordan",
            "jp" to "Japan",
            "ke" to "Kenya",
            "kg" to "Kyrgyzstan",
            "kh" to "Cambodia",
            "ki" to "Kiribati",
            "km" to "Comoros",
            "kp" to "North Korea",
            "kr" to "South Korea",
            "kw" to "Kuwait",
            "kz" to "Kazakhstan",
            "la" to "Lao People's Dem. Rep.",
            "lb" to "Lebanon",
            "li" to "Liechtenstein",
            "lk" to "Sri Lanka",
            "lr" to "Liberia",
            "ls" to "Lesotho",
            "lt" to "Lithuania",
            "lu" to "Luxembourg",
            "lv" to "Latvia",
            "ly" to "Libya",
            "ma" to "Morocco",
            "mc" to "Monaco",
            "md" to "Moldova",
            "me" to "Montenegro",
            "mg" to "Madagascar",
            "mk" to "Macedonia",
            "ml" to "Mali",
            "mm" to "Myanmar",
            "mn" to "Mongolia",
            "mo" to "Macao",
            "mq" to "Martinique",
            "mr" to "Mauritania",
            "ms" to "Montserrat",
            "mt" to "Malta",
            "mu" to "Mauritius",
            "mv" to "Maldives",
            "mw" to "Malawi",
            "mx" to "Mexico",
            "my" to "Malaysia",
            "mz" to "Mozambique",
            "na" to "Namibia",
            "nc" to "New Caledonia",
            "ne" to "Niger",
            "ng" to "Nigeria",
            "ni" to "Nicaragua",
            "nl" to "The Netherlands",
            "no" to "Norway",
            "np" to "Nepal",
            "nr" to "Nauru",
            "nu" to "Niue",
            "nz" to "New Zealand",
            "om" to "Oman",
            "pa" to "Panama",
            "pe" to "Peru",
            "pf" to "French Polynesia",
            "pg" to "Papua New Guinea",
            "ph" to "The Philippines",
            "pk" to "Pakistan",
            "pl" to "Poland",
            "pn" to "Pitcairn",
            "pr" to "Puerto Rico",
            "pt" to "Portugal",
            "pw" to "Palau",
            "py" to "Paraguay",
            "qa" to "Qatar",
            "re" to "Réunion",
            "ro" to "Romania",
            "rs" to "Serbia",
            "ru" to "Russia",
            "rw" to "Rwanda",
            "sa" to "Saudi Arabia",
            "sc" to "Seychelles",
            "sd" to "Sudan",
            "se" to "Sweden",
            "sg" to "Singapore",
            "si" to "Slovenia",
            "sk" to "Slovakia",
            "sl" to "Sierra Leone",
            "sm" to "San Marino",
            "sn" to "Senegal",
            "so" to "Somalia",
            "sr" to "Suriname",
            "ss" to "South Sudan",
            "sv" to "El Salvador",
            "sy" to "Syrian Arab Republic",
            "sz" to "Swaziland",
            "td" to "Chad",
            "tg" to "Togo",
            "th" to "Thailand",
            "tj" to "Tajikistan",
            "tm" to "Turkmenistan",
            "tn" to "Tunisia",
            "to" to "Tonga",
            "tr" to "Turkey",
            "tv" to "Tuvalu",
            "tw" to "Taiwan",
            "tz" to "Tanzania",
            "ua" to "Ukraine",
            "ug" to "Uganda",
            "us" to "United States of America",
            "uy" to "Uruguay",
            "uz" to "Uzbekistan",
            "ve" to "Venezuela",
            "vn" to "Viet Nam",
            "vu" to "Vanuatu",
            "ws" to "Samoa",
            "xk" to "Kosovo",
            "ye" to "Yemen",
            "yt" to "Mayotte",
            "za" to "South Africa",
            "zm" to "Zambia",
            "zw" to "Zimbabwe"
        )
    }
}