declare variable $maxCalories external;

for $recipe in db:get("smart_recipes")/recipes/recipe
where xs:decimal($recipe/nutrition/calories) <= xs:decimal($maxCalories)
order by xs:decimal($recipe/nutrition/calories)
return
    <recipe>
        <id>{data($recipe/@id)}</id>
        <name>{data($recipe/name)}</name>
        <calories>{data($recipe/nutrition/calories)}</calories>
    </recipe>