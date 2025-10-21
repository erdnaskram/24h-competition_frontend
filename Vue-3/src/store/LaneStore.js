// store/LaneStore.js
import { defineStore } from 'pinia';

export const useLaneStore = defineStore('lane', {
    state: () => ({
        laneCount: 5,
        lanes: [
            { id: 1, name: "Bahn 1" },
            { id: 2, name: "Bahn 2" },
            { id: 3, name: "Bahn 3" },
            { id: 4, name: "Bahn 4" },
            { id: 5, name: "Bahn 5" }
        ]
    }),
    actions: {
        setLaneCount(count) {
            this.laneCount = count;
            console.log("Lane count set to " + count);
        },
        setLanes(newLanes) {
            this.lanes = newLanes;
            this.setLaneCount(newLanes.length);
        },
        removeLane(id) {
            this.lanes = this.lanes.filter(lane => lane.id !== id);
            this.setLaneCount(this.lanes.length);
            console.log("Lane with ID " + id + " removed");
        },
        addLane(newLane) {
            const laneExists = this.lanes.some(lane => lane.id === newLane.id);
            if (laneExists) {
                console.error("Lane with ID " + newLane.id + " already exists");
                return;
            }
            this.lanes.push(newLane);
            this.setLaneCount(this.lanes.length);
            console.log("Lane added: ", newLane);
        }
    },
});