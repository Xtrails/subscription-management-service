<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.referralProgram.home.createOrEditLabel"
          data-cy="ReferralProgramCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.referralProgram.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="referralProgram.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="referralProgram.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.referralProgram.name')"
              for="referral-program-name"
            ></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="referral-program-name"
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
              v-text="t$('subscriptionManagementServiceApp.referralProgram.description')"
              for="referral-program-description"
            ></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="referral-program-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.referralProgram.rewardAmount')"
              for="referral-program-rewardAmount"
            ></label>
            <input
              type="number"
              class="form-control"
              name="rewardAmount"
              id="referral-program-rewardAmount"
              data-cy="rewardAmount"
              :class="{ valid: !v$.rewardAmount.$invalid, invalid: v$.rewardAmount.$invalid }"
              v-model.number="v$.rewardAmount.$model"
              required
            />
            <div v-if="v$.rewardAmount.$anyDirty && v$.rewardAmount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.rewardAmount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.referralProgram.sourceApplication')"
              for="referral-program-sourceApplication"
            ></label>
            <select
              class="form-control"
              id="referral-program-sourceApplication"
              data-cy="sourceApplication"
              name="sourceApplication"
              v-model="referralProgram.sourceApplication"
            >
              <option :value="null"></option>
              <option
                :value="
                  referralProgram.sourceApplication && sourceApplicationOption.id === referralProgram.sourceApplication.id
                    ? referralProgram.sourceApplication
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
<script lang="ts" src="./referral-program-update.component.ts"></script>
