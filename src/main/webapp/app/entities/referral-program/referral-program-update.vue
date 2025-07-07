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
              v-text="t$('subscriptionManagementServiceApp.referralProgram.referralCode')"
              for="referral-program-referralCode"
            ></label>
            <input
              type="text"
              class="form-control"
              name="referralCode"
              id="referral-program-referralCode"
              data-cy="referralCode"
              :class="{ valid: !v$.referralCode.$invalid, invalid: v$.referralCode.$invalid }"
              v-model="v$.referralCode.$model"
              required
            />
            <div v-if="v$.referralCode.$anyDirty && v$.referralCode.$invalid">
              <small class="form-text text-danger" v-for="error of v$.referralCode.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              v-text="t$('subscriptionManagementServiceApp.referralProgram.startDttm')"
              for="referral-program-startDttm"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="referral-program-startDttm"
                  v-model="v$.startDttm.$model"
                  name="startDttm"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="referral-program-startDttm"
                data-cy="startDttm"
                type="text"
                class="form-control"
                name="startDttm"
                :class="{ valid: !v$.startDttm.$invalid, invalid: v$.startDttm.$invalid }"
                v-model="v$.startDttm.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.startDttm.$anyDirty && v$.startDttm.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startDttm.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.referralProgram.endDttm')"
              for="referral-program-endDttm"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="referral-program-endDttm"
                  v-model="v$.endDttm.$model"
                  name="endDttm"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="referral-program-endDttm"
                data-cy="endDttm"
                type="text"
                class="form-control"
                name="endDttm"
                :class="{ valid: !v$.endDttm.$invalid, invalid: v$.endDttm.$invalid }"
                v-model="v$.endDttm.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.endDttm.$anyDirty && v$.endDttm.$invalid">
              <small class="form-text text-danger" v-for="error of v$.endDttm.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
              v-text="t$('subscriptionManagementServiceApp.referralProgram.status')"
              for="referral-program-status"
            ></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="referral-program-status"
              data-cy="status"
              required
            >
              <option
                v-for="referralStatus in referralStatusValues"
                :key="referralStatus"
                :value="referralStatus"
                :label="t$('subscriptionManagementServiceApp.ReferralStatus.' + referralStatus)"
              >
                {{ referralStatus }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
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
