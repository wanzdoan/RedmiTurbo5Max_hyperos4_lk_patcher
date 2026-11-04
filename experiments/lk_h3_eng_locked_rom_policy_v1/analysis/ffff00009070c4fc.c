
void FUN_ffff00009070c4fc(void)

{
  int iVar1;
  undefined8 uVar2;
  char *pcVar3;
  int iStack_34;
  undefined *puStack_30;
  undefined8 uStack_28;
  int iStack_8;
  undefined4 uStack_4;

  iStack_8 = 0;
  iStack_34 = 0;
  FUN_ffff0000907885fc(&iStack_8);
  FUN_ffff000090709cfc(s_Start_lock_flow_ffff0000907cd995);
  uStack_4 = 0;
  puStack_30 = PTR_s_do_nothing_ffff00009080ed50;
  uStack_28 = DAT_ffff00009080ed58;
  FUN_ffff000090788bf0(&puStack_30);
  iVar1 = thunk_FUN_ffff0000907b65cc(1);
  if (iVar1 == 0) {
LAB_ffff00009070c56c:
    iVar1 = thunk_FUN_ffff0000907b6588(&uStack_4);
    if (iVar1 == 0) {
      FUN_ffff00009077ea84(s_Current_lock_state____d_ffff0000907d42f2,uStack_4);
      FUN_ffff000090709dec(&DAT_ffff0000907d788e);
      FUN_ffff00009070c208();
      FUN_ffff000090709dec(&DAT_ffff0000907d788e);
      FUN_ffff0000907885fc(&iStack_34);
      if (iStack_8 == iStack_34) {
        FUN_ffff000090709dac(s_lock_state_has_not_changed__so_n_ffff0000907ecc34);
      }
      else {
        uVar2 = FUN_ffff000090709bc8(&DAT_ffff0000907cbefa);
        iVar1 = FUN_ffff00009077f990(uVar2,&DAT_ffff0000907d78b3);
        if (iVar1 == 0) {
          FUN_ffff000090709dec(&DAT_ffff0000907d788e);
          FUN_ffff0000907873d8();
        }
      }
      pcVar3 = s_Lock_the_device_successfully__Wi_ffff0000907cf617;
      goto LAB_ffff00009070c640;
    }
    FUN_ffff000090709dec(&DAT_ffff0000907d788e);
  }
  else {
    if (iVar1 == 6) {
      FUN_ffff00009077ea84(s_Set_lock_state_return_RPMB_NO_PR_ffff0000907e1575,6);
      goto LAB_ffff00009070c56c;
    }
    FUN_ffff00009077ea84(s_Failed_to_set_lock_state__error__ffff0000907dabf3,iVar1);
    FUN_ffff000090709dac(s_Failed_to_set_lock_state_ffff0000907f5942);
  }
  FUN_ffff000090709dac(s_Failed_to_lock_the_device__ffff0000907d430b);
  pcVar3 = s_Failed_to_lock_the_device__Will_r_ffff0000907ca646;
LAB_ffff00009070c640:
  FUN_ffff0000907468f0(pcVar3);
  FUN_ffff00009077d808(3000000);
  FUN_ffff000090709e04();
  return;
}
