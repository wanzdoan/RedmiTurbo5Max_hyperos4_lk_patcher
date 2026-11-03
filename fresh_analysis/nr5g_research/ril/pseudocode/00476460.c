
/* RmcNetworkHandler::resetNrCaCache(RIL_NR_CA_CACHE*) */

int __thiscall RmcNetworkHandler::resetNrCaCache(RmcNetworkHandler *this,RIL_NR_CA_CACHE *param_1)

{
  undefined *puVar1;
  int iVar2;
  
  puVar1 = PTR_s_nrCaMutex_005d3ca0;
  pthread_mutex_lock((pthread_mutex_t *)
                     (PTR_s_nrCaMutex_005d3ca0 + (ulong)*(uint *)(this + 8) * 0x28));
  *(undefined8 *)param_1 = 0;
  *(undefined4 *)(param_1 + 8) = 0;
  iVar2 = pthread_mutex_unlock((pthread_mutex_t *)(puVar1 + (ulong)*(uint *)(this + 8) * 0x28));
  return iVar2;
}


