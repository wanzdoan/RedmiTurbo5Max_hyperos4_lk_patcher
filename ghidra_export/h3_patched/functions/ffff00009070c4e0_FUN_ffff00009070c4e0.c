// ffff00009070c4e0  size=304  FUN_ffff00009070c4e0

void FUN_ffff00009070c4e0(undefined8 param_1,undefined8 param_2,uint param_3)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_40;
  undefined8 local_38;
  ulong local_30;
  undefined4 local_24;
  
  local_40 = 0;
  FUN_ffff000090788750((long)&local_40 + 4);
  FUN_ffff000090709c60(s_Start_unlock_flow_ffff0000907da4aa);
  local_24 = 0;
  local_30 = (ulong)param_3;
  local_38 = param_2;
  FUN_ffff000090788d38(&local_38);
  iVar1 = thunk_FUN_ffff0000907b5f98(0);
  if (iVar1 == 0) {
    iVar1 = thunk_FUN_ffff0000907b5f54(&local_24);
    FUN_ffff000090709d50(&DAT_ffff0000907df27e);
    if (iVar1 == 0) {
      FUN_ffff00009070c334();
      FUN_ffff000090709d50(&DAT_ffff0000907df27e);
      FUN_ffff000090788750(&local_40);
      if (local_40._4_4_ == (int)local_40) {
        FUN_ffff000090709d10(s_lock_state_has_not_changed__so_n_ffff0000907f3dcd);
      }
      else {
        FUN_ffff000090709d50(&DAT_ffff0000907df27e);
        FUN_ffff0000907874d0();
      }
      pcVar2 = s_Unlock_the_device_successfully__W_ffff0000907f24be;
      goto LAB_ffff00009070c598;
    }
  }
  else {
    FUN_ffff00009077de54(s_Failed_to_set_lock_state__error__ffff0000907e23f7,iVar1);
    pcVar2 = s_RPMB_no_provision_ffff0000907f79d2;
    if (iVar1 != 6) {
      pcVar2 = s_Failed_to_set_lock_state_ffff0000907fc9ae;
    }
    FUN_ffff000090709d10(pcVar2);
  }
  FUN_ffff000090709d10(s_Failed_to_unlock_the_device_ffff0000907ef273);
  pcVar2 = s_Failed_to_unlock_the_device__Wil_ffff0000907f0b89;
LAB_ffff00009070c598:
  FUN_ffff0000907458c8(pcVar2);
  FUN_ffff00009077cbd8(3000000);
  FUN_ffff000090709d68();
  return;
}


