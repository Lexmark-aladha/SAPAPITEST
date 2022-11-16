Param(
    [CmdletBinding()]
    [Parameter(Mandatory = $True)]
    [string] $recipients,

    [Parameter(Mandatory = $True)]
    [string] $Attachment,

    [Parameter(Mandatory = $True)]
    [string] $Subject,
    
    [Parameter(Mandatory = $True)]
    [string] $RefreshToken,
    
    [Parameter(Mandatory = $True)]
    [string] $ClientSecret,

    [Parameter(Mandatory = $True)]
    [string] $ClientId
)
#$recipients = "arvind.gupta@lexmark.com"
#$Attachment = "$(Build.ArtifactStagingDirectory)" + "\WebJobPractice.deploy-readme.txt"
function Send-EmailWithMSGraphAPI {
  Param(
    [Parameter(Mandatory = $True)]
    [String] $recipients,
    [Parameter(Mandatory = $True)]
    [String] $subject,
    [Parameter(Mandatory = $True)]
    [String] $content,
    [Parameter(Mandatory = $True)]
    [String] $accessToken,
    [Parameter(Mandatory = $True)]
    [String] $attachment
  )

  Write-Output "Entered Send-EmailWithMSGraphAPI Function"
  #Get File Name and Base64 string
  $FileName = (Get-Item -Path $attachment).name
  $base64string = [Convert]::ToBase64String([IO.File]::ReadAllBytes($attachment))

  
  $EmailRecipients = $recipients.Split(";") # for multiple addresses, delimit with a ";" Example "email1@lexmark.com;email2@lexmark.com"
  $recipList = @()

  # Creating the needed hash table for single or multiple recipients
  foreach ($recip in $EmailRecipients) {
    $emailAddress = @{ "address" = $recip }
    $hash = @{
      "emailAddress" = $emailAddress
    }
    
    $recipList += $hash
  }
      
  #Create message body and properties and send
  $MessageParams = @{
    "URI"         = "https://graph.microsoft.com/v1.0/users/anup.ladha@lexmark.com/sendMail"
    "Headers"     = @{Authorization = "Bearer $accessToken" }
    "Method"      = "POST"
    "ContentType" = 'application/json'
    "Body"        = (@{
        "message" = @{
          "subject"      = $subject
          "body"         = @{
            "contentType" = 'HTML' 
            "content"     = $content
          }

          "attachments"  = @(
            @{
              "@odata.type"  = "#microsoft.graph.fileAttachment"
              "name"         = $FileName
              "contentType"  = "text/plain"
              "contentBytes" = $base64string
            
            })
           
          "toRecipients" = $recipList
          
          
                 
        }
      }) | ConvertTo-JSON -Depth 6
  }   # Send the message
  Invoke-RestMethod @Messageparams
  Write-Output "Exited Send-EmailWithMSGraphAPI Function"
}
# Function used to update auth token using refresh token - used to send SMTP messages
function Get-MSGraphAccessToken {
  param (
      [Parameter(Mandatory = $True)] [string] $refreshToken,
      [Parameter(Mandatory = $True)] [string] $ClientSecret,
      [Parameter(Mandatory = $True)] [string] $clientId
  )
  Write-Output "Entered Update-MSGraphAccessToken"
  $tenant = "LexmarkAD.onmicrosoft.com"   
  $refreshTokenParams = @{ 
      grant_type    = "refresh_token"
      client_id     = $clientId
      refresh_token = $refreshToken
      client_secret = $ClientSecret
  }

  $tokenResponse = Invoke-RestMethod -Method POST -Uri "https://login.microsoftonline.com/$tenant/oauth2/token" -Body $refreshTokenParams
  
  return $tokenResponse
}

$htmlBody = "<html>
<body>
<p>Hello Team,<br>
<br>
Fiori Test Result Generated. PFA the Test Results.<br>
<br>
Regards,<br>
Test Automation Team<br>
anup.ladha@Lexmark.com</p>
</body>
</html>
"

$tokens = Get-MSGraphAccessToken -refreshToken $RefreshToken -ClientSecret $ClientSecret -clientId $ClientId

$MailParams = @{
  recipients  = $recipients
  subject     = $Subject
  content     = $htmlBody
  accessToken = $tokens.access_token
  attachment  = $Attachment
}
try {
  Write-Output "Sending Email...."
  Send-EmailWithMSGraphAPI @MailParams
  Write-Output "Email Sent Successfully...."
}
catch {
  Write-Output "Email Did not sent. An error occurred"
  Write-Output $_
}