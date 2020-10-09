Function Get-JavaServerId ($port) 
{
    (
        Get-Process -Id (Get-NetTCPConnection -LocalPort $port).OwningProcess | Select Id
    ).Id
}

Stop-Process -Id (Get-JavaServerId(8080))
