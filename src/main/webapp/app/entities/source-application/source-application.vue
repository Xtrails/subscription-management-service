<template>
  <div>
    <h2 id="page-heading" data-cy="SourceApplicationHeading">
      <span v-text="t$('subscriptionManagementServiceApp.sourceApplication.home.title')" id="source-application-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.sourceApplication.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SourceApplicationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-source-application"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.sourceApplication.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && sourceApplications && sourceApplications.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.sourceApplication.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="sourceApplications && sourceApplications.length > 0">
      <table class="table table-striped" aria-describedby="sourceApplications">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.sourceApplication.applicationName')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.sourceApplication.user')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sourceApplication in sourceApplications" :key="sourceApplication.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: sourceApplication.id } }">{{
                sourceApplication.id
              }}</router-link>
            </td>
            <td>{{ sourceApplication.applicationName }}</td>
            <td>
              <div v-if="sourceApplication.user">
                <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: sourceApplication.user.id } }">{{
                  sourceApplication.user.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: sourceApplication.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SourceApplicationEdit', params: { sourceApplicationId: sourceApplication.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(sourceApplication)"
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
          id="subscriptionManagementServiceApp.sourceApplication.delete.question"
          data-cy="sourceApplicationDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-sourceApplication-heading"
          v-text="t$('subscriptionManagementServiceApp.sourceApplication.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-sourceApplication"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSourceApplication()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./source-application.component.ts"></script>
