// ffff00009070c688  size=336  FUN_ffff00009070c688

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff00009070c688(void)

{
  int iVar1;
  undefined8 uVar2;
  char *pcVar3;
  int local_34;
  undefined *local_30;
  undefined8 uStack_28;
  int local_8 [2];
  
  local_8[0] = 0;
  local_34 = 0;
  FUN_ffff000090788a10(local_8);
  FUN_ffff000090709bec(s_Start_lock_flow_ffff0000907d57af);
  local_8[1] = 0;
  local_30 = PTR_s_do_nothing_ffff000090816d60;
  uStack_28 = DAT_ffff000090816d68;
  FUN_ffff000090788ff8(&local_30);
  iVar1 = thunk_FUN_ffff0000907b6608(1);
  if (iVar1 == 0) {
LAB_ffff00009070c6f8:
    iVar1 = thunk_FUN_ffff0000907b65c4(local_8 + 1);
    FUN_ffff000090709cdc(&DAT_ffff0000907df338);
    if (iVar1 == 0) {
      FUN_ffff00009070c3ac();
      FUN_ffff000090709cdc(&DAT_ffff0000907df338);
      FUN_ffff000090788a10(&local_34);
      if (local_8[0] == local_34) {
        FUN_ffff000090709c9c(s_lock_state_has_not_changed__so_n_ffff0000907f3f70);
      }
      else {
        uVar2 = FUN_ffff000090709ab8(&DAT_ffff0000907d3e22);
        iVar1 = FUN_ffff00009077f0ac(uVar2,&DAT_ffff0000907df35d);
        if (iVar1 == 0) {
          FUN_ffff000090709cdc(&DAT_ffff0000907df338);
          FUN_ffff000090787790();
        }
      }
      pcVar3 = s_Lock_the_device_successfully__Wi_ffff0000907d73d5;
      goto LAB_ffff00009070c7b0;
    }
  }
  else {
    if (iVar1 == 6) {
      FUN_ffff00009077e11c(s_Set_lock_state_return_RPMB_NO_PR_ffff0000907e8cdf,6);
      goto LAB_ffff00009070c6f8;
    }
    FUN_ffff00009077e11c(s_Failed_to_set_lock_state__error__ffff0000907e24df,iVar1);
    FUN_ffff000090709c9c(s_Failed_to_set_lock_state_ffff0000907fcb56);
  }
  FUN_ffff000090709c9c(s_Failed_to_lock_the_device__ffff0000907dbf92);
  pcVar3 = s_Failed_to_lock_the_device__Will_r_ffff0000907d2554;
LAB_ffff00009070c7b0:
  FUN_ffff000090745b60(pcVar3);
  FUN_ffff00009077cea0(3000000);
  FUN_ffff000090709cf4();
  return;
}


