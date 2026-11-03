// ffff00009070c3ac  size=428  FUN_ffff00009070c3ac

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff00009070c3ac(void)

{
  undefined *puVar1;
  int iVar2;
  uint uVar3;
  undefined8 uVar4;
  int local_2c [3];
  uint local_8;
  int local_4;
  
  local_4 = 0;
  iVar2 = thunk_FUN_ffff0000907b65c4(&local_4);
  if (iVar2 == 0) {
    uVar4 = FUN_ffff000090709ab8(s_secure_ffff0000907d254d);
    iVar2 = FUN_ffff00009077f0ac(uVar4,&DAT_ffff0000907df338);
    if (iVar2 == 0) {
      FUN_ffff0000907099c4(s_secure_ffff0000907d254d,(long)local_4 * 4 + -0xffff6f7fbfe4);
    }
    else {
      FUN_ffff000090709a58(s_secure_ffff0000907d254d);
    }
    uVar4 = FUN_ffff000090709ab8(s_unlocked_ffff0000907f5cb2);
    iVar2 = FUN_ffff00009077f0ac(uVar4,&DAT_ffff0000907df338);
    if (iVar2 == 0) {
      FUN_ffff0000907099c4(s_unlocked_ffff0000907f5cb2,(ulong)(local_4 == 0) * 4 + -0xffff6f7fbfe4);
    }
    else {
      FUN_ffff000090709a58(s_unlocked_ffff0000907f5cb2);
    }
    uVar3 = thunk_FUN_ffff00009074b22c(0x1b);
    local_8 = 0;
    local_2c[2] = 0;
    local_2c[1] = 0;
    FUN_ffff0000907099c4
              (&DAT_ffff0000907ea6c1,
               (ulong)(((uVar3 ^ 0xffffffff) & 0xe) == 0) * 4 + -0xffff6f7fbfe4);
    FUN_ffff0000907abc74(0,&local_8);
    FUN_ffff0000907abc74(1,local_2c + 2);
    uVar3 = local_8;
    if (local_8 <= (uint)local_2c[2]) {
      uVar3 = local_2c[2];
    }
    FUN_ffff00009077f654(local_2c + 1,&DAT_ffff0000907da555,uVar3);
    FUN_ffff0000907099c4(&DAT_ffff0000907e5b92,local_2c + 1);
    FUN_ffff0000907099c4(s_tokenversion_ffff0000907e5b97,&DAT_ffff0000907d2501);
    FUN_ffff0000907099c4(s_rpmblock_support_ffff0000907f3f5f,&DAT_ffff0000907df35d);
    local_2c[0] = 0;
    FUN_ffff00009074a780(6,local_2c);
    puVar1 = &DAT_ffff0000907f0cf7;
    if (local_2c[0] < 0) {
      puVar1 = &DAT_ffff0000907df35d;
    }
    FUN_ffff0000907099c4(s_sbc_epdev_ffff0000907cf14a,puVar1);
  }
  else {
    FUN_ffff000090709c9c(s_Failed_to_get_secure_status__ffff0000907d8c76);
  }
  return;
}


