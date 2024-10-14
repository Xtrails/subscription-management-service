<template>
  <div>
    <h2 id="page-heading" data-cy="ExternalUserHeading">
      <span v-text="t$('subscriptionManagementServiceApp.externalUser.home.title')" id="external-user-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.externalUser.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ExternalUserCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-external-user"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.externalUser.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && externalUsers && externalUsers.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.externalUser.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="externalUsers && externalUsers.length > 0">
      <table class="table table-striped" aria-describedby="externalUsers">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.externalUser.externalUserId')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.externalUser.sourceApplication')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="externalUser in externalUsers" :key="externalUser.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: externalUser.id } }">{{
                externalUser.id
              }}</router-link>
            </td>
            <td>{{ externalUser.externalUserId }}</td>
            <td>
              <div v-if="externalUser.sourceApplication">
                <router-link :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: externalUser.sourceApplication.id } }">{{
                  externalUser.sourceApplication.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: externalUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ExternalUserEdit', params: { externalUserId: externalUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(externalUser)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="subscriptionManagementServiceApp.externalUser.delete.question"
          data-cy="externalUserDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-externalUser-heading"
          v-text="t$('subscriptionManagementServiceApp.externalUser.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-externalUser"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeExternalUser()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./external-user.component.ts"></script>
