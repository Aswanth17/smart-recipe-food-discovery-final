param(
    [Parameter(Mandatory = $false)]
    [string]$XmlFile = ".\recipes.xml",

    [Parameter(Mandatory = $false)]
    [string]$XsdFile = ".\recipe.xsd"
)

$ErrorActionPreference = "Stop"

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " SMART RECIPE XML / XSD VALIDATOR" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

$xmlPath = Resolve-Path $XmlFile
$xsdPath = Resolve-Path $XsdFile

Write-Host "XML File :" $xmlPath
Write-Host "XSD File :" $xsdPath
Write-Host ""

$validationErrors = New-Object System.Collections.Generic.List[string]

$settings = New-Object System.Xml.XmlReaderSettings

$settings.ValidationType = [System.Xml.ValidationType]::Schema

$settings.Schemas.Add(
    $null,
    $xsdPath.Path
) | Out-Null

$settings.ValidationFlags =
    [System.Xml.Schema.XmlSchemaValidationFlags]::ReportValidationWarnings

$validationHandler = {
    param($sender, $eventArgs)

    $message = $eventArgs.Message

    $validationErrors.Add($message)

    Write-Host "VALIDATION ERROR:" -ForegroundColor Red
    Write-Host $message -ForegroundColor Red
    Write-Host ""
}

$settings.add_ValidationEventHandler($validationHandler)

try {

    $reader = [System.Xml.XmlReader]::Create(
        $xmlPath.Path,
        $settings
    )

    while ($reader.Read()) {
        # Reading the document triggers XSD validation.
    }

    $reader.Close()

}
catch {

    $validationErrors.Add($_.Exception.Message)

    Write-Host "XML PROCESSING ERROR:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
}

if ($validationErrors.Count -eq 0) {

    Write-Host "==========================================" -ForegroundColor Green
    Write-Host " VALIDATION SUCCESSFUL" -ForegroundColor Green
    Write-Host " XML conforms to the XSD schema." -ForegroundColor Green
    Write-Host "==========================================" -ForegroundColor Green

    exit 0

}
else {

    Write-Host "==========================================" -ForegroundColor Red
    Write-Host " VALIDATION FAILED" -ForegroundColor Red
    Write-Host " Errors found: $($validationErrors.Count)" -ForegroundColor Red
    Write-Host "==========================================" -ForegroundColor Red

    exit 1

}