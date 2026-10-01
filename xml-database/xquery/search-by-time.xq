declare variable $maxTime external;

for $recipe in db:get("smart_recipes")/recipes/recipe
where xs:integer($recipe/cookingTime) <= xs:integer($maxTime)
order by xs:integer($recipe/cookingTime)
return
    <recipe>
        <id>{data($recipe/@id)}</id>
        <name>{data($recipe/name)}</name>
        <cookingTime>{data($recipe/cookingTime)}</cookingTime>
    </recipe>