// ffff00009070c334  size=428  FUN_ffff00009070c334

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff00009070c334(void)

{
  undefined *puVar1;
  int iVar2;
  uint uVar3;
  undefined8 uVar4;
  int local_2c [3];
  uint local_8;
  int local_4;
  
  local_4 = 0;
  iVar2 = thunk_FUN_ffff0000907b5f54(&local_4);
  if (iVar2 == 0) {
    uVar4 = FUN_ffff000090709b2c(s_secure_ffff0000907d2510);
    iVar2 = FUN_ffff00009077ede4(uVar4,&DAT_ffff0000907df27e);
    if (iVar2 == 0) {
      FUN_ffff000090709a38(s_secure_ffff0000907d2510,(long)local_4 * 4 + -0xffff6f7fc20c);
    }
    else {
      FUN_ffff000090709acc(s_secure_ffff0000907d2510);
    }
    uVar4 = FUN_ffff000090709b2c(s_unlocked_ffff0000907f5b24);
    iVar2 = FUN_ffff00009077ede4(uVar4,&DAT_ffff0000907df27e);
    if (iVar2 == 0) {
      FUN_ffff000090709a38(s_unlocked_ffff0000907f5b24,(ulong)(local_4 == 0) * 4 + -0xffff6f7fc20c);
    }
    else {
      FUN_ffff000090709acc(s_unlocked_ffff0000907f5b24);
    }
    uVar3 = thunk_FUN_ffff00009074af94(0x1b);
    local_8 = 0;
    local_2c[2] = 0;
    local_2c[1] = 0;
    FUN_ffff000090709a38
              (&DAT_ffff0000907ea550,
               (ulong)(((uVar3 ^ 0xffffffff) & 0xe) == 0) * 4 + -0xffff6f7fc20c);
    FUN_ffff0000907ab9b4(0,&local_8);
    FUN_ffff0000907ab9b4(1,local_2c + 2);
    uVar3 = local_8;
    if (local_8 <= (uint)local_2c[2]) {
      uVar3 = local_2c[2];
    }
    FUN_ffff00009077f38c(local_2c + 1,&DAT_ffff0000907da4a7,uVar3);
    FUN_ffff000090709a38(&DAT_ffff0000907e5a67,local_2c + 1);
    FUN_ffff000090709a38(s_tokenversion_ffff0000907e5a6c,&DAT_ffff0000907d24c4);
    FUN_ffff000090709a38(s_rpmblock_support_ffff0000907f3dbc,&DAT_ffff0000907df2a3);
    local_2c[0] = 0;
    FUN_ffff00009074a4e8(6,local_2c);
    puVar1 = &DAT_ffff0000907f0b6f;
    if (local_2c[0] < 0) {
      puVar1 = &DAT_ffff0000907df2a3;
    }
    FUN_ffff000090709a38(s_sbc_epdev_ffff0000907cf14a,puVar1);
  }
  else {
    FUN_ffff000090709d10(s_Failed_to_get_secure_status__ffff0000907d8bdf);
  }
  return;
}


