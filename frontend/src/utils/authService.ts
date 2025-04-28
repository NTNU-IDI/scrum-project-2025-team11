export async function getJwtTokenFromLogin(username: string, password: string) {
    try {
        //TODO: Change fetch address and input in body when backend is ready.
        const response = await fetch("http://localhost:8888/????", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
        const data = await response.json();
        return data.token;
    } catch (error) {
        console.log('Error: ' + error);
        return null
    }
}

export async function getJwtTokenAfterRegistration(username: string, email:string, password: string, passwordAgain: string) {
    try {
        //TODO: Change fetch address and input in body when backend is ready.
        const response = await fetch("http://localhost:8080/????", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password,
                passwordAgain: passwordAgain
            })
        })
        const data = await response.json();
        return data.token;
    } catch (error) {
        console.log('Error: ' + error);
        return null
    }
}