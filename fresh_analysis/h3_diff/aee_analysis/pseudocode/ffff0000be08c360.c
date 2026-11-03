
undefined8 FUN_ffff0000be08c360(void)

{
  byte *pbVar1;
  undefined4 uVar2;
  undefined1 uVar3;
  byte bVar4;
  undefined *puVar5;
  bool bVar6;
  int iVar7;
  undefined8 uVar8;
  undefined8 uVar9;
  long lVar10;
  uint uVar11;
  uint uVar12;
  undefined1 *puVar13;
  ulong uVar14;
  byte *pbVar15;
  ulong uVar16;
  ulong uVar17;
  byte *pbVar18;
  undefined1 auStack_6d4 [512];
  undefined4 local_4d4;
  undefined8 local_4d0;
  undefined8 local_4c8;
  undefined1 local_4c0 [512];
  undefined1 local_2c0 [512];
  undefined8 local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 local_88;
  
  local_4d4 = 0;
  local_4d0 = 0;
  FUN_ffff0000be060ae0(auStack_6d4,0,0x200);
  uVar8 = FUN_ffff0000be08a7c0(&local_4d0,&local_4d4);
  pbVar18 = DAT_ffff0000be1b7160;
  uVar16 = DAT_ffff0000be1b7170;
  if ((int)uVar8 != 0) {
    return uVar8;
  }
joined_r0xffff0000be08c3d0:
  do {
    while( true ) {
      DAT_ffff0000be1b7410 = uVar16;
      DAT_ffff0000be1b7408 = pbVar18;
      if ((DAT_ffff0000be1b7410 == 0) || (iVar7 = FUN_ffff0000be089718(0), iVar7 != 0)) {
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return 0x7014;
      }
      pbVar1 = DAT_ffff0000be1b7408;
      if (DAT_ffff0000be1b7410 != 0) break;
LAB_ffff0000be08c48c:
      DAT_ffff0000be1b7408 = pbVar1;
      FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
      pbVar18 = DAT_ffff0000be1b7408;
      uVar16 = DAT_ffff0000be1b7410;
    }
    bVar4 = DAT_ffff0000be1b7408[1];
    uVar12 = (uint)bVar4;
    if (((char)bVar4 < '\0') && ((bVar4 & 0x7f) != 0)) {
      iVar7 = (uVar12 & 0x7f) + 1;
      uVar11 = uVar12 & 0x7f;
      uVar12 = 0;
      pbVar18 = DAT_ffff0000be1b7408 + 2;
      do {
        uVar11 = uVar11 - 1;
        uVar12 = (uint)*pbVar18 | uVar12 << 8;
        pbVar18 = pbVar18 + 1;
      } while (uVar11 != 0);
    }
    else {
      iVar7 = 1;
    }
    uVar14 = (ulong)(iVar7 + uVar12 + 1);
    bVar6 = DAT_ffff0000be1b7410 < uVar14;
    DAT_ffff0000be1b7410 = DAT_ffff0000be1b7410 - uVar14;
    if (bVar6) {
LAB_ffff0000be08c3e8:
      DAT_ffff0000be1b7408 = pbVar1;
      DAT_ffff0000be1b7410 = 0;
      pbVar18 = DAT_ffff0000be1b7408;
      uVar16 = DAT_ffff0000be1b7410;
      goto joined_r0xffff0000be08c3d0;
    }
    pbVar1 = DAT_ffff0000be1b7408 + uVar14;
    pbVar18 = pbVar1;
    uVar16 = DAT_ffff0000be1b7410;
    if (*DAT_ffff0000be1b7408 != 0x30) goto joined_r0xffff0000be08c3d0;
    if (DAT_ffff0000be1b7410 == 0) goto LAB_ffff0000be08c48c;
    bVar4 = pbVar1[1];
    uVar12 = (uint)bVar4;
    if (((char)bVar4 < '\0') && ((bVar4 & 0x7f) != 0)) {
      uVar11 = uVar12 & 0x7f;
      iVar7 = (uVar12 & 0x7f) + 1;
      uVar12 = 0;
      pbVar18 = DAT_ffff0000be1b7408 + uVar14 + 2;
      do {
        uVar11 = uVar11 - 1;
        uVar12 = (uint)*pbVar18 | uVar12 << 8;
        pbVar18 = pbVar18 + 1;
      } while (uVar11 != 0);
    }
    else {
      iVar7 = 1;
    }
    if ((*pbVar1 & 0x20) != 0) {
      uVar12 = 0;
    }
    uVar16 = (ulong)(iVar7 + uVar12 + 1);
    bVar6 = DAT_ffff0000be1b7410 < uVar16;
    DAT_ffff0000be1b7410 = DAT_ffff0000be1b7410 - uVar16;
    if (bVar6) goto LAB_ffff0000be08c3e8;
    pbVar18 = pbVar1 + uVar16;
    uVar16 = DAT_ffff0000be1b7410;
    if (*pbVar1 != 3) goto joined_r0xffff0000be08c3d0;
    bVar4 = pbVar1[1];
    if (((char)bVar4 < '\0') && (uVar17 = (ulong)bVar4 & 0x7f, (int)uVar17 != 0)) {
      uVar12 = 0;
      lVar10 = uVar17 + 1;
      pbVar15 = DAT_ffff0000be1b7408 + uVar14 + 2;
      do {
        uVar11 = (int)uVar17 - 1;
        uVar17 = (ulong)uVar11;
        uVar12 = (uint)*pbVar15 | uVar12 << 8;
        pbVar15 = pbVar15 + 1;
      } while (uVar11 != 0);
    }
    else {
      uVar12 = (uint)bVar4;
      lVar10 = 1;
    }
    uVar12 = uVar12 - 1;
  } while (0x200 < uVar12);
  DAT_ffff0000be1b7408 = pbVar18;
  FUN_ffff0000be087428(auStack_6d4,pbVar1 + lVar10 + 2,uVar12);
  uVar8 = local_4d0;
  uVar2 = local_4d4;
  DAT_ffff0000be1b7408 = (byte *)0x0;
  DAT_ffff0000be1b7410 = 0;
  local_88 = 0;
  local_90 = 0;
  local_98 = 0;
  local_a0 = 0;
  local_a8 = 0;
  local_b0 = 0;
  local_b8 = 0;
  local_c0 = 0;
  FUN_ffff0000be060ae0(local_2c0,0,0x200);
  FUN_ffff0000be060ae0(local_4c0,0,0x200);
  puVar5 = PTR_DAT_ffff0000be0f5750;
  local_4c8 = 0;
  iVar7 = FUN_ffff0000be07efb4(uVar8,uVar2,&local_c0,*(undefined4 *)PTR_DAT_ffff0000be0f5750);
  if (iVar7 != 0) {
    return 0x701d;
  }
  if (DAT_ffff0000be0f4098 != uVar12) {
    return 0x7022;
  }
  FUN_ffff0000be087428(local_2c0,auStack_6d4,uVar12);
  if (1 < DAT_ffff0000be0f4098) {
    uVar16 = (ulong)(DAT_ffff0000be0f4098 >> 1);
    puVar13 = local_2c0;
    uVar11 = DAT_ffff0000be0f4098;
    do {
      uVar11 = uVar11 - 1;
      uVar16 = uVar16 - 1;
      uVar3 = *puVar13;
      *puVar13 = local_2c0[uVar11];
      local_2c0[uVar11] = uVar3;
      puVar13 = puVar13 + 1;
    } while (uVar16 != 0);
  }
  FUN_ffff0000be07f0fc(local_2c0,&DAT_ffff0000be1b7180,local_4c0,*(undefined4 *)puVar5);
  if (1 < DAT_ffff0000be0f4098) {
    uVar16 = (ulong)(DAT_ffff0000be0f4098 >> 1);
    puVar13 = local_4c0;
    uVar11 = DAT_ffff0000be0f4098;
    do {
      uVar11 = uVar11 - 1;
      uVar16 = uVar16 - 1;
      uVar3 = *puVar13;
      *puVar13 = local_4c0[uVar11];
      local_4c0[uVar11] = uVar3;
      puVar13 = puVar13 + 1;
    } while (uVar16 != 0);
  }
  iVar7 = FUN_ffff0000be07f12c(&local_c0,local_4c0,(long)&local_4c8 + 4,*(undefined4 *)puVar5);
  if ((iVar7 == 0) || (iVar7 = FUN_ffff0000be08cfc0(uVar8,uVar2,auStack_6d4,uVar12), iVar7 == 0)) {
    if ((local_4c8._4_4_ != 1) &&
       (iVar7 = FUN_ffff0000be08cfc0(uVar8,uVar2,auStack_6d4,uVar12), iVar7 != 0)) {
      return 0x701f;
    }
    if (DAT_ffff0000be1b6acc != 0) {
      FUN_ffff0000be029a00(&local_4c8);
      FUN_ffff0000be05ede4((uint)local_4c8 % 0x14);
      iVar7 = FUN_ffff0000be07f12c(&local_c0,local_4c0,(long)&local_4c8 + 4,*(undefined4 *)puVar5);
      if (iVar7 != 0) goto LAB_ffff0000be08c76c;
      if (local_4c8._4_4_ != 1) {
        return 0x701f;
      }
    }
    uVar9 = 0;
    DAT_ffff0000be1b7168 = uVar8;
    DAT_ffff0000be1b7178 = uVar2;
  }
  else {
LAB_ffff0000be08c76c:
    uVar9 = 0x701e;
  }
  return uVar9;
}


