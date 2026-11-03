// ffff00009070c610  size=336  FUN_ffff00009070c610

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff00009070c610(void)

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
  FUN_ffff000090788750(local_8);
  FUN_ffff000090709c60(s_Start_lock_flow_ffff0000907d575f);
  local_8[1] = 0;
  local_30 = PTR_s_do_nothing_ffff000090816d50;
  uStack_28 = DAT_ffff000090816d58;
  FUN_ffff000090788d38(&local_30);
  iVar1 = thunk_FUN_ffff0000907b5f98(1);
  if (iVar1 == 0) {
LAB_ffff00009070c680:
    iVar1 = thunk_FUN_ffff0000907b5f54(local_8 + 1);
    FUN_ffff000090709d50(&DAT_ffff0000907df27e);
    if (iVar1 == 0) {
      FUN_ffff00009070c334();
      FUN_ffff000090709d50(&DAT_ffff0000907df27e);
      FUN_ffff000090788750(&local_34);
      if (local_8[0] == local_34) {
        FUN_ffff000090709d10(s_lock_state_has_not_changed__so_n_ffff0000907f3dcd);
      }
      else {
        uVar2 = FUN_ffff000090709b2c(&DAT_ffff0000907d3de5);
        iVar1 = FUN_ffff00009077ede4(uVar2,&DAT_ffff0000907df2a3);
        if (iVar1 == 0) {
          FUN_ffff000090709d50(&DAT_ffff0000907df27e);
          FUN_ffff0000907874d0();
        }
      }
      pcVar3 = s_Lock_the_device_successfully__Wi_ffff0000907d733f;
      goto LAB_ffff00009070c738;
    }
  }
  else {
    if (iVar1 == 6) {
      FUN_ffff00009077de54(s_Set_lock_state_return_RPMB_NO_PR_ffff0000907e8b84,6);
      goto LAB_ffff00009070c680;
    }
    FUN_ffff00009077de54(s_Failed_to_set_lock_state__error__ffff0000907e23f7,iVar1);
    FUN_ffff000090709d10(s_Failed_to_set_lock_state_ffff0000907fc9ae);
  }
  FUN_ffff000090709d10(s_Failed_to_lock_the_device__ffff0000907dbe70);
  pcVar3 = s_Failed_to_lock_the_device__Will_r_ffff0000907d2517;
LAB_ffff00009070c738:
  FUN_ffff0000907458c8(pcVar3);
  FUN_ffff00009077cbd8(3000000);
  FUN_ffff000090709d68();
  return;
}


