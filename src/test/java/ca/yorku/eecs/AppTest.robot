*** Settings ***
Library           Collections
Library           RequestsLibrary
Test Timeout      30 seconds

*** Test Cases ***
addActorPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		actorId=nm0000001	name=Matt Damon
	${resp}=    Put Request    localhost    /addActor    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200

addActorFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		actorId=nm0000007	
	${resp}=    Put Request    localhost    /addActor    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    400

addMoviePass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		movieId=nm1111110	name=Avatar
	${resp}=    Put Request    localhost    /addMovie    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200

addMovieFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		movieId=nm1111110	
	${resp}=    Put Request    localhost    /addMovie    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    400

addRelationshipPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		movieId=nm1111110	actorId=nm0000001
	${resp}=    Put Request    localhost    /addRelationship    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200

addRelationshipFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		movieId=nm1111110	actorId=nm0000001
	${resp}=    Put Request    localhost    /addRelationship    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    400

hasRelationshipPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		movieId=nm1111110	actorId=nm0000001
	${resp}=    Get Request    localhost    /hasRelationship    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200
	Dictionary Should Contain Value    ${resp.json()}    ${true}

hasRelationshipFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		movieId=nm1111110	actorId=nm0000
	${resp}=    Get Request    localhost    /hasRelationship    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    404

getActorPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary		actorId=nm0000001
    ${resp}=    Get Request    localhost    /getActor    json=${params}    headers=${headers}    
	Should Be Equal As Strings    ${resp.status_code}    200
	Dictionary Should Contain Value    ${resp.json()}    Matt Damon
	
getActorFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		actorId=0000102
	${resp}=    Get Request    localhost    /getActor    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    404
		
getMoviePass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary		movieId=nm1111110
    ${resp}=    Get Request    localhost    /getMovie    json=${params}    headers=${headers}    
	Should Be Equal As Strings    ${resp.status_code}    200
	Dictionary Should Contain Value    ${resp.json()}    Avatar
	
getMovieFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		movieId=0000102
	${resp}=    Get Request    localhost    /getMovie    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    404
	
computeBaconNumberPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		actorId=nm0000102
	${resp}=    Get Request    localhost    /computeBaconNumber    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200
	Dictionary Should Contain Value    ${resp.json()}    ${0}
	
computeBaconNumberFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		actorI=nm0000102
	${resp}=    Get Request    localhost    /computeBaconNumber    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    400	
	
computeBaconPathPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		actorId=nm0000102
	${resp}=    Get Request    localhost    /computeBaconPath    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200

computeBaconPathFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${params}=    Create Dictionary		actorI=nm0000102
	${resp}=    Get Request    localhost    /computeBaconPath    json=${params}    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    400	

getRatingsPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${resp}=    Get Request    localhost    /ratings    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200

getRatingsFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${resp}=    Get Request    localhost    /rating    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    400

getRatingsInRangePass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${resp}=	Get Request    localhost    /ratings/3    headers=${headers}    
	Should Be Equal As Strings    ${resp.status_code}    200

getRatingsInRangeFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${resp}=	Get Request    localhost    /ratings/6    headers=${headers}    
	Should Be Equal As Strings    ${resp.status_code}    400	
	
getActorsByMovieRatingsPass
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${resp}=    Get Request    localhost    /actorsByMovieRatings    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    200

getActorsByMovieRatingsFail
	Create Session    localhost    http://localhost:8080/api/v1
	${headers}=    Create Dictionary    Content-Type=application/json
	${resp}=    Get Request    localhost    /actorsByMoveRatings    headers=${headers}
	Should Be Equal As Strings    ${resp.status_code}    400

	
