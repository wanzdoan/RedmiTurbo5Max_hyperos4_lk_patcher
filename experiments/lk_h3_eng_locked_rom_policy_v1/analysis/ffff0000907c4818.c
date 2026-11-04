
undefined8 FUN_ffff0000907c4818(long param_1)

{
  byte bVar1;
  uint uVar2;
  int iVar3;
  byte *pbVar4;
  long lVar5;
  undefined8 uVar6;
  ulong uVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  long local_78;
  uint local_70 [4];

  if (*(long *)(param_1 + 0x10) != 0) {
    uVar10 = 0;
    do {
      local_78 = 0;
      if ((DAT_ffff00009090d8b0 == (byte *)0x0) &&
         (lVar5 = *(long *)(param_1 + 8) + uVar10 * 0x20,
         pbVar4 = (byte *)FUN_ffff0000907bd9a4
                                    (*(undefined8 *)(lVar5 + 8),*(undefined8 *)(lVar5 + 0x10),
                                     s_com_android_build_system_os_vers_ffff0000907dfac5,0,&local_78
                                    ), local_78 != 0)) {
        FUN_ffff00009077ea84
                  (s__s_is_found_in_idx_0x_x__val_____ffff0000907dfaa2,
                   s_com_android_build_system_os_vers_ffff0000907dfac5,uVar10,pbVar4);
        DAT_ffff00009090d8b0 = pbVar4;
      }
      if (DAT_ffff00009090d8b8 == 0) {
        local_78 = 0;
        lVar5 = *(long *)(param_1 + 8) + uVar10 * 0x20;
        lVar5 = FUN_ffff0000907bd9a4
                          (*(undefined8 *)(lVar5 + 8),*(undefined8 *)(lVar5 + 0x10),
                           s_com_android_build_system_securit_ffff0000907c88ae,0,&local_78);
        if (local_78 != 0) {
          FUN_ffff00009077ea84
                    (s__s_is_found_in_idx_0x_x__val_____ffff0000907dfaa2,
                     s_com_android_build_system_securit_ffff0000907c88ae,uVar10,lVar5);
          DAT_ffff00009090d8b8 = lVar5;
        }
      }
      if (DAT_ffff00009090d8c0 == 0) {
        local_78 = 0;
        lVar5 = *(long *)(param_1 + 8) + uVar10 * 0x20;
        lVar5 = FUN_ffff0000907bd9a4
                          (*(undefined8 *)(lVar5 + 8),*(undefined8 *)(lVar5 + 0x10),
                           s_com_android_build_vendor_securit_ffff0000907f3d60,0,&local_78);
        if (local_78 != 0) {
          FUN_ffff00009077ea84
                    (s__s_is_found_in_idx_0x_x__val_____ffff0000907dfaa2,
                     s_com_android_build_vendor_securit_ffff0000907f3d60,uVar10,lVar5);
          DAT_ffff00009090d8c0 = lVar5;
        }
      }
      local_78 = 0;
      if ((DAT_ffff00009090d8c8 == 0) &&
         (lVar5 = *(long *)(param_1 + 8) + uVar10 * 0x20,
         lVar5 = FUN_ffff0000907bd9a4
                           (*(undefined8 *)(lVar5 + 8),*(undefined8 *)(lVar5 + 0x10),
                            s_com_android_build_boot_security__ffff0000907f3d88,0,&local_78),
         local_78 != 0)) {
        FUN_ffff00009077ea84
                  (s__s_is_found_in_idx_0x_x__val_____ffff0000907dfaa2,
                   s_com_android_build_boot_security__ffff0000907f3d88,uVar10,lVar5);
        DAT_ffff00009090d8c8 = lVar5;
      }
      uVar10 = (ulong)((int)uVar10 + 1);
    } while (uVar10 < *(ulong *)(param_1 + 0x10));
  }
  local_78 = 0;
  if (DAT_ffff00009090d8b0 == (byte *)0x0) {
    DAT_ffff00009090d8b0 = &DAT_ffff0000907e96ae;
  }
  pbVar4 = DAT_ffff00009090d8b0;
  uVar2 = FUN_ffff00009077fb34(DAT_ffff00009090d8b0);
  local_70[2] = 0;
  local_70[0] = 0;
  local_70[1] = 0;
  iVar3 = 0;
  if (uVar2 != 0) {
    if ((uVar2 == 1) && (uVar9 = (uint)*pbVar4, uVar9 - 0x4d < 0xe)) {
      uVar8 = 0;
      uVar2 = uVar9 - 0x47;
    }
    else {
      uVar7 = 0;
      uVar10 = (ulong)uVar2;
      do {
        bVar1 = *pbVar4;
        if (bVar1 == 0x2e) {
          uVar2 = (int)uVar7 + 1;
          uVar7 = (ulong)uVar2;
          if (uVar2 == 3) break;
        }
        else if (bVar1 - 0x3a < 0xfffffff6) {
          FUN_ffff00009077ea84
                    (s___s__invalid_os_version_ffff0000907dfae9,
                     s_version_str_to_uint_ffff0000907cd812);
          iVar3 = 0;
          goto LAB_ffff0000907c4aec;
        }
        uVar10 = uVar10 - 1;
        local_70[uVar7] = ((uint)bVar1 + local_70[uVar7] * 10) - 0x30;
        pbVar4 = pbVar4 + 1;
      } while (uVar10 != 0);
      uVar8 = local_70[1];
      uVar2 = local_70[0];
    }
    if (0x62 < uVar2) {
      uVar2 = 99;
    }
    if (0x62 < uVar8) {
      uVar8 = 99;
    }
    uVar9 = local_70[2];
    if (0x62 < local_70[2]) {
      uVar9 = 99;
    }
    iVar3 = uVar9 + uVar8 * 100 + uVar2 * 10000;
  }
LAB_ffff0000907c4aec:
  lVar5 = DAT_ffff00009090d8b8;
  DAT_ffff00009090d8d0 = iVar3;
  if (DAT_ffff00009090d8b8 != 0) {
    uVar6 = FUN_ffff00009077fb34(DAT_ffff00009090d8b8);
    DAT_ffff00009090d8d4 = FUN_ffff0000907c4bac(1,lVar5,uVar6);
  }
  lVar5 = DAT_ffff00009090d8c0;
  if (DAT_ffff00009090d8c0 != 0) {
    uVar6 = FUN_ffff00009077fb34(DAT_ffff00009090d8c0);
    DAT_ffff00009090d8d8 = FUN_ffff0000907c4bac(2,lVar5,uVar6);
  }
  lVar5 = DAT_ffff00009090d8c8;
  if (DAT_ffff00009090d8c8 != 0) {
    uVar6 = FUN_ffff00009077fb34(DAT_ffff00009090d8c8);
    DAT_ffff00009090d8dc = FUN_ffff0000907c4bac(3,lVar5,uVar6);
  }
  FUN_ffff0000907c0030(param_1,0,&DAT_ffff00009090d8e0);
  return 0;
}
