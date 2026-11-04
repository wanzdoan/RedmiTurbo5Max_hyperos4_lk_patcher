
void FUN_ffff00009070c3b4(undefined8 param_1,undefined8 param_2,uint param_3)

{
  int iVar1;
  char *pcVar2;
  undefined8 uStack_40;
  undefined8 uStack_38;
  ulong uStack_30;
  undefined4 uStack_24;

  uStack_40 = 0;
  FUN_ffff0000907885fc((long)&uStack_40 + 4);
  FUN_ffff000090709cfc(s_Start_unlock_flow_ffff0000907d2931);
  uStack_24 = 0;
  uStack_30 = (ulong)param_3;
  uStack_38 = param_2;
  FUN_ffff000090788bf0(&uStack_38);
  iVar1 = thunk_FUN_ffff0000907b65cc(0);
  if (iVar1 == 0) {
    iVar1 = thunk_FUN_ffff0000907b6588(&uStack_24);
    if (iVar1 == 0) {
      FUN_ffff00009077ea84(s_Current_lock_state____d_ffff0000907d42f2,uStack_24);
      FUN_ffff000090709dec(&DAT_ffff0000907d788e);
      FUN_ffff00009070c208();
      FUN_ffff000090709dec(&DAT_ffff0000907d788e);
      FUN_ffff0000907885fc(&uStack_40);
      if (uStack_40._4_4_ == (int)uStack_40) {
        FUN_ffff000090709dac(s_lock_state_has_not_changed__so_n_ffff0000907ecc34);
      }
      else {
        FUN_ffff000090709dec(&DAT_ffff0000907d788e);
        FUN_ffff0000907873d8();
      }
      pcVar2 = s_Unlock_the_device_successfully__W_ffff0000907eb21b;
      goto LAB_ffff00009070c468;
    }
    FUN_ffff000090709dec(&DAT_ffff0000907d788e);
  }
  else {
    FUN_ffff00009077ea84(s_Failed_to_set_lock_state__error__ffff0000907dabf3,iVar1);
    pcVar2 = s_RPMB_no_provision_ffff0000907f0832;
    if (iVar1 != 6) {
      pcVar2 = s_Failed_to_set_lock_state_ffff0000907f5942;
    }
    FUN_ffff000090709dac(pcVar2);
  }
  FUN_ffff000090709dac(s_Failed_to_unlock_the_device_ffff0000907e7ed3);
  pcVar2 = s_Failed_to_unlock_the_device__Wil_ffff0000907e986d;
LAB_ffff00009070c468:
  FUN_ffff0000907468f0(pcVar2);
  FUN_ffff00009077d808(3000000);
  FUN_ffff000090709e04();
  return;
}
