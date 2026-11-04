
/* WARNING: Type propagation algorithm not settling */

void FUN_ffff00009070c208(void)

{
  undefined *puVar1;
  int iVar2;
  uint uVar3;
  undefined8 uVar4;
  int local_2c [3];
  uint local_8;
  int local_4;

  local_4 = 0;
  iVar2 = thunk_FUN_ffff0000907b6588(&local_4);
  if (iVar2 == 0) {
    uVar4 = FUN_ffff000090709bc8(s_secure_ffff0000907ca63f);
    iVar2 = FUN_ffff00009077f990(uVar4,&DAT_ffff0000907d788e);
    if (iVar2 == 0) {
      FUN_ffff000090709ab0(s_secure_ffff0000907ca63f,(long)local_4 * 4 + -0xffff6f803004);
    }
    else {
      FUN_ffff000090709b68(s_secure_ffff0000907ca63f);
    }
    uVar4 = FUN_ffff000090709bc8(s_unlocked_ffff0000907ee940);
    iVar2 = FUN_ffff00009077f990(uVar4,&DAT_ffff0000907d788e);
    if (iVar2 == 0) {
      FUN_ffff000090709ab0(s_unlocked_ffff0000907ee940,(ulong)(local_4 == 0) * 4 + -0xffff6f803004);
    }
    else {
      FUN_ffff000090709b68(s_unlocked_ffff0000907ee940);
    }
    uVar3 = thunk_FUN_ffff00009074bfa8(0x1b);
    local_8 = 0;
    local_2c[2] = 0;
    local_2c[1] = 0;
    FUN_ffff000090709ab0
              (&DAT_ffff0000907e3039,
               (ulong)(((uVar3 ^ 0xffffffff) & 0xe) == 0) * 4 + -0xffff6f803004);
    FUN_ffff0000907abf84(0,&local_8);
    FUN_ffff0000907abf84(1,local_2c + 2);
    uVar3 = local_8;
    if (local_8 <= (uint)local_2c[2]) {
      uVar3 = local_2c[2];
    }
    FUN_ffff00009077ff38(local_2c + 1,&DAT_ffff0000907d292e,uVar3);
    FUN_ffff000090709ab0(&DAT_ffff0000907de241,local_2c + 1);
    FUN_ffff000090709ab0(s_tokenversion_ffff0000907de246,&DAT_ffff0000907ca5f3);
    FUN_ffff000090709ab0(s_rpmblock_support_ffff0000907ecc23,&DAT_ffff0000907d78b3);
    local_2c[0] = 0;
    FUN_ffff00009074b510(6,local_2c);
    puVar1 = &DAT_ffff0000907e9853;
    if (local_2c[0] < 0) {
      puVar1 = &DAT_ffff0000907d78b3;
    }
    FUN_ffff000090709ab0(s_sbc_epdev_ffff0000907c714a,puVar1);
  }
  else {
    FUN_ffff000090709dac(s_Failed_to_get_secure_status__ffff0000907d1038);
  }
  return;
}
