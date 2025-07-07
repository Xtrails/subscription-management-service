<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.subscriptionDetails.home.createOrEditLabel"
          data-cy="SubscriptionDetailsCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="subscriptionDetails.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="subscriptionDetails.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.name')"
              for="subscription-details-name"
            ></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="subscription-details-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.description')"
              for="subscription-details-description"
            ></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="subscription-details-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.price')"
              for="subscription-details-price"
            ></label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="subscription-details-price"
              data-cy="price"
              :class="{ valid: !v$.price.$invalid, invalid: v$.price.$invalid }"
              v-model.number="v$.price.$model"
              required
            />
            <div v-if="v$.price.$anyDirty && v$.price.$invalid">
              <small class="form-text text-danger" v-for="error of v$.price.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.duration')"
              for="subscription-details-duration"
            ></label>
            <input
              type="number"
              class="form-control"
              name="duration"
              id="subscription-details-duration"
              data-cy="duration"
              :class="{ valid: !v$.duration.$invalid, invalid: v$.duration.$invalid }"
              v-model.number="v$.duration.$model"
              required
            />
            <div v-if="v$.duration.$anyDirty && v$.duration.$invalid">
              <small class="form-text text-danger" v-for="error of v$.duration.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.sourceApplication')"
              for="subscription-details-sourceApplication"
            ></label>
            <select
              class="form-control"
              id="subscription-details-sourceApplication"
              data-cy="sourceApplication"
              name="sourceApplication"
              v-model="subscriptionDetails.sourceApplication"
            >
              <option :value="null"></option>
              <option
                :value="
                  subscriptionDetails.sourceApplication && sourceApplicationOption.id === subscriptionDetails.sourceApplication.id
                    ? subscriptionDetails.sourceApplication
                    : sourceApplicationOption
                "
                v-for="sourceApplicationOption in sourceApplications"
                :key="sourceApplicationOption.id"
              >
                {{ sourceApplicationOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./subscription-details-update.component.ts"></script>
