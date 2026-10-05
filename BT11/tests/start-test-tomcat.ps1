param(
    [string]$TomcatHome = $env:CATALINA_HOME,
    [string]$JavaHome = $env:JAVA_HOME
)
$ErrorActionPreference = 'Stop'
if (!$TomcatHome -or !$JavaHome) { throw 'Set -TomcatHome and -JavaHome, or CATALINA_HOME/JAVA_HOME.' }
$projectPath = Split-Path $PSScriptRoot -Parent
$testBase = Join-Path $projectPath '.runtime/tomcat-test'
$warPath = Join-Path $projectPath 'target/GK_laptrinh_web_24133049.war'
if (!(Test-Path -LiteralPath $warPath -PathType Leaf)) { throw 'WAR missing. Run mvn package first.' }
$pidPath = Join-Path $testBase 'tomcat.pid'
if (Test-Path -LiteralPath $pidPath) {
    $savedProcessId = [int](Get-Content -LiteralPath $pidPath)
    if (Get-Process -Id $savedProcessId -ErrorAction SilentlyContinue) {
        throw 'Test Tomcat is already running. Use ./tests/stop-test-tomcat.ps1 before restarting.'
    }
}
foreach ($port in @(18080,18005)) {
    if (Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue) {
        throw "Port $port is in use. Stop the existing test Tomcat before starting."
    }
}
New-Item -ItemType Directory -Force -Path $testBase | Out-Null
foreach ($folder in @('conf','logs','temp','webapps','work')) {
    New-Item -ItemType Directory -Force -Path (Join-Path $testBase $folder) | Out-Null
}
Copy-Item -Path (Join-Path $TomcatHome 'conf/*') -Destination (Join-Path $testBase 'conf') -Recurse -Force
$serverPath = Join-Path $testBase 'conf/server.xml'
$server = [IO.File]::ReadAllText($serverPath).Replace('port="8080"','port="18080"').Replace('port="8005"','port="18005"').Replace('port="8009"','port="18009"')
[IO.File]::WriteAllText($serverPath,$server,[Text.UTF8Encoding]::new($false))
Copy-Item -LiteralPath $warPath -Destination (Join-Path $testBase 'webapps/GK_laptrinh_web_24133049.war') -Force
$javaArgs = @(
    "-Dcatalina.home=$TomcatHome", "-Dcatalina.base=$testBase", "-Djava.library.path=$TomcatHome/bin",
    "-Djava.io.tmpdir=$testBase/temp", '--enable-native-access=ALL-UNNAMED',
    '-classpath', "$TomcatHome/bin/bootstrap.jar;$TomcatHome/bin/tomcat-juli.jar",
    'org.apache.catalina.startup.Bootstrap','start'
)
$javaArgs = $javaArgs | ForEach-Object { '"' + $_ + '"' }
$proc = Start-Process -FilePath (Join-Path $JavaHome 'bin/java.exe') -ArgumentList $javaArgs -WindowStyle Hidden -PassThru `
    -RedirectStandardOutput (Join-Path $testBase 'logs/stdout.log') -RedirectStandardError (Join-Path $testBase 'logs/stderr.log')
$proc.Id | Set-Content (Join-Path $testBase 'tomcat.pid')
Write-Output "Test Tomcat PID=$($proc.Id): http://localhost:18080/GK_laptrinh_web_24133049/home"
