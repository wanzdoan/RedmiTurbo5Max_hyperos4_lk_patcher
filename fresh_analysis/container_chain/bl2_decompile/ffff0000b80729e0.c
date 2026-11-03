
int FUN_ffff0000b80729e0(uint param_1,long param_2,undefined4 param_3)

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
  iVar8 = FUN_ffff0000b800e378();
  if (6 < param_1) {
    return 0xf;
  }
  if ((1 << (ulong)(param_1 & 0x1f) & 0x4dU) == 0) {
    return 0xf;
  }
  if (param_2 == 0) {
LAB_ffff0000b8072b58:
    iVar9 = 0xf;
  }
  else {
    uVar10 = (ulong)param_1;
    lVar6 = uVar10 * 0x28;
    uVar1 = *(undefined4 *)(&DAT_ffff0000b8107780 + lVar6);
    uVar2 = *(uint *)(&DAT_ffff0000b8107784 + lVar6);
    uVar3 = *(undefined4 *)(&DAT_ffff0000b8107760 + lVar6);
    puVar11 = (&PTR_s_RPMB_KEY_ffff0000b8107768)[uVar10 * 5];
    uVar4 = *(undefined4 *)(&DAT_ffff0000b8107770 + lVar6);
    puVar12 = (&PTR_DAT_ffff0000b8107778)[uVar10 * 5];
    iVar9 = FUN_ffff0000b800e378();
    local_a0[6] = 0;
    local_a0[7] = 0x414d52;
    local_a0[4] = 0;
    local_a0[5] = 0x434553;
    local_a0[2] = 0;
    local_a0[3] = 0x4453;
    local_a0[0] = 0x4d43;
    local_a0[1] = 0x4d44;
    if (iVar9 == 1) {
      iVar9 = FUN_ffff0000b8072c7c(&local_a4);
      uVar10 = (ulong)local_a4;
      if ((local_a4 < 8) && ((1 << (ulong)(local_a4 & 0x1f) & 0xabU) != 0))
      goto LAB_ffff0000b8072ae4;
      FUN_ffff0000b804c33c(s_lcs___0x_x_Unknown__ffff0000b808a5b3);
      uVar7 = local_a4;
    }
    else {
      uVar10 = 0;
      iVar9 = 0xf;
LAB_ffff0000b8072ae4:
      FUN_ffff0000b804c33c(s_lcs___0x_x__s__ffff0000b80905b2,uVar10,local_a0 + uVar10);
      uVar7 = local_a4;
    }
    local_a4 = uVar7;
    if (iVar9 == 0) {
      uVar5 = 1 << (ulong)(uVar7 & 0x1f);
      FUN_ffff0000b804c33c(s_Status__0x_x__0x_x__ffff0000b8092f71,uVar5,uVar2);
      if ((uVar5 & uVar2) != 0) {
        if (iVar8 == 1) {
          iVar8 = FUN_ffff0000b8072cb0(uVar3,puVar11,uVar4,puVar12,uVar1,param_2,param_3);
          return iVar8;
        }
        goto LAB_ffff0000b8072b58;
      }
      FUN_ffff0000b804c33c(s_not_unique_key__lcs___0x_x_ffff0000b808a5c8,uVar7);
      iVar9 = 0xe;
    }
    FUN_ffff0000b804d070(param_2,0,param_3);
  }
  return iVar9;
}


