<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.referral.home.createOrEditLabel"
          data-cy="ReferralCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.referral.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="referral.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="referral.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.referral.referralCode')"
              for="referral-referralCode"
            ></label>
            <input
              type="text"
              class="form-control"
              name="referralCode"
              id="referral-referralCode"
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
            <label class="form-control-label" v-text="t$('subscriptionManagementServiceApp.referral.status')" for="referral-status"></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="referral-status"
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
              v-text="t$('subscriptionManagementServiceApp.referral.referrer')"
              for="referral-referrer"
            ></label>
            <select class="form-control" id="referral-referrer" data-cy="referrer" name="referrer" v-model="referral.referrer">
              <option :value="null"></option>
              <option
                :value="referral.referrer && externalUserOption.id === referral.referrer.id ? referral.referrer : externalUserOption"
                v-for="externalUserOption in externalUsers"
                :key="externalUserOption.id"
              >
                {{ externalUserOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.referral.referralProgram')"
              for="referral-referralProgram"
            ></label>
            <select
              class="form-control"
              id="referral-referralProgram"
              data-cy="referralProgram"
              name="referralProgram"
              v-model="referral.referralProgram"
            >
              <option :value="null"></option>
              <option
                :value="
                  referral.referralProgram && referralProgramOption.id === referral.referralProgram.id
                    ? referral.referralProgram
                    : referralProgramOption
                "
                v-for="referralProgramOption in referralPrograms"
                :key="referralProgramOption.id"
              >
                {{ referralProgramOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.referral.sourceApplication')"
              for="referral-sourceApplication"
            ></label>
            <select
              class="form-control"
              id="referral-sourceApplication"
              data-cy="sourceApplication"
              name="sourceApplication"
              v-model="referral.sourceApplication"
            >
              <option :value="null"></option>
              <option
                :value="
                  referral.sourceApplication && sourceApplicationOption.id === referral.sourceApplication.id
                    ? referral.sourceApplication
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
<script lang="ts" src="./referral-update.component.ts"></script>
