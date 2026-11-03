// ffff00009070c558  size=304  FUN_ffff00009070c558

void FUN_ffff00009070c558(undefined8 param_1,undefined8 param_2,uint param_3)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_40;
  undefined8 local_38;
  ulong local_30;
  undefined4 local_24;
  
  local_40 = 0;
  FUN_ffff000090788a10((long)&local_40 + 4);
  FUN_ffff000090709bec(s_Start_unlock_flow_ffff0000907da558);
  local_24 = 0;
  local_30 = (ulong)param_3;
  local_38 = param_2;
  FUN_ffff000090788ff8(&local_38);
  iVar1 = thunk_FUN_ffff0000907b6608(0);
  if (iVar1 == 0) {
    iVar1 = thunk_FUN_ffff0000907b65c4(&local_24);
    FUN_ffff000090709cdc(&DAT_ffff0000907df338);
    if (iVar1 == 0) {
      FUN_ffff00009070c3ac();
      FUN_ffff000090709cdc(&DAT_ffff0000907df338);
      FUN_ffff000090788a10(&local_40);
      if (local_40._4_4_ == (int)local_40) {
        FUN_ffff000090709c9c(s_lock_state_has_not_changed__so_n_ffff0000907f3f70);
      }
      else {
        FUN_ffff000090709cdc(&DAT_ffff0000907df338);
        FUN_ffff000090787790();
      }
      pcVar2 = s_Unlock_the_device_successfully__W_ffff0000907f2661;
      goto LAB_ffff00009070c610;
    }
  }
  else {
    FUN_ffff00009077e11c(s_Failed_to_set_lock_state__error__ffff0000907e24df,iVar1);
    pcVar2 = s_RPMB_no_provision_ffff0000907f7b5c;
    if (iVar1 != 6) {
      pcVar2 = s_Failed_to_set_lock_state_ffff0000907fcb56;
    }
    FUN_ffff000090709c9c(pcVar2);
  }
  FUN_ffff000090709c9c(s_Failed_to_unlock_the_device_ffff0000907ef3fb);
  pcVar2 = s_Failed_to_unlock_the_device__Wil_ffff0000907f0d11;
LAB_ffff00009070c610:
  FUN_ffff000090745b60(pcVar2);
  FUN_ffff00009077cea0(3000000);
  FUN_ffff000090709cf4();
  return;
}


