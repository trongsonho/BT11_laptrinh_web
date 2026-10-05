# ASCII source for Windows PowerShell 5.1 compatibility.
$ErrorActionPreference = 'Stop'
$projectPath = Split-Path $PSScriptRoot -Parent
$testBases = @(
    [IO.Path]::GetFullPath((Join-Path $projectPath '.runtime/tomcat-test')),
    [IO.Path]::GetFullPath((Join-Path $projectPath 'target/tomcat-test'))
)
$stopped = 0
# Inspect command lines even if mvn clean already removed the PID file.
$javaProcesses = Get-CimInstance Win32_Process -Filter "Name='java.exe'"
foreach ($process in $javaProcesses) {
    foreach ($testBase in $testBases) {
        $basePattern = '(?:^|\s)"?-Dcatalina\.base=' + [regex]::Escape($testBase) + '(?:"|\s|$)'
        if ($process.CommandLine -match $basePattern -and $process.CommandLine -match 'org\.apache\.catalina\.startup\.Bootstrap') {
            Stop-Process -Id $process.ProcessId -ErrorAction Stop
            Wait-Process -Id $process.ProcessId -Timeout 20 -ErrorAction SilentlyContinue
            Write-Output "Stopped test Tomcat PID=$($process.ProcessId)."
            $stopped++
            break
        }
    }
}
foreach ($testBase in $testBases) {
    $pidPath = Join-Path $testBase 'tomcat.pid'
    if (Test-Path -LiteralPath $pidPath) { Remove-Item -LiteralPath $pidPath -Force }
}
if ($stopped -eq 0) { Write-Output 'No project test Tomcat is running.' }
