<template>
  <main>
    <div>
      <h1> Welcome to the Event Registration Planner! </h1>
      <div>
        <h3> Login </h3>
        <input type="text" placeholder="Name" v-model="inputtedName">
        <input type="checkbox" id="checkboxtest" v-model="inputtedManager"> Event Manager
        <br>
        <br>
        <button @click="login(inputtedName)"> Login </button>
        <button @click="createNewUser(inputtedName, inputtedManager)"> Register </button>
        <span> {{ errorMessage }} </span>
      </div>
    </div>
    <div>
      <h2> Register for upcoming events: </h2>
      <table key: eventTest>
        <tr>
          <th> Event Name </th>
          <th> Description </th>
          <th> Location </th>
          <th> Year </th>
          <th> Month </th>
          <th> Day </th>
        </tr>
        <tr v-for="event in events">
          <td> {{ event.name }} </td>
          <td> {{ event.description }} </td>
          <td> {{ event.location }} </td>
          <td> {{ event.year }} </td>
          <td> {{ event.month }} </td>
          <td> {{ event.day }} </td>
          <td> <button> Register </button> </td>
        </tr>
      </table>

    </div>
  </main>
</template>

<script lang="js">
import ref from 'vue';
import axios from 'axios';


async function getAllEvents() {
    let rootUrl = 'http://localhost:8080'
    let response = await axios.get(rootUrl + '/plannedEvent/').then(response => {
        return response.data;
    }).catch(error => {
        console.log(error);
    });
    console.log(response);
    if (response == null) {
        return [];
    }
    return response;
}

let eventsList = getAllEvents();
export default {
  components: {
  },
  data() {
    return {
      events: eventsList,
      inputtedName: inputtedName,
      inputtedManager: inputtedManager,
      errorMessage: "",
      currentUser: null
    }
  },
  methods: {
    createNewUser: async function(inputtedName, inputtedManager) {
      let rootUrl = 'http://localhost:8080'
      let response;
      if (inputtedManager == false) {
        // Create General User
        response = await axios.post(rootUrl + '/generalUser/', {
        name: inputtedName
      }).then(response => {
        return response.data;
      }).catch(error => {
        console.log(error.message);
        this.errorMessage = "General User already exists";
      });
      console.log(response);
      } else {
        // Create Event Manager
        response = await axios.post(rootUrl + '/eventManager/', {
        name: inputtedName
      }).then(response => {
        return response.data;
      }).catch(error => {
        console.log(error.message);
        this.errorMessage = "Event Manager already exists";
      });
    }
   },
   login: async function(inputtedName) {
    let rootUrl = 'http://localhost:8080'
    let response = await axios.get(rootUrl + '/generalUser/' + inputtedName).then(response => {
      return response.data;
    }).catch(error => {
      console.log(error.message);
      errorMessage = "User does not exist";
    });
    console.log(response);
    if (response == null) {
      response = await axios.get(rootUrl + '/eventManager/' + inputtedName).then(response => {
        return response.data;
      }).catch(error => {
        console.log(error.message);
        this.errorMessage = "User does not exist";
      });
      }
    console.log(response);
    if (response == null) {
      this.errorMessage = "User does not exist";
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
