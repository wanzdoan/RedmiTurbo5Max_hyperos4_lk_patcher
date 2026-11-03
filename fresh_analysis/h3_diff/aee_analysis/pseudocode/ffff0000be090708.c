
int FUN_ffff0000be090708(uint param_1,long param_2,undefined4 param_3)

{
  undefined4 uVar1;
  uint uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  uint uVar5;
  long lVar6;
  uint uVar7;
  int iVar8;
  int iVar9;
  ulong uVar10;
  undefined *puVar11;
  undefined *puVar12;
  uint local_a4;
  undefined8 local_a0 [8];
  
  local_a4 = 0;
  iVar8 = FUN_ffff0000be01d35c();
  if (6 < param_1) {
    return 0xf;
  }
  if ((1 << (ulong)(param_1 & 0x1f) & 0x4dU) == 0) {
    return 0xf;
  }
  if (param_2 == 0) {
LAB_ffff0000be090880:
    iVar9 = 0xf;
  }
  else {
    uVar10 = (ulong)param_1;
    lVar6 = uVar10 * 0x28;
    uVar1 = *(undefined4 *)(&DAT_ffff0000be0f4130 + lVar6);
    uVar2 = *(uint *)(&DAT_ffff0000be0f4134 + lVar6);
    uVar3 = *(undefined4 *)(&DAT_ffff0000be0f4110 + lVar6);
    puVar11 = (&PTR_s_RPMB_KEY_ffff0000be0f4118)[uVar10 * 5];
    uVar4 = *(undefined4 *)(&DAT_ffff0000be0f4120 + lVar6);
    puVar12 = (&PTR_DAT_ffff0000be0f4128)[uVar10 * 5];
    iVar9 = FUN_ffff0000be01d35c();
    local_a0[6] = 0;
    local_a0[7] = 0x414d52;
    local_a0[4] = 0;
    local_a0[5] = 0x434553;
    local_a0[2] = 0;
    local_a0[3] = 0x4453;
    local_a0[0] = 0x4d43;
    local_a0[1] = 0x4d44;
    if (iVar9 == 1) {
      iVar9 = FUN_ffff0000be0909a4(&local_a4);
      uVar10 = (ulong)local_a4;
      if ((local_a4 < 8) && ((1 << (ulong)(local_a4 & 0x1f) & 0xabU) != 0))
      goto LAB_ffff0000be09080c;
      FUN_ffff0000be05fd98(s_lcs___0x_x_Unknown__ffff0000be0a9cef);
      uVar7 = local_a4;
    }
    else {
      uVar10 = 0;
      iVar9 = 0xf;
LAB_ffff0000be09080c:
      FUN_ffff0000be05fd98(s_lcs___0x_x__s__ffff0000be0b1797,uVar10,local_a0 + uVar10);
      uVar7 = local_a4;
    }
    local_a4 = uVar7;
    if (iVar9 == 0) {
      uVar5 = 1 << (ulong)(uVar7 & 0x1f);
      FUN_ffff0000be05fd98(s_Status__0x_x__0x_x__ffff0000be0b4ca4,uVar5,uVar2);
      if ((uVar5 & uVar2) != 0) {
        if (iVar8 == 1) {
          iVar8 = FUN_ffff0000be0909d8(uVar3,puVar11,uVar4,puVar12,uVar1,param_2,param_3);
          return iVar8;
        }
        goto LAB_ffff0000be090880;
      }
      FUN_ffff0000be05fd98(s_not_unique_key__lcs___0x_x_ffff0000be0a9d04,uVar7);
      iVar9 = 0xe;
    }
    FUN_ffff0000be060ae0(param_2,0,param_3);
  }
  return iVar9;
}


