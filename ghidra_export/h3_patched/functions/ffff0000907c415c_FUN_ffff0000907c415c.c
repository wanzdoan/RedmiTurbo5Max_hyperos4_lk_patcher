// ffff0000907c415c  size=776  FUN_ffff0000907c415c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907c415c(long param_1)

{
  byte bVar1;
  uint uVar2;
  byte *pbVar3;
  long lVar4;
  undefined8 uVar5;
  ulong uVar6;
  uint uVar7;
  uint uVar8;
  ulong uVar9;
  long local_78;
  uint local_70 [4];
  
  if (*(long *)(param_1 + 0x10) != 0) {
    uVar9 = 0;
    do {
      local_78 = 0;
      if ((_DAT_ffff00009090d8b0 == (byte *)0x0) &&
         (lVar4 = *(long *)(param_1 + 8) + uVar9 * 0x20,
         pbVar3 = (byte *)FUN_ffff0000907bd370
                                    (*(undefined8 *)(lVar4 + 8),*(undefined8 *)(lVar4 + 0x10),
                                     s_com_android_build_system_os_vers_ffff0000907e70cf,0,&local_78
                                    ), local_78 != 0)) {
        _DAT_ffff00009090d8b0 = pbVar3;
      }
      if (_DAT_ffff00009090d8b8 == 0) {
        local_78 = 0;
        lVar4 = *(long *)(param_1 + 8) + uVar9 * 0x20;
        lVar4 = FUN_ffff0000907bd370
                          (*(undefined8 *)(lVar4 + 8),*(undefined8 *)(lVar4 + 0x10),
                           s_com_android_build_system_securit_ffff0000907d07de,0,&local_78);
        if (local_78 != 0) {
          _DAT_ffff00009090d8b8 = lVar4;
        }
      }
      if (_DAT_ffff00009090d8c0 == 0) {
        local_78 = 0;
        lVar4 = *(long *)(param_1 + 8) + uVar9 * 0x20;
        lVar4 = FUN_ffff0000907bd370
                          (*(undefined8 *)(lVar4 + 8),*(undefined8 *)(lVar4 + 0x10),
                           s_com_android_build_vendor_securit_ffff0000907fad62,0,&local_78);
        if (local_78 != 0) {
          _DAT_ffff00009090d8c0 = lVar4;
        }
      }
      if (_DAT_ffff00009090d8c8 == 0) {
        local_78 = 0;
        lVar4 = *(long *)(param_1 + 8) + uVar9 * 0x20;
        lVar4 = FUN_ffff0000907bd370
                          (*(undefined8 *)(lVar4 + 8),*(undefined8 *)(lVar4 + 0x10),
                           s_com_android_build_boot_security__ffff0000907fad8a,0,&local_78);
        if (local_78 != 0) {
          _DAT_ffff00009090d8c8 = lVar4;
        }
      }
      uVar9 = (ulong)((int)uVar9 + 1);
    } while (uVar9 < *(ulong *)(param_1 + 0x10));
  }
  local_78 = 0;
  if (_DAT_ffff00009090d8b0 == (byte *)0x0) {
    _DAT_ffff00009090d8b0 = &DAT_ffff0000907f09da;
  }
  pbVar3 = _DAT_ffff00009090d8b0;
  uVar2 = FUN_ffff00009077ef88(_DAT_ffff00009090d8b0);
  lVar4 = _DAT_ffff00009090d8b8;
  local_70[2] = 0;
  local_70[0] = 0;
  local_70[1] = 0;
  _DAT_ffff00009090d8d0 = 0;
  if (uVar2 != 0) {
    if ((uVar2 == 1) && (uVar8 = (uint)*pbVar3, uVar8 - 0x4d < 0xe)) {
      uVar7 = 0;
      uVar2 = uVar8 - 0x47;
    }
    else {
      uVar6 = 0;
      uVar9 = (ulong)uVar2;
      do {
        bVar1 = *pbVar3;
        if (bVar1 == 0x2e) {
          uVar2 = (int)uVar6 + 1;
          uVar6 = (ulong)uVar2;
          if (uVar2 == 3) break;
        }
        else if (bVar1 - 0x3a < 0xfffffff6) {
          _DAT_ffff00009090d8d0 = 0;
          goto LAB_ffff0000907c43a8;
        }
        uVar9 = uVar9 - 1;
        local_70[uVar6] = ((uint)bVar1 + local_70[uVar6] * 10) - 0x30;
        pbVar3 = pbVar3 + 1;
      } while (uVar9 != 0);
      uVar7 = local_70[1];
      uVar2 = local_70[0];
    }
    if (0x62 < uVar2) {
      uVar2 = 99;
    }
    if (0x62 < uVar7) {
      uVar7 = 99;
    }
    uVar8 = local_70[2];
    if (0x62 < local_70[2]) {
      uVar8 = 99;
    }
    _DAT_ffff00009090d8d0 = uVar8 + uVar7 * 100 + uVar2 * 10000;
  }
LAB_ffff0000907c43a8:
  if (_DAT_ffff00009090d8b8 != 0) {
    uVar5 = FUN_ffff00009077ef88(_DAT_ffff00009090d8b8);
    _DAT_ffff00009090d8d4 = FUN_ffff0000907c4464(1,lVar4,uVar5);
  }
  lVar4 = _DAT_ffff00009090d8c0;
  if (_DAT_ffff00009090d8c0 != 0) {
    uVar5 = FUN_ffff00009077ef88(_DAT_ffff00009090d8c0);
    _DAT_ffff00009090d8d8 = FUN_ffff0000907c4464(2,lVar4,uVar5);
  }
  lVar4 = _DAT_ffff00009090d8c8;
  if (_DAT_ffff00009090d8c8 != 0) {
    uVar5 = FUN_ffff00009077ef88(_DAT_ffff00009090d8c8);
    _DAT_ffff00009090d8dc = FUN_ffff0000907c4464(3,lVar4,uVar5);
  }
  FUN_ffff0000907bf9fc(param_1,0,&DAT_ffff00009090d8e0);
  return 0;
}


